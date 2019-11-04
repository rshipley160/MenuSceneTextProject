
import edu.utc.game.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainScene implements Scene {

    // GameObjects
    Turret player = new Turret();
    java.util.List<Bullet> bullets = new java.util.LinkedList<>();
    java.util.List<Enemy> enemies = new java.util.LinkedList<>();
    List<Collectible> collectibles = new java.util.LinkedList<>();

    // # of collectibles collected in the current level so far
    int collectedAmount = 0;

    // Determines enemy speed, collectible de-spawn time, and # of collectibles required to advance
    int level = 0;

    // Set to true to clear all GameObjects except player
    boolean clearField = false;

    {
        spawnEnemies(6);

        GL11.glClearColor(1f, 0.9f, 0.8f, 1.0f);

        //Collectible dragging procedure
        GLFW.glfwSetMouseButtonCallback(MouseGame.ui.getWindow(),
                new GLFWMouseButtonCallback() {
                    Collectible captured;

                    public void invoke(long window, int button, int action, int mods) {
                        // If user presses the right mouse...
                        if (button == 1 && action == GLFW.GLFW_PRESS) {

                            XYPair<Integer> lastClick = MouseGame.ui.getMouseLocation();
                            Rectangle mousePoint = new Rectangle(lastClick.x, lastClick.y, 1, 1);
                            for (Collectible c : collectibles) {
                                // and the mouse is over a collectible...
                                if (mousePoint.intersects(c.getHitbox())) {
                                    //capture the Collectible, meaning that its position will be synchronized with the mouse position
                                    captured = c.capture(lastClick);
                                }
                                return;
                            }

                        }
                        // If the user releases the right mouse, release the collectible
                        if (button == 1 && action == GLFW.GLFW_RELEASE) {
                            if (captured != null) {
                                captured.release();
                            }
                        }
                    }

                });
    }

private class Turret extends GameObject
{
    private int size = MouseGame.ui.getWidth()/10;
    {
        int newX = MouseGame.ui.getWidth()/2-size/2;
        int newY = MouseGame.ui.getHeight()/2-size/2;
        this.hitbox.setBounds(newX, newY , size, size);
        this.r = 0f;
        this.g = 0.6f;
        this.b = 0f;
    }
}

private class Bullet extends GameObject
{
    XYPair<Float> direction;
    float speed = 0.5f;
    {
        this.r = 0f;
        this.g = 0.8f;
        this.b = 1f;
    }

    public Bullet(XYPair<Integer> destination)
    {
        // Make the bullet
        this.hitbox.setBounds(MouseGame.ui.getWidth()/2, MouseGame.ui.getHeight()/2, 5, 5);
        // Use destination point to calculate x and y components of the direction vector using trigonometry
        XYPair<Float> clamped = new XYPair<>((float)(destination.x - MouseGame.ui.getWidth()/2)*2 / MouseGame.ui.getWidth(),2*(float)(destination.y - MouseGame.ui.getHeight()/2)/MouseGame.ui.getHeight());
        double angle = Math.atan2(clamped.y, clamped.x);
        this.direction = new XYPair<>((float)(Math.cos(angle)), (float)(Math.sin(angle)));
    }

    public void update (int delta)
    {
        // Move using the calculated direction vector
        this.hitbox.translate((int)(direction.x*speed*delta), (int)(direction.y*speed*delta));

        // Deactivate bullets that leave the screen
        if ((this.hitbox.getX()+this.hitbox.getWidth())< 0 || this.hitbox.getX() > MouseGame.ui.getWidth() ||
                this.hitbox.getY() > MouseGame.ui.getHeight() || (this.hitbox.getY() + this.hitbox.getHeight() < 0))
        {
            this.deactivate();
        }
    }


}

private class Enemy extends GameObject
{
    // square size
    int size;

    // Number of bullets that have to hit the Enemy for it to disappear
    int maxHealth;

    // number of hits remaining
    int currentHealth;

    // The number of milliseconds to wait before moving the enemy again
    int moveTime;

    // Amount of distance covered in one frame
    // Represents a portion of delta
    float moveSpeed = 0.3f;

    // Stores the x and y component of the direction vector
    XYPair<Float> direction;

    //Stores the color so we can switch between red and original color when hit
    Color originalColor;

    //Counts down the milliseconds until the enemy can move again
    int moveCounter;

    {
        // Used several times to randomize Enemy attributes
        Random randomizer = new Random();

        //  Generate random RGB values 50-150, divide by 255 to convert to float equivalent
        originalColor = new Color(
                (randomizer.nextInt(100)+50),
                (randomizer.nextInt(50)+50),
                (randomizer.nextInt(100)+50));

        //  Give enemy 20-40 health points
        this.maxHealth = (randomizer.nextInt(6)+5)*4;
        this.currentHealth = maxHealth;

        // Show player how much health they have by adjusting size
        // Multiplied by (ui-width / 800) so it scales to smaller screen sizes
        // I built the game in 800 x 800 and based sizes off of that
        this.size = (3 * maxHealth - 20) * MouseGame.ui.getWidth() / 800;

        this.moveTime = (maxHealth/4 - 4) * 100 - 10 * level;
        this.moveCounter = moveTime;
        boolean spotTaken;

        // Randomize the Enemy's position in a radius around the player
        // Repeat until the Enemy is in a spot that is not occupied by another Enemy
        do {
            spotTaken = false;
            // Randomize the angle around the circle that the Enemy will spawn at
            int posAngle = randomizer.nextInt(12) * 30 - 180;
            int x = MouseGame.ui.getWidth()/2 + (int) (MouseGame.ui.getWidth()/2 * Math.cos(Math.toRadians(posAngle)) - size / 2);
            int y = MouseGame.ui.getHeight()/2 - (int) (MouseGame.ui.getHeight()/2 * Math.sin(Math.toRadians(posAngle)) + size / 2);

            //  Make sure the Enemy is within the window
            if (x < 0) {    x = 0; }
            if (x + size > MouseGame.ui.getWidth()/2 * 2) {  x = MouseGame.ui.getWidth()/2 * 2 - size;    }
            if (y < 0) {    y = 0;  }
            if (y + size > MouseGame.ui.getHeight()) {    y = MouseGame.ui.getHeight() - size;  }

            this.hitbox.setBounds(x, y, size, size);

            // Make sure there is not already an enemy at this spot
            for (Enemy e : enemies)
            {
                if (this.hitbox.intersects(e.hitbox)) {spotTaken = true;}
            }
        }
        while (spotTaken);

        // Figure out the Enemy's movement vector
        // by converting coordinates to a Cartesian plane centered at the center of the window...
        XYPair<Float> clamped = new XYPair<>((float)(this.hitbox.x + size/2 - MouseGame.ui.getWidth()/2) / MouseGame.ui.getWidth()/2,(float)(this.hitbox.y + size/2 - MouseGame.ui.getHeight()/2)/MouseGame.ui.getHeight()/2);

        // Using those distances to get the angle they are at relative to the center...
        double angle = Math.atan2(clamped.y, clamped.x);

        // And using that angle to get the x and y component of the movement vector
        this.direction = new XYPair<>((float)(Math.cos(angle)), (float)(Math.sin(angle)));
    }

    @Override
    public void update(int delta) {
        //If we have waited enough frames to move
        if (moveCounter <= 0)
        {
            // move towards the center of the window (thus the inversion)
            this.hitbox.translate(-1*(int) (moveSpeed*direction.x*delta), -1*(int) (moveSpeed*direction.y*delta));
            moveCounter = moveTime;
        }

        // update move counter
        moveCounter -= delta;

        //Reset color - I figured checking to see if we need to reset color first is actually less efficient than just doing it
        this.r = originalColor.getRed()/255f;
        this.g = originalColor.getGreen()/255f;
        this.b = originalColor.getBlue()/255f;

        //If the enemy reaches the player
        if (this.intersects(player))
        {
            //Game over
            MouseGame.ui.destroy();
            return;
        }
        for (Bullet b: bullets) {
            // If hit by a bullet, absorb the bullet, lose 1 HP and flash red
            if (this.intersects(b)) {
                b.deactivate();

                currentHealth--;

                this.r = 1f;
                this.g = 0f;
                this.b = 0f;
            }
        }

        // If we don't have any more health...
        if (currentHealth <= 0)
        {
            // we disappear...
            this.deactivate();

            // and there's a 2 in 7 chance we'll drop a Collectible
            Random rand = new Random();
            if (rand.nextInt(7) < 2) {
                spawnCollectible(new Collectible((int) this.hitbox.getCenterX(), (int) this.hitbox.getCenterY()));
            }

            return;
        }

        // If we go outside the window, we disappear
        if  (   this.hitbox.x + size < 0 || this.hitbox.x > MouseGame.ui.getWidth()/2*2 ||
                this.hitbox.y + size < 0 || this.hitbox.y > MouseGame.ui.getHeight()  )
        {   this.deactivate(); }
    }
}

private class Collectible extends GameObject
{
    //Time before the collectible de-spawns, in milliseconds
    int maxTTL = 4000 - level*10;

    // Current TimeToLive counter
    int TTL = maxTTL;

    // Has the Collectible been captured via right-click?
    boolean captured = false;

    // How far the cursor was from the Collectible's origin point when it was captured
    XYPair<Integer> mouseOffset = new XYPair<>(0,0);

    public Collectible (int x, int y)
    {
        //Set center of the collectible in the center of the Enemy dropping it
        int size = (int) (35 * MouseGame.ui.getWidth() / 800.);
        this.hitbox.setBounds(x-size/2, y-size/2, size, size);
        this.setColor(0.9f, 0.9f, 0.1f);
    }

    @Override
    public void update(int delta) {
        // Count down TTL
        TTL -= delta;

        // If the timer ends, de-spawn
        if (TTL <= 0)
        {
            this.deactivate();
        }

        // If we are captured, sync position with mouse position
        if (captured) {
            // Reset TTL
            TTL = maxTTL;

            // update position based on current mouse position
            XYPair<Integer> mousePos = MouseGame.ui.getMouseLocation();
            this.hitbox.setLocation (mousePos.x + mouseOffset.x, mousePos.y + mouseOffset.y);

            // If the collectible is dragged to the player, collect it and level up if the quota has been met
            Rectangle center = new Rectangle((int) hitbox.getCenterX(), (int) hitbox.getCenterY(), 1, 1);
            if (center.intersects(player.getHitbox())) {
                release();
                collectedAmount++;
                if (collectedAmount >= (level + 1)) {
                    levelUp();
                    collectedAmount = 0;
                }
                this.deactivate();
            }
        }
    }

    public Collectible capture (XYPair<Integer> mousePos)
    {
        // Don't let a Collectible be captured twice at once
        if (captured) {return null;}
        // Determine how far this Collectible's origin is from the cursor and store for updates
        mouseOffset.x = this.hitbox.x - mousePos.x;
        mouseOffset.y = this.hitbox.y - mousePos.y;

        captured = true;

        // Return this object to the calling method
        return this;
    }

    // We no longer need to synchronize the Collectible with the mouse
    public void release() {captured = false;}

}

    // Spawn a wave of enemies
    public void spawnEnemies(int numEnemies)
    {
        for (int i =0; i<numEnemies; i++)
        {
            enemies.add(new Enemy());
        }
    }

    // Drop a collectible
    public void spawnCollectible(Collectible newC)
    {
        collectibles.add(newC);
    }

    public void levelUp()
    {
        // increase level (raises Enemy speed and lowers Collectible TTL)
        level++;

        // remove everything but the player
        clearField = true;

        // Set a new background color
        Random random = new Random();
        float r = (random.nextInt(101)+155) / 255f;
        float g = (random.nextInt(101)+155) / 255f;
        float b = (random.nextInt(101)+155) / 255f;
        GL11.glClearColor(r, g, b, 1.0f);
    }

    public Scene drawFrame(int delta)
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Shoot on left click, send Bullets toward mouse location
        if (MouseGame.ui.mouseButtonIsPressed(0))
        {
            XYPair<Integer> mouseLoc = MouseGame.ui.getMouseLocation();
            bullets.add(new Bullet(mouseLoc));
        }

        // Draw all of the objects
        player.update(delta);
        player.draw();

        for (Bullet b : bullets)
        {
            b.update(delta);
            b.draw();
        }
        for (Enemy e : enemies)
        {
            e.update(delta);
            e.draw();
        }
        for (Collectible c : collectibles)
        {

            c.draw();
            c.update(delta);
        }


        // Check for deactivated objects and clearField signal
        Iterator<Bullet> b = bullets.iterator();
        while (b.hasNext()) {
            GameObject o = b.next();
            if (! o.isActive() || clearField)
            {
                b.remove();
            }
        }
        Iterator<Enemy> e = enemies.iterator();
        while (e.hasNext()) {
            GameObject o = e.next();
            if (! o.isActive() || clearField)
            {
                e.remove();
            }
        }
        Iterator<Collectible> c = collectibles.iterator();
        while (c.hasNext()) {
            GameObject o = c.next();
            if (! o.isActive() || clearField)
            {
                c.remove();
            }
        }

        // reset clearField once it's been taken care of
        if (clearField) {clearField = false;}

        // if no enemies remain, spawn a new wave
        if (enemies.isEmpty())
        {
            spawnEnemies(6);
        }

        return this;
    }
}

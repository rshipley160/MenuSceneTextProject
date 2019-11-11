import edu.utc.game.Scene;
import edu.utc.game.SimpleMenu;
import edu.utc.game.Sound;
import edu.utc.game.Text;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class PauseMenu extends SimpleMenu {

    private Text score = new Text (MouseGame.ui.getWidth()/2-120, MouseGame.ui.getHeight()/2, 30, 30, "Score: "+MouseGame.main.getScore());
    private Sound pauseSound = new Sound("res\\pause.wav");

    public PauseMenu() {
        MouseGame.mainMenu.reset();
        pauseSound.play();

        setBackground(0.2f, 0.2f, 0.2f);

        addItem(new SimpleMenu.SelectableText(MouseGame.ui.getWidth()/2-120, MouseGame.ui.getHeight()/2-100, 30, 30, "Resume", 0.4f, 0.6f, 0.8f, 1f, 1f, 1f), MouseGame.main);

        addItem(new SimpleMenu.SelectableText(MouseGame.ui.getWidth()/2-120, MouseGame.ui.getHeight()/2-50, 30, 30, "Main Menu", 0.4f, 0.6f, 0.8f, 1f, 1f, 1f), MouseGame.mainMenu);

        select(0);
    }

    @Override
    public Scene drawFrame(int delta) {

        glClearColor(bgR, bgG, bgB, .0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        if (go) {
            if (items.get(selected).scene.equals(MouseGame.mainMenu))
                MouseGame.main.reset();
            return items.get(selected).scene;
        }

        for (Item item : items)
        {
            item.label.update(delta);
            item.label.draw();
        }

        score.draw();

        return this;
    }
}

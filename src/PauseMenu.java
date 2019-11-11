import edu.utc.game.Scene;
import edu.utc.game.SimpleMenu;
import edu.utc.game.Text;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class PauseMenu extends SimpleMenu {

    private Text score = new Text (MouseGame.ui.getWidth()/4, MouseGame.ui.getHeight()/4, 40, 40, "Score: "+MouseGame.main.getScore());
    public PauseMenu() {
        setBackground(0.2f, 0.2f, 0.6f);

        addItem(new SimpleMenu.SelectableText(20, 20, 20, 20, "Resume", 0.4f, 0.6f, 0.8f, 1f, 1f, 1f), MouseGame.main);

        addItem(new SimpleMenu.SelectableText(20, 60, 20, 20, "Exit Game", 0.4f, 0.6f, 0.8f, 1f, 1f, 1f), MouseGame.main);

        select(0);
    }

    @Override
    public Scene drawFrame(int delta) {
        glClearColor(bgR, bgG, bgB, .0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        if (go) { return items.get(selected).scene; }

        for (Item item : items)
        {
            item.label.update(delta);
            item.label.draw();
        }

        return this;
    }
}

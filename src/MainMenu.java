import edu.utc.game.Scene;
import edu.utc.game.SimpleMenu;
import javafx.util.Pair;

import java.util.Dictionary;
import java.util.Iterator;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MainMenu extends SimpleMenu {
    public MainMenu (Map<String, Scene> sceneOptions){
        Iterator<String> it = sceneOptions.keySet().iterator();
        int count = 0;
        while( it.hasNext()) {
            String nextItem = it.next();
            addItem(new SimpleMenu.SelectableText(20, 20+count*40, 20, 20, nextItem, 0f, 0f, 1, 1, 1, 1), sceneOptions.get(nextItem));
            count++;
        }
        select(0);
    }

    @Override
    public Scene drawFrame(int delta) {
        glClearColor(1f, 1f, 1f, .0f);
        glClearColor(.0f, .0f, .0f, .0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        if (super.go) { return items.get(selected).scene; }

        for (Item item : items)
        {
            item.label.update(delta);
            item.label.draw();
        }

        return this;
    }
}

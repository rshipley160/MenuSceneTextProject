import edu.utc.game.Scene;
import edu.utc.game.SimpleMenu;
import javafx.util.Pair;

import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MainMenu extends SimpleMenu {


    public MainMenu (List<String> textOptions, List<Scene> sceneOptions){
        for (int i = 0; i < textOptions.size(); i++)
        {
            addItem(new SimpleMenu.SelectableText(20, 20+i*40, 20, 20, textOptions.get(i), 0.4f, 0.6f, 0.8f, 1f, 1f, 1f), sceneOptions.get(i));
        }
        select(0);
    }

}

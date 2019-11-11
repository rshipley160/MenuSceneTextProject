import edu.utc.game.Scene;
import edu.utc.game.SimpleMenu;

import java.util.List;


public class MainMenu extends SimpleMenu {


    public MainMenu (List<String> textOptions, List<Scene> sceneOptions){
        setBackground(0.2f, 0.2f, 0.2f);
        for (int i = 0; i < textOptions.size(); i++)
        {
            addItem(new SimpleMenu.SelectableText(MouseGame.ui.getWidth()/2-120, MouseGame.ui.getHeight()/2-100+i*50, 30, 30, textOptions.get(i), 0.4f, 0.6f, 0.8f, 1f, 1f, 1f), sceneOptions.get(i));
        }
        select(0);
    }

}

import edu.utc.game.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.awt.*;
import java.lang.ref.Cleaner;
import java.nio.DoubleBuffer;
import java.nio.charset.IllegalCharsetNameException;
import java.util.*;
import java.util.List;


public class MouseGame extends Game{

    static SimpleMenu mainMenu;
    static MainScene main;
    static PauseMenu pause;

    public static void main(String[] args)
    {
        MouseGame game = new MouseGame();
        main = new MainScene();
        pause = new PauseMenu();
        List<String> textOptions = Arrays.asList("Start","Exit Game");
        List<Scene> sceneOptions = Arrays.asList(main,null);

        game.registerGlobalCallbacks();


        mainMenu = new MainMenu(textOptions, sceneOptions);
        game.setScene(mainMenu);
        game.gameLoop();
    }


    public MouseGame() {
        initUI(400, 400, "Mouse Game");
        ui.showMouseCursor(true);

    }



}

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

    public static void main(String[] args)
    {
        MouseGame game = new MouseGame();
        Map<String, Scene> options = new HashMap<>();

        game.registerGlobalCallbacks();

        Scene main = new MainScene();
        options.put("Start",main);
        options.put("Exit Game", null);
        Scene mainMenu = new MainMenu(options);
        game.setScene(mainMenu);
        game.gameLoop();
    }


    public MouseGame() {
        initUI(400, 400, "Mouse Game");
        ui.showMouseCursor(true);

    }



}

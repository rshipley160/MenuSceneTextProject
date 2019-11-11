import edu.utc.game.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.CallbackI;

public class VictoryScene implements Scene {
    private Text youWin = new Text(MouseGame.ui.getWidth()/2-120, MouseGame.ui.getHeight()/2-100, 30, 30, "You Win!");
    private Text score = new Text(MouseGame.ui.getWidth()/2-120, MouseGame.ui.getHeight()/2-50, 30, 30, "Score: "+MouseGame.main.getScore());
    int minutes = MouseGame.main.getTime()/60000;
    int seconds = (MouseGame.main.getTime()-(minutes*60000))/1000;
    private Text time = new Text(MouseGame.ui.getWidth()/2-120, MouseGame.ui.getHeight()/2, 30, 30, "Time: "+
            minutes + " m " + seconds + " s");
    private Text returnBtnText = new Text(MouseGame.ui.getWidth()/2-120, MouseGame.ui.getHeight()/2+70, 30, 30, "Return to main menu ");
    private GameObject returnBtn = new GameObject();

    private Sound victory = new Sound("res\\Victory.wav");

    private Scene nextScene = this;

    @Override
    public void onMouseEvent(int button, int action, int mods) {
        if (button == 0 && action == GLFW.GLFW_PRESS)
        {
            XYPair<Integer> mousePos = MouseGame.ui.getMouseLocation();
            if (mousePos.x >= returnBtn.getHitbox().x && mousePos.x <= returnBtn.getHitbox().x + returnBtn.getHitbox().width &&
                    mousePos.y >= returnBtn.getHitbox().y && mousePos.y <= returnBtn.getHitbox().y + returnBtn.getHitbox().height) {
                MouseGame.mainMenu.reset();
                MouseGame.main.reset();
                nextScene = MouseGame.mainMenu;
            }
        }
    }

    {
        GL11.glClearColor(0.5f, 0.5f, 1f, 0f);
        victory.play();
        returnBtn.getHitbox().setBounds(MouseGame.ui.getWidth()/2-120, MouseGame.ui.getHeight()/2+65, 300, 40);
        returnBtn.setColor(0f, 0.6f, 1f);
    }


    @Override
    public Scene drawFrame(int delta) {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        youWin.draw();
        score.draw();
        time.draw();
        returnBtn.draw();
        returnBtnText.draw();

        return nextScene;
    }
}

package gdxtwo.desktop;

import GameV1.GameCore;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1280;
        config.height = 720;
        //config.width = 1320;
        //config.height = 860;
        config.fullscreen = false;
        config.foregroundFPS = 600;
        config.backgroundFPS = 600;
        new LwjglApplication(new GameCore(), config);
    }
}

package gdxtwo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import gdxtwo.gdxtwo;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1024;
        config.height = 768;
        config.fullscreen = false;
        config.foregroundFPS = 60;
        config.backgroundFPS = 10;
        new LwjglApplication(new gdxtwo(), config);
    }
}

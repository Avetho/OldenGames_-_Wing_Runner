package gdxtwo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.math.*;
import java.io.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.viewport.*;

public class gdxtwo extends ApplicationAdapter implements InputProcessor {

    SpriteBatch batch;
    Texture imgreticle, imgsprite, imgobstacle, imgbackground, imgpause;
    Sprite spReticle, spChar, spObs1, spObs2, spObs3, spBG;
    int nPosX, nPosY, nCursorX, nCursorY;
    boolean isTouch, isPaused;
    private ExtendViewport viewport;

    @Override
    public void create() {
        OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (Gdx.app.getType() == ApplicationType.Android) {
            int ANDROID_WIDTH = Gdx.graphics.getWidth();
            int ANDROID_HEIGHT = Gdx.graphics.getHeight();
            camera = new OrthographicCamera(ANDROID_WIDTH, ANDROID_HEIGHT);
            camera.translate(ANDROID_WIDTH / 2, ANDROID_HEIGHT / 2);
            camera.update();
        } else {
            camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            camera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            camera.update();
        }
        batch = new SpriteBatch();
        imgpause = new Texture("pausedImg.png");
        imgreticle = new Texture("badlogic.jpg");
        imgsprite = new Texture("characterSprite0.png");
        imgobstacle = new Texture("rockObstacle.png");
        imgbackground = new Texture("imgBG.jpg");
        spObs1 = new Sprite(imgobstacle);
        spObs2 = new Sprite(imgobstacle);
        spObs3 = new Sprite(imgobstacle);
        spBG = new Sprite(imgbackground);
        spBG.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spReticle = new Sprite(imgreticle);
        spReticle.setSize(5, 5);
        spReticle.setCenter(spReticle.getWidth() / 2, spReticle.getHeight() / 2);
        spChar = new Sprite(imgsprite);
        spChar.setScale(1, 1);
        spChar.setCenter(spChar.getWidth() / 2, spChar.getHeight() / 2);
        //spChar.setOrigin(spChar.getWidth()/2, spChar.getHeight()/2);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        if(isPaused!=isPaused)System.out.println(isPaused);
        spChar.setRotation(-(45));
        //if(Gdx.input.isKeyPressed(Keys.F11))Gdx.graphics.setDisplayMode(Gdx.graphics.);
        Gdx.gl.glClearColor(0.128f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!isPaused)nPosX = Gdx.graphics.getWidth() / 5;
        if (!isPaused)nPosY = Gdx.graphics.getHeight() - Gdx.input.getY();//Fix Y coordinate to be a better way of getting mouse/tap Y.
        batch.begin();
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            isPaused = true;
        }
        while (isPaused == true) {
            System.out.println("Paused.");
            batch.draw(imgpause, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
            if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
                System.out.println("Goodbye!");
                batch.end();
                System.exit(3);
            } else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
                System.out.println("Unpaused.");
                isPaused = false;
            } else {
                System.out.println("Waiting.");
                isPaused = true;
                batch.end();
            }
        }
        if (!isPaused)batch.draw(spBG, 0, 0);
        if (!isPaused)batch.draw(spObs1, Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2);
        if (!isPaused)batch.draw(spObs2, Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 2 - Gdx.graphics.getHeight() / 3);
        if (!isPaused)batch.draw(spObs3, Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 3);
        if (!isPaused)batch.draw(spChar, nPosX, nPosY, spChar.getOriginX(), spChar.getOriginY(), spChar.getHeight(), spChar.getWidth(), spChar.getScaleX(), spChar.getScaleY(), spChar.getRotation(), true);
        if (!isPaused)batch.draw(spReticle, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), spReticle.getOriginX(), spReticle.getOriginY(), spReticle.getWidth(), spReticle.getHeight(), spReticle.getScaleX(), spReticle.getScaleY(), spReticle.getRotation());
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // viewport must be updated for it to work properly
        //viewport.update(width, height, true);
    }

    @Override
    public boolean keyDown(int i) {
        /*if (Input == Keys.ESCAPE) {
         return true;//This senses Escape key to exit program.
         }
         if (Input.Buttons == Keys.F11) {
         if (config.fullscreen == false) {
         Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayModes());
         } else {
         config.fullscreen = false;
         }
         return true;
         } else {*/
        return false;
        //}
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
/*
 * private void handleInput() {
 if (Gdx.input.isKeyPressed(Input.Keys.A)) {
 cam.zoom += 0.02;
 }
 if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
 cam.zoom -= 0.02;
 }
 if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
 cam.translate(-3, 0, 0);
 }
 if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
 cam.translate(3, 0, 0);
 }
 if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
 cam.translate(0, -3, 0);
 }
 if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
 cam.translate(0, 3, 0);
 }
 if (Gdx.input.isKeyPressed(Input.Keys.W)) {
 cam.rotate(-rotationSpeed, 0, 0, 1);
 }
 if (Gdx.input.isKeyPressed(Input.Keys.E)) {
 cam.rotate(rotationSpeed, 0, 0, 1);
 }

 cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 100/cam.viewportWidth);

 float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
 float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

 cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
 cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
 }
 */

/*       Boolean fullScreen = Gdx.graphics.isFullscreen();
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
        if (fullScreen == true)
            Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
        else
            Gdx.graphics.setFullscreenMode(currentMode);
*/
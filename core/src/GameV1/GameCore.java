package GameV1;

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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.viewport.*;
import java.util.Random;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.awt.geom.Ellipse2D;

public class GameCore extends ApplicationAdapter implements InputProcessor {

    //Temp Variables
    double dR1S = 1, dR2S = 1, dR3S = 1;
    Ellipse2D elHB1, elHB2, elHB3, elHBP;
    Vector2 vChar = new Vector2();
    Vector2 vObs1 = new Vector2();
    Vector2 vObs2 = new Vector2();
    Vector2 vObs3 = new Vector2();
    Vector2 vRet = new Vector2();
    Random rand = new Random();
    SpriteBatch batch;
    ShapeRenderer renderHB;
    Texture imgReticle, imgSprite, imgSprite2, imgObstacle, imgObstacle2, imgBg, imgBg2, imgPause, imgBox, imgMenu, imgStart, imgMusic;
    Sprite spReticle, spChar, spObs1, spObs2, spObs3, spObs4, spObs5, spBG, spBox, spMenuBG, spStart, spMusic;
    int nCursorX, nCursorY, nWindW, nWindH, nRockX1, nRockX2, nRockX3, nScore;
    float fCharRot, fCharMove, fCharAdditive, fPosX;
    boolean isTouch, isPaused, isMenu = true, isMusicOn, isMMusic, isMusicEnable, isMusicClassic = false;
    private ExtendViewport viewport;
    Music menuMusic, bgMusic, menuMusicFly, menuMusicSci, bgMusicFly, bgMusicSci;
    GameCore game;
    EntityPlayer objPlayer;
    float fbgX = 0;
    OrthographicCamera camera;

    @Override
    public void create() {
        nScore = 0;
        nWindW = Gdx.graphics.getWidth();
        nWindH = Gdx.graphics.getHeight();
        nRockX1 = nWindW * 3;
        nRockX2 = nWindW * 6;
        nRockX3 = nWindW * 9;
        /*if (Gdx.app.getType() == ApplicationType.Android) {
            int ANDROID_WIDTH = nWindW;
            int ANDROID_HEIGHT = nWindH;
            camera = new OrthographicCamera(ANDROID_WIDTH, ANDROID_HEIGHT);
            camera.translate(ANDROID_WIDTH / 2, ANDROID_HEIGHT / 2);
            camera.update();
        } else {
            camera = new OrthographicCamera(nWindW, nWindH);
            camera.setToOrtho(false, nWindW, nWindH);
            camera.translate(nWindW / 2, nWindH / 2);
            camera.update();
        }*/
            camera = new OrthographicCamera(nWindW, nWindH);
            camera.setToOrtho(false, nWindW, nWindH);
            camera.translate(nWindW / 2, nWindH / 2);
            camera.update();
        batch = new SpriteBatch();
        renderHB = new ShapeRenderer();
        imgMenu = new Texture("Main_Menu.png");
        imgPause = new Texture("pausedImg.png");
        imgReticle = new Texture("badlogic.jpg");
        imgSprite = new Texture("characterSprite1.png");
        imgSprite2 = new Texture("characterSprite0.png");
        imgObstacle = new Texture("rockObstacleL2.png");
        imgObstacle2 = new Texture("rockObstacleL.png");
        imgBg = new Texture("imgBG2.jpg");
        imgBg2 = new Texture("imgBG.jpg");
        imgBox = new Texture("RectBarr.jpg");
        imgStart = new Texture("StartButton.png");
        imgMusic = new Texture("MusicButton.png");
        spMenuBG = new Sprite(imgMenu);
        spMenuBG.setSize(nWindW, nWindH);
        spBox = new Sprite(imgBox);
        spBox.setScale(nWindW, nWindH/15);
        spObs1 = new Sprite(imgObstacle);
        spObs2 = new Sprite(imgObstacle);
        spObs3 = new Sprite(imgObstacle);
        //spObs4 = new Sprite(imgobstacle);
        //spObs5 = new Sprite(imgobstacle);
        spBG = new Sprite(imgBg);
        spBG.setSize(nWindW, nWindH);
        spReticle = new Sprite(imgReticle);
        spReticle.setOrigin(spReticle.getWidth() / 2, spReticle.getHeight() / 2);
        spReticle.setSize(nWindW / 200, nWindW / 200);
        spChar = new Sprite(imgSprite);
        spChar.setCenter(spChar.getWidth(), spChar.getHeight());
        spChar.setSize(nWindH / 12/*5*/, nWindH / 15);
        Gdx.input.setInputProcessor(this);
        menuMusicFly = Gdx.audio.newMusic(Gdx.files.internal("gliding_by_isaac_wilkins.ogg"));
        bgMusicFly = Gdx.audio.newMusic(Gdx.files.internal("gliding_by_isaac_wilkins_loop.ogg"));
        menuMusicSci = Gdx.audio.newMusic(Gdx.files.internal("lucs-200th_floor.ogg"));//Hint next time:
        bgMusicSci = Gdx.audio.newMusic(Gdx.files.internal("Astrum-Wormhole.ogg"));//Dont make track sets the same!
        menuMusic = menuMusicFly;
        bgMusic = bgMusicFly;
        objPlayer = new EntityPlayer();
        vRet.set(nWindW * 2 / 3, nWindH / 2);
        dR1S = (rand.nextDouble()+1)*12;
        dR2S = (rand.nextDouble()+1)*12;
        dR3S = (rand.nextDouble()+1)*12;
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (isMenu == false) {
                isMenu = true;
            } else {
                System.exit(3);
            }
        }
        if (isMenu) {
            batch.begin();
            batch.draw(imgMenu, 0, 0, nWindW, nWindH);
            batch.end();
            if (menuMusic.isPlaying() == false) {
                bgMusic.stop();
                menuMusic.play();
                menuMusic.setLooping(true);//music code came from http://stackoverflow.com/questions/27767121/how-to-play-music-in-loop-in-libgdx
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                menuMusic.setLooping(false);
                menuMusic.stop();
                isMenu = false;
            }
        } else if (isMenu == false) {
            nScore++;
            if (bgMusic.isPlaying() == false) {
                bgMusic.setLooping(true);
                bgMusic.play();//music code came from http://stackoverflow.com/questions/27767121/how-to-play-music-in-loop-in-libgdx
            }
            //nCursorX = Gdx.input.getX();
            nCursorY = nWindH - Gdx.input.getY();
            //fCharRot = findAngle(fPosX, fPosY, nWindW * 2 / 3, nCursorY);
            fCharRot = findAngle2(vChar, vRet);
            fCharMove = (vRet.y - vChar.y) / 13;
            vChar.add(0, fCharMove);
            spChar.setRotation(fCharRot);
            fPosX = nWindW / 5;
            vRet.set(nWindW * 2 / 3, nCursorY - spReticle.getHeight() / 2);
            //if(Gdx.input.isKeyPressed(Keys.F11))Gdx.graphics.setDisplayMode(Gdx.graphics.);
            Gdx.gl.glClearColor(0.128f, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            fPosX = nWindW / 5;
            //batch.setTransformMatrix(game.getCamera().view);
            //camera.update();
            fbgX-=3;
            if(fbgX < -nWindW) {
                fbgX=0;
            }
            fCharMove += nWindH/252;
            if(vObs1.x > -spObs1.getWidth()) {
                //nRockX1-=dR1S;
                vObs1.sub((float)dR1S, 0);
            } else {
                dR1S = (rand.nextDouble()+1)*8;
                //nRockX1 = nWindW + nWindW/10;
                vObs1.set(nWindW + nWindW/10, 0);
            }
            if(vObs2.x > -spObs2.getWidth()) {
                //nRockX2-=dR2S;
                vObs2.sub((float)dR2S, 0);
            } else {
                dR2S = (rand.nextDouble()+1)*10;
                //nRockX2 = nWindW + nWindW/10;
                vObs2.set(nWindW + nWindW/10, 0);
            }
            if(vObs3.x > -spObs3.getWidth()) {
                //nRockX3-=dR3S;
                vObs3.sub((float)dR3S, 0);
            } else {
                dR3S = (rand.nextDouble()+1)*12;
                //nRockX3 = nWindW + nWindW/10;
                vObs3.set(nWindW + nWindW/10, 0);
            }
            vObs1.set(vObs1.x, nWindH - nWindH / 4 - spObs1.getHeight() / 2 + nWindH / 12);
            vObs2.set(vObs2.x, nWindH / 2 - spObs2.getHeight() / 2);
            vObs3.set(vObs3.x, nWindH / 2 - nWindH / 4 - spObs3.getHeight() / 2 - nWindH / 12);
            elHB1.setFrame(vObs1.x, vObs1.y, spObs1.getWidth(), spObs1.getWidth());
            elHB2.setFrame(vObs2.x, vObs2.y, spObs2.getWidth(), spObs2.getWidth());
            elHB3.setFrame(vObs3.x, vObs3.y, spObs3.getWidth(), spObs3.getWidth());
            elHBP.setFrame(vChar.x, vChar.y, spChar.getHeight(), spChar.getHeight());
            
            ////////////////////////////////////////
            
            if(elHBP.getCenterX()-elHB1.getCenterX()<elHB1.getWidth() ||
                    elHBP.getCenterX()-elHB2.getCenterX()<elHB2.getWidth() ||
                    elHBP.getCenterX()-elHB3.getCenterX()<elHB3.getWidth()) {
                if(elHBP.getCenterY()-elHB1.getCenterY()<elHB1.getHeight() ||
                    elHBP.getCenterY()-elHB2.getCenterY()<elHB2.getHeight() ||
                    elHBP.getCenterY()-elHB3.getCenterY()<elHB3.getHeight()) {
                    isMenu=true;
                    System.out.println(nScore);
                    create();
                }
            }
            
            ////////////////////////////////////////
            
            batch.begin();
            //batch.setProjectionMatrix(camera.combined);

            batch.draw(spBG, fbgX, 0, nWindW, nWindH);
            batch.draw(spBG, fbgX + nWindW, 0, nWindW, nWindH);
            batch.draw(spBox, 0, nWindH);
            batch.draw(spBox, 0, 0);
            batch.draw(spObs1, vObs1.x, vObs1.y, nWindH/3 - nWindH/15, nWindH/3 - nWindH/15);
            batch.draw(spObs2, vObs2.x, vObs2.y, nWindH/3 - nWindH/15, nWindH/3 - nWindH/15);
            batch.draw(spObs3, vObs3.x, vObs3.y, nWindH/3 - nWindH/15, nWindH/3 - nWindH/15);
            //batch.draw(spObs4, Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 5, 45);
            //batch.draw(spObs5, Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 5, 45);
            batch.draw(spChar, fPosX - spChar.getWidth() / 2, vChar.y - spChar.getHeight() / 2, spChar.getOriginX(), spChar.getOriginY(), spChar.getHeight(), spChar.getWidth(), spChar.getScaleX(), spChar.getScaleY(), spChar.getRotation(), true);
            //batch.draw(spChar, fPosX - spChar.getWidth() / 2, fPosY - spChar.getHeight() / 2, spChar.getOriginX(), spChar.getOriginY(), spChar.getHeight(), spChar.getWidth(), spChar.getScaleX(), spChar.getScaleY(), spChar.getRotation(), true);
            batch.draw(spReticle, vRet.x, vRet.y, spReticle.getOriginX(), spReticle.getOriginY(), spReticle.getWidth(), spReticle.getHeight(), spReticle.getScaleX(), spReticle.getScaleY(), spReticle.getRotation());
            //batch.end();
            
            batch.end();
            /*renderHB.begin(ShapeType.Line);
            renderHB.setColor(0, 0, 0, 0);
            renderHB.circle(vObs1.x - spObs1.getWidth()/2, vObs1.y - spObs1.getHeight()/2, spObs1.getHeight()/2);
            renderHB.circle(vObs2.x - spObs2.getWidth()/2, vObs2.y - spObs2.getHeight()/2, spObs2.getHeight()/2);
            renderHB.circle(vObs3.x - spObs3.getWidth()/2, vObs3.y - spObs3.getHeight()/2, spObs3.getHeight()/2);
            renderHB.circle(vChar.x - spChar.getWidth()/2, vChar.y - spChar.getHeight()/2, spChar.getHeight()/2);
            renderHB.end();*/
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                menuMusic.stop();
                bgMusic.stop();
                if(isMusicClassic) {
                    spChar = new Sprite(imgSprite);
                    spChar.setSize(nWindH / 12/*5*/, nWindH / 15);
                    spObs1 = new Sprite(imgObstacle);
                    spObs2 = new Sprite(imgObstacle);
                    spObs3 = new Sprite(imgObstacle);
                    spBG = new Sprite(imgBg);
                    spBG.setSize(nWindW, nWindH);
                    menuMusic = menuMusicFly;
                    menuMusic.setVolume(2f);
                    bgMusic = bgMusicFly;
                    bgMusic.setVolume(2f);
                    isMusicClassic = false;
                } else {
                    spChar = new Sprite(imgSprite2);
                    spChar.setSize(nWindH / 12/*5*/, nWindH / 15);
                    spObs1 = new Sprite(imgObstacle2);
                    spObs2 = new Sprite(imgObstacle2);
                    spObs3 = new Sprite(imgObstacle2);
                    spBG = new Sprite(imgBg2);
                    spBG.setSize(nWindW, nWindH);
                    menuMusic = menuMusicSci;
                    menuMusic.setVolume(0.2f);
                    bgMusic = bgMusicSci;
                    bgMusic.setVolume(0.2f);
                    isMusicClassic = true;
                }
            }
    }

    /*public float findAngle(double dX1, double dY1, double dX2, double dY2) {
        float fAngle;
        fAngle = (float) Math.toDegrees(Math.atan2(dY1 - dY2, dX1 - dX2));
        return fAngle;
    }*/

    public float findAngle2(Vector2 vObj1, Vector2 vObj2) {
        float fAngle;
        //System.out.println(vObj1.y - vObj2.y);
        //System.out.println(vObj1.x - vObj2.x);
        fAngle = (float) Math.toDegrees(Math.atan2(vObj1.y - vObj2.y, vObj1.x - vObj2.x));
        return fAngle - 90;
    }

    @Override
    public void resize(int width, int height) {
        // viewport must be updated for it to work properly
        //viewport.update(width, height);
        //camera.update();
    }
    
    @Override
    public void dispose() {
        //img.dispose();
        //item.dispose();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
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


/*       Boolean fullScreen = Gdx.graphics.isFullscreen();
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
        if (fullScreen == true)
            Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
        else
            Gdx.graphics.setFullscreenMode(currentMode);
*/
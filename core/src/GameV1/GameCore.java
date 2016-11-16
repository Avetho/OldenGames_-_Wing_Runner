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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.viewport.*;
import java.util.Random;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.awt.geom.Ellipse2D;
import com.badlogic.gdx.utils.Timer;

public class GameCore extends ApplicationAdapter implements InputProcessor {
    //Permanent Variables
    //Ellipse2D elHB1, elHB2, elHB3, elHBP; //Was going to be hit boxes. Pythagorean Theorem is better at it :/
    Vector2 vChar; //Character position.
    Vector2 vObs1; //Top Rock position.
    Vector2 vObs2; //Middle Rock position.
    Vector2 vObs3; //Bottom Rock position.
    Vector2 vRet; //Mouse position on y coordinates. The Badlogic logo is the character rotation aim point.
    Random rand; //To randomize the rocks' speeds and other things.
    SpriteBatch batch; //To draw all the things.
    ShapeRenderer renderHB; //To render all the shapes, of which are none as of yet.
    Texture imgReticle, imgSprite, imgSprite2, imgObstacle, imgObstacle2, imgBg, imgBg2, imgPause, imgBox, imgMenu, imgMenuInv, imgStart, imgMusic; //All that you see are belong to these.
    Sprite spReticle, spChar, spObs1, spObs2, spObs3, spObs4, spObs5, spBG, spBox, spMenuBG, spStart, spMusic, spHitRestore; //All the stuff are belong to here.
    int nCursorX, nCursorY, nWindW, nWindH, nRockX1, nRockX2, nRockX3; //More generic variables.
    float fCharRot, fCharMove, fCharAdditive, fPosX; //Generic variables for the game.
    boolean isTouch, isPaused, isMenu, isMusicOn, isMMusic, isMusicEnable, isMusicClassic; //If you are doing these things.
    boolean hasHit1, hasHit2, hasHit3; //To check if you have hit a rock.
    double dR1S = 1, dR2S = 1, dR3S = 1; //Moving across at the speed of rock! These are the rocks' speeds.
    //private ExtendViewport viewport; //This was to fix the y coordinates.
    Music menuMusic, bgMusic, menuMusicFly, menuMusicSci, bgMusicFly, bgMusicSci; //The tracks. First two are the played ones, last ones are just to set the playing ones.
    GameCore game; //The game. A new instance is created.
    EntityPlayer objPlayer; //The player. What else? A new instance is created.
    float fbgX = 0; //So the background scrolls. It was not intended to be uniform with the rocks.
    OrthographicCamera camera;
    int nLives, nScore;
    BitmapFont fontGeneric; //http://stackoverflow.com/questions/12466385/how-can-i-draw-text-using-libgdx-java
    double dRockSpeed, dRockSpeedO; //dRockSpeedO is original speed so only one number has to be changed.
    float fSpeedMod; //Changes vertical speed based on rotation. Up is slower than down.
    int nScoreInc; //Every time this hits 1000 it is set to 0 after incrementing the score by one: nScore++;
    boolean justStarted; //Gives you a delay from starting to avoid 3 score instantly, and to avoid losing a life at the start.
    int nTime; //Counting until you are free from danger at the start.
    int nMode; //The mode of the game. 1 is easy, 2 is intermediate, 3 is hard, 4 is colormania, 5 is shadow.

    @Override
    public void create() {
        dRockSpeed = 5;
        nMode = 1;
        dRockSpeedO = dRockSpeed;
        fontGeneric = new BitmapFont();
        justStarted = true;
        fSpeedMod = 0;
        nLives = 3;
        hasHit1 = false;
        hasHit2 = false;
        hasHit3 = false;
        vChar = new Vector2();
        vObs1 = new Vector2();
        vObs2 = new Vector2();
        vObs3 = new Vector2();
        vRet = new Vector2();
        rand = new Random();
        isMenu = true;
        isMusicClassic = false;
        nScore = 0;
        nWindW = Gdx.graphics.getWidth();
        nWindH = Gdx.graphics.getHeight();
        nRockX1 = nWindW * 30;
        nRockX2 = nWindW * 60;
        nRockX3 = nWindW * 90;
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
        imgMenu = new Texture("Main_Menu2.png");
        imgMenuInv = new Texture("Main_Menu2-Invis.png");
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
        fontGeneric.setColor(Color.RED);
        fontGeneric.getData().scale(1/(860/nWindH));//http://stackoverflow.com/questions/33633395/how-set-libgdx-bitmap-font-size
            /*elHB1 = new Ellipse2D.Double(vObs1.x, vObs1.y, spObs1.getWidth(), spObs1.getWidth());
            elHB2 = new Ellipse2D.Double(vObs2.x, vObs2.y, spObs2.getWidth(), spObs2.getWidth());
            elHB3 = new Ellipse2D.Double(vObs3.x, vObs3.y, spObs3.getWidth(), spObs3.getWidth());
            elHBP = new Ellipse2D.Double(vChar.x, vChar.y, spChar.getHeight(), spChar.getHeight());*/
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
            if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                nMode++;
                if(nMode == 6) {
                    nMode = 1;
                }
            }
            if(justStarted) {
                batch.draw(imgMenu, 0, 0, nWindW, nWindH);
            }
            else {
                batch.draw(imgMenuInv, 0, 0, nWindW, nWindH);
            }
            fontGeneric.draw(batch, "Score: " + Integer.toString(nScore), nWindW/50, nWindH);
            fontGeneric.draw(batch, "Mode: " + Integer.toString(nMode), nWindW/25, nWindH/2);
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
            if(nMode == 5) {
                batch.setColor(128, 128, 128, 50); //A fun 'bug' I came across while color changing was a possible "Shadow" mode.
            }
            else {
                batch.setColor(Color.WHITE);
            }
            if(nMode == 5) {
                batch.setColor(0, 0, 0, 100); //You can barely see the player, rocks, and no background, just all on a crimson color backdrop.
            }
            if(nMode == 4) {
                batch.setColor(rand.nextInt(255 - 200 + 1), rand.nextInt(255 - 200 + 1), rand.nextInt(255 - 200 + 1), 100);
            }
            if(nTime >= 150) {
                justStarted = false;
            }
            if(nTime < 150) {
                nTime++;
            }
            if(nScore % 100 == 0 && nScore != 0) {
                nLives++;
                nScore++;
            }
            if(Gdx.input.justTouched()) {
                hasHit1 = false;
                hasHit2 = false;
                hasHit3 = false;
            }
            if (bgMusic.isPlaying() == false) {
                bgMusic.setLooping(true);
                bgMusic.play();//music code came from http://stackoverflow.com/questions/27767121/how-to-play-music-in-loop-in-libgdx
            }
            if(dRockSpeed > 3) {
                dRockSpeed -= 0.00025 - dRockSpeed/49126;
            }
            //nCursorX = Gdx.input.getX();
            nCursorY = nWindH - Gdx.input.getY();
            //fCharRot = findAngle(fPosX, fPosY, nWindW * 2 / 3, nCursorY);
            fCharRot = findAngle2(vChar, vRet);
            fCharMove = (vRet.y - vChar.y) / 13;
            //Grants vertical movement, slows upward speed more than downward using fSpeedMod.
            vChar.add(0, fCharMove*(1/(float)dRockSpeed)+fSpeedMod);
            //Rotates the player. Makes it look cool and advanced. It is.
            spChar.setRotation(fCharRot);
            //Lets you know where you are going when not using a mouse.
            vRet.set(nWindW * 2 / 3, nCursorY - spReticle.getHeight() / 2);
            //Makes background a cool color.
            Gdx.gl.glClearColor(0.128f, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            //Rotation conversion by inversion. Makes calculations easier afterward by keeping it positive ;)
            if(fCharRot > 0) { //Sets the rotation to a smoother number set for subsequent calculations.
                fCharRot-=180;
            }
            else if(fCharRot < 0) { //Sets the rotation to a smoother number set for subsequent calculations.
                fCharRot+=180;
            }
            //Makes character move a bit forward when pitching down and back when pitching up. Can look like a flying fish when pitching fast enough XD
            if(fCharRot > 0) {
                fPosX = nWindW / 5 + (fCharRot-180)*2;
            }
            else if(fCharRot < 0) {
                fPosX = nWindW / 5 - (fCharRot+180)*2;
            }
            //Makes character move slower up than down. Effect becomes moot at higher speeds as it doesn't scale with speed. At higher speed a lower pitch is needed to maintain altitude. Somewhat simulates drag.
            fSpeedMod = (fCharRot+120)/120;
            //Moving background picture. Looks cool and gets weird under high acceleration, like it desyncs. That isn't an issue, it a warp feature.
            fbgX-=3*(1/dRockSpeed*dRockSpeedO);
            if(fbgX < -nWindW) {
                fbgX=0;
            }
            //Scaled gravitational effect on the player.
            fCharMove += nWindH/52;
            //Rock movement. Makes them go fast at random speed according to the global speed modifier.
            if(vObs1.x > -spObs1.getWidth()) {
                //nRockX1-=dR1S;
                vObs1.sub((float)dR1S, 0);
            } else {
                dR1S = (rand.nextDouble()+2)*(24/(860/nWindH)/dRockSpeed);
                //nRockX1 = nWindW + nWindW/10;
                vObs1.add(nWindW*3, 0);
            }
            if(vObs2.x > -spObs2.getWidth()) {
                //nRockX2-=dR2S;
                vObs2.sub((float)dR2S, 0);
            } else {
                dR2S = (rand.nextDouble()+2)*(30/(860/nWindH)/dRockSpeed);
                //nRockX2 = nWindW + nWindW/10;
                vObs2.add(nWindW*3, 0);
            }
            if(vObs3.x > -spObs3.getWidth()) {
                //nRockX3-=dR3S;
                vObs3.sub((float)dR3S, 0);
            } else {
                dR3S = (rand.nextDouble()+2)*(36/(860/nWindH)/dRockSpeed);
                //nRockX3 = nWindW + nWindW/10;
                vObs3.add(nWindW*3, 0);
            }
            //Keeps the rock rails intact, could be done better though.
            vObs1.set(vObs1.x, nWindH - nWindH / 4 - spObs1.getHeight() / 2 + nWindH / 12);
            vObs2.set(vObs2.x, nWindH / 2 - spObs2.getHeight() / 2);
            vObs3.set(vObs3.x, nWindH / 2 - nWindH / 4 - spObs3.getHeight() / 2 - nWindH / 12);
            //Hit detection below. Has no mercy on it's own.
            if(!hasHit1 && Math.sqrt(Math.pow((vChar.x+spChar.getWidth()/2)-(vObs1.x+spObs1.getWidth()/2), 2) + Math.pow((vChar.y-spChar.getHeight()/2)-(vObs1.y+spObs1.getHeight()/2), 2)) < spChar.getHeight()/2+spObs1.getHeight()/2 && !justStarted) {
                //System.out.println("Hit On 1");
                nLives--;
                hasHit1 = true;
            }
            if(!hasHit2 && Math.sqrt(Math.pow((vChar.x+spChar.getWidth()/2)-(vObs2.x+spObs2.getWidth()/2), 2) + Math.pow((vChar.y-spChar.getHeight()/2)-(vObs2.y+spObs2.getHeight()/2), 2)) < spChar.getHeight()/2+spObs2.getHeight()/2 && !justStarted) {
                //System.out.println("Hit On 2");
                nLives--;
                hasHit2 = true;
            }
            if(!hasHit3 && Math.sqrt(Math.pow((vChar.x+spChar.getWidth()/2)-(vObs3.x+spObs3.getWidth()/2), 2) + Math.pow((vChar.y-spChar.getHeight()/2)-(vObs3.y+spObs3.getHeight()/2), 2)) < spChar.getHeight()/2+spObs3.getHeight()/2 && !justStarted) {
                //System.out.println("Hit On 3");
                System.out.println(Math.sqrt(Math.pow((vChar.x+spChar.getWidth()/2)-(vObs3.x+spObs3.getWidth()/2), 2) + Math.pow((vChar.y-spChar.getHeight()/2)-(vObs3.y+spObs3.getHeight()/2), 2)));
                System.out.println(spChar.getHeight()/2+spObs3.getHeight()/2);
                nLives--;
                hasHit3 = true;
            }
            //Hit reapeat defense below. Stops insta-death on first impact in modes with more than 1 life.
            if(vChar.x >= vObs1.x + spObs1.getWidth() && vChar.x <= vObs1.x + nWindW/25 + spObs1.getWidth() && hasHit1) {
                hasHit1 = false;
            }
            if(vChar.x >= vObs2.x + spObs2.getWidth() && vChar.x <= vObs2.x + nWindW/25 + spObs2.getWidth() && hasHit2) {
                hasHit2 = false;
            }
            if(vChar.x >= vObs3.x + spObs3.getWidth() && vChar.x <= vObs3.x + nWindW/25 + spObs3.getWidth() && hasHit3) {
                hasHit3 = false;
            }
            //Scoring below.
            if(vChar.x >= vObs1.x + spObs1.getWidth() && vChar.x <= vObs1.x + nWindW/25 + spObs1.getWidth() && !hasHit1 && !justStarted) {
                nScore++;
            }
            if(vChar.x >= vObs2.x + spObs2.getWidth() && vChar.x <= vObs2.x + nWindW/25 + spObs2.getWidth() && !hasHit2 && !justStarted) {
                nScore++;
            }
            if(vChar.x >= vObs3.x + spObs3.getWidth() && vChar.x <= vObs3.x + nWindW/25 + spObs3.getWidth() && !hasHit3 && !justStarted) {
                nScore++;
            }
            //Death sensor below.
            if(nLives == 0) {
                isMenu=true;
                System.out.println(nScore);
                create();
            }
            //Graphics below.
            batch.begin();

            batch.draw(spBG, fbgX, 0, nWindW, nWindH);
            batch.draw(spBG, fbgX + nWindW, 0, nWindW, nWindH);
            
            batch.draw(spBox, 0, nWindH);
            batch.draw(spBox, 0, 0);
            
            batch.draw(spObs1, vObs1.x, vObs1.y, nWindH/3 - nWindH/15, nWindH/3 - nWindH/15);
            batch.draw(spObs2, vObs2.x, vObs2.y, nWindH/3 - nWindH/15, nWindH/3 - nWindH/15);
            batch.draw(spObs3, vObs3.x, vObs3.y, nWindH/3 - nWindH/15, nWindH/3 - nWindH/15);
            
            batch.draw(spChar, fPosX - spChar.getWidth() / 2, vChar.y - spChar.getHeight() / 2, spChar.getOriginX(), spChar.getOriginY(), spChar.getHeight(), spChar.getWidth(), spChar.getScaleX(), spChar.getScaleY(), spChar.getRotation(), true);
            //objPlayer.render(batch);
            
            batch.draw(spReticle, vRet.x, vRet.y, spReticle.getOriginX(), spReticle.getOriginY(), spReticle.getWidth(), spReticle.getHeight(), spReticle.getScaleX(), spReticle.getScaleY(), spReticle.getRotation());
            
            fontGeneric.draw(batch, "Score: " + Integer.toString(nScore), nWindW/50, nWindH);
            fontGeneric.draw(batch, "Lives: " + Integer.toString(nLives), nWindW/6, nWindH);
            fontGeneric.draw(batch, "FPS: " + Float.toString(Gdx.graphics.getFramesPerSecond()), nWindW/50, nWindH/2+nWindH/3+nWindH/9);
            fontGeneric.draw(batch, "TRS: " + Double.toString(Math.round(dR1S*10000.0)/10000.0), nWindW/50, nWindH/2+nWindH/3+nWindH/14);
            fontGeneric.draw(batch, "MRS: " + Double.toString(Math.round(dR2S*10000.0)/10000.0), nWindW/50, nWindH/2+nWindH/3+nWindH/23);
            fontGeneric.draw(batch, "BRS: " + Double.toString(Math.round(dR3S*10000.0)/10000.0), nWindW/50, nWindH/2+nWindH/3+nWindH/75);
            fontGeneric.draw(batch, "Speed Multiplier: " + Double.toString(Math.round((1/dRockSpeed*dRockSpeedO)*10000.0)/10000.0), nWindW/50, nWindH/2+nWindH/3-nWindH/65);
            
            fontGeneric.draw(batch, "hasHit1: " + hasHit1, 0, 250);
            fontGeneric.draw(batch, "hasHit2: " + hasHit2, 0, 350);
            fontGeneric.draw(batch, "hasHit3: " + hasHit3, 0, 450);
            
            batch.end();
        }
        //Miscellaneous key sensing below.
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
        bgMusic.stop();
        menuMusic.stop();
        create();
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
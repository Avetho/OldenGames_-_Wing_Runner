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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.awt.geom.Ellipse2D;
import com.badlogic.gdx.utils.Timer;
import java.util.logging.Level;

public class GameCore extends ApplicationAdapter implements InputProcessor {
    //Permanent Variables
    public Vector2 vChar; //Character position.
    public Vector2 vObs1; //Top Rock position.
    public Vector2 vObs2; //Middle Rock position.
    public Vector2 vObs3; //Bottom Rock position.
    public Vector2 vRet; //Mouse position on y coordinates. The Badlogic logo is the character rotation aim point.
    public Random rand; //To randomize the rocks' speeds and other things.
    public SpriteBatch batch; //To draw all the things.
    public ShapeRenderer renderHB; //To render all the shapes, of which are none as of yet.
    public Texture imgReticle, imgSprite, imgSprite2, imgObstacle, imgObstacle2, imgBg, imgBg2, imgPause, imgBox, imgMenu, imgMenuInv, imgStart, imgMusic, imgExit; //All that you see are belong to these.
    public Sprite spReticle, spChar, spObs1, spObs2, spObs3, spObs4, spObs5, spBG, spBox, spMenuBG, spStart, spMusic, spExit, spHitRestore; //All the stuff are belong to here.
    public int nCursorX, nCursorY, nWindW, nWindH, nRockX1, nRockX2, nRockX3; //More generic variables.
    public float fCharRot, fCharMove, fCharAdditive, fPosX; //Generic variables for the game.
    public boolean isTouch, isPaused, isMenu, isMusicOn, isMMusic, isMusicEnable, isMusicClassic; //If you are doing these things.
    public boolean hasHit1, hasHit2, hasHit3; //To check if you have hit a rock.
    public double dR1S = 1, dR2S = 1, dR3S = 1; //Moving across at the speed of rock! These are the rocks' speeds.
    public Music menuMusic, bgMusic, menuMusicFly, menuMusicSci, bgMusicFly, bgMusicSci; //The tracks. First two are the played ones, last ones are just to set the playing ones.
    public GameCore game; //The game. A new instance is created.
    public EntityPlayer objPlayer; //The player. What else? A new instance is created.
    public float fbgX = 0; //So the background scrolls. It was not intended to be uniform with the rocks.
    public OrthographicCamera camera;
    public int nLives, nScore;
    public BitmapFont fontGeneric; //http://stackoverflow.com/questions/12466385/how-can-i-draw-text-using-libgdx-java
    public double dRockSpeed, dRockSpeedO; //dRockSpeedO is original speed so only one number has to be changed.
    public float fSpeedMod; //Changes vertical speed based on rotation. Up is slower than down.
    public int nScoreInc; //Every time this hits 1000 it is set to 0 after incrementing the score by one: nScore++;
    public boolean justStarted; //Gives you a delay from starting to avoid 3 score instantly, and to avoid losing a life at the start.
    public int nTime; //Counting until you are free from danger at the start.
    public int nMode; //The mode of the game. 1 is easy, 2 is intermediate, 3 is hard, 4 is colormania, 5 is shadow.
    public int nLivesDef = 9; //Default Lives. Used to calculate different amounts of lives per mode basis.
    public GameMenu menu;
    //ClickListener buttonPress;
    //Actor acStart, acMusic;
    //Stage disp;
    public Texture imgTap;
    public Sprite spTap;

    @Override
    public void create() {
        //PlayerData playerData = new PlayerData();
        //playerData.Init();
        dRockSpeed = 5;
        nMode = 1;
        nTime = 0;
        dRockSpeedO = dRockSpeed;
        fontGeneric = new BitmapFont();
        justStarted = true;
        fSpeedMod = 0;
        nLives = nLivesDef;
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
        imgExit = new Texture("ExitButton.png");
        spMenuBG = new Sprite(imgMenu);
        spMenuBG.setSize(nWindW, nWindH);
        spBox = new Sprite(imgBox);
        spBox.setScale(nWindW, nWindH/15);
        spObs1 = new Sprite(imgObstacle);
        spObs2 = new Sprite(imgObstacle);
        spObs3 = new Sprite(imgObstacle);
        //spObs4 = new Sprite(imgobstacle);
        //spObs5 = new Sprite(imgobstacle);
        
        spStart = new Sprite(imgStart);
        spStart.setBounds(nWindW/5, nWindH/5, spStart.getWidth(), spStart.getHeight());
        spMusic = new Sprite(imgMusic);
        spMusic.setBounds(nWindW-nWindW/5, nWindH/5, spMusic.getWidth(), spMusic.getHeight());
        spExit = new Sprite(imgExit);
        spExit.setBounds(nWindW/2, nWindH/5, spExit.getWidth(), spExit.getHeight());
        
        //acStart = new Actor();
        //acMusic = new Actor();
        imgTap = new Texture("MouseClick.png");
        spTap = new Sprite(imgTap);
        //disp.addActor(acStart);
        //disp.addActor(acMusic);
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
        //menu = new GameMenu();
    }
    
  //========================================================================================================================//
    
    public void recreate() {
        //PlayerData playerData = new PlayerData();
        //playerData.Init();
        dRockSpeed = 5;
        nMode = 1;
        nTime = 0;
        dRockSpeedO = dRockSpeed;
        justStarted = true;
        fSpeedMod = 0;
        nLives = nLivesDef;
        hasHit1 = false;
        hasHit2 = false;
        hasHit3 = false;
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
            camera.translate(nWindW / 2, nWindH / 2);
            camera.update();
        spMenuBG.setSize(nWindW, nWindH);
        spBox.setScale(nWindW, nWindH/15);
        //spObs4 = new Sprite(imgobstacle);
        //spObs5 = new Sprite(imgobstacle);
        //acStart = new Actor();
        //acMusic = new Actor();
        //disp.addActor(acStart);
        //disp.addActor(acMusic);
        spBG.setSize(nWindW, nWindH);
        spReticle.setOrigin(spReticle.getWidth() / 2, spReticle.getHeight() / 2);
        spReticle.setSize(nWindW / 200, nWindW / 200);
        spChar.setCenter(spChar.getWidth(), spChar.getHeight());
        spChar.setSize(nWindH / 12/*5*/, nWindH / 15);
        Gdx.input.setInputProcessor(this);
        menuMusic = menuMusicFly;
        bgMusic = bgMusicFly;
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
        //menu = new GameMenu();
    }
    
  //========================================================================================================================//
    
    @Override
    public void render() {
        batch.begin();
        //spTap.setBounds(nCursorX, nCursorY, 1, 1);
        spTap.setPosition(nCursorX, nCursorY);
        spTap.draw(batch);
        batch.end();
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.5f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (isMenu == false) {
                isMenu = true;
            } else {
                batch.flush();
                batch.dispose();
                Gdx.input.vibrate(10);
                System.out.println("Goodbye! 8)");
                System.exit(0);
            }
        }
        if (isMenu) {
            /*disp.getActors();
            acStart.setPosition(nWindW/5, nWindH/5);
            acMusic.setPosition((nWindW-nWindW)/5, nWindH/5);
            acStart.setScale(nWindW/25);
            acMusic.setScale(nWindW/25);
            disp.draw();*/
            batch.begin();
            batch.draw(spStart, nWindW/5, nWindH/5);
            batch.draw(spMusic, nWindW-nWindW/5, nWindH/5);
            batch.draw(spExit, nWindW/2, nWindH/5);
            batch.end();
            if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.D) && justStarted) {
                System.out.println("Added one to mode value.");
                nMode++;
                if(nMode == 6) {
                    System.out.println("Went to mode 6. This is normal. Setting back to Mode-Easy.");
                    nMode = 1;
                }
                if(nMode == 1) {///////////This code deals with lives changes. nLivesDef is the default amount.
                    System.out.println("Setting to Mode-Easy.");
                    nLives = nLivesDef;//The default amount. Easy mode.
                }
                if(nMode == 2) {
                    System.out.println("Setting to Mode-Medium.");
                    nLives = nLivesDef/2;//Medium difficulty gives half of the standard lives.
                }
                if(nMode == 3) {
                    System.out.println("Setting to Mode-Hard.");
                    nLives = 1;//You always have 1 life in hard. It's hard for a reason. No mercy for beginners.
                }
                if(nMode == 4) {
                    System.out.println("Setting to Mode-Medium, Type-Psych.");
                    nLives = nLivesDef/2;//Psycadellic mode gives half of the standard lives, like Intermediate.
                }
                if(nMode == 5) {
                    System.out.println("Setting to Mode-Hardcore.");
                    nLives = 1;//Shadow is hardcore, so you get no chance unless your are really good. Same lives as hard mode.
                }//////////////////////////
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                System.out.println("Shutting down music.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.out.println("Uh oh, something happened.");
                    java.util.logging.Logger.getLogger(GameCore.class.getName()).log(Level.SEVERE, null, ex);
                }
                bgMusic.stop();
                menuMusic.stop();
                System.out.println("Quit early with score of: " + nScore + ".");
                System.out.println("Restarting internally.");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                    System.out.println("Uh oh, something happened.");
                    java.util.logging.Logger.getLogger(GameCore.class.getName()).log(Level.SEVERE, null, ex);
                }
                recreate();//Recreate the game, but don't reload any sprites or music or anything.
            }
            if(justStarted) {
                batch.begin();
                batch.draw(imgMenu, 0, 0, nWindW, nWindH);
                batch.end();
            }
            else {
                batch.begin();
                batch.draw(imgMenuInv, 0, 0, nWindW, nWindH);
                batch.end();
            }
            batch.begin();
            fontGeneric.draw(batch, "Score: " + Integer.toString(nScore), nWindW/50, nWindH);
            if(nMode == 1) {
                fontGeneric.draw(batch, "Mode: Novice (" + Integer.toString(nMode) + ")", nWindW/25, nWindH/2);
            } else if(nMode == 2) {
                fontGeneric.draw(batch, "Mode: Intermediate (" + Integer.toString(nMode) + ")", nWindW/25, nWindH/2);
            } else if(nMode == 3) {
                fontGeneric.draw(batch, "Mode: Difficult (" + Integer.toString(nMode) + ")", nWindW/25, nWindH/2);
            } else if(nMode == 4) {
                fontGeneric.draw(batch, "Mode: Psychedelic (" + Integer.toString(nMode) + ")", nWindW/25, nWindH/2);
            } else if(nMode == 5) {
                fontGeneric.draw(batch, "Mode: Shadow (" + Integer.toString(nMode) + ")", nWindW/25, nWindH/2);
            } else {
                fontGeneric.draw(batch, "Mode: ØÍ×öæÚV☼ (" + Integer.toString(nMode) + ")", nWindW/25, nWindH/2);
            }
            batch.end();
            if (menuMusic.isPlaying() == false) {
                bgMusic.stop();
                menuMusic.play();
                menuMusic.setLooping(true);//music code came from http://stackoverflow.com/questions/27767121/how-to-play-music-in-loop-in-libgdx
            }
            if (spStart.getBoundingRectangle().contains(nCursorX, nCursorY) && Gdx.input.isTouched()) {
                menuMusic.setLooping(false);
                menuMusic.stop();
                isMenu = false;
            }
        } else if (isMenu == false) {
            if(nMode == 5) { //A fun 'bug' I came across while color changing was a possible "Shadow" mode.
                batch.setColor(0, 0, 0, 0.5f); //You can barely see the player, rocks, and no background, just all on a crimson color backdrop.
                Gdx.gl.glClearColor(0.256f, 0.128f, 0.128f, 1);
            }
            else {
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.5f);
            }
            if(nMode == 4) {
                batch.setColor(rand.nextInt(255 - 100 + 1), rand.nextInt(255 - 100 + 1), rand.nextInt(255 - 100 + 1), 0.5f);
            }
            if(nTime >= 150) {
                justStarted = false;
            }
            if(nTime < 150) {
                nTime++; //Increment 
            }
            if(nScore % 100 == 0 && nScore != 0 && nMode != 5) {
                nLives++; //Add one to score every 100 score except for 0, unless in Shadow mode, otherwise known in code as Mode-Hardcore.
                nScore++;
            }
            if(Gdx.input.justTouched()) {
                hasHit1 = false; //Makes sure last level end doesn't affect new game.
                hasHit2 = false;
                hasHit3 = false;
            }
            if(Gdx.input.isTouched() && nCursorX > nWindW-nWindW/25 && nCursorY > nWindH-nWindH/25) {
                nMode = 6;
            }
            if (bgMusic.isPlaying() == false) {
                bgMusic.setLooping(true); //In case it doesn't set it looping.
                bgMusic.play();//music code came from http://stackoverflow.com/questions/27767121/how-to-play-music-in-loop-in-libgdx
            }
            if(dRockSpeed > 2 + 1/nMode*2) {
                dRockSpeed -= 0.00025 - dRockSpeed/49126; //Psychadellic mode is faster than Hard. Have fun!
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
            Gdx.gl.glClearColor(0, 0, 0, 0.5f);
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
                System.out.println("Hit Top Rock.");
                nLives--;
                hasHit1 = true;
            }
            if(!hasHit2 && Math.sqrt(Math.pow((vChar.x+spChar.getWidth()/2)-(vObs2.x+spObs2.getWidth()/2), 2) + Math.pow((vChar.y-spChar.getHeight()/2)-(vObs2.y+spObs2.getHeight()/2), 2)) < spChar.getHeight()/2+spObs2.getHeight()/2 && !justStarted) {
                System.out.println("Hit Mid Rock.");
                nLives--;
                hasHit2 = true;
            }
            if(!hasHit3 && Math.sqrt(Math.pow((vChar.x+spChar.getWidth()/2)-(vObs3.x+spObs3.getWidth()/2), 2) + Math.pow((vChar.y-spChar.getHeight()/2)-(vObs3.y+spObs3.getHeight()/2), 2)) < spChar.getHeight()/2+spObs3.getHeight()/2 && !justStarted) {
                System.out.println("Hit Low Rock.");
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
                bgMusic.stop();
                menuMusic.stop();
                isMenu=true;
                System.out.println(nScore);
                create();
            }
            
            spObs1.setSize(nWindH/3 - nWindH/18, nWindH/3 - nWindH/18);
            spObs2.setSize(nWindH/3 - nWindH/18, nWindH/3 - nWindH/18);
            spObs3.setSize(nWindH/3 - nWindH/18, nWindH/3 - nWindH/18);
            spChar.setSize(nWindH / 12, nWindH / 15);
            //Graphics below.
            batch.begin();

            batch.draw(spBG, fbgX, 0, nWindW, nWindH);
            batch.draw(spBG, fbgX + nWindW, 0, nWindW, nWindH);
            
            batch.draw(spBox, 0, nWindH);
            batch.draw(spBox, 0, 0);
            
            batch.draw(spObs1, vObs1.x, vObs1.y, nWindH/3 - nWindH/18, nWindH/3 - nWindH/18);
            batch.draw(spObs2, vObs2.x, vObs2.y, nWindH/3 - nWindH/18, nWindH/3 - nWindH/18);
            batch.draw(spObs3, vObs3.x, vObs3.y, nWindH/3 - nWindH/18, nWindH/3 - nWindH/18);
            
            //batch.end();
            
            batch.draw(spChar, fPosX - spChar.getWidth() / 2, vChar.y - spChar.getHeight() / 2, spChar.getOriginX(), spChar.getOriginY(), spChar.getHeight(), spChar.getWidth(), spChar.getScaleX(), spChar.getScaleY(), spChar.getRotation(), true);
            //objPlayer.render(spChar);
            
            //batch.begin();
            
            batch.draw(spReticle, vRet.x, vRet.y, spReticle.getOriginX(), spReticle.getOriginY(), spReticle.getWidth(), spReticle.getHeight(), spReticle.getScaleX(), spReticle.getScaleY(), spReticle.getRotation());
            
            fontGeneric.draw(batch, "Score: " + Integer.toString(nScore), nWindW/50, nWindH);
            fontGeneric.draw(batch, "Lives: " + Integer.toString(nLives), nWindW/6, nWindH);
            fontGeneric.draw(batch, "FPS: " + Float.toString(Gdx.graphics.getFramesPerSecond()), nWindW/50, nWindH/2+nWindH/3+nWindH/9);
            fontGeneric.draw(batch, "TRS: " + Double.toString(Math.round(dR1S*10000.0)/10000.0), nWindW/50, nWindH/2+nWindH/3+nWindH/14);
            fontGeneric.draw(batch, "MRS: " + Double.toString(Math.round(dR2S*10000.0)/10000.0), nWindW/50, nWindH/2+nWindH/3+nWindH/23);
            fontGeneric.draw(batch, "BRS: " + Double.toString(Math.round(dR3S*10000.0)/10000.0), nWindW/50, nWindH/2+nWindH/3+nWindH/75);
            fontGeneric.draw(batch, "Speed Multiplier: " + Double.toString(Math.round((1/dRockSpeed*dRockSpeedO)*10000.0)/10000.0), nWindW/50, nWindH/2+nWindH/3-nWindH/65);
            
            if(nMode == 0){
                fontGeneric.draw(batch, "hasHit1: " + hasHit1, 0, 300);
                fontGeneric.draw(batch, "hasHit2: " + hasHit2, 0, 250);
                fontGeneric.draw(batch, "hasHit3: " + hasHit3, 0, 200);
            }
            
            batch.end();
        }
        //Miscellaneous key sensing below.
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                menuMusic.stop();
                bgMusic.stop();
                if(isMusicClassic) {
                    spChar = new Sprite(imgSprite);
                    spChar.setSize(nWindH / 12, nWindH / 15);
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
                    spChar.setSize(nWindH / 12, nWindH / 15);
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

//    public float findAngle(double dX1, double dY1, double dX2, double dY2) {
//        float fAngle;
//        fAngle = (float) Math.toDegrees(Math.atan2(dY1 - dY2, dX1 - dX2));
//        return fAngle;
//    }

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
        //bgMusic.stop();
        //menuMusic.stop();
        //create();
    }
    
    @Override
    public void dispose() {
//        img.dispose();
//        item.dispose();
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
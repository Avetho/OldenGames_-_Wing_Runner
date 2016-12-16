package GameV1;

import GameV1.GameCore;
import GameV1.GameMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import gdx.menu.TbMenu;
import gdx.menu.TbsMenu;




public class ScrPlay extends GameV1.GameCore implements Screen, InputProcessor {
    GameMenu.GamMenu gamMenu;
    TbsMenu tbsMenu;
    TbMenu tbMenu, tbGameover;
    Stage stage;
    SpriteBatch batch;
    BitmapFont screenName;

    public ScrPlay(GameMenu.GamMenu _gamMenu, GameCore gameBlock) {  //Referencing the main class.
        gamMenu = _gamMenu;
    }

    public ScrPlay(GameMenu.GamMenu aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void show() {
        stage = new Stage();
        tbsMenu = new TbsMenu();
        batch = new SpriteBatch();
        screenName = new BitmapFont();
        tbMenu = new TbMenu("BACK", tbsMenu);
        tbGameover = new TbMenu("GAMEOVER", tbsMenu);
        tbMenu.setY(0);
        tbMenu.setX(0);
        tbGameover.setY(0);
        tbGameover.setX(440);
        stage.addActor(tbMenu);
        stage.addActor(tbGameover);
        Gdx.input.setInputProcessor(stage);
        btnMenuListener();
        btnGameoverListener();
    }

    @Override
    public void render(float delta) {
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
            if(nMode == 5) { //A fun 'bug' I came across while color changing was a possible "Shadow" mode.
                batch.setColor(0, 0, 0, 1); //You can barely see the player, rocks, and no background, just all on a crimson color backdrop.
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
            if(dRockSpeed > 2 + nMode/5*2) {
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
                System.out.println("Hit On 1");
                nLives--;
                hasHit1 = true;
            }
            if(!hasHit2 && Math.sqrt(Math.pow((vChar.x+spChar.getWidth()/2)-(vObs2.x+spObs2.getWidth()/2), 2) + Math.pow((vChar.y-spChar.getHeight()/2)-(vObs2.y+spObs2.getHeight()/2), 2)) < spChar.getHeight()/2+spObs2.getHeight()/2 && !justStarted) {
                System.out.println("Hit On 2");
                nLives--;
                hasHit2 = true;
            }
            if(!hasHit3 && Math.sqrt(Math.pow((vChar.x+spChar.getWidth()/2)-(vObs3.x+spObs3.getWidth()/2), 2) + Math.pow((vChar.y-spChar.getHeight()/2)-(vObs3.y+spObs3.getHeight()/2), 2)) < spChar.getHeight()/2+spObs3.getHeight()/2 && !justStarted) {
                System.out.println("Hit On 3");
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
                //isMenu=true;
                gamMenu.updateState(2);
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
            
            fontGeneric.draw(batch, "hasHit1: " + hasHit1, 0, 300);
            fontGeneric.draw(batch, "hasHit2: " + hasHit2, 0, 250);
            fontGeneric.draw(batch, "hasHit3: " + hasHit3, 0, 200);
            
            batch.end();
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
            Gdx.gl.glClearColor(.135f, .206f, .235f, 1); //blue background.
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            screenName.draw(batch, "This is the PLAY screen", 230, 275);
            batch.end();
            stage.act();
            stage.draw();
        }
    
    public float findAngle2(Vector2 vObj1, Vector2 vObj2) {
        float fAngle;
        //System.out.println(vObj1.y - vObj2.y);
        //System.out.println(vObj1.x - vObj2.x);
        fAngle = (float) Math.toDegrees(Math.atan2(vObj1.y - vObj2.y, vObj1.x - vObj2.x));
        return fAngle - 90;
    }
    
    @Override
    public void hide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public void btnGameoverListener() {
        tbGameover.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
              
                gamMenu.updateState(3);
            }
        });
    }

    public void btnMenuListener() {
        tbMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
               
                gamMenu.updateState(0);
            }
        });
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
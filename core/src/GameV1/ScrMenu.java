package GameV1;

import GameV1.GameCore;
import GameV1.GameMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import gdx.menu.TbMenu;
import gdx.menu.TbsMenu;

public class ScrMenu extends GameV1.GameCore implements Screen, InputProcessor {
    GameMenu.GamMenu gamMenu;
    TbsMenu tbsMenu;
    TbMenu tbPlay, tbGameover;
    Stage stage;
    SpriteBatch batch;
    BitmapFont screenName;

    public ScrMenu(GameMenu.GamMenu _gamMenu, GameCore gameBlock) {  //Referencing the main class.
        gamMenu = _gamMenu;
    }

    public ScrMenu(GameMenu.GamMenu aThis) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void show() {
        stage = new Stage();
        tbsMenu = new TbsMenu();
        batch = new SpriteBatch();
        screenName = new BitmapFont();
        tbPlay = new TbMenu("PLAY", tbsMenu);
        tbGameover = new TbMenu("BACK", tbsMenu);
        tbGameover.setY(0);
        tbGameover.setX(0);
        tbPlay.setY(0);
        tbPlay.setX(440);
        stage.addActor(tbPlay);
        stage.addActor(tbGameover);
        Gdx.input.setInputProcessor(stage);
        btnPlayListener();
        btnGameoverListener();
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D) && justStarted) {
            nMode++;
            if(nMode == 6) {
                nMode = 1;
            }
            if(nMode == 1) {///////////This code deals with lives changes. nLivesDef is the default amount.
                nLives = nLivesDef;//The default amount. Easy mode.
            }
            if(nMode == 2) {
                nLives = nLivesDef/2;//Medium difficulty gives half of the standard lives.
            }
            if(nMode == 3) {
                nLives = 1;//You always have 1 life in hard. It's hard for a reason. No mercy for beginners.
            }
            if(nMode == 4) {
                nLives = nLivesDef/3;//Phycadellic mode gives you a third of standard lives.
            }
            if(nMode == 5) {
                nLives = nLivesDef/2 + nLivesDef/3;//Shadow is hard to see, so you get a chance with 5/6 of the standard lives.
            }//////////////////////////
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            bgMusic.stop();
            menuMusic.stop();
            System.out.println("Quit early with score of: " + nScore + ".");
            create();
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
        }
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
        Gdx.gl.glClearColor(0, 1, 0, 1); //Green background.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        screenName.draw(batch, "This is the MENU screen", 230, 275);
        batch.end();
        stage.act();
        stage.draw();
        if(!isMenu) {
            gamMenu.updateState(1);
        }
    }

    public void btnPlayListener() {
        tbPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                
                gamMenu.updateState(1); // switch to Play screen.
            }
        });
    }

    public void btnGameoverListener() {
        tbGameover.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {               
                gamMenu.updateState(2);
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
    public void hide() {

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
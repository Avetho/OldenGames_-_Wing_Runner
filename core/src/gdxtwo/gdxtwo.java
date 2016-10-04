package gdxtwo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.math.*;
import java.io.*;
import com.badlogic.gdx.utils.*;
//import java.lang.Enum<Cursor.SystemCursor>;

public class gdxtwo extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
        Sprite spCharacter;
        
        //Vector2 vPos;
        int nPosX, nPosY, CROSSHAIR_CURSOR;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
                spCharacter = new Sprite(img);
                spCharacter.setScale(0.5f);
                //Gdx.graphics.setSystemCursor(Cursor(CROSSHAIR_CURSOR));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                nPosX = Gdx.input.getX();
                nPosY = Gdx.graphics.getHeight()-Gdx.input.getY();//Fix Y coordinate to be a better way of getting mouse/tap Y.
		batch.begin();
		batch.draw(spCharacter, nPosX-spCharacter.getWidth()/2, nPosY-spCharacter.getHeight()/2);
		batch.end();
                //vPos.add(Gdx.input.getX(), Gdx.input.getY());
	}
}

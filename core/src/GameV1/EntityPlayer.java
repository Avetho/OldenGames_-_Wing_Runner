package GameV1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EntityPlayer extends GameCore {
    //SpriteBatch bChar = new SpriteBatch();
    public void render(SpriteBatch batch) {
        //bChar.begin();
        batch.draw(spChar, fPosX - spChar.getWidth() / 2, vChar.y - spChar.getHeight() / 2, spChar.getOriginX(), spChar.getOriginY(), spChar.getHeight(), spChar.getWidth(), spChar.getScaleX(), spChar.getScaleY(), spChar.getRotation(), true);
        //bChar.end();
    }
    public void updateData() {
        
    }
}

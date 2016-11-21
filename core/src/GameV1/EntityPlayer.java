package GameV1;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class EntityPlayer extends GameCore {
    //SpriteBatch bChar = new SpriteBatch();
    public void render(Sprite _spChar) {
        Sprite spChar = new Sprite(_spChar);
        //bChar.begin();
        batch.begin();
        batch.draw(spChar, fPosX - spChar.getWidth() / 2, vChar.y - spChar.getHeight() / 2, spChar.getOriginX(), spChar.getOriginY(), spChar.getHeight(), spChar.getWidth(), spChar.getScaleX(), spChar.getScaleY(), spChar.getRotation(), true);
        batch.end();
        //bChar.end();
    }
    public void updateData() {
        
    }
}

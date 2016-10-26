package GameV1;
public class EntityPlayer extends GameCore{
    @Override
    public void render() {
        batch.draw(spChar, fPosX - spChar.getWidth() / 2, fPosY - spChar.getHeight() / 2, spChar.getOriginX(), spChar.getOriginY(), spChar.getHeight(), spChar.getWidth(), spChar.getScaleX(), spChar.getScaleY(), spChar.getRotation(), true);
    }
    public void updateData() {
        
    }
}

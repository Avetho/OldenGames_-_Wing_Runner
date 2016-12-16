package GameV1;

import GameV1.GameCore;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import java.util.*;
import java.io.*;

public class PlayerData extends GameCore {
    
    private String sGamerTag;
    private int nHighScore;
    private Scanner sInput = new Scanner(System.in);
    
    public void GamerTag() {
        SpriteBatch batch = new SpriteBatch();
        System.out.println("Enter your gamertag. Use no spaces.");
        sGamerTag = sInput.next();
    }
    
    public void HighScore() {
        if(nScore>nHighScore) {
            nHighScore = nScore;
        }
    }
    
    public void Init() {
        GamerTag();
        HighScore();
        Json json = new Json();
        //json.writeArrayStart(sGamerTag, nHighScore);
    }
    
    public void Save() {
        
    }
    
    public void Clear() {
        
    }
    
}
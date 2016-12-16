package GameV1;//@author matts6206

import com.badlogic.gdx.Game;

public class GameMenu {

    public class GamMenu extends Game {

        ScrMenu scrMenu;
        ScrPlay scrPlay;
        ScrGameover scrGameover;
        public int nScreen; // 0 for menu, 1 for play, and 2 for game over

        public void updateState(int _nScreen) {
            nScreen = _nScreen;
            if (nScreen == 0) {
                setScreen(scrMenu);
            } else if (nScreen == 1) {
                setScreen(scrPlay);
            } else if (nScreen == 2) {
                setScreen(scrGameover);
            }
        }

        @Override
        public void create() {
            GameCore gameBlock = new GameCore();
            gameBlock.create();
            nScreen = 0;
            // notice that "this" is passed to each screen. Each screen now has access to methods within the "game" master program
            scrMenu = new ScrMenu(this, gameBlock);
            scrPlay = new ScrPlay(this, gameBlock);
            scrGameover = new ScrGameover(this, gameBlock);
            updateState(0);
        }

        @Override
        public void render() {
            super.render();
        }

        @Override
        public void dispose() {
            super.dispose();
        }
    }
}
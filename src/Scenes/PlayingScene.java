package Scenes;

import bagel.Input;
import enums.GameStage;

public class PlayingScene implements Scene{
    private GameStage gameStage;

    public PlayingScene(int curLevel){}
    @Override
    public void loadScene(int currentLevel) {

    }

    @Override
    public void drawScene() {

    }

    @Override
    public void updateScene(Input input) {

    }

    @Override
    public boolean isEnd() {
        return false;
    }

    public GameStage getGameStage(){
        return this.gameStage;
    }
}

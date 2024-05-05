package Scenes;

import GameEntity.*;
import bagel.Input;
import bagel.util.Point;
import enums.GameStage;
import utils.IOUtils;

public class PlayingScene implements Scene{
    private GameStage gameStage;

    public PlayingScene(){
        return;

    }

    public PlayingScene(int curLevel){}

    protected void loadScene(int currentLevel) {

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

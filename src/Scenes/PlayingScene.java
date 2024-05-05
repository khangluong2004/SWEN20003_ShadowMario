package Scenes;

import CollisionHandling.CollisionMediator;
import GameEntities.GameEntity;
import bagel.Input;
import enums.GameEntityTypes;
import enums.GameStage;
import utils.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayingScene implements Scene{
    // TODO: Change this to abstract, and inherit for each level
    private GameStage gameStage;
    private boolean isEnd;
    private Map<GameEntityTypes, List<GameEntity>> entitiesByTypes;
    private List<Message> allMessages;
    private CollisionMediator collisionMediator;

    public PlayingScene(int curLevel){
        entitiesByTypes = new HashMap<GameEntityTypes, List<GameEntity>>();
        allMessages = new ArrayList<Message>();
    }

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

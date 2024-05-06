package Scenes.PlayingScenes;

import CollisionHandling.CollisionMediator;
import GameEntities.Characters.Player;
import GameEntities.GameEntity;
import Scenes.Scene;
import bagel.Input;
import enums.GameEntityTypes;
import enums.GameStage;
import utils.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PlayingScene implements Scene {
    // TODO: Change this to abstract, and inherit for each level
    private GameStage gameStage;
    private boolean isEnd;
    private Map<GameEntityTypes, List<GameEntity>> entitiesByTypes;
    private List<Message> allMessages;
    private CollisionMediator collisionMediator;

    public PlayingScene(){
        entitiesByTypes = new HashMap<GameEntityTypes, List<GameEntity>>();
        allMessages = new ArrayList<Message>();
        this.isEnd = false;
        this.gameStage = GameStage.PLAYING;
        this.loadScene();
    }

    protected abstract void loadScene();
    protected abstract void loadCollisionMediator();


    @Override
    public void drawScene() {
        for (var entitiesEntry: entitiesByTypes.entrySet()){
            List<GameEntity> listEntities = entitiesEntry.getValue();
            for (GameEntity entity: listEntities){
                entity.draw();
            }
        }

        for (Message message: allMessages){
            message.write();
        }
    }

    @Override
    public void updateScene(Input input) {
        for (var entitiesEntry: entitiesByTypes.entrySet()){
            List<GameEntity> listEntities = entitiesEntry.getValue();
            for (GameEntity entity: listEntities){
                entity.updatePerFrame(input);
            }
        }
    }

    protected abstract void updateMessage();


    @Override
    public boolean isEnd() {
        // Check if all players finish losing or winning
        for (GameEntity entity: entitiesByTypes.get(GameEntityTypes.PLAYERS)){
            Player player = (Player) entity;
            GameStage endingStage = player.getEndingStage();
            if (endingStage == GameStage.FINISH_LOSING){
                this.gameStage = GameStage.FINISH_LOSING;
            } else if (endingStage == GameStage.WINNING){
                this.gameStage = GameStage.WINNING;
            } else {
                this.gameStage = GameStage.PLAYING;
                return false;
            }
        }
        return true;
    }

    public GameStage getGameStage(){
        return this.gameStage;
    }
}

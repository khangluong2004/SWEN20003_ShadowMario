package Scenes;

import CollisionHandling.CollisionMediator;
import GameEntities.Attackers.Enemy;
import GameEntities.Characters.EnemyBoss;
import GameEntities.Characters.Player;
import GameEntities.Flags.EndFlag;
import GameEntities.GameEntity;
import GameEntities.PickUpItems.Coin;
import GameEntities.PickUpItems.DoubleScorePower;
import GameEntities.PickUpItems.InvinciblePower;
import GameEntities.Platforms.FlyingPlatform;
import GameEntities.Platforms.Platform;
import GameProperties.GameProps;
import Scenes.Scene;
import bagel.Input;
import bagel.util.Point;
import enums.GameStage;
import utils.IOUtils;
import utils.Message;

import java.util.*;

public class PlayingScene implements Scene {
    protected GameStage gameStage;
    protected Set<GameEntity> allGameEntities;
    protected List<Message> allMessages;
    protected CollisionMediator collisionMediator;

    public PlayingScene(int currentLevel){
        allMessages = new ArrayList<Message>();
        allGameEntities = new HashSet<GameEntity>();
        collisionMediator = new CollisionMediator(allGameEntities);

        this.gameStage = GameStage.PLAYING;

        Properties gameProps = GameProps.getGameProps();
        String filename = "";

        if (currentLevel == 1){
            filename = gameProps.getProperty("level1File");
        } else if (currentLevel == 2){
            filename = gameProps.getProperty("level2File");
        } else if (currentLevel == 3){
            filename = gameProps.getProperty("level3File");
        }

        this.loadScene(filename);
    }


    protected void loadScene(String filename){
        String[][] worldInfo = IOUtils.readCsv(filename);

        // Load game entities
        for (String[] strings : worldInfo) {
            Point location = new Point(Double.parseDouble(strings[1]), Double.parseDouble(strings[2]));
            GameEntity currentEntity = null;
            String name = strings[0];

            switch (name) {
                case "PLAYER":
                    currentEntity = new Player(location);
                    break;
                case "PLATFORM":
                    currentEntity = new Platform(location);
                    break;
                case "ENEMY":
                    currentEntity = new Enemy(location);
                    break;
                case "END_FLAG":
                    currentEntity = new EndFlag(location);
                    break;
                case "COIN":
                    currentEntity = new Coin(location);
                    break;
                case "FLYING_PLATFORM":
                    currentEntity = new FlyingPlatform(location);
                    break;
                case "ENEMY_BOSS":
                    currentEntity = new EnemyBoss(location);
                    break;
                case "INVINCIBLE_POWER":
                    currentEntity = new InvinciblePower(location);
                    break;
                case "DOUBLE_SCORE":
                    currentEntity = new DoubleScorePower(location);
                    break;
            }

            allGameEntities.add(currentEntity);
        }


    };


    @Override
    public void drawScene() {
        for (GameEntity entity: allGameEntities){
            entity.draw();
        }

        for (Message message: allMessages){
            message.write();
        }
    }

    @Override
    public void updateScene(Input input) {
        cleanDeletedEntity();
        for (GameEntity entity: allGameEntities){
            entity.updatePerFrame(input);
        }
    }

    protected void updateMessage(){

    };

    public GameStage getGameStage(){
        return this.gameStage;
    }


    @Override
    public boolean isEnd() {
        // Check if all players finish losing or winning
        for (GameEntity entity: allGameEntities){
            // Test of the
            if (!(entity instanceof Player)){
                continue;
            }

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

    /**
     * Clean the deleted GameEntity to save space every update
     */
    protected void cleanDeletedEntity(){
        // Clean the deleted objects
        Set<GameEntity> deletedEntities = new HashSet<>();
        for (GameEntity entity: allGameEntities){
            if (entity.isDeleted()){
                deletedEntities.add(entity);
            }
        }

        for (GameEntity deletedEntity: deletedEntities){
            allGameEntities.remove(deletedEntity);
        }
    }




}

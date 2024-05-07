package Scenes.PlayingScenes;

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
import utils.Fonts;
import utils.IOUtils;
import utils.Message;
import utils.StatusMessages.HealthStatusMessage;
import utils.StatusMessages.ScoreStatusMessage;
import utils.StatusMessages.StatusObserver;

import java.util.*;

public abstract class PlayingScene implements Scene {
    protected GameStage gameStage;
    protected Set<GameEntity> allGameEntities;
    protected List<Message> allMessages;
    protected CollisionMediator collisionMediator;

    public PlayingScene(){
        allMessages = new ArrayList<Message>();
        allGameEntities = new HashSet<GameEntity>();
        collisionMediator = new CollisionMediator(allGameEntities);

        this.gameStage = GameStage.PLAYING;
        this.loadScene();
        this.loadCollisionDetectors();
    }


    protected void loadScene(String filename){
        Properties gameProps = GameProps.getGameProps();
        List<String[]> worldInfo = IOUtils.readCsv(filename);

        // Load game entities
        for (String[] strings : worldInfo) {
            Point location = new Point(Double.parseDouble(strings[1]), Double.parseDouble(strings[2]));
            GameEntity currentEntity = null;
            String name = strings[0];

            switch (name) {
                case "PLAYER":
                    ScoreStatusMessage scoreStatusMessage = new ScoreStatusMessage("",
                            new Point(Double.parseDouble(gameProps.getProperty("score.x")),
                                    Double.parseDouble(gameProps.getProperty("score.y"))),
                            Fonts.getMediumFont(), false);
                    HealthStatusMessage healthStatusMessage = new HealthStatusMessage("",
                            new Point(Double.parseDouble(gameProps.getProperty("playerHealth.x")),
                                    Double.parseDouble(gameProps.getProperty("playerHealth.y"))),
                            Fonts.getMediumFont(), false);
                    allMessages.add(scoreStatusMessage);
                    allMessages.add(healthStatusMessage);

                    // Add observers and update them
                    Player player = new Player(location);
                    player.addStatusObserver(scoreStatusMessage);
                    player.addStatusObserver(healthStatusMessage);
                    player.notifyObservers();

                    currentEntity = player;
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
                    // TODO: Add messages for enemy boss
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

    protected abstract void loadScene();
    protected abstract void loadCollisionDetectors();


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
        this.collisionMediator.handleCollision();
        for (GameEntity entity: allGameEntities){
            entity.updatePerFrame(input);
        }
    }

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

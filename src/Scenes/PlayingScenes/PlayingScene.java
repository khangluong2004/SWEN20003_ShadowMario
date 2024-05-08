package Scenes.PlayingScenes;

import CollisionHandling.CollisionMediator;
import GameEntities.Attackers.Enemy;
import GameEntities.Characters.EnemyBoss;
import GameEntities.Characters.Player.Player;
import GameEntities.Characters.Player.PlayerStage;
import GameEntities.Flags.EndFlag;
import GameEntities.GameEntity;
import GameEntities.PickUpItems.Coin;
import GameEntities.PickUpItems.DoubleScorePower;
import GameEntities.PickUpItems.InvinciblePower;
import GameEntities.Platforms.FlyingPlatform;
import GameEntities.Platforms.Platform;
import GameProperties.GameProps;
import Scenes.GameStage;
import Scenes.Scene;
import bagel.Input;
import bagel.util.Colour;
import bagel.util.Point;
import utils.Fonts;
import utils.IOUtils;
import utils.Message;
import utils.StatusMessages.HealthStatusMessage;
import utils.StatusMessages.ScoreStatusMessage;

import java.util.*;

public abstract class PlayingScene implements Scene {
    protected List<GameEntity> allGameEntities;
    protected List<Message> allMessages;
    protected CollisionMediator collisionMediator;
    private GameStage gameStage;

    public PlayingScene(){
        allMessages = new ArrayList<Message>();
        allGameEntities = new ArrayList<GameEntity>();
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
                    int scoreSize = Integer.parseInt(gameProps.getProperty("score.fontSize"));
                    ScoreStatusMessage scoreStatusMessage = new ScoreStatusMessage("",
                            new Point(Double.parseDouble(gameProps.getProperty("score.x")),
                                    Double.parseDouble(gameProps.getProperty("score.y"))),
                            Fonts.getFont(scoreSize), false);

                    int playerHealthSize = Integer.parseInt(gameProps.getProperty("playerHealth.fontSize"));
                    HealthStatusMessage healthStatusMessage = new HealthStatusMessage("",
                            new Point(Double.parseDouble(gameProps.getProperty("playerHealth.x")),
                                    Double.parseDouble(gameProps.getProperty("playerHealth.y"))),
                            Fonts.getFont(playerHealthSize), false);
                    allMessages.add(scoreStatusMessage);
                    allMessages.add(healthStatusMessage);

                    // Add observers and update them
                    Player player = new Player(location, this);
                    player.addStatusObserver(scoreStatusMessage);
                    player.addStatusObserver(healthStatusMessage);
                    player.notifyObservers();

                    currentEntity = player;
                    break;
                case "PLATFORM":
                    currentEntity = new Platform(location, this);
                    break;
                case "ENEMY":
                    currentEntity = new Enemy(location, this);
                    break;
                case "END_FLAG":
                    currentEntity = new EndFlag(location, this);
                    break;
                case "COIN":
                    currentEntity = new Coin(location, this);
                    break;
                case "FLYING_PLATFORM":
                    currentEntity = new FlyingPlatform(location, this);
                    break;
                case "ENEMY_BOSS":
                    // TODO: Change the font class
                    // Create a status message for health
                    int bossHealthSize = Integer.parseInt(gameProps.getProperty("enemyBossHealth.fontSize"));
                    HealthStatusMessage healthBossStatusMessage = new HealthStatusMessage("",
                            new Point(Double.parseDouble(gameProps.getProperty("enemyBossHealth.x")),
                                    Double.parseDouble(gameProps.getProperty("enemyBossHealth.y"))),
                            Fonts.getFont(bossHealthSize), false, Colour.RED);
                    allMessages.add(healthBossStatusMessage);

                    EnemyBoss enemyBoss = new EnemyBoss(location, this);
                    enemyBoss.addStatusObserver(healthBossStatusMessage);
                    enemyBoss.notifyObservers();

                    currentEntity = enemyBoss;
                    break;
                case "INVINCIBLE_POWER":
                    currentEntity = new InvinciblePower(location, this);
                    break;
                case "DOUBLE_SCORE":
                    currentEntity = new DoubleScorePower(location, this);
                    break;
            }

            allGameEntities.add(currentEntity);
        }
    };

    protected abstract void loadScene();
    protected abstract void loadCollisionDetectors();

    public void addGameEntity(GameEntity entity){
        allGameEntities.add(entity);
    }

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
        // Create a clone so the update doesn't interfere with the looping
        Set<GameEntity> allGameEntitiesCopy = new HashSet<>(this.allGameEntities);
        this.collisionMediator.handleCollision();
        for (GameEntity entity: allGameEntitiesCopy){
            entity.updatePerFrame(input);
        }
    }


    @Override
    public boolean isEnd() {
        return checkWinning() || checkLosing();
    }

    public GameStage getGameStage(){
        this.isEnd();
        return this.gameStage;
    }

    private boolean checkWinning(){
        if (this.gameStage == GameStage.WINNING){
            return true;
        }

        // The count that increments when player satisfied
        // positive condition (eg: reaching flag) and decrements
        // with negative condition (eg: boss is still alive)
        int conditionCount = 0;

        for (GameEntity entity: allGameEntities){
            if (entity instanceof Player){
                Player player = (Player) entity;
                if (player.getPlayerStage() == PlayerStage.REACHED_FLAG){
                    conditionCount++;
                }
            } else if (entity instanceof EnemyBoss){
                // If the boss exist, then decrement the count
                conditionCount--;
            }
        }

        if (conditionCount == 1){
            this.gameStage = GameStage.WINNING;
            return true;
        }

        return false;
    }

    private boolean checkLosing(){
        if (this.gameStage == GameStage.LOSING){
            return true;
        }

        for (GameEntity entity: allGameEntities){
            if (entity instanceof Player){
                return false;
            }
        }

        this.gameStage = GameStage.LOSING;
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

        collisionMediator.removeDeletedCollision(deletedEntities);
    }




}

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
import Scenes.Scene;
import bagel.Input;
import bagel.util.Colour;
import bagel.util.Point;
import Utils.Fonts;
import Utils.IOUtils;
import Messages.Message;
import Messages.StatusMessages.HealthStatusMessage;
import Messages.StatusMessages.ScoreStatusMessage;

import java.util.*;

/**
 * Abstract class for the PlayingScene (where the user can play).
 * Make use of Factory method pattern, with the loadScene and loadCollisionDetectors
 * being the factory method(s) which will be overridden by specific Level to load appropriate objects
 */
public abstract class PlayingScene implements Scene {
    protected List<GameEntity> allGameEntities;
    // Buffer for the added game entities, used to avoid interfering with the looping/ update process
    protected List<GameEntity> bufferedEntities;
    protected List<Message> allMessages;
    protected CollisionMediator collisionMediator;
    private GameStage gameStage;

    /**
     * Constructor (helper) to initialize all the storage and game stage
     * Call the factory method to load scene and collision detectors
     */
    public PlayingScene(){
        allMessages = new ArrayList<>();
        allGameEntities = new ArrayList<>();
        bufferedEntities = new ArrayList<>();
        collisionMediator = new CollisionMediator(allGameEntities);
        this.gameStage = GameStage.PLAYING;

        // Call factory methods
        this.loadScene();
        this.loadCollisionDetectors();
    }

    /**
     * Factory method to loadScene based on each level implementation
     */
    protected abstract void loadScene();

    /**
     * Factory method to loadCollisionDetectors based on each level implementation
     */
    protected abstract void loadCollisionDetectors();

    /**
     * Load the game entities (and related messages) from the given csv
     * @param filename name of the csv file
     */
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
                    // Create a status message for health
                    int bossHealthSize = Integer.parseInt(gameProps.getProperty("enemyBossHealth.fontSize"));
                    HealthStatusMessage healthBossStatusMessage = new HealthStatusMessage("",
                            new Point(Double.parseDouble(gameProps.getProperty("enemyBossHealth.x")),
                                    Double.parseDouble(gameProps.getProperty("enemyBossHealth.y"))),
                            Fonts.getFont(bossHealthSize), false, Colour.RED);
                    allMessages.add(healthBossStatusMessage);

                    // Create the boss, add the message and update the message status
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
    }

    /**
     * Draw all gameEntities and message
     */
    @Override
    public void drawScene() {
        for (GameEntity entity: allGameEntities){
            entity.draw();
        }

        for (Message message: allMessages){
            message.write();
        }
    }


    /**
     * Methods to add game entity to a buffer, which will be added after the update to avoid interfere with
     * updating process
     */
    public void addGameEntity(GameEntity entity){
        bufferedEntities.add(entity);
    }

    /**
     * Flush the buffer to add all the added game entities to the list of allGameEntities
     * after each update
     */
    protected void flushBuffer(){
        allGameEntities.addAll(bufferedEntities);
        bufferedEntities.clear();
    }

    /**
     * Call the update method on all gameEntities, passing on the input as delegation, and handle collision
     */
    @Override
    public void updateScene(Input input) {
        cleanDeletedEntity();

        for (GameEntity entity: allGameEntities){
            entity.updatePerFrame(input);
        }

        // Flush the buffer to actually add game entities (if any) to the internal list
        flushBuffer();

        // Handle collision
        this.collisionMediator.handleCollision();
    }

    /**
     * Check if the player wins this game, by checking for reaching flag (level 1 and 2)
     * and no boss still exists (if any at the start)
     */
    private boolean checkWinning(){
        if (this.gameStage == GameStage.WINNING){
            return true;
        }

        // The count that increments when player satisfied
        // positive condition (reaching flag) and decrements
        // with negative condition (boss is still alive)
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

    /**
     * Check losing by checking if the player still exists in the scene
     * (if the player lost and moved out of the window, it's deleted)
     */
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
     * Check if the scene end
     */
    @Override
    public boolean isEnd() {
        return checkWinning() || checkLosing();
    }

    /**
     * Get the current game stage
     * @return the current game stage
     */
    public GameStage getGameStage(){
        this.isEnd();
        return this.gameStage;
    }


    /**
     * Clean the deleted GameEntity to save space every update,
     * and simplify collision handling and losing/winning stage checking
     */
    protected void cleanDeletedEntity(){
        // Clean the deleted objects
        Set<GameEntity> deletedEntities = new HashSet<>();
        for (GameEntity entity: allGameEntities){
            if (entity.isDeleted()){
                deletedEntities.add(entity);
            }
        }

        allGameEntities.removeAll(deletedEntities);
        collisionMediator.removeDeletedCollision(deletedEntities);
    }

}

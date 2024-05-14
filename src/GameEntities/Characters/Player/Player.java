package GameEntities.Characters.Player;

import GameEntities.Attackers.FireBall;
import GameEntities.Characters.EnemyBoss;
import GameEntities.Characters.Fireable;
import GameEntities.Characters.Killable;
import GameEntities.Characters.PowerUps.PowerUpManager;
import GameEntities.Characters.PowerUps.TimedPowerUp;
import GameEntities.Characters.ScoreContainer;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.GameEntity;
import GameEntities.Movable;

import GameEntities.Attackers.Enemy;
import GameEntities.PickUpItems.Coin;
import GameEntities.PickUpItems.DoubleScorePower;
import GameEntities.PickUpItems.InvinciblePower;
import GameEntities.Platforms.FlyingPlatform;
import GameEntities.Platforms.Platform;
import GameEntities.Flags.EndFlag;


import GameEntities.Characters.StatusContainer;
import GameProperties.GameProps;
import Scenes.PlayingScenes.PlayingScene;
import bagel.*;
import bagel.util.Point;

import GameEntities.MoveDirection;
import GameEntities.Characters.PowerUps.PowerUpItem;
import Messages.StatusMessages.StatusObserver;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Player class, handling the changing image when the player is moving
 * left or right, jumping motion, losing motion, collision side effects and status notifications for displays
 */
public class Player extends GameEntity implements Movable, RadiusCollidable, Killable, Fireable, ScoreContainer, StatusContainer {
    // Images and constants
    private final static int START_JUMPING_SPEED = -20;
    private final static int DIE_SPEED = 2;
    private final static int JUMP_ACCELERATION = 1;
    private final double RADIUS;

    // GameEntity.Characters.Player properties
    private boolean lockJump;

    // Vertical velocity
    private int velocity;
    private int score;
    private double health;

    // Boolean to check if in range to fire
    private boolean isFirable;

    // Stage management
    private PlayerStage playerStage;


    // Powerup Manager
    PowerUpManager powerUpManager;

    // Status observer
    Set<StatusObserver> observers;

    // Previous platform location: To handle the requirement for not able to jump down from higher flying platform
    Point previousPlatformLocation;

    /**
     * Create player and initialize key attributes
     * @param location
     * @param scene
     */
    public Player(Point location, PlayingScene scene){
        super(new ArrayList<Image>(), 0, location, scene);
        Properties gameProps = GameProps.getGameProps();

        // Store the right image at 0, left image at 1
        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.player.imageRight")));
        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.player.imageLeft")));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.player.radius"));

        // Add the powerup manager
        this.powerUpManager = new PowerUpManager();

        // Add observers list
        this.observers = new HashSet<StatusObserver>();

        // Initialize attributes
        this.velocity = 0;
        this.score = 0;
        this.setHealth(Double.parseDouble(gameProps.getProperty("gameObjects.player.health")));
        this.lockJump = false;
        this.playerStage = PlayerStage.PLAYING;
        this.previousPlatformLocation = new Point(0.0, 0.0);
        this.isFirable = false;
    }

    /**
     * Update per frame to update movement and power ups
     * @param input
     */
    @Override
    public void updatePerFrame(Input input){
        // Check if outside the bottom screen, then deleted
        if (this.location.y > Window.getHeight()){
            this.isDeleted = true;
            return;
        }

        // Update the power ups
        this.powerUpManager.updatePerFrameAllPowerUp();

        // If not lost, then update move (and fire if possible)
        if (this.playerStage != PlayerStage.LOSING){
            updateMove(input);
            if (input.wasPressed(Keys.S) && isFirable){
                this.fire(null);
            }

            // Set jump acceleration always to simulate gravity
            this.velocity += JUMP_ACCELERATION;
            // Lock jump until collided with platform
            this.lockJump = true;

        } else {
            // If lost, then just continue the downward movement
            move(MoveDirection.CONTINUE);
        }

        // Notify statuses
        notifyObservers();
    }

    /**
     * Set health and game stage (also velocity and acceleration) if dead
     * @param newHealth
     */
    private void setHealth(double newHealth){
        this.health = Math.max(0, newHealth);
        if (this.health <= 0){
            this.playerStage = PlayerStage.LOSING;
            this.lockJump = false; // Remove any jumping acceleration when dead
            this.velocity = DIE_SPEED;
        }
    }

    /**
     * Get health
     * @return
     */
    public double getHealth(){
        return this.health;
    }

    /**
     * Get score
     * @return
     */
    public int getScore() {
        return score;
    }

    /**
     * Get player stage
     * @return
     */
    public PlayerStage getPlayerStage(){
        return this.playerStage;
    }

    /**
     * Check if is invincible or not (damageable)
     * @return
     */
    @Override
    public boolean isDamageable() {
        return !(powerUpManager.getPowerUpItems().contains(PowerUpItem.INVINCIBLE));
    }


    /**
     * Method to jump, change velocity and lock the jump to ensure no double jump
     */
    public void jump(){
        // Check if jumping
        if (lockJump){
            return;
        }

        velocity = START_JUMPING_SPEED;
        lockJump = true;
    }

    /**
     * Handle jumping movement and changing image facing direction
     * @param direction
     */
    public void move(MoveDirection direction){
        if (direction == MoveDirection.UP){
            this.jump();
        } else if (direction == MoveDirection.LEFT){
            this.currentImageIndex = 1;
        } else if (direction == MoveDirection.RIGHT){
            this.currentImageIndex = 0;
        } else if (direction == MoveDirection.CONTINUE){
            location = new Point(location.x, location.y + velocity);
        }
    }

    /**
     * Handling collision, identify type of entity and delegate collision handling to appropriate helpers
     * @param entity the entity that is collided with
     */
    @Override
    public void startCollideWith(Collidable entity) {
        // Check if lost
        if (this.playerStage == PlayerStage.LOSING){
            return;
        }

        if (entity instanceof Enemy){
            handleCollisionEntity((Enemy) entity);
        } else if (entity instanceof Platform){
            handleCollisionEntity((Platform) entity);
        } else if (entity instanceof FlyingPlatform){
            handleCollisionEntity((FlyingPlatform) entity);
        } else if (entity instanceof Coin){
            handleCollisionEntity((Coin) entity);
        } else if (entity instanceof DoubleScorePower){
            handleCollisionEntity((DoubleScorePower) entity);
        } else if (entity instanceof InvinciblePower) {
            handleCollisionEntity((InvinciblePower) entity);
        } else if (entity instanceof EndFlag){
            handleCollisionEntity((EndFlag) entity);
        } else if (entity instanceof FireBall){
            handleCollisionEntity((FireBall) entity);
        } else if (entity instanceof EnemyBoss){
            handleCollisionEntity((EnemyBoss) entity);
        }
    }

    /**
     * Collision handling for enemy
     * @param enemy
     */
    private void handleCollisionEntity(Enemy enemy){
        // Down cast enemy and get the damage value
        // then change health (if first time collided)
        double damage = enemy.getDamage(this);

        if (!enemy.isInflictedDamage() && this.isDamageable()){
            this.setHealth(this.health + damage);
        }
    }

    /**
     * Collision handling for platform
     * @param platform
     */
    private void handleCollisionEntity(Platform platform){
        // Stop falling and allow jumping
        this.lockJump = false;
        this.velocity = 0;

        // Downcast the entity and get the Offset, location and height values from the platform
        double offsetPixels = platform.getOffsetPixels();
        Point platformLocation = platform.getLocation();
        double platformHeight = platform.getHeight();

        // Calculate the location just above the platform after the collision
        // Need this calculation to ensure the player is always above the platform, even if the
        // high velocity of the player leads to collision detection appears after the player overlaps
        // with the platform
        this.location = new Point(this.location.x, platformLocation.y - platformHeight - offsetPixels);
        this.previousPlatformLocation = platformLocation;
    }

    /**
     * Collision handling for flyingPlatform
     * @param flyingPlatform
     */
    private void handleCollisionEntity(FlyingPlatform flyingPlatform){
        // Check if jumping from a higher platform, then do nothing
        Point currentPlatformLocation = flyingPlatform.getLocation();
        if (this.previousPlatformLocation.y < currentPlatformLocation.y){
            return;
        }

        // Stop falling and allow jumping
        this.lockJump = false;
        this.velocity = 0;

        Properties gameProps = GameProps.getGameProps();

        // Offset the player to stand above the platform
        this.location = new Point(this.location.x, currentPlatformLocation.y - Double.parseDouble(gameProps.getProperty("gameObjects.flyingPlatform.halfHeight")));
        this.previousPlatformLocation = currentPlatformLocation;
    }

    /**
     * Collision handling for coin
     * @param coin
     */
    private void handleCollisionEntity(Coin coin){
        // Downcast the coin, and change score if first time collided
        if (!coin.isPlayerCollided()){
            int value = coin.getCoinValue();
            if (powerUpManager.getPowerUpItems().contains(PowerUpItem.DOUBLE_SCORE)){
                value *= 2;
            }
            this.score += value;
        }
    }

    /**
     * Collision handling for double score
     * @param doubleScore
     */
    private void handleCollisionEntity(DoubleScorePower doubleScore){
        powerUpManager.addPowerUp(new TimedPowerUp(PowerUpItem.DOUBLE_SCORE, doubleScore.getDoubleScoreLastingFrames()));
    }

    /**
     * Collision handling for invincible
     * @param invincible
     */
    private void handleCollisionEntity(InvinciblePower invincible){
        powerUpManager.addPowerUp(new TimedPowerUp(PowerUpItem.INVINCIBLE, invincible.getInvincibleLastingFrames()));
    }

    /**
     * Collision handling for endFlag
     * @param endFlag
     */
    private void handleCollisionEntity(EndFlag endFlag){
        this.playerStage = PlayerStage.REACHED_FLAG;
    }

    /**
     * Collision handling for fireball
     * @param fireBall
     */
    private void handleCollisionEntity(FireBall fireBall){
        // Check if invincible, else change the health
        if (powerUpManager.getPowerUpItems().contains(PowerUpItem.INVINCIBLE)){
            return;
        }

        if (fireBall.isFirer(this)){
            return;
        }

        this.setHealth(this.health + fireBall.getDamage(this));
    }

    /**
     * Collision handling for enemyBoss
     * @param enemyBoss
     */
    private void handleCollisionEntity(EnemyBoss enemyBoss){
        this.isFirable = true;
    }

    /**
     * Handling effects when get out of the collision of different entity
     * For player, only endFlag and enemyBoss
     * @param entity
     */
    @Override
    public void outOfCollision(Collidable entity){
        if (entity instanceof EndFlag){
            if (this.health <= 0){
                this.playerStage = PlayerStage.LOSING;
            } else {
                this.playerStage = PlayerStage.PLAYING;
            }
        } else if (entity instanceof EnemyBoss){
            this.isFirable = false;
        }
    }

    /**
     * Get the collision radius
     * @param entity
     * @return
     */
    @Override
    public double getCollisionRadius(Collidable entity) {
        return this.RADIUS;
    }

    /**
     * Fire the fireball with the velocity moving in the direction facing by the characters
     * @param target
     */
    @Override
    public void fire(GameEntity target) {
        boolean moveLeft = true;
        if (this.currentImageIndex == 0){
            moveLeft = false;
        }

        this.currentScene.addGameEntity(new FireBall(this.location, moveLeft, this, this.currentScene));
    }

    /**
     * Add observer
     * @param observer
     */
    @Override
    public void addStatusObserver(StatusObserver observer) {
        observers.add(observer);
    }

    /**
     * Remove observer
     * @param observer
     */
    @Override
    public void removeObserver(StatusObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notify observers
     */
    @Override
    public void notifyObservers() {
        for (StatusObserver observer: observers){
            observer.notify(this);
        }
    }
}

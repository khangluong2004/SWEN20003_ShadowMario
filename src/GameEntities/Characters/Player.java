package GameEntities.Characters;

import GameEntities.Attackers.FireBall;
import GameEntities.Characters.PowerUps.PowerUpManager;
import GameEntities.Characters.PowerUps.TimedPowerUp;
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


import GameEntities.StatusContainer;
import GameProperties.GameProps;
import Scenes.PlayingScenes.PlayingScene;
import bagel.*;
import bagel.util.Point;

import enums.GameStage;
import enums.MoveDirection;
import enums.PowerUpItem;
import utils.StatusMessages.StatusObserver;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * GameEntity.Characters.Player class, handling the changing image when the player is moving
 * left or right, jumping motion, losing motion and the score/ health display.
 * Collision side-effects, as explained in GameEntity.Movable interface, is handled by the different
 * entities that collide with GameEntity.Characters.Player (and not the GameEntity.Characters.Player itself), ensuring Open-Closed Principle (I hope)
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

    private GameStage gameStage;

    // Powerup Manager
    PowerUpManager powerUpManager;

    // Status observer
    Set<StatusObserver> observers;

    // Previous platform location: To handle the silly requirements for not able to jump down
    Point previousPlatformLocation;


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
        this.gameStage = GameStage.PLAYING;
        this.previousPlatformLocation = new Point(0.0, 0.0);
        this.isFirable = false;
    }

    @Override
    public void updatePerFrame(Input input){
        // Update the power ups
        this.powerUpManager.updatePerFrameAllPowerUp();

        if (this.gameStage == GameStage.PLAYING){
            updateMove(input);
            if (input.isDown(Keys.S) && isFirable){
                this.fire(null);
            }
        } else {
            // If lost, then just continue the downward movement
            move(MoveDirection.CONTINUE);
        }

        notifyObservers();

        // Set jump acceleration always to simulate gravity
        this.velocity += JUMP_ACCELERATION;
        // Reset isFirable, so it's only set when in range with boss
        this.isFirable = false;
    }

    private void setHealth(double newHealth){
        this.health = Math.max(0, newHealth);
        if (this.health <= 0){
            this.gameStage = GameStage.START_LOSING;
            this.lockJump = false; // Remove any jumping acceleration when dead
            this.velocity = DIE_SPEED;
        }
    }

    public double getHealth(){
        return this.health;
    }

    public int getScore() {
        return score;
    }

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

    public void move(MoveDirection direction){
        if (direction == MoveDirection.UP){
            this.jump();
        } else if (direction == MoveDirection.LEFT){
            this.currentImageIndex = 1;
        } else if (direction == MoveDirection.RIGHT){
            this.currentImageIndex = 0;
        } else if (direction == MoveDirection.CONTINUE){
            location = new Point(location.x, location.y + velocity);

            // Handling losing movement
            // Change state to finish losing when the player is out of the screen
            if (this.gameStage == GameStage.START_LOSING){
                if (location.y > Window.getHeight()){
                    this.gameStage = GameStage.FINISH_LOSING;
                }
            }
        }
    }

    public GameStage getEndingStage(){
        return gameStage;
    }


    @Override
    public void startCollideWith(Collidable entity) {
        // Check if lost
        if (this.gameStage != GameStage.PLAYING){
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

    private void handleCollisionEntity(Enemy enemy){
        // Down cast enemy and get the damage value
        // then change health (if first time collided)
        double damage = enemy.getDamage(this);

        if (!enemy.isInflictedDamage() && this.isDamageable()){
            this.setHealth(this.health + damage);
        }
    }

    private void handleCollisionEntity(Platform platform){
        // Stop jumping
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

    private void handleCollisionEntity(FlyingPlatform flyingPlatform){
        // Check if jumping from a higher platform, then do nothing
        Point currentPlatformLocation = flyingPlatform.getLocation();
        if (this.previousPlatformLocation.y < currentPlatformLocation.y){
            return;
        }

        // Stop jumping
        this.lockJump = false;
        this.velocity = 0;

        Properties gameProps = GameProps.getGameProps();

        // Offset the player to stand above the platform
        this.location = new Point(this.location.x, currentPlatformLocation.y - Double.parseDouble(gameProps.getProperty("gameObjects.flyingPlatform.halfHeight")));
        this.previousPlatformLocation = currentPlatformLocation;
    }

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

    private void handleCollisionEntity(DoubleScorePower doubleScore){
        powerUpManager.addPowerUp(new TimedPowerUp(PowerUpItem.DOUBLE_SCORE, doubleScore.getDoubleScoreLastingFrames()));
    }

    private void handleCollisionEntity(InvinciblePower invincible){
        powerUpManager.addPowerUp(new TimedPowerUp(PowerUpItem.INVINCIBLE, invincible.getInvincibleLastingFrames()));
    }

    private void handleCollisionEntity(EndFlag endFlag){
        // TODO: Fix this. Delegate winning decision to the scene.
        // Set winning stage
        this.gameStage = GameStage.WINNING;
    }

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

    private void handleCollisionEntity(EnemyBoss enemyBoss){
        this.isFirable = true;
    }

    @Override
    public void endCollideWith(Collidable entity) {}

    @Override
    public double getCollisionRadius(Collidable entity) {
        return this.RADIUS;
    }

    @Override
    public void fire(GameEntity target) {
        boolean moveLeft = true;
        if (this.currentImageIndex == 0){
            moveLeft = false;
        }

        this.currentScene.addGameEntity(new FireBall(this.location, moveLeft, this, this.currentScene));
    }



    @Override
    public void addStatusObserver(StatusObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(StatusObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (StatusObserver observer: observers){
            observer.notify(this);
        }
    }
}

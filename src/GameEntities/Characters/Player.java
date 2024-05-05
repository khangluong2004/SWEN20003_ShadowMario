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
import GameEntities.Platforms.Platform;
import GameEntities.Flags.EndFlag;


import GameProperties.GameProps;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import enums.GameStage;
import enums.MoveDirection;
import enums.PowerUpItem;


import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

/**
 * GameEntity.Characters.Player class, handling the changing image when the player is moving
 * left or right, jumping motion, losing motion and the score/ health display.
 * Collision side-effects, as explained in GameEntity.Movable interface, is handled by the different
 * entities that collide with GameEntity.Characters.Player (and not the GameEntity.Characters.Player itself), ensuring Open-Closed Principle (I hope)
 */
public class Player extends GameEntity implements Movable, RadiusCollidable, Killable, Fireable, ScoreContainer {
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

    private GameStage gameStage;

    // Powerup Manage
    PowerUpManager powerUpManager;
    private Set<PowerUpItem> allPowerUpItems;



    public Player(Point location){
        super(new ArrayList<Image>(), 0, location);
        Properties gameProps = GameProps.getGameProps();

        // Store the right image at 0, left image at 1
        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.player.imageRight")));
        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.player.imageLeft")));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.player.radius"));

        // Add the powerup manager
        this.powerUpManager = new PowerUpManager();

        this.velocity = 0;
        this.score = 0;
        this.setHealth(Double.parseDouble(gameProps.getProperty("gameObjects.player.health")));
        this.lockJump = false;
        this.gameStage = GameStage.PLAYING;
    }

    @Override
    public void updatePerFrame(){
        // Update the power ups
        this.powerUpManager.updatePerFrameAllPowerUp();
        this.allPowerUpItems = powerUpManager.getPowerUpItems();
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
    public boolean isDead() {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return allPowerUpItems.contains(PowerUpItem.INVINCIBLE);
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
            if (lockJump){
                velocity += JUMP_ACCELERATION;
            }

            // Handling losing movement
            // Change state to finish losing when the player is out of the screen
            if (this.gameStage == GameStage.START_LOSING){
                if (location.y > Window.getHeight()){
                    this.gameStage = GameStage.FINISH_LOSING;
                }
            }
        }
    }


    public void draw(){
        super.draw();
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
        }
    }

    private void handleCollisionEntity(Enemy enemy){

        // Down cast enemy and get the damage value
        // then change health (if first time collided)
        double damage = enemy.getDamage(this);

        if (!enemy.isInflictedDamage()){
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
    }

    private void handleCollisionEntity(Coin coin){
        // Downcast the coin, and change score if first time collided
        if (!coin.isPlayerCollided()){
            this.score += coin.getCoinValue();
        }
    }

    private void handleCollisionEntity(DoubleScorePower doubleScore){
        powerUpManager.addPowerUp(new TimedPowerUp(PowerUpItem.DOUBLE_SCORE, doubleScore.getDoubleScoreLastingFrames()));
    }

    private void handleCollisionEntity(InvinciblePower invincible){
        powerUpManager.addPowerUp(new TimedPowerUp(PowerUpItem.INVINCIBLE, invincible.getInvincibleLastingFrames()));
    }

    private void handleCollisionEntity(EndFlag endFlag){
        // Set winning stage
        this.gameStage = GameStage.WINNING;
    }

    private void handleCollisionEntity(FireBall fireBall){
        // Check if invincible, else change the health
        if (allPowerUpItems.contains(PowerUpItem.INVINCIBLE)){
            return;
        }
        this.setHealth(this.health + fireBall.getDamage(this));
    }

    @Override
    public void endCollideWith(Collidable entity) {}

    @Override
    public double getCollisionRadius(Collidable entity) {
        return this.RADIUS;
    }

    @Override
    public void fire() {
        // TODO: Fire after set up the playing scene
    }

    @Override
    public void target(GameEntity target) {
        //TODO: Finish after set up the playing scene
    }
}

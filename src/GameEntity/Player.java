package GameEntity;

import CollisionHandling.Collidable;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import enums.EndingStage;
import enums.MoveDirection;
import utils.DistanceUtils;
import utils.Message;
import utils.PercentageUtils;

import java.util.Properties;

/**
 * GameEntity.Player class, handling the changing image when the player is moving
 * left or right, jumping motion, losing motion and the score/ health display.
 * Collision side-effects, as explained in GameEntity.Movable interface, is handled by the different
 * entities that collide with GameEntity.Player (and not the GameEntity.Player itself), ensuring Open-Closed Principle (I hope)
 */
public class Player extends GameEntity implements Movable, Collidable {
    // Images and constants
    private final static int START_JUMPING_SPEED = -20;
    private final static int END_SPEED = 2;
    private final static int INIT_JUMPING_SPEED = 0;
    private final static int INIT_SCORE = 0;
    private final static int JUMP_ACCELERATION = 1;
    // Store the windowHeight to check if the player is out of the window when the player lost
    private final Image PLAYER_LEFT;
    private final Image PLAYER_RIGHT;

    // GameEntity.Player properties
    private boolean lockJump;
    private final double RADIUS;
    // Vertical velocity
    private int velocity;
    private int score;
    private double health;

    private EndingStage endingStage;

    // Score and health display
    // scoreMessage and healthMessage is mutable, so not set to final
    private final String SCORE_INIT_MESSAGE;
    private Message scoreMessage;
    private final String HEALTH_INIT_MESSAGE;
    private Message healthMessage;


    public Player(Point location, Properties gameProps, Properties messageProps){
        super(new Image(gameProps.getProperty("gameObjects.player.imageLeft")), location);
        PLAYER_RIGHT = new Image(gameProps.getProperty("gameObjects.player.imageRight"));
        PLAYER_LEFT = entityImg;
        RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.player.radius"));

        Font scoreFont = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("score.fontSize")));
        SCORE_INIT_MESSAGE = messageProps.getProperty("score");
        scoreMessage = new Message(SCORE_INIT_MESSAGE + this.score, new Point(Double.parseDouble(gameProps.getProperty("score.x")),
                Double.parseDouble(gameProps.getProperty("score.y"))), scoreFont, false);

        Font healthFont = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("health.fontSize")));
        HEALTH_INIT_MESSAGE = messageProps.getProperty("health");
        healthMessage = new Message(HEALTH_INIT_MESSAGE + this.health, new Point(Double.parseDouble(gameProps.getProperty("health.x")),
                Double.parseDouble(gameProps.getProperty("health.y"))), healthFont, false);

        this.resetAttributes(gameProps);
    }

    public void resetAttributes(Properties gameProps){
        super.resetAttributes(gameProps);
        this.velocity = INIT_JUMPING_SPEED;
        this.setScore(INIT_SCORE);
        this.setHealth(Double.parseDouble(gameProps.getProperty("gameObjects.player.health")));
        this.lockJump = false;
        this.endingStage = EndingStage.NOT_END;
        entityImg = PLAYER_RIGHT;
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

    public void updateLocation(MoveDirection direction){
        if (direction == MoveDirection.UP){
            this.jump();
        } else if (direction == MoveDirection.LEFT){
            entityImg = PLAYER_LEFT;
        } else if (direction == MoveDirection.RIGHT){
            entityImg = PLAYER_RIGHT;
        } else if (direction == MoveDirection.CONTINUE){
            location = new Point(location.x, location.y + velocity);
            if (lockJump){
                velocity += JUMP_ACCELERATION;
            }

            // Handling losing movement
            // Change state to finish losing when the player is out of the screen
            if (this.endingStage == EndingStage.START_LOSING){
                if (location.y > Window.getHeight()){
                    this.endingStage = EndingStage.FINISH_LOSING;
                }
            }
        }
    }


    public void draw(){
        super.draw();
        scoreMessage.draw();
        healthMessage.draw();
    }

    public double getHealth(){
        return this.health;
    }

    private void setHealth(double health){
        this.health = Math.max(0, health);
        healthMessage.setMessageStr(HEALTH_INIT_MESSAGE + PercentageUtils.toPercentage(this.health));

        // Check if the player lost
        // If yes, then change the endingStage and
        // set velocity to END_SPEED
        if (this.health <= 0){
            this.endingStage = EndingStage.START_LOSING;
            this.lockJump = false; // Remove any jumping acceleration when dead
            this.velocity = END_SPEED;
        }
    }


    public int getScore() {
        return score;
    }

    private void setScore(int score){
        this.score = score;
        scoreMessage.setMessageStr(SCORE_INIT_MESSAGE + this.score);
    }

    public EndingStage getEndingStage(){
        return endingStage;
    }


    @Override
    public void collideWith(Collidable entity) {
        if (entity instanceof Enemy){
            // Down cast enemy and get the damage value
            // then change health (if first time collided)
            Enemy enemy = (Enemy) entity;
            double damage = enemy.getDamageSize();

            if (!enemy.isInflictedDamage()){
                this.setHealth(this.health + damage);
            }

        } else if (entity instanceof Platform){
            // Stop jumping
            this.lockJump = false;
            this.velocity = 0;

            // Downcast the entity and get the Offset, location and height values from the platform
            Platform platform = (Platform) entity;
            double offsetPixels = platform.getOffsetPixels();
            Point platformLocation = platform.getLocation();
            double platformHeight = platform.getHeight();

            // Calculate the location just above the platform after the collision
            this.location = new Point(this.location.x, platformLocation.y - platformHeight - offsetPixels);
        } else if (entity instanceof Coin){
            // Downcast the coin, and change score if first time collided
            Coin coin = (Coin) entity;
            if (!coin.isPlayerCollided()){
                this.setScore(score + coin.getCoinValue());
            }
        } else if (entity instanceof EndFlag){
            // Set winning stage
            this.endingStage = EndingStage.WINNING;
        }
    }

    @Override
    public void finishColliding(Collidable entity) {}

    @Override
    public double getRadius() {
        return this.RADIUS;
    }

    @Override
    public double calcDistanceSquared(Point location2) {
        return DistanceUtils.calcEuclideanDistanceSquared(this.location, location2);
    }
}

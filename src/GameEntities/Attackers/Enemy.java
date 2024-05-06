package GameEntities.Attackers;


import GameEntities.Characters.Player;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.GameEntity;
import GameEntities.Movable;
import GameProperties.GameProps;

import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import enums.MoveDirection;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

/**
 * Class for enemy entity, inherits from GameEntity.EuclideanCollidableMovableEntity,
 * with overidden collision handling methods and reset attributes
 */
public class Enemy extends GameEntity implements RadiusCollidable, Movable, Attacker {
    private final double RADIUS;
    private final int STEP_SIZE;
    private final int RANDOM_STEP_SIZE;
    private final int FRAMES_UNTIL_CHANGE_DIRECTION;
    private final double DAMAGE_SIZE;
    private boolean inflictedDamage;
    // Negative velocity is to the left, positive is to the right
    private int velocity;
    private int frameSinceLastChangeDirection;

    public Enemy(Point location){
        super(new ArrayList<Image>(), 0, location);
        Properties gameProps = GameProps.getGameProps();

        this.DAMAGE_SIZE = -1 * Double.parseDouble(gameProps.getProperty("gameObjects.enemy.damageSize"));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.enemy.radius"));
        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.enemy.speed"));
        this.RANDOM_STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.enemy.randomSpeed"));
        this.FRAMES_UNTIL_CHANGE_DIRECTION = Integer.parseInt(gameProps.getProperty("gameObjects.enemy.maxRandomDisplacementX")) / this.RANDOM_STEP_SIZE;

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.enemy.image")));
        this.inflictedDamage = false;
        this.updateDirection();
    }

    @Override
    public void updatePerFrame(Input input){
        updateMove(input);
    }

    /**
     * Common updateLocation for LEFT/RIGHT movements for all movable objects
     * @param direction the direction from the key pressed by the user
     */
    @Override
    public void move(MoveDirection direction) {
        if (direction == MoveDirection.LEFT){
            this.location = new Point(location.x + STEP_SIZE, location.y);
        } else if (direction == MoveDirection.RIGHT){
            this.location = new Point(location.x - STEP_SIZE, location.y);
        } else if (direction == MoveDirection.CONTINUE){
            this.frameSinceLastChangeDirection++;
            if (this.frameSinceLastChangeDirection >= FRAMES_UNTIL_CHANGE_DIRECTION){
                this.updateDirection();
                this.frameSinceLastChangeDirection = 0;
            }

            this.location = new Point(location.x + velocity, location.y);
        }
    }

    private void updateDirection(){
        Random random = new Random();
        boolean toLeft = random.nextBoolean();
        if (toLeft){
            // Move left
            velocity = -1 * RANDOM_STEP_SIZE;
        } else {
            velocity = RANDOM_STEP_SIZE;
        }
    }

    @Override
    public double getDamage(GameEntity entity){
        return DAMAGE_SIZE;
    }

    public boolean isInflictedDamage(){
        return inflictedDamage;
    }

    /**
     * Handling side-effect during collision with other entity
     * @param entity
     */
    @Override
    public void startCollideWith(Collidable entity) {}

    /**
     * Handling side-effect after collision with other entity
     * @param entity the entity that is collided with
     */
    @Override
    public void endCollideWith(Collidable entity) {
        if (!this.inflictedDamage && entity instanceof Player){
            // Check if the player is damageable. If not, then don't set the inflictedDamage flag
            Player player = (Player) entity;
            if (!player.isDamageable()){
                return;
            }

            // Set flag so not to inflict damage twice
            this.inflictedDamage = true;
        }
    }

    /**
     * Get the collision radius of the object
     * @return the radius of the object
     */
    @Override
    public double getCollisionRadius(Collidable entity) {
        return RADIUS;
    }
}

package GameEntity;

import bagel.Image;
import bagel.util.Point;
import enums.MoveDirection;
import utils.DistanceUtils;

import java.util.Properties;

/**
 * Class for enemy entity, inherits from GameEntity.EuclideanCollidableMovableEntity,
 * with overidden collision handling methods and reset attributes
 */
public class Enemy extends GameEntity implements Collidable{
    private int RADIUS;
    private int STEP_SIZE;
    private final double damageSize;
    private boolean inflictedDamage;

    public Enemy(Point location, Properties game_props){
        super(new Image(game_props.getProperty("gameObjects.enemy.image")), location);
        damageSize = -1 * Double.parseDouble(game_props.getProperty("gameObjects.enemy.damageSize"));
        this.inflictedDamage = false;
    }

    public double getDamageSize(){
        return damageSize;
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
            // Set flag so not to inflict damage twice
            this.inflictedDamage = true;
        }
    }

    /**
     * Get the collision radius of the object
     * @return the radius of the object
     */
    @Override
    public double getRadius() {
        return RADIUS;
    }

    /**
     * Calculate the distance between this entity and another entity using Euclidean distance
     * @param location2
     * @return the distance squared
     */
    @Override
    public double calcDistanceSquared(Point location2) {
        return DistanceUtils.calcEuclideanDistanceSquared(this.location, location2);
    }


    /**
     * Common updateLocation for LEFT/RIGHT movements for all movable objects
     * @param direction the direction from the key pressed by the user
     */

    public void updateLocation(MoveDirection direction) {
        if (direction == MoveDirection.LEFT){
            this.location = new Point(location.x + STEP_SIZE, location.y);
        } else if (direction == MoveDirection.RIGHT){
            this.location = new Point(location.x - STEP_SIZE, location.y);
        }
    }
}

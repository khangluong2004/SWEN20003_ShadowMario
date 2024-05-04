package GameEntity;

import bagel.Image;
import bagel.util.Point;
import enums.MoveDirection;
import utils.DistanceUtils;
import CollisionHandling.Collidable;

/**
 * Base abstract class for entities that are movable and collidable, with collision detected by
 * Euclidean distance. Since the Image should be a static singleton for each subclass, it should be defined
 * within the subclass
 */
public abstract class EuclideanCollidableMovableEntity extends GameEntity implements Movable, Collidable {
    private final int STEP_SIZE;
    private final double RADIUS;


    /**
     * Common "constructor" (helper) shared by all the child, initializing collision range,
     * location, stepSize and range
     * @param location the entity location
     * @param radius the radius of the objects for collision detection
     * @param stepSize the size of each moving steps per update
     */
    public EuclideanCollidableMovableEntity(Point location, double radius, int stepSize, Image entityImg){
        super(entityImg, location);
        this.RADIUS = radius;
        this.STEP_SIZE = stepSize;
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
    @Override
    public void updateLocation(MoveDirection direction) {
        if (direction == MoveDirection.LEFT){
            this.location = new Point(location.x + STEP_SIZE, location.y);
        } else if (direction == MoveDirection.RIGHT){
            this.location = new Point(location.x - STEP_SIZE, location.y);
        }
    }
}
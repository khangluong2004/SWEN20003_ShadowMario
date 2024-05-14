package GameEntities.CollisionInterface;

/**
 * Interface for entity that detects collision using the collision radius
 */
public interface RadiusCollidable extends Collidable {
    /**
     * Get the collision radius
     * @param entity entity
     * @return collision radius
     */
    double getCollisionRadius(Collidable entity);
}

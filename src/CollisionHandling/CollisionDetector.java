package CollisionHandling;

import GameEntities.CollisionInterface.Collidable;

/**
 * Interface for all collision detector (served to check if there is collision between 2 entities)
 */
public interface CollisionDetector {
    /**
     * Check if there is a collision, no collision, or the entity has incompatible type
     * @param entity1 first entity
     * @param entity2 second entity
     * @return one of the 3 outcomes stated above
     */
    CollisionType checkCollision(Collidable entity1, Collidable entity2);
}

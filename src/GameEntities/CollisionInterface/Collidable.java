package GameEntities.CollisionInterface;

import java.awt.*;

/**
 * Interface for entities that can collide with other entities,
 * and change the behaviour of the other collided entities.
 *
 * Some methods are left as default, since not all entities require an effect,
 * yet are still collidable
 */
public interface Collidable {
    /**
     * The collideWith methods inflicts side effect on the object based on the entity collided with
     * at the start of the collision
     * @param entity the entity that is collided with
     */
    default void startCollideWith(Collidable entity){};

    /**
     * Method to create any effects after the collision, when all "collideWith" effects are processed
     * @param entity the entity that is collided with
     */
    default void endCollideWith(Collidable entity){};

    /**
     * Method to create effects when get out of the collision.
     *
     */
    default void outOfCollision(Collidable entity){}

}

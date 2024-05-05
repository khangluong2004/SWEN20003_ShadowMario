package CollisionHandling;

import GameEntity.GameEntity;
import GameEntity.Player;

/**
 * The mediator class to control the collision between entities
 */
public class CollisionMediator {
    // References of all entities
    // GameEntity.Player is a special one, since all effect is derived
    // from the player collided with other entities.

    // Keeping player as a separate entity to ensure an O(n)
    // scan instead of O(n^2) scanning for collision between all pairs
    // Might require further abstraction for more complicated collision between object pairs
    private final Player PLAYER;
    private final GameEntity[] OTHER_ENTITIES;

    private final int MAX_OTHER_ENTITIES;

    /**
     * Constructor: Initialize all entities references
     * @param player
     * @param otherEntities
     * @param MAX_ENTITIES
     */
    public CollisionMediator(Player player, GameEntity[] otherEntities, int MAX_ENTITIES){
        this.PLAYER = player;
        this.OTHER_ENTITIES = otherEntities;
        this.MAX_OTHER_ENTITIES = MAX_ENTITIES - 1;
    }

    /**
     * Helper methods to compare between the current distance and the specified collision range
     * Keep squared distance to avoid precision and performance issue with square root
     * @param distanceSquared Current distance between 2 objects squared
     * @param range The collision range between 2 objects (collided if closer than the specified range)
     * @return a boolean indicating if the 2 objects collided or not
     */
    private boolean checkCollide(double distanceSquared, double range){
        double rangeSquared = range * range;
        return distanceSquared <= rangeSquared;
    }

    /**
     * Main method to scan through all the gameEntities, detect if the player collide with any of them.
     * If yes, then call the collideWith() method on the collided entities to produce entity-specific on the player
     * and the entity
     */
    public void handleCollision(){
        // Check if the player collides with any other entities
        for (int i=0; i <  MAX_OTHER_ENTITIES; i++){
            if (OTHER_ENTITIES[i] == null || !(OTHER_ENTITIES[i] instanceof Collidable)){
                continue;
            }

            Collidable collidableEntity = (Collidable) OTHER_ENTITIES[i];
            double distanceSquared = collidableEntity.calcDistanceSquared(PLAYER.getLocation());
            double range = PLAYER.getRadius() + collidableEntity.getRadius();

            // Range is different for the GameEntity.Platform, which only considers the platform radius
            if (collidableEntity instanceof Platform){
                range = collidableEntity.getRadius();
            }

            if (checkCollide(distanceSquared, range)){
                collidableEntity.startCollideWith(PLAYER);
                PLAYER.startCollideWith(collidableEntity);
                collidableEntity.endCollideWith(PLAYER);
                PLAYER.startCollideWith(collidableEntity);
            }
        }
    }

}

package CollisionHandling;

import GameEntities.CollisionInterface.Collidable;
import GameEntities.GameEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The mediator class to control the collision between entities
 */
public class CollisionMediator {
    // Keep a references of all entities in the scene owned it
    private final List<GameEntity> allGameEntities;
    private final List<CollisionDetector> collisionDetectors;

    /**
     * Create a collision mediator, and passed in the reference to the list of game entities
     * Pass the reference maintained in the scene, so that the deletion of entities are synced
     * Initialize an empty list of collision detectors (that can be added later based on the needs)
     * @param allGameEntities
     */
    public CollisionMediator(List<GameEntity> allGameEntities){
        this.allGameEntities = allGameEntities;
        this.collisionDetectors = new ArrayList<>();
    }

    /***
     * Add collisionDetector
     * @param detector
     */
    public void addCollisionDetector(CollisionDetector detector){
        collisionDetectors.add(detector);
    }

    /***
     * Handling collision by calling all the collisionDetectors on all objects,
     * and call the collide with method
     */
    public void handleCollision(){
        List<Collidable> listAllCollidables = this.getCollidables();
        // Loop through all pairs of entities
        for (int i=0; i < listAllCollidables.size(); i++){
            for (int j=i+1; j < listAllCollidables.size(); j++){
                Collidable entity1 = listAllCollidables.get(i);
                Collidable entity2 = listAllCollidables.get(j);

                // Loop through all collision detectors
                for (CollisionDetector collisionDetector: collisionDetectors){
                    // Check 2 directions (since all collision for this game is bidirectional)
                    // Check if incompatible, then skip
                    if (collisionDetector.checkCollision(entity1, entity2) == CollisionType.NOT_COMPATIBLE &&
                            collisionDetector.checkCollision(entity2, entity1) == CollisionType.NOT_COMPATIBLE) {
                        continue;
                    }
                    // Check if collided
                    if (collisionDetector.checkCollision(entity1, entity2) == CollisionType.COLLIDED ||
                            collisionDetector.checkCollision(entity2, entity1) == CollisionType.COLLIDED) {
                        // Call collision callback
                        entity1.startCollideWith(entity2);
                        entity2.startCollideWith(entity1);
                        entity1.endCollideWith(entity2);
                        entity2.endCollideWith(entity1);
                    } else {
                        // Call out of collision callback
                        entity1.outOfCollision(entity2);
                        entity2.outOfCollision(entity1);
                    }

                }
            }
        }
    }

    /**
     * Call outOfCollision for all deleted entity
     * @param deletedEntities
     */
    public void removeDeletedCollision(Set<GameEntity> deletedEntities){
        // Loop through each game entity
        for (GameEntity entity: allGameEntities){
            // Skip non-collidable entity
            if (!(entity instanceof Collidable)){
                continue;
            }

            // Now loop through the entire list of deleted entities, and call out of collision between
            // the pair (since no more collision with deleted entity)
            Collidable collidable = (Collidable) entity;

            for (GameEntity deletedEntity: deletedEntities){
                if (deletedEntity instanceof Collidable){
                    collidable.outOfCollision((Collidable) deletedEntity);
                }
            }

        }
    }

    /***
     * Parse and get the array of Collidable and not deleted item for easier collision handling
     *
     */
    private List<Collidable> getCollidables(){
        List<Collidable> listAllCollidables = new ArrayList<>();
        for (GameEntity entity: allGameEntities){
            if (!entity.isDeleted() && entity instanceof Collidable){
                listAllCollidables.add((Collidable) entity);
            }
        }
        return listAllCollidables;
    }


}

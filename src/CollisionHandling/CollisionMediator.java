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
    private List<GameEntity> allGameEntities;
    private List<CollisionDetector> collisionDetectors;


    public CollisionMediator(List<GameEntity> allGameEntities){
        this.allGameEntities = allGameEntities;
        this.collisionDetectors = new ArrayList<CollisionDetector>();
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
        for (int i=0; i < listAllCollidables.size(); i++){
            for (int j=i+1; j < listAllCollidables.size(); j++){
                Collidable entity1 = listAllCollidables.get(i);
                Collidable entity2 = listAllCollidables.get(j);

                for (CollisionDetector collisionDetector: collisionDetectors){
                    // Check 2 directions (since all collision for this game is bi-directional)
                    if (collisionDetector.checkCollision(entity1, entity2) != CollisionType.NOT_COMPATIBLE ||
                            collisionDetector.checkCollision(entity2, entity1) != CollisionType.NOT_COMPATIBLE){
                        if (collisionDetector.checkCollision(entity1, entity2) == CollisionType.COLLIDED ||
                                collisionDetector.checkCollision(entity2, entity1) == CollisionType.COLLIDED) {
                            entity1.startCollideWith(entity2);
                            entity2.startCollideWith(entity1);
                            entity1.endCollideWith(entity2);
                            entity2.endCollideWith(entity1);
                        } else {
                            entity1.outOfCollision(entity2);
                            entity2.outOfCollision(entity1);
                        }
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
        for (GameEntity entity: allGameEntities){
            if (entity instanceof Collidable){
                Collidable collidable = (Collidable) entity;
                for (GameEntity deletedEntity: deletedEntities){
                    if (deletedEntity instanceof Collidable){
                        collidable.outOfCollision((Collidable) deletedEntity);
                    }
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

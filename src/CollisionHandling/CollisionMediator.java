package CollisionHandling;

import GameEntities.CollisionInterface.Collidable;
import GameEntities.GameEntity;
import GameEntities.Characters.Player;
import enums.GameEntityTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The mediator class to control the collision between entities
 */
public class CollisionMediator {
    // Keep a references of all entities in the scene owned it
    private Map<GameEntityTypes, List<GameEntity>> entitiesByTypes;
    private List<CollisionDetector> collisionDetectors;

    public CollisionMediator(Map<GameEntityTypes, List<GameEntity>> entitiesByTypes){
        this.entitiesByTypes = entitiesByTypes;
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
                    if (collisionDetector.checkCollision(entity1, entity2) ||
                            collisionDetector.checkCollision(entity2, entity1)){
                        entity1.startCollideWith(entity2);
                        entity2.startCollideWith(entity1);
                        entity1.endCollideWith(entity2);
                        entity2.endCollideWith(entity1);
                    }
                }
            }
        }
    }

    /***
     * Convert the map to array of Collidable for easier collision handling
     *
     */
    private List<Collidable> getCollidables(){
        List<Collidable> listAllCollidables = new ArrayList<>();
        for (var entitiesEntry: entitiesByTypes.entrySet()){
            for (GameEntity entity: entitiesEntry.getValue()){
                if (entity instanceof Collidable){
                    listAllCollidables.add((Collidable) entity);
                }
            }
        }
        return listAllCollidables;
    }


}

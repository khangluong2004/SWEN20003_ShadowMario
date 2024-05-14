package CollisionHandling;

import GameEntities.CollisionInterface.Collidable;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.GameEntity;
import bagel.util.Point;

/**
 * Class for the collision detector between all RadiusCollidable objects
 */
public class RadiusCollidableCollisionDetector implements CollisionDetector{
    /**
     * Check the type and collision
     * @param entity1 first entity
     * @param entity2 second entity
     * @return outcome of collision check
     */
    @Override
    public CollisionType checkCollision(Collidable entity1, Collidable entity2) {
        if (!(entity1 instanceof RadiusCollidable && entity2 instanceof RadiusCollidable)){
            return CollisionType.NOT_COMPATIBLE;
        }

        return checkCollision((RadiusCollidable) entity1, (RadiusCollidable) entity2)
                ? CollisionType.COLLIDED
                : CollisionType.NOT_COLLIDED;
    }

    /**
     * Check the collision by comparing the distance and the total of the collision radius of the 2 objects
     * @param radiusEntity1 entity1
     * @param radiusEntity2 entity2
     * @return outcomes of collision
     */
    private boolean checkCollision(RadiusCollidable radiusEntity1, RadiusCollidable radiusEntity2){
        double radius1 = radiusEntity1.getCollisionRadius(radiusEntity2);
        double radius2 = radiusEntity2.getCollisionRadius(radiusEntity1);
        double range = radius1 + radius2;

        if (!(radiusEntity1 instanceof GameEntity && radiusEntity2 instanceof GameEntity)){
            return false;
        }

        Point radiusEntity1Location = ((GameEntity) radiusEntity1).getLocation();
        Point radiusEntity2Location = ((GameEntity) radiusEntity2).getLocation();

        if (radiusEntity1Location.distanceTo(radiusEntity2Location) <= range){
            return true;
        }
        return false;
    }
}

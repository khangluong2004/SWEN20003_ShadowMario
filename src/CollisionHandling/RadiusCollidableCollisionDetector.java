package CollisionHandling;

import GameEntities.CollisionInterface.Collidable;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.GameEntity;
import bagel.util.Point;

public class RadiusCollidableCollisionDetector implements CollisionDetector{
    @Override
    public boolean checkCollision(Collidable entity1, Collidable entity2) {
        if (!(entity1 instanceof RadiusCollidable && entity2 instanceof RadiusCollidable)){
            return false;
        }

        return checkCollision((RadiusCollidable) entity1, (RadiusCollidable) entity2);
    }

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

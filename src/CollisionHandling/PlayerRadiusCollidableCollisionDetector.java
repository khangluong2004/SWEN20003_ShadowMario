package CollisionHandling;

import GameEntities.Characters.EnemyBoss;
import GameEntities.Characters.Player;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.GameEntity;
import bagel.util.Point;

public class PlayerRadiusCollidableCollisionDetector implements CollisionDetector{
    @Override
    public boolean checkCollision(Collidable entity1, Collidable entity2) {
        if (!(entity1 instanceof Player && entity2 instanceof RadiusCollidable)){
            return false;
        }

        return checkCollision((Player) entity1, (RadiusCollidable) entity2);
    }

    private boolean checkCollision(Player player, RadiusCollidable radiusCollidable){
        double playerRadius = player.getCollisionRadius(radiusCollidable);
        double radiusCollidableRadius = radiusCollidable.getCollisionRadius(player);
        double range = playerRadius + radiusCollidableRadius;

        Point playerLocation = player.getLocation();
        Point radiusCollidablesLocation = ((GameEntity) radiusCollidable).getLocation();

        if (playerLocation.distanceTo(radiusCollidablesLocation) <= range){
            return true;
        }
        return false;
    }
}

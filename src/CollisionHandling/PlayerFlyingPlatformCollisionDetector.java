package CollisionHandling;

import GameEntities.Characters.Player;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.GameEntity;
import GameEntities.Platforms.FlyingPlatform;
import bagel.util.Point;

public class PlayerFlyingPlatformCollisionDetector implements CollisionDetector{
    @Override
    public boolean checkCollision(Collidable entity1, Collidable entity2) {
        if (!(entity1 instanceof Player && entity2 instanceof FlyingPlatform)){
            return false;
        }

        return checkCollision((Player) entity1, (FlyingPlatform) entity2);
    }


}

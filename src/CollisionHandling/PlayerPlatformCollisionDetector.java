package CollisionHandling;

import GameEntities.Characters.Player.Player;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.Platforms.Platform;
import bagel.util.Point;

public class PlayerPlatformCollisionDetector implements CollisionDetector{
    @Override
    public CollisionType checkCollision(Collidable entity1, Collidable entity2) {
        if (!(entity1 instanceof Player && entity2 instanceof Platform)){
            return CollisionType.NOT_COMPATIBLE;
        }

        return checkCollision((Player) entity1, (Platform) entity2) ? CollisionType.COLLIDED : CollisionType.NOT_COLLIDED;
    }

    private boolean checkCollision(Player player, Platform platform){
        Point playerLocation = player.getLocation();
        Point platformLocation = platform.getLocation();
        double diffY = Math.abs(playerLocation.y - platformLocation.y);
        double diffX = Math.abs(playerLocation.x - platformLocation.x);

        double rangeY = platform.getHeight() + platform.getOffsetPixels();
        double rangeX = (platform.getWidth()) / 2.0;

        if (diffY <= rangeY && diffX <= rangeX){
            return true;
        }
        return false;
    }
}

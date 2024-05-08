package CollisionHandling;

import GameEntities.Characters.Player.Player;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.Platforms.FlyingPlatform;
import GameProperties.GameProps;
import bagel.util.Point;

import java.util.Properties;

public class PlayerFlyingPlatformCollisionDetector implements CollisionDetector{
    @Override
    public boolean checkCollision(Collidable entity1, Collidable entity2) {
        if (!(entity1 instanceof Player && entity2 instanceof FlyingPlatform)){
            return false;
        }

        return checkCollision((Player) entity1, (FlyingPlatform) entity2);
    }

    private boolean checkCollision(Player player, FlyingPlatform flyingPlatform){
        Properties gameProps = GameProps.getGameProps();
        Point playerLocation = player.getLocation();
        Point flyingPlatformLocation = flyingPlatform.getLocation();
        double diffX = playerLocation.x - flyingPlatformLocation.x;
        double diffY = -(playerLocation.y - flyingPlatformLocation.y);

        int HALF_LENGTH = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.halfLength"));
        int HALF_HEIGHT = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.halfHeight"));


        if (Math.abs(diffX) < HALF_LENGTH && (HALF_HEIGHT - 1) <= diffY && diffY <= HALF_HEIGHT){
            return true;
        }

        return false;
    }


}

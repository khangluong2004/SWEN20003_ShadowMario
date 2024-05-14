package CollisionHandling;

import GameEntities.Characters.Player.Player;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.Platforms.FlyingPlatform;
import GameProperties.GameProps;
import bagel.util.Point;

import java.util.Properties;

/**
 * Class for the collision detector between the player and flying platform
 */
public class PlayerFlyingPlatformCollisionDetector implements CollisionDetector{
    /**
     * Check the type and collision
     * @param entity1 first entity
     * @param entity2 second entity
     * @return outcome of collision check
     */
    @Override
    public CollisionType checkCollision(Collidable entity1, Collidable entity2) {
        if (!(entity1 instanceof Player && entity2 instanceof FlyingPlatform)){
            return CollisionType.NOT_COMPATIBLE;
        }

        return checkCollision((Player) entity1, (FlyingPlatform) entity2) ? CollisionType.COLLIDED: CollisionType.NOT_COLLIDED;
    }

    /**
     * Check for the collision between the player and the flying platform with the criteria given in the spec
     * @param player player
     * @param flyingPlatform flying platform
     * @return outcome of collision
     */
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

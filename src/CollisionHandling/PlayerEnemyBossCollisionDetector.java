package CollisionHandling;

import GameEntities.Characters.EnemyBoss;
import GameEntities.Characters.Player.Player;
import GameEntities.CollisionInterface.Collidable;
import GameProperties.GameProps;
import bagel.util.Point;

import java.util.Properties;

/**
 * Class for the collision detector between player and enemy boss
 */
public class PlayerEnemyBossCollisionDetector implements CollisionDetector{

    /**
     * Check type, and then collision
     * @param entity1 first entity
     * @param entity2 second entity
     * @return the outcomes of collision check
     */
    @Override
    public CollisionType checkCollision(Collidable entity1, Collidable entity2) {
        if (!(entity1 instanceof Player && entity2 instanceof EnemyBoss)){
            return CollisionType.NOT_COMPATIBLE;
        }

        return checkCollision((Player) entity1, (EnemyBoss) entity2) ? CollisionType.COLLIDED: CollisionType.NOT_COLLIDED;
    }

    /**
     * Check for collision between player and enemy boss by comparing the x distance with the given range
     * @param player player object
     * @param enemyBoss enemyBoss object
     * @return if collided or not
     */
    private boolean checkCollision(Player player, EnemyBoss enemyBoss){
        Properties gameProps = GameProps.getGameProps();
        Point playerLocation = player.getLocation();
        Point bossLocation = enemyBoss.getLocation();

        int range = Integer.parseInt(gameProps.getProperty("gameObjects.enemyBoss.activationRadius"));

        if (Math.abs(playerLocation.x - bossLocation.x) <= range){
            return true;
        }

        return false;
    }
}

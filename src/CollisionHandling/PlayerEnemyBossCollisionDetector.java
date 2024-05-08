package CollisionHandling;

import GameEntities.Characters.EnemyBoss;
import GameEntities.Characters.Player.Player;
import GameEntities.CollisionInterface.Collidable;
import GameProperties.GameProps;
import bagel.util.Point;

import java.util.Properties;

public class PlayerEnemyBossCollisionDetector implements CollisionDetector{

    @Override
    public boolean checkCollision(Collidable entity1, Collidable entity2) {
        if (!(entity1 instanceof Player && entity2 instanceof EnemyBoss)){
            return false;
        }

        return checkCollision((Player) entity1, (EnemyBoss) entity2);
    }

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

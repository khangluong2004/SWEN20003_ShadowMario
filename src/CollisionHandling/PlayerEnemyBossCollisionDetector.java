package CollisionHandling;

import GameEntities.Characters.EnemyBoss;
import GameEntities.Characters.Player;
import GameEntities.CollisionInterface.Collidable;
import bagel.util.Point;

public class PlayerEnemyBossCollisionDetector implements CollisionDetector{

    @Override
    public boolean checkCollision(Collidable entity1, Collidable entity2) {
        if (!(entity1 instanceof Player && entity2 instanceof EnemyBoss)){
            return false;
        }

        return checkCollision((Player) entity1, (EnemyBoss) entity2);
    }

    private boolean checkCollision(Player player, EnemyBoss enemyBoss){
        Point playerLocation = player.getLocation();
        Point bossLocation = enemyBoss.getLocation();

        if (Math.abs(playerLocation.x - bossLocation.x) <= 500){
            return true;
        }

        return false;
    }
}

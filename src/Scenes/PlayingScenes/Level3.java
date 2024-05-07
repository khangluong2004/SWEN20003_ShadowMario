package Scenes.PlayingScenes;

import CollisionHandling.PlayerEnemyBossCollisionDetector;
import CollisionHandling.PlayerFlyingPlatformCollisionDetector;
import CollisionHandling.PlayerPlatformCollisionDetector;
import CollisionHandling.PlayerRadiusCollidableCollisionDetector;
import GameProperties.GameProps;

import java.util.Properties;

public class Level3 extends PlayingScene{
    @Override
    protected void loadScene() {
        Properties gameProps = GameProps.getGameProps();
        this.loadScene(gameProps.getProperty("level3File"));
    }

    @Override
    protected void loadCollisionDetectors() {
        this.collisionMediator.addCollisionDetector(new PlayerPlatformCollisionDetector());
        this.collisionMediator.addCollisionDetector(new PlayerRadiusCollidableCollisionDetector());
        this.collisionMediator.addCollisionDetector(new PlayerFlyingPlatformCollisionDetector());
        this.collisionMediator.addCollisionDetector(new PlayerEnemyBossCollisionDetector());
    }
}

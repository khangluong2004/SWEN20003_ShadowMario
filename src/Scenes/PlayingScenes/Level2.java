package Scenes.PlayingScenes;

import CollisionHandling.PlayerFlyingPlatformCollisionDetector;
import CollisionHandling.PlayerPlatformCollisionDetector;
import CollisionHandling.RadiusCollidableCollisionDetector;
import GameProperties.GameProps;

import java.util.Properties;

/**
 * Implementation of Playing Scene for Level 2
 */
public class Level2 extends PlayingScene {
    @Override
    protected void loadScene() {
        Properties gameProps = GameProps.getGameProps();
        this.loadScene(gameProps.getProperty("level2File"));
    }

    @Override
    protected void loadCollisionDetectors() {
        this.collisionMediator.addCollisionDetector(new PlayerPlatformCollisionDetector());
        this.collisionMediator.addCollisionDetector(new RadiusCollidableCollisionDetector());
        this.collisionMediator.addCollisionDetector(new PlayerFlyingPlatformCollisionDetector());
    }
}

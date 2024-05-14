package Scenes.PlayingScenes;

import CollisionHandling.PlayerPlatformCollisionDetector;
import CollisionHandling.RadiusCollidableCollisionDetector;
import GameProperties.GameProps;

import java.util.Properties;

/**
 * Implementation of Playing Scene for Level 1
 */
public class Level1 extends PlayingScene{

    /**
     * Load entities from a level1file
     */
    @Override
    protected void loadScene() {
        Properties gameProps = GameProps.getGameProps();
        this.loadScene(gameProps.getProperty("level1File"));
    }

    /**
     * Add required collision detector
     */
    @Override
    protected void loadCollisionDetectors() {
        this.collisionMediator.addCollisionDetector(new PlayerPlatformCollisionDetector());
        this.collisionMediator.addCollisionDetector(new RadiusCollidableCollisionDetector());
    }
}

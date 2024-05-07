package Scenes.PlayingScenes;

import CollisionHandling.PlayerPlatformCollisionDetector;
import CollisionHandling.PlayerRadiusCollidableCollisionDetector;
import GameProperties.GameProps;

import java.util.Properties;

public class Level1 extends PlayingScene{

    @Override
    protected void loadScene() {
        Properties gameProps = GameProps.getGameProps();
        this.loadScene(gameProps.getProperty("level1File"));
    }

    @Override
    protected void loadCollisionDetectors() {
        this.collisionMediator.addCollisionDetector(new PlayerPlatformCollisionDetector());
        this.collisionMediator.addCollisionDetector(new PlayerRadiusCollidableCollisionDetector());
    }
}

package GameEntities.PickUpItems;

import GameProperties.GameProps;
import Scenes.PlayingScenes.PlayingScene;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Class for the GameEntity.PickUpItems.Coin entities, inheriting from the base
 * class GameEntity.EuclideanCollidableMovableEntity (since the coin
 * can move and detect collision by Euclidean distance)
 */
public class Coin extends PickupItem {
    private final int COIN_VALUE;

    /**
     * Constructor to initialize location, velocity, coin's value, image, collision range
     * @param location Location to place the coin
     *
     */
    public Coin(Point location, PlayingScene scene){
        super(new ArrayList<Image>(), 0, location, scene);
        Properties gameProps = GameProps.getGameProps();

        this.COIN_VALUE = Integer.parseInt(gameProps.getProperty("gameObjects.coin.value"));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.coin.radius"));
        this.POPPING_SPEED = -10;
        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.coin.speed"));

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.coin.image")));
    }

    /**
     * Get coin value
     * @return coin value
     */
    public int getCoinValue(){
        return this.COIN_VALUE;
    }


}
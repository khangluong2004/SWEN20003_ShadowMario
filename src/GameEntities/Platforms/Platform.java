package GameEntities.Platforms;

import GameEntities.GameEntity;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.Movable;

import GameProperties.GameProps;
import Scenes.PlayingScenes.PlayingScene;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import GameEntities.MoveDirection;

import java.util.ArrayList;
import java.util.Properties;

/**
 * GameEntity.Platforms.Platform class for the platform objects.
 * Stop player's falling velocity from jumping after collision
 */
public class Platform extends GameEntity implements Collidable, Movable {
    // The offset pixel to make the player appears slightly above the platform
    private static final int OFFSET_PIXELS = 8;
    // Minimum displacement to the left
    private static final int LEFT_MIN = 3000;
    private final double STEP_SIZE;


    /**
     * Make a platform
     * @param location location
     * @param scene scene the entity is in
     */
    public Platform(Point location, PlayingScene scene){
        super(new ArrayList<Image>(), 0, location, scene);

        Properties gameProps = GameProps.getGameProps();
        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.platform.image")));
        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.platform.speed"));
    }

    /**
     * Update the entity per frame
     * @param input user input
     */
    @Override
    public void updatePerFrame(Input input){
        updateMove(input);
    }

    /**
     * Get the offset pixels above the platform
     * @return OFFSET_PIXELS
     */
    public int getOffsetPixels(){
        return OFFSET_PIXELS;
    }

    /**
     * Method to update the x-coordinates, but restricting the minimum to 3000
     * as specified in the spec
     * @param oldX old value of x
     * @param change change in x
     * @return new value of x
     */
    private double updateX(double oldX, double change){
        double newX = oldX + change;
        newX = Math.min(newX, LEFT_MIN);
        return newX;
    }

    /**
     * Handling movement based on the direction
     * @param direction the direction indicated by key pressed
     */
    public void move(MoveDirection direction){
        double change = 0;
        if (direction == MoveDirection.LEFT){
            change = STEP_SIZE;
        } else if (direction == MoveDirection.RIGHT){
            change = -STEP_SIZE;
        }

        this.location = new Point(updateX(location.x, change), location.y);
    }
}

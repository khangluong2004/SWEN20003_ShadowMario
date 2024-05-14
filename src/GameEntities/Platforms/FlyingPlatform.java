package GameEntities.Platforms;

import GameEntities.CollisionInterface.Collidable;
import GameEntities.GameEntity;
import GameProperties.GameProps;
import GameEntities.Movable;

import Scenes.PlayingScenes.PlayingScene;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import GameEntities.MoveDirection;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

/**
 * Class for the FlyingPlatform, handling movement and display
 */
public class FlyingPlatform extends GameEntity implements Movable, Collidable {
    private final int STEP_SIZE;
    private final int RANDOM_STEP_SIZE;
    private final int MAX_DISPLACEMENT;

    // Negative velocity is to the left, positive is to the right
    private int velocity;
    private int randomDisplacement;


    /**
     * Make the FlyingPlatform, initialize constants (STEP_SIZE, RANDOM_STEP_SIZE, MAX_DISPLACEMENT)
     * @param location location
     * @param scene scene
     */
    public FlyingPlatform(Point location, PlayingScene scene){
        super(new ArrayList<Image>(), 0, location, scene);
        Properties gameProps = GameProps.getGameProps();

        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.speed"));
        this.RANDOM_STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.randomSpeed"));
        this.MAX_DISPLACEMENT = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.maxRandomDisplacementX"));

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.flyingPlatform.image")));
        this.randomDisplacement = 0;
        this.initializeRandomDirection();
    }

    /**
     * Pick a random direction initially, then initialize the velocity
     */
    private void initializeRandomDirection(){
        Random random = new Random();
        boolean toLeft = random.nextBoolean();
        if (toLeft){
            // Move left
            velocity = -1 * RANDOM_STEP_SIZE;
        } else {
            velocity = RANDOM_STEP_SIZE;
        }
    }

    /**
     * Update the entity per frame (mostly movement)
     * @param input input
     */
    @Override
    public void updatePerFrame(Input input){
        updateMove(input);
    }

    /**
     * Common updateLocation for LEFT/RIGHT movements for all movable objects
     * @param direction the direction from the key pressed by the user
     */
    @Override
    public void move(MoveDirection direction) {
        if (direction == MoveDirection.LEFT){
            this.location = new Point(location.x + STEP_SIZE, location.y);
        } else if (direction == MoveDirection.RIGHT){
            this.location = new Point(location.x - STEP_SIZE, location.y);
        } else if (direction == MoveDirection.CONTINUE){
            this.randomDisplacement += velocity;
            if (Math.abs(randomDisplacement) >= MAX_DISPLACEMENT){
                this.velocity = -1 * this.velocity;
            }

            this.location = new Point(location.x + velocity, location.y);
        }
    }
}

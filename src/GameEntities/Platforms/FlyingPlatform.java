package GameEntities.Platforms;

import GameEntities.CollisionInterface.Collidable;
import GameEntities.GameEntity;
import GameProperties.GameProps;
import GameEntities.Movable;

import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import enums.MoveDirection;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

public class FlyingPlatform extends GameEntity implements Movable, Collidable {
    private final int STEP_SIZE;
    private final int RANDOM_STEP_SIZE;
    private final int FRAMES_UNTIL_CHANGE_DIRECTION;

    // Negative velocity is to the left, positive is to the right
    private int velocity;
    private int frameSinceLastChangeDirection;

    public FlyingPlatform(Point location){
        super(new ArrayList<Image>(), 0, location);
        Properties gameProps = GameProps.getGameProps();

        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.speed"));
        this.RANDOM_STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.randomSpeed"));
        this.FRAMES_UNTIL_CHANGE_DIRECTION = Integer.parseInt(gameProps.getProperty("gameObjects.flyingPlatform.maxRandomDisplacementX")) / this.RANDOM_STEP_SIZE;

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.flyingPlatform.image")));
        this.updateDirection();
    }

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
            this.frameSinceLastChangeDirection++;
            if (this.frameSinceLastChangeDirection >= FRAMES_UNTIL_CHANGE_DIRECTION){
                this.updateDirection();
                this.frameSinceLastChangeDirection = 0;
            }

            this.location = new Point(location.x + velocity, location.y);
        }
    }

    private void updateDirection(){
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
     * Handling side-effect during collision with other entity
     * @param entity
     */
    @Override
    public void startCollideWith(Collidable entity) {}

    /**
     * Handling side-effect after collision with other entity
     * @param entity the entity that is collided with
     */
    @Override
    public void endCollideWith(Collidable entity) {}
}

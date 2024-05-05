package GameEntity;

import GameProperties.GameProps;
import bagel.Image;
import bagel.util.Point;
import enums.MoveDirection;
import utils.DistanceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class for GameEntity.EndFlag inheriting from GameEntity.EuclideanCollidableMovableEntity
 */
public class EndFlag extends GameEntity implements RadiusCollidable, Movable{
    private final double RADIUS;
    private final int STEP_SIZE;

    /**
     * Constructor to initialize the location and image
     * @param location
     */
    public EndFlag(Point location){
        super(new ArrayList<Image>(), 0, location);
        Properties gameProps = GameProps.getGameProps();

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.endFlag.image")));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.endFlag.radius"));
        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.endFlag.speed"));
    }


    @Override
    public void startCollideWith(Collidable entity) {}

    @Override
    public void endCollideWith(Collidable entity) {}

    /**
     * Get the collision radius of the object
     * @return the radius of the object
     */
    @Override
    public double getCollisionRadius(Collidable entity) {
        return RADIUS;
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
        }
    }
}

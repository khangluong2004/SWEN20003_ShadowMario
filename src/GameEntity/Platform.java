package GameEntity;

import CollisionHandling.Collidable;
import bagel.Image;
import bagel.util.Point;
import enums.MoveDirection;
import utils.DistanceUtils;

import java.util.Properties;

/**
 * GameEntity.Platform class for the platform objects.
 * Stop player's falling velocity from jumping after collision
 */
public class Platform extends GameEntity implements Collidable, Movable {
    private static final int LEFT_MIN = 3000;
    private final double STEP_SIZE;
    // The offset pixel to make the player appears slightly above the platform
    private final static int OFFSET_PIXELS = 8;
    private final double RADIUS;


    public Platform(Point location, Properties game_props){
        super(new Image(game_props.getProperty("gameObjects.platform.image")), location);
        this.STEP_SIZE = Integer.parseInt(game_props.getProperty("gameObjects.platform.speed"));
        RADIUS = entityImg.getHeight();
    }

    public double getOffsetPixels(){
        return OFFSET_PIXELS;
    }

    /**
     * Method to update the x-coordinates, but restricting the minimum to 3000
     * as specified in the spec
     * @param oldX
     * @param change
     * @return
     */
    private double updateX(double oldX, double change){
        double newX = oldX + change;
        newX = Math.min(newX, LEFT_MIN);
        // TODO: Check what to do if go over right bound?
        return newX;
    }

    public void updateLocation(MoveDirection direction){
        double change = 0;
        if (direction == MoveDirection.LEFT){
            change = STEP_SIZE;
        } else if (direction == MoveDirection.RIGHT){
            change = -STEP_SIZE;
        }

        this.location = new Point(updateX(location.x, change), location.y);
    }

    public void collideWith(Collidable entity){}

    @Override
    public void finishColliding(Collidable entity) {}

    @Override
    public double getRadius() {
        return RADIUS;
    }


    /**
     * Methods to calculate the distance squared.
     * For the platform, instead of euclidean, only the vertical distance
     * is calculated. Besides, if the collided entity is not in between the 2
     * edges of the platform, the distance is set to be larger than the
     * range automatically.
     *
     * @param location2 the location of the second entity (only player in this case)
     * @return
     */
    public double calcDistanceSquared(Point location2){
        // Return out of range when the x distance is more than half the width
        double diffX = Math.abs(location.x - location2.x);
        if (diffX * 2 > this.getWidth()){
            return RADIUS * RADIUS + 1;
        }
        return DistanceUtils.calcVerticalDistanceSquared(location, location2);
    }
}

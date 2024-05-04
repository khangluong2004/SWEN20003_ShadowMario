import bagel.Image;
import bagel.util.Point;

import java.util.Properties;

/**
 * The abstract class that all game entities should inherit,
 * containing methods to get the location, img's attributes,
 * and reset attributes method (to replay games)
 */
public abstract class GameEntity {
    // Store the initial location for resetting attributes during restarting
    private final Point INIT_LOCATION;
    protected Image entityImg;
    protected Point location;

    public GameEntity(Image entityImg, Point location){
        this.entityImg = entityImg;
        this.INIT_LOCATION = location;
        this.location = location;
    }

    void draw(){
        entityImg.draw(location.x, location.y);
    }

    Point getLocation(){
        return location;
    }

    double getHeight(){
        return entityImg.getHeight();
    }

    double getWidth(){
        return  entityImg.getWidth();
    }

    /**
     * Reset attributes when game restarts
     * @param gameProps
     */
    void resetAttributes(Properties gameProps){
        this.location = INIT_LOCATION;
    }
}

package GameEntity;

import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * The abstract class that all game entities should inherit,
 * containing methods to get the location, img's attributes,
 * and reset attributes method (to replay games)
 */
public abstract class GameEntity {
    protected List<Image> entityImages;
    protected int currentImageIndex;
    protected Point location;
    protected boolean isDeleted;

    public GameEntity(List<Image> entityImages, int currentImageIndex, Point location){
        this.entityImages = entityImages;
        this.currentImageIndex = currentImageIndex;

        this.location = location;
        this.isDeleted = false;
    }

    public void draw(){
        entityImages.get(currentImageIndex).draw(location.x, location.y);
    }

    public Point getLocation(){
        return location;
    }

    public double getHeight(){
        return entityImages.get(currentImageIndex).getHeight();
    }

    public double getWidth(){
        return entityImages.get(currentImageIndex).getWidth();
    }

}

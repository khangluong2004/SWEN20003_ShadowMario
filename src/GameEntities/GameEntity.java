package GameEntities;

import Scenes.PlayingScenes.PlayingScene;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;

import java.util.List;

/**
 * The abstract class that all game entities should inherit,
 * containing methods to get the location, img's attributes,
 * draw the object
 *
 */
public abstract class GameEntity {
    protected List<Image> entityImages;
    protected int currentImageIndex;
    protected Point location;
    protected boolean isDeleted;
    protected PlayingScene currentScene;

    /**
     * Constructor (helper) to set up the required attributes for a game entity
     * @param entityImages entityImages
     * @param currentImageIndex currentImageIndex
     * @param location location
     * @param scene the scene it's in
     */
    public GameEntity(List<Image> entityImages, int currentImageIndex, Point location, PlayingScene scene){
        this.entityImages = entityImages;
        this.currentImageIndex = currentImageIndex;

        this.location = location;
        this.isDeleted = false;
        this.currentScene = scene;
    }

    /**
     * Draw the entity's image at its location
     */
    public void draw(){
        if (isDeleted){
            return;
        }
        entityImages.get(currentImageIndex).draw(location.x, location.y);
    }

    /**
     * Used if the entity needs to update any attributes per frame
     * Is called first before any actions
     * @param input user input delegated down from Bagel's update method
     */
    public abstract void updatePerFrame(Input input);

    /**
     * Get location
     * @return location
     */
    public Point getLocation(){
        return location;
    }

    /**
     * Get image height
     * @return entity current image's height
     */
    public double getHeight(){
        return entityImages.get(currentImageIndex).getHeight();
    }

    /**
     * Get image width
     * @return entity current image's width
     */
    public double getWidth(){
        return entityImages.get(currentImageIndex).getWidth();
    }

    /**
     * Check if the entity is deleted
     * @return isDeleted flag
     */
    public boolean isDeleted(){
        return isDeleted;
    }

}

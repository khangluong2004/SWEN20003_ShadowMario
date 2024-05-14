package GameEntities.PickUpItems;

import GameProperties.GameProps;
import Scenes.PlayingScenes.PlayingScene;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Class for the double score power pickup item (cherry)
 */
public class DoubleScorePower extends PickupItem{
    private final int DOUBLE_SCORE_LASTING_FRAMES;

    /**
     * Make a double score power pickup item, initializing constants (lasting frames, radius, popping speed, ...)
     * and attributes (images)
     * @param location location
     * @param scene the scene it's in
     */
    public DoubleScorePower(Point location, PlayingScene scene){
        super(new ArrayList<Image>(), 0, location, scene);
        Properties gameProps = GameProps.getGameProps();

        this.DOUBLE_SCORE_LASTING_FRAMES = Integer.parseInt(gameProps.getProperty("gameObjects.doubleScore.maxFrames"));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.doubleScore.radius"));
        this.POPPING_SPEED = -10;
        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.doubleScore.speed"));

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.doubleScore.image")));
    }

    /**
     * Get the frame number that the double score will last
     * @return lasting frame
     */
    public int getDoubleScoreLastingFrames(){
        return this.DOUBLE_SCORE_LASTING_FRAMES;
    }
}

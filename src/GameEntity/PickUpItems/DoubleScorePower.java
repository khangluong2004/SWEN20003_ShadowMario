package GameEntity.PickUpItems;

import GameProperties.GameProps;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DoubleScorePower extends PickupItem{
    private final int DOUBLE_SCORE_LASTING_FRAMES;
    public DoubleScorePower(Point location){
        super(new ArrayList<Image>(), 0, location);
        Properties gameProps = GameProps.getGameProps();

        this.DOUBLE_SCORE_LASTING_FRAMES = Integer.parseInt(gameProps.getProperty("gameObjects.doubleScore.maxFrames"));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.doubleScore.radius"));
        this.POPPING_SPEED = Integer.parseInt(gameProps.getProperty("gameObjects.doubleScore.speed"));

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.doubleScore.image")));
    }

    public int getDoubleScoreLastingFrames(){
        return this.DOUBLE_SCORE_LASTING_FRAMES;
    }
}

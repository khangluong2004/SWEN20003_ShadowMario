package GameEntities.PickUpItems;

import GameProperties.GameProps;
import Scenes.PlayingScenes.PlayingScene;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Class for the invincible power pickup item
 */
public class InvinciblePower extends PickupItem{
    private final int INVINCIBLE_LASTING_FRAMES;

    /**
     * Make an invincible power pickup item
     * @param location location
     * @param scene the scene it's in
     */
    public InvinciblePower(Point location, PlayingScene scene){
        super(new ArrayList<Image>(), 0, location, scene);
        Properties gameProps = GameProps.getGameProps();

        this.INVINCIBLE_LASTING_FRAMES = Integer.parseInt(gameProps.getProperty("gameObjects.invinciblePower.maxFrames"));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.invinciblePower.radius"));
        this.POPPING_SPEED = -10;
        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.invinciblePower.speed"));

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.invinciblePower.image")));
    }

    /**
     * Get the lasting frame for invincible ability
     * @return
     */
    public int getInvincibleLastingFrames(){
        return this.INVINCIBLE_LASTING_FRAMES;
    }
}

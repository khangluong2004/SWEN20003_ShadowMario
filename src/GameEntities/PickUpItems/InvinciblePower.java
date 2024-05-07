package GameEntities.PickUpItems;

import GameProperties.GameProps;
import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Properties;

public class InvinciblePower extends PickupItem{
    private final int INVINCIBLE_LASTING_FRAMES;
    public InvinciblePower(Point location){
        super(new ArrayList<Image>(), 0, location);
        Properties gameProps = GameProps.getGameProps();

        this.INVINCIBLE_LASTING_FRAMES = Integer.parseInt(gameProps.getProperty("gameObjects.invinciblePower.maxFrames"));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.invinciblePower.radius"));
        this.POPPING_SPEED = -10;
        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.invinciblePower.speed"));

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.invinciblePower.image")));
    }

    public int getInvincibleLastingFrames(){
        return this.INVINCIBLE_LASTING_FRAMES;
    }
}

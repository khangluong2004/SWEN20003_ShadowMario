package GameEntities.Characters.PowerUps;

import enums.PowerUpItem;

public class TimedPowerUp extends PowerUp{
    private int LASTING_FRAMES;
    private int frameSinceStart;

    public TimedPowerUp(PowerUpItem powerUpItem, int lastingFrames){
        super(powerUpItem);
        this.LASTING_FRAMES = lastingFrames;
        this.frameSinceStart = 0;
    }

    public void updatePerFramePowerUp(){
        if (frameSinceStart < LASTING_FRAMES){
            frameSinceStart++;
        }
    }

    public boolean isExpired(){
        return frameSinceStart >= LASTING_FRAMES;
    }
}

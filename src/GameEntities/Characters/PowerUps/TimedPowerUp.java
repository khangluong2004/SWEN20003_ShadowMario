package GameEntities.Characters.PowerUps;

/**
 * A specific implementation of the PowerUp class: TimedPowerUp,
 * which expires after a set amount of frames
 */
public class TimedPowerUp extends PowerUp{
    private int LASTING_FRAMES;
    private int frameSinceStart;

    public TimedPowerUp(PowerUpItem powerUpItem, int lastingFrames){
        super(powerUpItem);
        this.LASTING_FRAMES = lastingFrames;
        this.frameSinceStart = 0;
    }

    /**
     * Increment the frame counting until reaching the last frames
     */
    public void updatePerFramePowerUp(){
        if (frameSinceStart < LASTING_FRAMES){
            frameSinceStart++;
        }
    }

    /**
     * Check if expired, by comparing the frame counts with amount of
     * frames the powerup lasts
     * @return
     */
    public boolean isExpired(){
        return frameSinceStart >= LASTING_FRAMES;
    }
}

package GameEntities.Characters.PowerUps;

/**
 * Abstract class for the PowerUp attributes stored inside the player
 * (Note: This is not the same as the pickup items, which is a separate entity)
 */
public abstract class PowerUp {
    // Store the enum to identify the type of power up
    protected PowerUpItem powerUpItem;

    public PowerUp(PowerUpItem powerUpItem){
        this.powerUpItem = powerUpItem;
    }

    /**
     * Update the powerup (if any) per frame depends on the type of power up
     */
    public abstract void updatePerFramePowerUp();

    /**
     * Check if the powerup expired
     * @return
     */
    public abstract boolean isExpired();
    public PowerUpItem getPowerUpItem(){
        return powerUpItem;
    }
}

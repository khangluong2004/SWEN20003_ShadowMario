package GameEntities.Characters.PowerUps;

import enums.PowerUpItem;

public abstract class PowerUp {
    protected PowerUpItem powerUpItem;

    public PowerUp(PowerUpItem powerUpItem){
        this.powerUpItem = powerUpItem;
    }

    public abstract void updatePerFramePowerUp();
    public abstract boolean isExpired();
    public PowerUpItem getPowerUpItem(){
        return powerUpItem;
    }
}

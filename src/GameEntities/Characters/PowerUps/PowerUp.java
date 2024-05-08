package GameEntities.Characters.PowerUps;

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

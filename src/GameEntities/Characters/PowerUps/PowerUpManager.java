package GameEntities.Characters.PowerUps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class PowerUpManager {
    private Set<PowerUp> allPowerUps = new HashSet<PowerUp>();
    public void updatePerFrameAllPowerUp(){
        // Call update on all power ups and clean the expired one
        List<PowerUp> expiredPowerUps = new ArrayList<>();

        for (var powerUp: allPowerUps){
            powerUp.updatePerFramePowerUp();
            if (powerUp.isExpired()){
                expiredPowerUps.add(powerUp);
            }
        }

        for (var expiredPowerUp: expiredPowerUps){
            allPowerUps.remove(expiredPowerUp);
        }
    }

    public Set<PowerUpItem> getPowerUpItems(){
        Set<PowerUpItem> allPowerUpsItems = new HashSet<PowerUpItem>();
        for (var powerUp: allPowerUps){
            if (!powerUp.isExpired()){
                allPowerUpsItems.add(powerUp.getPowerUpItem());
            }
        }

        return allPowerUpsItems;
    }

    public void addPowerUp(PowerUp powerUp){
        allPowerUps.add(powerUp);
    }
}

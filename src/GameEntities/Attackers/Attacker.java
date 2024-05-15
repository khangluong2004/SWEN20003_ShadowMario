package GameEntities.Attackers;

import GameEntities.GameEntity;

/**
 * Interface for Attacker entity (entity that can cause damage to others)
 */
public interface Attacker {
    /**
     * Get the damage for the attacker
     */
    double getDamage(GameEntity entity);
}

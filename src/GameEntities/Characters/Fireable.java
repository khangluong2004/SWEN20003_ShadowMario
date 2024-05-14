package GameEntities.Characters;

import GameEntities.GameEntity;

/**
 * Interface for characters that can fire fireballs
 */
public interface Fireable {
    /**
     * Fire a fire-ball towards the target (by adding a fireball to the scene)
     * @param target
     */
    void fire(GameEntity target);
}

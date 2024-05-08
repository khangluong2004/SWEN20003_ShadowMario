package GameEntities.Characters;

import GameEntities.GameEntity;

/**
 * Interface for characters that can fire fireballs
 */
public interface Fireable {
    void fire(GameEntity target);
}

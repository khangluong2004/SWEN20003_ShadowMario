package GameEntities.Characters;

import GameEntities.GameEntity;

public interface Fireable {
    void fire();
    void target(GameEntity target);
}

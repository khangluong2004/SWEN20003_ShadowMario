package GameEntity.Characters;

import GameEntity.GameEntity;

public interface Fireable {
    void fire();
    void target(GameEntity target);
}

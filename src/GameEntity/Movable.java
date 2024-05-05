package GameEntity;

import enums.MoveDirection;

/**
 * Interface for all movable objects
 */
public interface Movable {
    void move(MoveDirection direction);
}

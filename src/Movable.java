import enums.MoveDirection;

/**
 * Interface for all movable objects
 */
public interface Movable {
    void updateLocation(MoveDirection direction);
}

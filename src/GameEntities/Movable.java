package GameEntities;

import bagel.Input;
import bagel.Keys;
import enums.MoveDirection;

/**
 * Interface for all movable objects
 */
public interface Movable {
    void move(MoveDirection direction);

    default void updateMove(Input input){
        // Move based on the input
        if (input.isDown(Keys.UP)){
            this.move(MoveDirection.UP);
        }

        if (input.isDown(Keys.DOWN)){
            this.move(MoveDirection.DOWN);
        }

        if (input.isDown(Keys.LEFT)){
            this.move(MoveDirection.LEFT)
        }

        if (input.isDown(Keys.RIGHT)){
            this.move(MoveDirection.RIGHT);
        }

        this.move(MoveDirection.CONTINUE);
    }
}

package GameEntities;

import bagel.Input;
import bagel.Keys;

/**
 * Interface for all movable objects
 */
public interface Movable {
    /**
     * Move method, used to move the object based on the required direction
     * @param direction direction to move
     */
    void move(MoveDirection direction);

    /**
     * Method used to update the movement based on the input received.
     * The default method provided is the most common
     * @param input user input
     */
    default void updateMove(Input input){
        // Move based on the input
        if (input.wasPressed(Keys.UP)){
            this.move(MoveDirection.UP);
        }

        if (input.isDown(Keys.DOWN)){
            this.move(MoveDirection.DOWN);
        }

        if (input.isDown(Keys.LEFT)){
            this.move(MoveDirection.LEFT);
        }

        if (input.isDown(Keys.RIGHT)){
            this.move(MoveDirection.RIGHT);
        }

        this.move(MoveDirection.CONTINUE);
    }
}

import bagel.Image;
import bagel.util.Point;
import enums.MoveDirection;

import java.util.Properties;

/**
 * Class for the Coin entities, inheriting from the base
 * class EuclideanCollidableMovableEntity (since the coin
 * can move and detect collision by Euclidean distance)
 */
public class Coin extends EuclideanCollidableMovableEntity {
    private final static int POPPING_SPEED = -10;
    private final static int INITAL_VERTICAL_SPEED = 0;
    private final int COIN_VALUE;
    private int verticalVelocity;
    private boolean isPlayerCollided; // Check if collided with the player

    /**
     * Constructor to initialize location, velocity, coin's value, image, collision range
     * @param location Location to place the coin
     * @param game_props Props read from app.properties
     */
    public Coin(Point location, Properties game_props){
        super(location,
                Double.parseDouble(game_props.getProperty("gameObjects.coin.radius")),
                Integer.parseInt(game_props.getProperty("gameObjects.coin.speed")),
                new Image(game_props.getProperty("gameObjects.coin.image"))
        );
        COIN_VALUE = Integer.parseInt(game_props.getProperty("gameObjects.coin.value"));
        this.resetAttributes(game_props);
    }

    /**
     * Method to re-initialize the coin's attributes (except Image and value)
     * when the game is re-played
     * @param game_props
     */
    @Override
    public void resetAttributes(Properties game_props){
        super.resetAttributes(game_props);
        this.verticalVelocity = INITAL_VERTICAL_SPEED;
        this.isPlayerCollided = false;
    }

    public boolean isPlayerCollided(){
        return isPlayerCollided;
    }

    public int getCoinValue(){
        return COIN_VALUE;
    }


    @Override
    public void collideWith(Collidable entity) {}

    /**
     * Override the behaviour after collision with player.
     * For coin, increase the score and change its vertical velocity
     * to make the coin popped out of the screen
     * @param entity The entity this coin collided with
     */
    @Override
    public void finishColliding(Collidable entity) {
        if (entity instanceof Player){
            // Do nothing if collided with the player before
            if (isPlayerCollided){
                return;
            }

            // Set the vertical velocity for the popping coin
            verticalVelocity = POPPING_SPEED;

            // Set collided
            isPlayerCollided = true;
        }
    }


    /**
     * Update the location of the coin after the user pressed a key
     * or to continue the popping out movement
     * @param direction The direction of user's key pressed or signal to continue movement
     */
    @Override
    public void updateLocation(MoveDirection direction){
        super.updateLocation(direction);
        if (direction == MoveDirection.CONTINUE){
            Point location = this.getLocation();
            this.location = new Point(location.x, location.y + verticalVelocity);
        }
    }
}
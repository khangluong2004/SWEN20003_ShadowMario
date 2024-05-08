package GameEntities.PickUpItems;

import GameEntities.Characters.Player.Player;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.GameEntity;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.Movable;

import Scenes.PlayingScenes.PlayingScene;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import GameEntities.MoveDirection;

import java.util.List;

public abstract class PickupItem extends GameEntity implements Movable, RadiusCollidable {
    protected int POPPING_SPEED;
    protected double STEP_SIZE;
    protected double RADIUS;
    protected int verticalVelocity;
    protected boolean isPlayerCollided;

    public PickupItem(List<Image> entityImages, int currentImageIndex, Point location, PlayingScene scene) {
        super(entityImages, currentImageIndex, location, scene);
        this.verticalVelocity = 0;
        this.isPlayerCollided = false;
    }

    public boolean isPlayerCollided() {
        return isPlayerCollided;
    }

    @Override
    public void updatePerFrame(Input input){
        updateMove(input);
    }


    /**
     * Override the behaviour after collision with player.
     * For coin, increase the score and change its vertical velocity
     * to make the coin popped out of the screen
     * @param entity The entity this coin collided with
     */
    @Override
    public void endCollideWith(Collidable entity) {
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
     * Get the collision radius of the object
     * @return the radius of the object
     */
    @Override
    public double getCollisionRadius(Collidable entity) {
        return RADIUS;
    }


    /**
     * Update the location of the coin after the user pressed a key
     * or to continue the popping out movement
     * @param direction The direction of user's key pressed or signal to continue movement
     */
    @Override
    public void move(MoveDirection direction){
        if (direction == MoveDirection.LEFT){
            this.location = new Point(location.x + STEP_SIZE, location.y);
        } else if (direction == MoveDirection.RIGHT){
            this.location = new Point(location.x - STEP_SIZE, location.y);
        }

        if (direction == MoveDirection.CONTINUE){
            Point location = this.getLocation();
            this.location = new Point(location.x, location.y + verticalVelocity);
        }
    }
}

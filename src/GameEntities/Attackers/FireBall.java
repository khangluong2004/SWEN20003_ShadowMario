package GameEntities.Attackers;

import GameEntities.Characters.Fireable;
import GameEntities.Characters.Killable;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.GameEntity;
import GameEntities.Movable;
import GameProperties.GameProps;
import Scenes.PlayingScenes.PlayingScene;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import GameEntities.MoveDirection;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Class for FireBall
 */
public class FireBall extends GameEntity implements Movable, Attacker, RadiusCollidable {
    private final double DAMAGE_SIZE;
    private final int FIRE_STEP_SIZE;
    private final int STEP_SIZE;
    private final double RADIUS;
    private final int LOWER_BOUNDARY;
    private final int UPPER_BOUNDARY;
    private final Fireable firer;
    private final int velocity;

    /**
     * Make FireBall, initialize constants and attributes (direction, firer)
     * @param location
     * @param moveLeft
     * @param firer
     * @param scene
     */
    public FireBall(Point location, boolean moveLeft, Fireable firer, PlayingScene scene){
        super(new ArrayList<Image>(), 0, location, scene);
        Properties gameProps = GameProps.getGameProps();

        this.firer = firer;

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.fireball.image")));
        this.DAMAGE_SIZE = -1 * Double.parseDouble(gameProps.getProperty("gameObjects.fireball.damageSize"));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.fireball.radius"));
        this.FIRE_STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.fireball.speed"));
        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.coin.speed"));
        this.LOWER_BOUNDARY = 0;
        this.UPPER_BOUNDARY = Integer.parseInt(gameProps.getProperty("windowWidth"));

        if (moveLeft){
            this.velocity = -1 * FIRE_STEP_SIZE;
        } else {
            this.velocity = FIRE_STEP_SIZE;
        }
    }

    /**
     * Update the entities per frame
     * @param input
     */
    @Override
    public void updatePerFrame(Input input){
        updateMove(input);
    }

    /**
     * Get the damage for the attacker
     * @param entity
     * @return
     */
    @Override
    public double getDamage(GameEntity entity) {
        return DAMAGE_SIZE;
    }

    /**
     * Test if the entity is a firer (so don't deal damage)
     * @param entity
     * @return
     */
    public boolean isFirer(Collidable entity){
        // Use equality operators, since we actually want to check
        // if the 2 instances are the exact same instances
        return entity == firer;
    }

    /**
     * Delete the fireball after collision with any Killable except
     * the firer
     * @param entity the entity that is collided with
     */
    @Override
    public void endCollideWith(Collidable entity) {
        if (!this.isFirer(entity) && entity instanceof Killable){
            isDeleted = true;
        }
    }


    /**
     * Move relative to the player, and continue its horizontal movement
     * @param direction
     */
    @Override
    public void move(MoveDirection direction) {
        if (isDeleted){
            return;
        }

        if (checkReachBoundary()){
            isDeleted = true;
            return;
        }

        if (direction == MoveDirection.LEFT){
            this.location = new Point(location.x + STEP_SIZE, location.y);
        } else if (direction == MoveDirection.RIGHT){
            this.location = new Point(location.x - STEP_SIZE, location.y);
        }

        if (direction == MoveDirection.CONTINUE){
            this.location = new Point(location.x + velocity, location.y);
        }
    }

    /**
     * Check if the fireball is out of screen and should be deleted
     * Take into account the direction of the fireball (eg: If it's fired
     * from the left, then only delete when it passes the right boundary)
     * @return
     */
    private boolean checkReachBoundary(){
        return ((velocity < 0 && this.location.x <= LOWER_BOUNDARY)
                || (velocity > 0 && this.location.x >= UPPER_BOUNDARY));
    }

    /**
     * Get Collision radius
     * @param entity
     * @return
     */
    @Override
    public double getCollisionRadius(Collidable entity) {
        return RADIUS;
    }
}

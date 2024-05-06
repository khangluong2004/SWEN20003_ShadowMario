package GameEntities.Attackers;

import GameEntities.Characters.Fireable;
import GameEntities.Characters.Killable;
import GameEntities.Characters.Player;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.GameEntity;
import GameEntities.Movable;
import GameProperties.GameProps;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import enums.MoveDirection;

import java.util.ArrayList;
import java.util.Properties;

public class FireBall extends GameEntity implements Movable, Attacker, RadiusCollidable {
    private final double DAMAGE_SIZE;
    private final int FIRE_STEP_SIZE;
    private final int STEP_SIZE;
    private final double RADIUS;
    private final int LOWER_BOUNDARY;
    private final int UPPER_BOUNDARY;
    private Fireable firer;
    private int velocity;

    public FireBall(Point location, boolean moveLeft, Fireable firer){
        super(new ArrayList<Image>(), 0, location);
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

    @Override
    public void updatePerFrame(Input input){
        updateMove(input);
    }

    @Override
    public double getDamage(GameEntity entity) {
        return DAMAGE_SIZE;
    }

    public boolean isFirer(Collidable entity){
        // Use equality operators, since we actually want to check
        // if the 2 instances are the exact same instances
        return entity == firer;
    }

    @Override
    public void startCollideWith(Collidable entity) {

    }

    @Override
    public void endCollideWith(Collidable entity) {
        // Delete the fireball after collision with any Killable except
        // the firer
        if (!this.isFirer(entity) && entity instanceof Killable){
            isDeleted = true;
        }
    }

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

    private boolean checkReachBoundary(){
        return (this.location.x <= LOWER_BOUNDARY || this.location.x >= UPPER_BOUNDARY);
    }

    @Override
    public double getCollisionRadius(Collidable entity) {
        return RADIUS;
    }
}

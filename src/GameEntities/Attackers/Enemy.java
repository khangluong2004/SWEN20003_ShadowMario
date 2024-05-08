package GameEntities.Attackers;


import GameEntities.Characters.Player.Player;
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
import java.util.Random;

/**
 * Class for enemy entity, with the added random movement
 *
 */
public class Enemy extends GameEntity implements RadiusCollidable, Movable, Attacker {
    private final double RADIUS;
    private final int STEP_SIZE;
    private final int RANDOM_STEP_SIZE;
    private final int MAX_DISPLACEMENT;
    private final double DAMAGE_SIZE;
    private boolean inflictedDamage;
    // Negative velocity is to the left, positive is to the right
    private int velocity;
    private int randomDisplacement;

    public Enemy(Point location, PlayingScene scene){
        super(new ArrayList<Image>(), 0, location, scene);
        Properties gameProps = GameProps.getGameProps();

        this.DAMAGE_SIZE = -1 * Double.parseDouble(gameProps.getProperty("gameObjects.enemy.damageSize"));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.enemy.radius"));
        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.enemy.speed"));
        this.RANDOM_STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.enemy.randomSpeed"));
        this.MAX_DISPLACEMENT = Integer.parseInt(gameProps.getProperty("gameObjects.enemy.maxRandomDisplacementX"));

        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.enemy.image")));
        this.inflictedDamage = false;
        this.randomDisplacement = 0;
        this.updateDirection();
    }

    private void updateDirection(){
        Random random = new Random();
        boolean toLeft = random.nextBoolean();
        if (toLeft){
            // Move left
            velocity = -1 * RANDOM_STEP_SIZE;
        } else {
            velocity = RANDOM_STEP_SIZE;
        }
    }

    @Override
    public void updatePerFrame(Input input){
        updateMove(input);
    }

    /**
     * Common updateLocation for LEFT/RIGHT movements for all movable objects,
     * plus the random displacement around the starting point
     * @param direction the direction from the key pressed by the user
     */
    @Override
    public void move(MoveDirection direction) {
        if (direction == MoveDirection.LEFT){
            this.location = new Point(location.x + STEP_SIZE, location.y);
        } else if (direction == MoveDirection.RIGHT){
            this.location = new Point(location.x - STEP_SIZE, location.y);
        } else if (direction == MoveDirection.CONTINUE){
            // Keep track of the total displacement from the start,
            // and flip direction after reaching max displacement
            this.randomDisplacement += velocity;
            if (Math.abs(randomDisplacement) >= MAX_DISPLACEMENT){
                this.velocity = -1 * this.velocity;
            }

            this.location = new Point(location.x + velocity, location.y);
        }
    }

    /**
     * Get the damage size
     * @param entity
     * @return
     */
    @Override
    public double getDamage(GameEntity entity){
        return DAMAGE_SIZE;
    }

    /**
     * Get if inflictedDamage to the player or not
     * @return
     */
    public boolean isInflictedDamage(){
        return inflictedDamage;
    }

    /**
     * Handling side-effect after collision with other entity (player)
     * @param entity the entity that is collided with
     */
    @Override
    public void endCollideWith(Collidable entity) {
        if (!this.inflictedDamage && entity instanceof Player){
            // Check if the player is damageable. If not, then don't set the inflictedDamage flag
            Player player = (Player) entity;
            if (!player.isDamageable()){
                return;
            }

            // Set flag so not to inflict damage twice
            this.inflictedDamage = true;
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
}

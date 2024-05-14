package GameEntities.Characters;

import GameEntities.Attackers.FireBall;
import GameEntities.Characters.Player.Player;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.GameEntity;
import GameEntities.Movable;
import GameProperties.GameProps;
import Scenes.PlayingScenes.PlayingScene;
import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Point;
import GameEntities.MoveDirection;
import Messages.StatusMessages.StatusObserver;

import java.util.*;

/**
 * Class for the EnemyBoss, which can move, fire, be killed, collided and contains status to be displayed
 */
public class EnemyBoss extends GameEntity implements Fireable, Movable, Killable, RadiusCollidable, StatusContainer {
    private final int STEP_SIZE;
    private final int DIE_SPEED;
    private final int FRAME_UNTIL_NEXT_FIRE;
    private final double RADIUS;
    private final Random random;
    private double health;
    private int verticalVelocity;
    private int frameSinceLastFire;

    // Status observers for the status message
    private Set<StatusObserver> observers;

    /**
     * Make an enemy boss, initialize constants (images, RADIUS, speed, firing frame, ...) and initial attributes
     * (velocity, health, ...)
     * @param location location
     * @param scene the scene it's in
     */
    public EnemyBoss(Point location, PlayingScene scene) {
        super(new ArrayList<Image>(), 0, location, scene);
        Properties gameProps = GameProps.getGameProps();
        this.entityImages.add(new Image(gameProps.getProperty("gameObjects.enemyBoss.image")));

        this.STEP_SIZE = Integer.parseInt(gameProps.getProperty("gameObjects.enemyBoss.speed"));
        this.RADIUS = Double.parseDouble(gameProps.getProperty("gameObjects.enemyBoss.radius"));
        this.DIE_SPEED = 2;
        this.FRAME_UNTIL_NEXT_FIRE = 100;
        this.random = new Random();

        this.observers = new HashSet<>();

        this.health = Double.parseDouble(gameProps.getProperty("gameObjects.enemyBoss.health"));
        this.verticalVelocity = 0;
        this.frameSinceLastFire = 0;
    }

    /**
     * Update movement, delete if outside of window and increment the frame count
     * for the next firing
     * @param input
     */
    @Override
    public void updatePerFrame(Input input) {
        // Check if outside the screen, then delete it
        if (this.location.y > Window.getHeight()){
            this.isDeleted = true;
            return;
        }

        updateMove(input);
        this.frameSinceLastFire = (this.frameSinceLastFire + 1) % FRAME_UNTIL_NEXT_FIRE;
        this.notifyObservers();
    }

    /**
     * Fire (Create) a fireball moving toward the target
     * @param target
     */
    @Override
    public void fire(GameEntity target) {
        // Can't fire if is dead
        if (this.health <= 0){
            return;
        }

        boolean moveLeft = true;
        Point targetLocation = target.getLocation();
        Point firerLocation = this.location;

        if (targetLocation.x > firerLocation.x){
            moveLeft = false;
        }

        this.currentScene.addGameEntity(new FireBall(this.location, moveLeft, this, this.currentScene));
    }

    /**
     * Get the health of the boss
     * @return
     */
    @Override
    public double getHealth() {
        return this.health;
    }

    /**
     * Set the health and change to die speed if dead (health <= 0)
     * @param newHealth
     */
    private void setHealth(double newHealth){
        this.health = Math.max(newHealth, 0);
        if (this.health <= 0){
            verticalVelocity = DIE_SPEED;
        }
    }

    /**
     * Check if damageable
     * @return
     */
    @Override
    public boolean isDamageable() {
        return false;
    }

    /**
     * Check the type, and delegate collision handling to helper methods
     * @param entity the entity that is collided with
     */
    @Override
    public void startCollideWith(Collidable entity) {
        if (entity instanceof Player){
            handleCollision((Player) entity);
        } else if (entity instanceof FireBall){
            handleCollision((FireBall) entity);
        }
    }

    /**
     * Handle collision with player: Start firing randomly every 100 frames
     * @param player
     */
    private void handleCollision(Player player){
        if (frameSinceLastFire == 0){
            boolean firable = random.nextBoolean();
            if (firable){
                this.fire(player);
            }
        }
    }

    /**
     * Handle collision with fireball: If not the firer, then takes damage
     * @param fireBall
     */
    private void handleCollision(FireBall fireBall){
        if (fireBall.isFirer(this)){
            return;
        }

        this.setHealth(this.health + fireBall.getDamage(this));
    }

    /**
     * Move logic: relative to player and update velocity after death
     * @param direction
     */
    @Override
    public void move(MoveDirection direction) {
        if (direction == MoveDirection.LEFT){
            this.location = new Point(location.x + STEP_SIZE, location.y);
        } else if (direction == MoveDirection.RIGHT){
            this.location = new Point(location.x - STEP_SIZE, location.y);
        } else if (direction == MoveDirection.CONTINUE){
            location = new Point(location.x, location.y + verticalVelocity);
        }
    }


    /**
     * Add status observer (eg: health status message)
     * @param observer
     */
    @Override
    public void addStatusObserver(StatusObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Remove status observer
     * @param observer
     */
    @Override
    public void removeObserver(StatusObserver observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all registered observers
     */
    @Override
    public void notifyObservers() {
        for (StatusObserver observer: observers){
            observer.notify(this);
        }
    }

    /**
     * Get the collision radius of the entity
     * @param entity
     * @return
     */
    @Override
    public double getCollisionRadius(Collidable entity) {
        return this.RADIUS;
    }
}

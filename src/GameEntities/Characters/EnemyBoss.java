package GameEntities.Characters;

import GameEntities.Attackers.FireBall;
import GameEntities.Characters.Player.Player;
import GameEntities.CollisionInterface.Collidable;
import GameEntities.CollisionInterface.RadiusCollidable;
import GameEntities.GameEntity;
import GameEntities.Movable;
import GameEntities.StatusContainer;
import GameProperties.GameProps;
import Scenes.PlayingScenes.PlayingScene;
import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Point;
import GameEntities.MoveDirection;
import utils.StatusMessages.StatusObserver;

import java.util.*;

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

    @Override
    public void updatePerFrame(Input input) {
        // Check if outside the bottom screen, then deleted
        if (this.location.y > Window.getHeight()){
            this.isDeleted = true;
            return;
        }

        updateMove(input);
        this.frameSinceLastFire = (this.frameSinceLastFire + 1) % FRAME_UNTIL_NEXT_FIRE;
        this.notifyObservers();
    }

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


    @Override
    public double getHealth() {
        return this.health;
    }

    private void setHealth(double newHealth){
        this.health = Math.max(newHealth, 0);
        if (this.health <= 0){
            verticalVelocity = DIE_SPEED;
        }
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public void startCollideWith(Collidable entity) {
        if (entity instanceof Player){
            handleCollision((Player) entity);
        } else if (entity instanceof FireBall){
            handleCollision((FireBall) entity);
        }
    }

    private void handleCollision(Player player){
        if (frameSinceLastFire == 0){
            boolean firable = random.nextBoolean();
            if (firable){
                this.fire(player);
            }
        }
    }

    private void handleCollision(FireBall fireBall){
        if (fireBall.isFirer(this)){
            return;
        }

        this.setHealth(this.health + fireBall.getDamage(this));
    }

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


    @Override
    public void addStatusObserver(StatusObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(StatusObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (StatusObserver observer: observers){
            observer.notify(this);
        }
    }

    @Override
    public double getCollisionRadius(Collidable entity) {
        return this.RADIUS;
    }
}

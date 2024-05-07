package GameEntities.Characters;

import GameEntities.CollisionInterface.Collidable;
import GameEntities.GameEntity;
import GameEntities.Movable;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import enums.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class EnemyBoss extends GameEntity implements Fireable, Movable, Killable, Collidable {
    public EnemyBoss(Point location) {
        super(new ArrayList<Image>(), 0, location);
    }

    @Override
    public void fire() {

    }

    @Override
    public void target(GameEntity target) {

    }

    @Override
    public boolean isDead() {
        return false;
    }

    @Override
    public double getHealth() {
        return 0;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public void startCollideWith(Collidable entity) {

    }

    @Override
    public void endCollideWith(Collidable entity) {

    }

    @Override
    public void move(MoveDirection direction) {

    }

    // TODO: Implement EnemyBoss
}

package CollisionHandling;

import GameEntities.CollisionInterface.Collidable;

public interface CollisionDetector {
    boolean checkCollision(Collidable entity1, Collidable entity2);
}

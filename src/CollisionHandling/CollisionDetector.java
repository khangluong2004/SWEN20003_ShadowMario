package CollisionHandling;

import GameEntities.CollisionInterface.Collidable;

public interface CollisionDetector {
    CollisionType checkCollision(Collidable entity1, Collidable entity2);
}

package utils.StatusMessages;

import GameEntities.GameEntity;

/***
 * Use Observer pattern for health and score message
 */
public interface StatusMessage {
    void notify(GameEntity entity);
}

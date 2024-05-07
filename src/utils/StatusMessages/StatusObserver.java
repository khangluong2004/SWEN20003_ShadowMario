package utils.StatusMessages;

import GameEntities.StatusContainer;

/***
 * Use Observer pattern for health and score message
 */
public interface StatusObserver {
    void notify(StatusContainer entity);
}

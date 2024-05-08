package Messages.StatusMessages;

import GameEntities.Characters.StatusContainer;

/***
 * Use Observer pattern for health and score message
 */
public interface StatusObserver {
    void notify(StatusContainer entity);
}

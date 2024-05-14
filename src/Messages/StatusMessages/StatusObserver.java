package Messages.StatusMessages;

import GameEntities.Characters.StatusContainer;

/***
 * Abstract class for all status observers (eg: health and score message)
 * Use Observer pattern with notify callback
 */
public interface StatusObserver {
    /**
     * Notify call back to receive updates from the objects with desirable status
     * @param entity observed object
     */
    void notify(StatusContainer entity);
}

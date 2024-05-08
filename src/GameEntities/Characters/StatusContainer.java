package GameEntities.Characters;


import Messages.StatusMessages.StatusObserver;

/**
 * Interface for characters containing status that needs to be monitored
 * for display. Use observer pattern, with the observer being the
 * status messages (health and score)
 */
public interface StatusContainer {
    /**
     * Add observer
     * @param observer
     */
    void addStatusObserver(StatusObserver observer);

    /**
     * Remove observer
     * @param observer
     */
    void removeObserver(StatusObserver observer);

    /**
     * Notify all observers
     */
    void notifyObservers();
}

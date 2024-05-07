package GameEntities;


import utils.StatusMessages.StatusObserver;

public interface StatusContainer {
    void addStatusObserver(StatusObserver observer);
    void removeObserver(StatusObserver observer);
    void notifyObservers();
}

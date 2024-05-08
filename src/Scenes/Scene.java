package Scenes;

import bagel.Input;

/**
 * Interface for all Scene, which handles the storage of entities in the scene, displaying the entities
 * and delegating updating and collision handling to appropriate classes
 */
public interface Scene {
    void drawScene();
    void updateScene(Input input);
    boolean isEnd();
}

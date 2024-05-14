package Scenes;

import bagel.Input;

/**
 * Interface for all Scene, which handles the storage of entities in the scene, displaying the entities
 * and delegating updating and collision handling to appropriate classes
 */
public interface Scene {
    /**
     * Draw the entities and write the messages
     */
    void drawScene();

    /**
     * Update the scene based on user input (delegate input to its entities mostly)
     * @param input user input
     */
    void updateScene(Input input);

    /**
     * Check if the scene ends
     * @return if the scene ends
     */
    boolean isEnd();
}

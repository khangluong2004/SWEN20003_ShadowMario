package Scenes;

import bagel.Input;

public interface Scene {
    void loadScene(int currentLevel);
    void drawScene();
    void updateScene(Input input);
    boolean isEnd();
}

package Scenes;

import bagel.Input;

public interface Scene {
    void drawScene();
    void updateScene(Input input);
    boolean isEnd();
}

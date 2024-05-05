import GameProperties.GameProps;
import GameProperties.MessageProps;
import Scenes.Scene;
import Scenes.SceneIterator;
import bagel.*;

import java.util.Properties;

/*
 * Implementation based on skeleton Code for SWEN20003 Project 1, Semester 1, 2024
 *
 * Please enter your name below
 * @author LUONG AN KHANG
 */

/**
 * Main class for Shadow Mario game controls
 * @author LUONG AN KHANG
 */
public class ShadowMario extends AbstractGame {
    private final Image BACKGROUND_IMAGE;
    private final SceneIterator sceneIterator;

    /**
     * The constructor
     */
    public ShadowMario(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("windowWidth")),
              Integer.parseInt(gameProps.getProperty("windowHeight")),
              messageProps.getProperty("title"));

        BACKGROUND_IMAGE = new Image(gameProps.getProperty("backgroundImage"));
        sceneIterator = new SceneIterator();
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Properties game_props = GameProps.getGameProps();
        Properties message_props = MessageProps.getMessageProps();

        ShadowMario game = new ShadowMario(game_props, message_props);

        game.run();
    }


    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        // close window
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        // Games stage setting
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        Scene currentScene = sceneIterator.nextScene();
        currentScene.drawScene();
        currentScene.updateScene(input);
    }

}

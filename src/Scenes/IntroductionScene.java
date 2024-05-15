package Scenes;

import GameProperties.GameProps;
import GameProperties.MessageProps;
import Utils.Fonts;
import Messages.Message;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.Properties;

/**
 * Initial scene which contains the instruction and title
 */
public class IntroductionScene extends TextScene{
    private int nextLevel;
    /***
     * Load the messages of the scene, and initialize current Level (which will be updated based on user input)
     */

    @Override
    protected void loadScene() {
        Properties messageProps = MessageProps.getMessageProps();
        Properties gameProps = GameProps.getGameProps();

        int windowWidth = Integer.parseInt(gameProps.getProperty("windowWidth"));

        int titleSize = Integer.parseInt(gameProps.getProperty("title.fontSize"));
        Message title = new Message(messageProps.getProperty("title"), new Point(Integer.parseInt(gameProps.getProperty("title.x")),
                Integer.parseInt(gameProps.getProperty("title.y"))), Fonts.getFont(titleSize), false);

        int instructionSize = Integer.parseInt(gameProps.getProperty("instruction.fontSize"));
        Message instruction = new Message(messageProps.getProperty("instruction"), new Point(windowWidth * 1.0/2,
                Integer.parseInt(gameProps.getProperty("instruction.y"))), Fonts.getFont(instructionSize), true);

        this.messages.add(title);
        this.messages.add(instruction);

        this.nextLevel = -1;
    }

    /**
     * Update the internal storage of the next level based on input,
     * and mark end scene if 1, 2 or 3 is pressed
     * @param input user input
     */
    @Override
    public void updateScene(Input input){
        isEnd = true;
        if (input.isDown(Keys.NUM_1)){
            nextLevel = 1;
        } else if (input.isDown(Keys.NUM_2)){
            nextLevel = 2;
        } else if (input.isDown(Keys.NUM_3)){
            nextLevel = 3;
        } else {
            isEnd = false;
        }

    }

    /**
     * Get the next level (-1 if unset)
     * @return next level
     */
    public int getNextLevel(){
        return nextLevel;
    }


}

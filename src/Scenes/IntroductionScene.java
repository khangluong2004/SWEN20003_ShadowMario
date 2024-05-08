package Scenes;

import GameProperties.GameProps;
import GameProperties.MessageProps;
import bagel.Font;
import utils.Fonts;
import utils.Message;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import utils.Message;

import java.util.Properties;

public class IntroductionScene extends TextScene{
    private int curLevel;
    /***
     * Load the messages of the scene
     * @param
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

        this.curLevel = -1;
    }

    @Override
    public void updateScene(Input input){
        isEnd = true;
        if (input.isDown(Keys.NUM_1)){
            curLevel = 1;
        } else if (input.isDown(Keys.NUM_2)){
            curLevel = 2;
        } else if (input.isDown(Keys.NUM_3)){
            curLevel = 3;
        } else {
            isEnd = false;
        }

    }

    public int getNextLevel(){
        return curLevel;
    }


}

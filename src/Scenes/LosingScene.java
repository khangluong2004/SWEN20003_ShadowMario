package Scenes;

import GameProperties.GameProps;
import GameProperties.MessageProps;
import bagel.util.Point;
import utils.Fonts;
import utils.Message;

import java.util.Properties;

public class LosingScene extends TextScene{

    protected void loadScene() {
        Properties messageProps = MessageProps.getMessageProps();
        Properties gameProps = GameProps.getGameProps();
        int windowWidth = Integer.parseInt(gameProps.getProperty("windowWidth"));

        int messageSize = Integer.parseInt(gameProps.getProperty("message.fontSize"));
        Message losingMessage = new Message(messageProps.getProperty("gameOver"), new Point(windowWidth * 1.0/2,
                Integer.parseInt(gameProps.getProperty("message.y"))), Fonts.getFont(messageSize), true);;

        this.messages.add(losingMessage);
    }
}

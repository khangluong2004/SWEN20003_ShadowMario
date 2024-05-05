package Scenes;

import GameProperties.GameProps;
import GameProperties.MessageProps;
import bagel.util.Point;
import utils.Fonts;
import utils.Message;

import java.util.Properties;

public class WinningScene extends TextScene{
    protected void loadScene() {
        Properties messageProps = MessageProps.getMessageProps();
        Properties gameProps = GameProps.getGameProps();
        int windowWidth = Integer.parseInt(gameProps.getProperty("windowWidth"));

        Message winningMessage = new Message(messageProps.getProperty("gameWon"), new Point(windowWidth * 1.0/2,
                Integer.parseInt(gameProps.getProperty("message.y"))), Fonts.getSmallFont(), true);

        this.messages.add(winningMessage);
    }
}

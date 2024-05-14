package Scenes;

import GameProperties.GameProps;
import GameProperties.MessageProps;
import bagel.util.Point;
import Utils.Fonts;
import Messages.Message;

import java.util.Properties;

/**
 * Implementation of Text Scene for WinningScene
 */
public class WinningScene extends TextScene{
    /**
     * Load winning message
     */
    @Override
    protected void loadScene() {
        Properties messageProps = MessageProps.getMessageProps();
        Properties gameProps = GameProps.getGameProps();
        int windowWidth = Integer.parseInt(gameProps.getProperty("windowWidth"));

        int messageSize = Integer.parseInt(gameProps.getProperty("message.fontSize"));
        Message winningMessage = new Message(messageProps.getProperty("gameWon"), new Point(windowWidth * 1.0/2,
                Integer.parseInt(gameProps.getProperty("message.y"))), Fonts.getFont(messageSize), true);

        this.messages.add(winningMessage);
    }
}

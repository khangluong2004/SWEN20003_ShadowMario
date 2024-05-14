package Messages.StatusMessages;

import GameEntities.Characters.ScoreContainer;
import GameEntities.Characters.StatusContainer;
import GameProperties.MessageProps;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Point;
import Messages.Message;

import java.util.Properties;

/**
 * Class for score status message
 */
public class ScoreStatusMessage extends Message implements StatusObserver {
    private final String PREFIX;

    /**
     * Make a score status message
     * @param messageStr content of the message
     * @param location location of the message
     * @param font font of the message
     * @param isCentered if the text needs to be centered
     */
    public ScoreStatusMessage(String messageStr, Point location, Font font, boolean isCentered) {
        this(messageStr, location, font, isCentered, Colour.WHITE);
    }

    /**
     * Overloaded constructor with additional parameters for colour
     */
    public ScoreStatusMessage(String messageStr, Point location, Font font, boolean isCentered, Colour colour) {
        super(messageStr, location, font, isCentered, colour);
        Properties messageProps = MessageProps.getMessageProps();

        PREFIX = messageProps.getProperty("score");
    }

    /**
     * Notify callback function, for any ScoreContainer object to update the score message
     * @param entity observed object
     */
    @Override
    public void notify(StatusContainer entity) {
        if (entity instanceof ScoreContainer){
            ScoreContainer scoreContainer = (ScoreContainer) entity;
            this.setMessageContent(PREFIX + scoreContainer.getScore());
        }
    }
}

package Messages.StatusMessages;

import GameEntities.Characters.ScoreContainer;
import GameEntities.StatusContainer;
import GameProperties.MessageProps;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Point;
import Messages.Message;

import java.util.Properties;

public class ScoreStatusMessage extends Message implements StatusObserver {
    private final String PREFIX;
    public ScoreStatusMessage(String messageStr, Point location, Font font, boolean isCentered) {
        this(messageStr, location, font, isCentered, Colour.WHITE);
    }

    public ScoreStatusMessage(String messageStr, Point location, Font font, boolean isCentered, Colour colour) {
        super(messageStr, location, font, isCentered, colour);
        Properties messageProps = MessageProps.getMessageProps();

        PREFIX = messageProps.getProperty("score");
    }

    @Override
    public void notify(StatusContainer entity) {
        if (entity instanceof ScoreContainer){
            ScoreContainer scoreContainer = (ScoreContainer) entity;
            this.setMessageContent(PREFIX + scoreContainer.getScore());
        }
    }
}

package utils.StatusMessages;

import GameEntities.Characters.ScoreContainer;
import GameEntities.StatusContainer;
import GameProperties.MessageProps;
import bagel.Font;
import bagel.util.Point;
import utils.Message;

import java.util.Properties;

public class ScoreStatusMessage extends Message implements StatusObserver {
    private final String PREFIX;
    public ScoreStatusMessage(String messageStr, Point location, Font font, boolean isCentered) {
        super(messageStr, location, font, isCentered);
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

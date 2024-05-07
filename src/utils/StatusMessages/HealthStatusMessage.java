package utils.StatusMessages;

import GameEntities.Characters.Killable;
import GameEntities.StatusContainer;
import GameProperties.MessageProps;
import bagel.Font;
import bagel.util.Point;
import utils.Message;
import utils.PercentageUtils;

import java.util.Properties;

public class HealthStatusMessage extends Message implements StatusObserver {
    private final String PREFIX;
    public HealthStatusMessage(String messageStr, Point location, Font font, boolean isCentered) {
        super(messageStr, location, font, isCentered);
        Properties messageProps = MessageProps.getMessageProps();
        PREFIX = messageProps.getProperty("health");
    }

    @Override
    public void notify(StatusContainer entity) {
        if (entity instanceof Killable){
            Killable killable = (Killable) entity;
            this.setMessageContent(PREFIX + PercentageUtils.toPercentage(killable.getHealth()));
        }
    }
}

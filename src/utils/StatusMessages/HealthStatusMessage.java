package utils.StatusMessages;

import GameEntities.Characters.Killable;
import GameEntities.GameEntity;
import GameProperties.MessageProps;
import bagel.Font;
import bagel.util.Point;
import utils.Message;

import java.util.Properties;

public class HealthStatusMessage extends Message implements StatusMessage {
    private final String PREFIX;
    public HealthStatusMessage(String messageStr, Point location, Font font, boolean isCentered) {
        super(messageStr, location, font, isCentered);
        Properties messageProps = MessageProps.getMessageProps();

        PREFIX = messageProps.getProperty("health");
    }

    @Override
    public void notify(GameEntity entity) {
        if (entity instanceof Killable){
            Killable killable = (Killable) entity;
            this.setMessageContent(PREFIX + killable.getHealth());
        }
    }
}

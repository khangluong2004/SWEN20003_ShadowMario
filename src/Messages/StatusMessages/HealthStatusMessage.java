package Messages.StatusMessages;

import GameEntities.Characters.Killable;
import GameEntities.Characters.StatusContainer;
import GameProperties.MessageProps;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Point;
import Messages.Message;
import utils.PercentageUtils;

import java.util.Properties;

/**
 * Class for the health status message
 */
public class HealthStatusMessage extends Message implements StatusObserver {
    private final String PREFIX;

    /**
     * Create a health status message
     * @param messageStr content of the message
     * @param location location
     * @param font font
     * @param isCentered if needed to center the message
     */
    public HealthStatusMessage(String messageStr, Point location, Font font, boolean isCentered) {
        this(messageStr, location, font, isCentered, Colour.WHITE);
    }

    /**
     * Overloaded constructor with an additional colour parameters for setting colour
     */
    public HealthStatusMessage(String messageStr, Point location, Font font, boolean isCentered, Colour colour) {
        super(messageStr, location, font, isCentered, colour);
        Properties messageProps = MessageProps.getMessageProps();
        PREFIX = messageProps.getProperty("health");
    }

    /**
     * Notify callback function, for any Killable object to update the health message
     * @param entity the killable entity
     */
    @Override
    public void notify(StatusContainer entity) {
        if (entity instanceof Killable){
            Killable killable = (Killable) entity;
            this.setMessageContent(PREFIX + PercentageUtils.toPercentage(killable.getHealth()));
        }
    }
}

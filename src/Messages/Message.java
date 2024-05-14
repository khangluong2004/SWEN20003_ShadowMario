package Messages;

import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Point;

/**
 * Messages.Message class for message objects
 * to handle drawing font at specified locations
 */
public class Message {
    private String messageStr;
    // Do not set location to final, as might need to change the location in the future
    // (although don't need that feature for now)
    private Point location;
    private final Font FONT;
    private DrawOptions options;

    /**
     * Create a message
     * @param messageStr content of the message
     * @param location location
     * @param font font
     * @param isCentered if want to center or not
     */
    public Message(String messageStr, Point location, Font font, boolean isCentered){
        this(messageStr, location, font, isCentered, Colour.WHITE);
    }

    public Message(String messageStr, Point location, Font font, boolean isCentered, Colour color){
        this.messageStr = messageStr;
        this.FONT = font;
        this.options = new DrawOptions();
        options.setBlendColour(color);

        // If the text needs centering, then shift left half the width of the string
        // since the message is drawn from the bottom left
        if (isCentered){
            this.location = new Point(location.x - font.getWidth(messageStr)/2, location.y);
        } else {
            this.location = location;
        }
    }

    /**
     * Write/ draw the text on the screen
     */
    public void write(){
        FONT.drawString(messageStr, location.x, location.y, options);
    }

    /**
     * Set/ change the content of the message
     * @param newMessageStr new message contents
     */
    public void setMessageContent(String newMessageStr){
        messageStr = newMessageStr;
    }
}

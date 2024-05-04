import bagel.Font;
import bagel.util.Point;

/**
 * Message class for message objects
 * to handle drawing font at specified locations
 */
public class Message {
    private String messageStr;
    // Do not set location to final, as might need to change the location in the future
    // (although don't need that feature for now)
    private Point location;
    private final Font FONT;

    public Message(String messageStr, Point location, Font font, boolean isCentered){
        this.messageStr = messageStr;
        this.FONT = font;

        // If the text needs centering, then shift left half the width of the string
        // since the message is drawn from the bottom left
        if (isCentered){
            this.location = new Point(location.x - font.getWidth(messageStr)/2, location.y);
        } else {
            this.location = location;
        }
    }

    public void draw(){
        FONT.drawString(messageStr, location.x, location.y);
    }

    public void setMessageStr(String newMessageStr){
        messageStr = newMessageStr;
    }
}

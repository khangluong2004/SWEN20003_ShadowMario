package utils;

import GameProperties.GameProps;
import bagel.*;

import java.util.Properties;

/***
 * Global access for fonts: Singleton(-ish)
 */

// TODO: Might need to create a more adaptive one for each type
public class Fonts {
    private static Font largeFont;
    private static Font mediumFont;
    private static Font smallFont;
    private Fonts(){};

    /**
     * Font for message and instruction
     * @return
     */
    public static Font getSmallFont(){
        Properties gameProps = GameProps.getGameProps();

        if (smallFont == null){
            smallFont = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("instruction.fontSize")));
        }
        return smallFont;
    }

    /**
     * Font for title
     * @return
     */
    public static Font getLargeFont(){
        Properties gameProps = GameProps.getGameProps();

        if (largeFont == null){
            largeFont = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("title.fontSize")));
        }
        return largeFont;
    }

    /**
     * Font for health and score
     * @return
     */
    public static Font getMediumFont(){
        Properties gameProps = GameProps.getGameProps();

        if (mediumFont == null){
            mediumFont = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("score.fontSize")));
        }
        return mediumFont;
    }

}

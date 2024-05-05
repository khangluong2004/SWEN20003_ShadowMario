package utils;

import GameProperties.GameProps;
import bagel.*;

import java.util.Properties;

/***
 * Global access for fonts: Singleton(-ish)
 */
public class Fonts {
    private static Font largeFont;
    private static Font smallFont;
    private Fonts(){};

    public static Font getSmallFont(){
        Properties gameProps = GameProps.getGameProps();

        if (smallFont == null){
            smallFont = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("instruction.fontSize")));
        }
        return smallFont;
    }

    public static Font getLargeFont(){
        Properties gameProps = GameProps.getGameProps();

        if (largeFont == null){
            largeFont = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("title.fontSize")));
        }
        return largeFont;
    }

}

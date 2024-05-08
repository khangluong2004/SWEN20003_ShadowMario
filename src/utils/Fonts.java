package utils;

import GameProperties.GameProps;
import bagel.*;
import bagel.util.Colour;

import java.util.Properties;

/***
 * Create a wrapper for less lengthy font constructor
 */

// TODO: Might need to create a more adaptive one for each type
public class Fonts {
    public static Font getFont(int fontSize){
        Properties gameProps = GameProps.getGameProps();
        return new Font(gameProps.getProperty("font"), fontSize);
    }
}

package Utils;

import GameProperties.GameProps;
import bagel.*;

import java.util.Properties;

/***
 * Create a wrapper for less lengthy font constructor
 */

public class Fonts {
    /**
     * Get font based on font size
     * @param fontSize fontSize
     * @return font with font size (and correct font)
     */
    public static Font getFont(int fontSize){
        Properties gameProps = GameProps.getGameProps();
        return new Font(gameProps.getProperty("font"), fontSize);
    }
}

package GameProperties;

import Utils.IOUtils;

import java.util.Properties;

/***
 * Store GameProps as a single instance with global access points: Singleton(-ish)
 */
public class GameProps {
    private static Properties gameProps;

    // Set private constructor to prevent external access
    private GameProps(){};

    /**
     * Method to get game props
     * @return single game props instance
     */
    public static Properties getGameProps(){
        if (gameProps == null){
            gameProps = IOUtils.readPropertiesFile("res/app.properties");
        }

        return gameProps;
    }
}

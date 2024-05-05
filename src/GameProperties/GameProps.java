package GameProperties;

import utils.IOUtils;

import java.util.Properties;

/***
 * Store GameProps as a single instance with global access points: Singleton(-ish)
 */
public class GameProps {
    private static Properties gameProps;
    private GameProps(){};

    public static Properties getGameProps(){
        if (gameProps == null){
            gameProps = IOUtils.readPropertiesFile("res/app.properties");
        }

        return gameProps;
    }
}

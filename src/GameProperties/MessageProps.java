package GameProperties;

import utils.IOUtils;

import java.util.Properties;

/***
 * Store MessageProps as a single instance with global access points: Singleton(-ish)
 */
public class MessageProps {
    private static Properties messageProps;
    private MessageProps(){};

    public static Properties getMessageProps(){
        if (messageProps == null){
            messageProps = IOUtils.readPropertiesFile("res/message_en.properties");
        }

        return messageProps;
    }
}

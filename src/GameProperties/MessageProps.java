package GameProperties;

import Utils.IOUtils;

import java.util.Properties;

/***
 * Store MessageProps as a single instance with global access points: Singleton(-ish)
 */
public class MessageProps {
    private static Properties messageProps;

    // Set private constructor to prevent external access
    private MessageProps(){}

    /**
     * Get the message props
     * @return the single message props
     */
    public static Properties getMessageProps(){
        if (messageProps == null){
            messageProps = IOUtils.readPropertiesFile("res/message_en.properties");
        }

        return messageProps;
    }
}

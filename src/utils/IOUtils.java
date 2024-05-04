package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Class that contains methods to read a CSV file and a properties file.
 * You may edit this as you wish.
 */
public class IOUtils {

    private static final int MAX_LINES = 50;

    /***
     * Method that reads a CSV file and return a 2D String array
     * @param csvFile: the path to the CSV file
     * @return 2D String array
     */
    public static String[][] readCsv(String csvFile) {
        // Assume 50 lines in csv
        String[][] result = new String[MAX_LINES][];
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            String line;

            for (int lineCount=0; lineCount < MAX_LINES; lineCount++){
                line = br.readLine();
                // Stop early when nothing is read
                if (line == null){
                    return result;
                }
                result[lineCount] = line.split(",");
            }
        } catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
        return result;
    }

    /***
     * Method that reads a properties file and return a Properties object
     * @param configFile: the path to the properties file
     * @return Properties object
     */
    public static Properties readPropertiesFile(String configFile) {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(configFile));
        } catch(IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }

        return appProps;
    }
}
package utils;

public class PercentageUtils {
    public static final int PERCENT_CONVERT = 100;
    public static int toPercentage(double value){
        return (int) (value * PERCENT_CONVERT);
    }
}

package Utils;

/**
 * Very small "functional" to provide small helpers with conversion to percent
 */
public class PercentageUtils {
    /**
     * Constant for 100 percent
     */
    public static final int PERCENT_CONVERT = 100;

    /**
     * Static method to convert to percent
     * @param value the decimal value
     * @return the equivalent percentage
     */
    public static int toPercentage(double value){
        return (int) Math.round(value * PERCENT_CONVERT);
    }
}

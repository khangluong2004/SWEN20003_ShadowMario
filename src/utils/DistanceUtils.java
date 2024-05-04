package utils;

import bagel.util.Point;

/**
 * "Functional" class with static helper methods for calculating distances squared
 * 2 types supported: Euclidean distances and Vertical distance difference
 */
public class DistanceUtils {
    /**
     * Find the Euclidean distance between 2 locations (for coin, endFlag, enemy)
     * @param location1
     * @param location2
     * @return the euclidean distance
     */
    public static double calcEuclideanDistanceSquared(Point location1, Point location2){
        double diffX = location1.x - location2.x;
        double diffY = location1.y - location2.y;
        return (diffX * diffX) + (diffY * diffY);
    }

    /**
     * Find the vertical distances between 2 locations (for platform)
     * @param location1
     * @param location2
     * @return vertical distance squared
     */
    public static double calcVerticalDistanceSquared(Point location1, Point location2){
        double diffY = location1.y - location2.y;
        return (diffY * diffY);
    }
}

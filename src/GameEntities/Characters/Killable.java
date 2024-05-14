package GameEntities.Characters;

/**
 * Interface for characters with health and can be killed/ damaged
 */
public interface Killable {
    /**
     * Get health
     * @return health
     */
    double getHealth();

    /**
     * Check if the entity can be damaged (eg: Player without invincible)
     * @return isDamageable
     */
    boolean isDamageable();
}

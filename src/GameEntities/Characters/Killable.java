package GameEntities.Characters;

/**
 * Interface for characters with health and can be killed/ damaged
 */
public interface Killable {
    double getHealth();
    boolean isDamageable();
}

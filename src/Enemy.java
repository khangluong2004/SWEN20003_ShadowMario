import bagel.Image;
import bagel.util.Point;

import java.util.Properties;

/**
 * Class for enemy entity, inherits from EuclideanCollidableMovableEntity,
 * with overidden collision handling methods and reset attributes
 */
public class Enemy extends EuclideanCollidableMovableEntity {
    private final double damageSize;
    private boolean inflictedDamage;

    public Enemy(Point location, Properties game_props){
        super(location,
                Double.parseDouble(game_props.getProperty("gameObjects.enemy.radius")),
                Integer.parseInt(game_props.getProperty("gameObjects.enemy.speed")),
                new Image(game_props.getProperty("gameObjects.enemy.image"))
        );
        damageSize = -1 * Double.parseDouble(game_props.getProperty("gameObjects.enemy.damageSize"));
        this.inflictedDamage = false;
    }

    public double getDamageSize(){
        return damageSize;
    }

    public boolean isInflictedDamage(){
        return inflictedDamage;
    }

    /**
     * Methods to reset attributes when game restart
     * @param game_props
     */
    @Override
    public void resetAttributes(Properties game_props){
        super.resetAttributes(game_props);
        this.inflictedDamage = false;
    }

    /**
     * Handling side-effect during collision with other entity
     * @param entity
     */
    @Override
    public void collideWith(Collidable entity) {}

    /**
     * Handling side-effect after collision with other entity
     * @param entity the entity that is collided with
     */
    @Override
    public void finishColliding(Collidable entity) {
        if (!this.inflictedDamage && entity instanceof Player){
            // Set flag so not to inflict damage twice
            this.inflictedDamage = true;
        }
    }
}

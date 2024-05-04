import bagel.Image;
import bagel.util.Point;

import java.util.Properties;

/**
 * Class for EndFlag inheriting from EuclideanCollidableMovableEntity
 */
public class EndFlag extends EuclideanCollidableMovableEntity {

    /**
     * Constructor to initialize the velocity, location and image
     * @param location
     * @param game_props
     */
    public EndFlag(Point location, Properties game_props){
        super(location,
                Double.parseDouble(game_props.getProperty("gameObjects.endFlag.radius")),
                Integer.parseInt(game_props.getProperty("gameObjects.endFlag.speed")),
                new Image(game_props.getProperty("gameObjects.endFlag.image"))
        );
    }


    @Override
    public void collideWith(Collidable entity) {}

    @Override
    public void finishColliding(Collidable entity) {}
}

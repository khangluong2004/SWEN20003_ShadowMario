import GameEntity.*;
import bagel.*;
import bagel.util.Point;
import enums.MoveDirection;
import utils.IOUtils;
import utils.Message;

import java.util.Properties;

/*
 * Implementation based on skeleton Code for SWEN20003 Project 1, Semester 1, 2024
 *
 * Please enter your name below
 * @author LUONG AN KHANG
 */

/**
 * Main class for Shadow Mario game controls
 * @author LUONG AN KHANG
 */
public class ShadowMario extends AbstractGame {
    private static final int MAX_ENTITIES = 50;
    private static final int MAX_OTHER_ENTITIES = MAX_ENTITIES - 1;

    private final Image BACKGROUND_IMAGE;

    private final Message TITLE_MESSAGE;
    private final Message INSTRUCTION_MESSAGE;
    private final Message WINNING_MESSAGE;

    private final Message LOSING_MESSAGE;


    // Game States
    enum GameStage {
        START_SCREEN,
        PLAYING,
        ENDING
    }

    private GameStage curStage;

    // Save game properties to reset the stages at the end
    private final Properties GAME_INIT_PROPS;

    // Game elements (might change so don't set as final)
    private Player player;
    private GameEntity[] otherEntities;
    private CollisionMediator collisionMediator;


    /**
     * The constructor
     */
    public ShadowMario(Properties gameProps, Properties messageProps) {
        super(Integer.parseInt(gameProps.getProperty("windowWidth")),
              Integer.parseInt(gameProps.getProperty("windowHeight")),
              messageProps.getProperty("title"));

        this.GAME_INIT_PROPS = gameProps;

        int windowWidth = Integer.parseInt(gameProps.getProperty("windowWidth"));

        BACKGROUND_IMAGE = new Image(gameProps.getProperty("backgroundImage"));
        this.curStage = ShadowMario.GameStage.START_SCREEN;

        // Fonts and messages
        // All fonts
        Font LARGE_FONT = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("title.fontSize")));
        Font SMALL_FONT = new Font(gameProps.getProperty("font"), Integer.parseInt(gameProps.getProperty("instruction.fontSize")));

        // All messages
        TITLE_MESSAGE = new Message(messageProps.getProperty("title"), new Point(Integer.parseInt(gameProps.getProperty("title.x")),
                Integer.parseInt(gameProps.getProperty("title.y"))), LARGE_FONT, false);
        INSTRUCTION_MESSAGE = new Message(messageProps.getProperty("instruction"), new Point(windowWidth * 1.0/2,
                Integer.parseInt(gameProps.getProperty("instruction.y"))), SMALL_FONT, true);
        WINNING_MESSAGE = new Message(messageProps.getProperty("gameWon"), new Point(windowWidth * 1.0/2,
                Integer.parseInt(gameProps.getProperty("message.y"))), SMALL_FONT, true);
        LOSING_MESSAGE = new Message(messageProps.getProperty("gameOver"), new Point(windowWidth * 1.0/2,
                Integer.parseInt(gameProps.getProperty("message.y"))), SMALL_FONT, true);


        // you can initialise other values from the property files here
        this.otherEntities = new GameEntity[MAX_OTHER_ENTITIES];
        int otherEntitiesCount = 0;


        String[][] worldInfo = IOUtils.readCsv(gameProps.getProperty("level1File"));

        // Iterate through the array
        for (int i=0; i < MAX_ENTITIES; i++){
            // Stop when reach MAX_ENTITIES or encounter an empty position
            if (worldInfo[i] == null){
                break;
            }
            double x_coord = Double.parseDouble(worldInfo[i][1]);
            double y_coord = Double.parseDouble(worldInfo[i][2]);
            switch (worldInfo[i][0]) {
                case "PLATFORM":
                    otherEntities[otherEntitiesCount] = new Platform(new Point(x_coord, y_coord), gameProps);
                    otherEntitiesCount++;
                    break;
                case "PLAYER":
                    player = new Player(new Point(x_coord, y_coord), gameProps, messageProps);
                    break;
                case "ENEMY":
                    otherEntities[otherEntitiesCount] = new Enemy(new Point(x_coord, y_coord), gameProps);
                    otherEntitiesCount++;
                    break;
                case "COIN":
                    otherEntities[otherEntitiesCount] = new Coin(new Point(x_coord, y_coord), gameProps);
                    otherEntitiesCount++;
                    break;
                case "END_FLAG":
                    otherEntities[otherEntitiesCount] = new EndFlag(new Point(x_coord, y_coord), gameProps);
                    otherEntitiesCount++;
            }
        }

        // Set up collision mediator to handle collisions between entities
        // For now, mainly implemented to handle collision between players and the others
        collisionMediator = new CollisionMediator(player, otherEntities, MAX_ENTITIES);

    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");

        ShadowMario game = new ShadowMario(game_props, message_props);

        game.run();
    }

    /**
     * Update the locations of all entities except players
     * @param direction
     */
    private void updateOtherEntitiesLocations(MoveDirection direction){
        for (int i=0; i < MAX_OTHER_ENTITIES; i++){
            if (otherEntities[i] != null && otherEntities[i] instanceof Movable){
                Movable movableEntity = (Movable) otherEntities[i];
                movableEntity.updateLocation(direction);
            }
        }
    }

    /**
     * Update location of the player
     * @param direction
     */
    private void updatePlayerLocation(MoveDirection direction){
        player.updateLocation(direction);
    }

    /**
     * Update locations of all entities
     * @param direction
     */
    private void updateAllLocations(MoveDirection direction){
        updateOtherEntitiesLocations(direction);
        updatePlayerLocation(direction);
    }

    /**
     * Draw all entities except the player
     */
    private void drawOtherEntities(){
        for (int i=0; i < MAX_OTHER_ENTITIES; i++){
            if (otherEntities[i] != null){
                otherEntities[i].draw();
            }
        }
    }

    /**
     * Draw the player
     */
    private void drawPlayer(){
        player.draw();
    }

    /**
     * Draw all entities
     */
    private void drawAllEntities(){
        drawOtherEntities();
        drawPlayer();
    }

    /**
     * Reset attributes when game restarts of all entities except the player
     */
    private void resetOtherEntities(){
        // Reset all entities except player
        for (int i=0; i < MAX_OTHER_ENTITIES; i++){
            if (otherEntities[i] != null){
                otherEntities[i].resetAttributes(GAME_INIT_PROPS);
            }
        }
    }

    /**
     * Reset attributes when game restarts of the player
     */
    private void resetPlayer(){
        // Reset attributes of the player
        player.resetAttributes(GAME_INIT_PROPS);
    }

    /**
     * Reset attributes when game restarts of all entities
     */
    private void resetAllEntities(){
        // Reset attributes of all entities
        resetOtherEntities();
        resetPlayer();
    }


    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        // close window
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        // Change scene when space is pressed on start screen and end screen
        if (input.wasPressed(Keys.SPACE)){
            if (curStage == ShadowMario.GameStage.START_SCREEN){
                curStage = ShadowMario.GameStage.PLAYING;
            } else if (curStage == ShadowMario.GameStage.ENDING){
                // Reset game stage and entities attributes
                curStage = ShadowMario.GameStage.START_SCREEN;
                resetAllEntities();
            }
        }

        // Only detect inputs and update location during PLAYING stage
        if (curStage == ShadowMario.GameStage.PLAYING) {
            // Moving entities
            // Update all GameEntity.Movable based on the keys pressed
            // Async input, so call as separate if
            if (input.isDown(Keys.LEFT)) {
                updateAllLocations(MoveDirection.LEFT);
            }

            if (input.isDown(Keys.RIGHT)) {
                updateAllLocations(MoveDirection.RIGHT);
            }

            if (input.isDown(Keys.UP)) {
                updateAllLocations(MoveDirection.UP);
            }

            if (input.isDown(Keys.DOWN)) {
                updateAllLocations(MoveDirection.DOWN);
            }

            // Call update with continue for objects with continuing velocity (eg: GameEntity.Player after jumping)
            updateAllLocations(MoveDirection.CONTINUE);

            // Call the mediator to manage collision
            collisionMediator.handleCollision();

            // Check if the game ends
            if (player.getEndingStage() != enums.GameStage.PLAYING){
                curStage = ShadowMario.GameStage.ENDING;
            }
        }

        // Continue the movement of the players downward if ending
        if (curStage == ShadowMario.GameStage.ENDING && player.getEndingStage() == enums.GameStage.START_LOSING){
            player.updateLocation(MoveDirection.CONTINUE);
        }

        // Games stage setting
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        // Display starting screen
        if (curStage == ShadowMario.GameStage.START_SCREEN){
            TITLE_MESSAGE.draw();
            INSTRUCTION_MESSAGE.draw();
            return;
        }


        // Playing and Ending stage
        // Draw all entities in playing stage
        if (curStage == ShadowMario.GameStage.PLAYING){
            drawAllEntities();
            return;
        }

        // Handle ending stages
        if (curStage == ShadowMario.GameStage.ENDING){
            if (player.getEndingStage() == enums.GameStage.WINNING){
                WINNING_MESSAGE.draw();
            } else if (player.getEndingStage() == enums.GameStage.FINISH_LOSING){
                LOSING_MESSAGE.draw();
            } else if (player.getEndingStage() == enums.GameStage.START_LOSING){
                // Still draw everything until the player disappears out of screen
                drawAllEntities();
            }
        }

    }

}

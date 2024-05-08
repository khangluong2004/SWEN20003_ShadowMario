package Scenes;

import bagel.Input;
import bagel.Keys;
import Messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * The abstract class for all scenes with text only (Introduction, Winning and Losing scene).
 * Used the factory method pattern.
 */
public abstract class TextScene implements Scene{
    protected boolean isEnd;
    protected List<Message> messages = new ArrayList<Message>();

    public TextScene(){
        this.loadScene();
    }

    /**
     * Factory method which loads different message depends on the implementation
     */
    protected void loadScene(){};

    /**
     * Draw the scene
     */
    @Override
    public void drawScene() {
        for (Message message: messages){
            message.write();
        }
    }

    /**
     * Depends on the implementation. The default update is to end the scene when
     * Space is pressed.
     * @param input
     */
    @Override
    public void updateScene(Input input) {
        if (input.isDown(Keys.SPACE)){
            isEnd = true;
        }
    }

    /**
     * Check if the scene ends
     * @return
     */
    @Override
    public boolean isEnd() {
        return isEnd;
    }
}

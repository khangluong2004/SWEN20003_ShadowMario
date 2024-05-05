package Scenes;

import bagel.Input;
import bagel.Keys;
import utils.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class TextScene implements Scene{
    protected boolean isEnd;
    protected List<Message> messages = new ArrayList<Message>();

    public TextScene(){
        this.loadScene();
    }


    protected void loadScene(){};

    @Override
    public void drawScene() {
        for (Message message: messages){
            message.write();
        }
    }

    @Override
    public void updateScene(Input input) {
        if (input.isDown(Keys.SPACE)){
            isEnd = true;
        }
    }

    @Override
    public boolean isEnd() {
        return isEnd;
    }
}

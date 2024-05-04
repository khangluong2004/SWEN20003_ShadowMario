package Scenes;

import bagel.Input;
import bagel.Keys;

public class IntroductionScene extends TextScene{
    public IntroductionScene(){};
    public int getNextLevel(Input input){
        int curLevel = -1;
        if (input.isDown(Keys.NUM_2)){
            curLevel = 2;
        } else if (input.isDown(Keys.NUM_3)){
            curLevel = 3;
        } else if (input.isDown(Keys.NUM_1)){
            curLevel = 1;
        }

        return curLevel;
    }
}

package Scenes;

import bagel.Input;
import bagel.Keys;
import enums.GameStage;

import java.util.Properties;

public class SceneIterator {
    private Properties gameProps;
    private Properties messageProps;
    private Scene currentScene;

    public SceneIterator(Properties gameProps, Properties messageProps){
        this.gameProps = gameProps;
        this.messageProps = messageProps;
        this.currentScene = new IntroductionScene();
    }

    /***
     * Handling switching scenes with Iterator pattern
     * @param input
     * @return
     */
    public Scene nextScene(Input input){
        // If the scene hasn't ended, change nothing
        if (!currentScene.isEnd()){
            return currentScene;
        }

        // Else, move to the corresponding next scene
        if (currentScene instanceof IntroductionScene){
            IntroductionScene currentScene = (IntroductionScene) this.currentScene;
            int curLevel = currentScene.getNextLevel(input);
            this.currentScene = new PlayingScene(curLevel);
        } else if (currentScene instanceof PlayingScene){
            PlayingScene currentScene = (PlayingScene) this.currentScene;
            if (currentScene.getGameStage() == GameStage.WINNING){
                this.currentScene = new WinningScene();
            } else if (currentScene.getGameStage() == GameStage.FINISH_LOSING){
                this.currentScene = new LosingScene();
            }
        } else {
            this.currentScene = new IntroductionScene();
        }

        return this.currentScene;
    }

    /***
     * Due to the infinite cycle of the game, the hasNextScene method always return true
     * @return
     */
    public boolean hasNextScene(){
        return true;
    }
}

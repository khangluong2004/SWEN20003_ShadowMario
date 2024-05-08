package Scenes;

import Scenes.PlayingScenes.*;

/***
 * Scene iterator: Handling the switching scenes
 */
public class SceneIterator {
    private Scene currentScene;

    public SceneIterator(){
        this.currentScene = new IntroductionScene();
    }

    /***
     * Handling switching scenes with Iterator pattern
     * @param
     * @return
     */
    public Scene nextScene(){
        // If the scene hasn't ended, change nothing
        if (!currentScene.isEnd()){
            return currentScene;
        }

        // Else, move to the corresponding next scene
        if (currentScene instanceof IntroductionScene){
            IntroductionScene currentScene = (IntroductionScene) this.currentScene;
            int curLevel = currentScene.getNextLevel();
            switch (curLevel){
                case 1:
                    this.currentScene = new Level1();
                    break;
                case 2:
                    this.currentScene = new Level2();
                    break;
                case 3:
                    this.currentScene = new Level3();
                    break;
            }
        } else if (currentScene instanceof PlayingScene){
            PlayingScene currentScene = (PlayingScene) this.currentScene;
            if (currentScene.getGameStage() == GameStage.WINNING){
                this.currentScene = new WinningScene();
            } else if (currentScene.getGameStage() == GameStage.LOSING){
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

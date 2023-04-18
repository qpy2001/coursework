package uk.ac.soton.comp1206.scene;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.game.Score;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

public class ScoreScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    protected Score score;


    /**
     * Create a new ScoreScene
     * @param gameWindow the Game Window this will be displayed in
     */
    public ScoreScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Score Scene");
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());
        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var challengePane = new StackPane();
        challengePane.setMaxWidth(gameWindow.getWidth());
        challengePane.setMaxHeight(gameWindow.getHeight());
        challengePane.getStyleClass().add("menu-background");
        root.getChildren().add(challengePane);

        var mainPane = new BorderPane();
        challengePane.getChildren().add(mainPane);




    }


}

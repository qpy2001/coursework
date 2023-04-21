package uk.ac.soton.comp1206.scene;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.game.Score;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.utils.TxtUtils;

import java.util.List;

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


        List<String> scoreList = TxtUtils.readTxt("src/main/scores.txt");
        VBox vBox = new VBox(15);
        vBox.setPadding(new Insets(15, 5, 5, 5));
        Label label = new Label("username scores");
        label.setFont(javafx.scene.text.Font.font(20));
        vBox.getChildren().add(label);
        for(int i =0; i< 6 && i< scoreList.size();i++){
            Label temp = new Label(scoreList.get(i));
            temp.setFont(javafx.scene.text.Font.font(20));
            vBox.getChildren().add(temp);
        }



        var button = new Button("Restart the game");
        mainPane.setCenter(button);
        mainPane.setRight(vBox);

        //Bind the button action to the startGame method in the menu
        button.setOnAction(this::startGame);

    }

    /**
     * Handle when the Start Game button is pressed
     * @param event event
     */
    private void startGame(ActionEvent event) {
        gameWindow.startChallenge();
    }

}

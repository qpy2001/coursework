package uk.ac.soton.comp1206.scene;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * The main menu of the game. Provides a gateway to the rest of the game.
 */
public class MenuScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);

    /**
     * Create a new menu scene
     * @param gameWindow the Game Window this will be displayed in
     */
    public MenuScene(GameWindow gameWindow) {
        super(gameWindow);
        gameWindow.playMusic("src/main/resources/music/menu.mp3");
        logger.info("Creating Menu Scene");
    }

    /**
     * Build the menu layout
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var menuPane = new StackPane();
        menuPane.setMaxWidth(gameWindow.getWidth());
        menuPane.setMaxHeight(gameWindow.getHeight());
        menuPane.getStyleClass().add("menu-background");
        root.getChildren().add(menuPane);

        var mainPane = new BorderPane();
        menuPane.getChildren().add(mainPane);

        //Awful title
        var title = new Text("TetrECS");
        title.getStyleClass().add("title");
        mainPane.setTop(title);

        //For now, let us just add a button that starts the game. I'm sure you'll do something way better.
        var button = new Button("Play");
        var exit =  new Button("exit");
        var instruction = new Button("instruction");
        VBox vBox = new VBox(15);
        vBox.setPadding(new Insets(15, 5, 5, 5));
        vBox.getChildren().add(button);
        vBox.getChildren().add(instruction);
        vBox.getChildren().add(exit);
        mainPane.setCenter(vBox);

        //Bind the button action to the startGame method in the menu
        button.setOnAction(this::startGame);
        exit.setOnAction(this::exitGame);
        instruction.setOnAction(this::instruction);
    }

    /**
     * Initialise the menu
     */
    @Override
    public void initialise() {

    }

    /**
     * Handle when the Start Game button is pressed
     * @param event event
     */
    private void startGame(ActionEvent event) {

        gameWindow.startChallenge();
    }

    private void exitGame(ActionEvent event){
        App.getInstance().shutdown();
    }

    private void instruction(ActionEvent event){
        gameWindow.startInstruction();
    }

}

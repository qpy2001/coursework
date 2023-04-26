package uk.ac.soton.comp1206.scene;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

public class InstructionScene extends BaseScene{
    private static final Logger logger = LogManager.getLogger(MenuScene.class);

    public InstructionScene(GameWindow gameWindow){
        super(gameWindow);
        logger.info("Creating Menu Scene");
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var menuPane = new StackPane();
        menuPane.setMaxWidth(gameWindow.getWidth());
        menuPane.setMaxHeight(gameWindow.getHeight());
        menuPane.getStyleClass().add("instructions-background");
        root.getChildren().add(menuPane);

        var mainPane = new BorderPane();
        menuPane.getChildren().add(mainPane);

        Button toMenu = new Button("turn to menu");
        toMenu.setOnAction(this::loadMenu);
        mainPane.setTop(toMenu);

    }


    private void loadMenu(ActionEvent event) {
        gameWindow.startMenu();
    }
}

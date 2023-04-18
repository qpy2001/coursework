package uk.ac.soton.comp1206.scene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    protected Game game;

    /**
     * Create a new Single Player challenge scene
     * @param gameWindow the Game Window
     */
    public ChallengeScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Challenge Scene");
    }


    /**
     * Build the Challenge window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        setupGame();

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var challengePane = new StackPane();
        challengePane.setMaxWidth(gameWindow.getWidth());
        challengePane.setMaxHeight(gameWindow.getHeight());
        challengePane.getStyleClass().add("menu-background");
        root.getChildren().add(challengePane);

        var mainPane = new BorderPane();
        challengePane.getChildren().add(mainPane);




        //For now, let us just add a button that starts the game. I'm sure you'll do something way better.
        var freshButton = new Button("fresh");
        var restartButton = new Button("restart");
        var scoreButton = new Button("see scores");


        Label lives=new Label("lives: "+ game.getLives());
        Label grades = new Label("scores: "+ game.getGrades());
        Label highestScores = new Label("The highest current score is: " + game.getHighest());

        //绑定点击刷新事件
        EventHandler eh = a -> {
            lives.setText("lives: "+ game.getLives());
            grades.setText("scores: "+ game.getGrades());
        };
        root.setOnMouseClicked(eh);

        //刷新piece
        EventHandler ehButton = a -> {
            this.fresh();
            lives.setText("lives: "+ game.getLives());
            grades.setText("scores: "+ game.getGrades());
        };
        freshButton.setOnAction(ehButton);

        //重启游戏
        EventHandler ehButtonRestart = a -> {
            this.restart();
            lives.setText("lives: "+ game.getLives());
            grades.setText("scores: "+ game.getGrades());
        };
        restartButton.setOnAction(ehButtonRestart);

        //打开分数面板
        EventHandler ehButtonScore = a -> {
            this.score();
        };
        scoreButton.setOnAction(ehButtonScore);

        //按钮布局
        HBox hBox = new HBox(6);     //首先创建一个HBox对象
        hBox.setStyle("-fx-background-color: gold");

        //然后，在面板上添加6个按钮，代码如下：
        hBox.getChildren().add(freshButton);
        hBox.getChildren().add(restartButton);
        hBox.getChildren().add(scoreButton);
        hBox.getChildren().add(lives);
        hBox.getChildren().add(grades);
        hBox.getChildren().add(highestScores);

        mainPane.setBottom(hBox);



        //5*5的大界面
        var board = new GameBoard(game.getGrid(),gameWindow.getWidth()/2,gameWindow.getWidth()/2);
        mainPane.setLeft(board);


        //两个3*3的选择小界面
        var choiceBoard = new GameBoard(game.getChoiceGrid(),gameWindow.getWidth()/5,gameWindow.getWidth()/5);
        var choiceBoardNext = new GameBoard(game.getNextChoiceGrid(),gameWindow.getWidth()/7,gameWindow.getWidth()/7);
        mainPane.setCenter(choiceBoard);
        mainPane.setRight(choiceBoardNext);

        //Handle block on gameboard grid being clicked
        board.setOnBlockClick(this::blockClickedWithShape);
        choiceBoard.setOnBlockClick(this::blockClickedWithRotate);
        choiceBoardNext.setOnBlockClick(this::blockClickedWithSwap);

    }

    /**
     * Handle when a block is clicked
     * @param gameBlock the Game Block that was clocked
     */
    private void blockClicked(GameBlock gameBlock) {
        game.blockClicked(gameBlock);
    }

    /**
     * 旋转这个game piece
     * @param gameBlock the Game Block that was clocked
     */
    private void blockClickedWithRotate(GameBlock gameBlock) {
        game.blockClickedWithRotate(gameBlock);
    }

    /**
     * 交换当前game piece和下一个
     * @param gameBlock the Game Block that was clocked
     */
    private void blockClickedWithSwap(GameBlock gameBlock){
        game.blockClickedWithSwap(gameBlock);
    }

    private void blockClickedWithShape(GameBlock gameBlock) {
        game.blockClickedWithShape(gameBlock);
    }

    private void fresh() {
        game.fresh();
    }

    private void restart(){
        game.restart();
    }

    private void score(){
        gameWindow.startScore();
    }

    /**
     * Setup the game object and model
     */
    public void setupGame() {
        logger.info("Starting a new challenge");

        //Start new game
        game = new Game(5, 5, 3,3);
    }

    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        logger.info("Initialising Challenge");
        game.start();
    }

}

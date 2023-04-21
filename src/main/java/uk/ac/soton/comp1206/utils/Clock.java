package uk.ac.soton.comp1206.utils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GameWindow;

public class Clock{

    private Timeline animation;
    private String S = "left time: 10s";
    private int tmp = 10;

    GameWindow gameWindow;
    Game game;
    Label label = new Label(S);



    public Clock(GameWindow gameWindow, Game game) {
        this.game = game;
        this.gameWindow = gameWindow;
        label.setFont(javafx.scene.text.Font.font(20));
        animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> timelabel()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    public void timelabel() {
        tmp--;
        if (tmp == -1){
            animation.stop();
            game.fresh();
        }
        S = tmp + "";
        label.setText("left time: "+S+" s");
    }

    public Label getLabel(){
        return label;
    }

    public void freshTime(){
        animation.play();
        tmp = 10;
    }

    public void overAndSeeScores(){
        animation.stop();
        gameWindow.startScore();
    }

}

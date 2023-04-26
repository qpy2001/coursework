package uk.ac.soton.comp1206.game;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.utils.Clock;
import uk.ac.soton.comp1206.utils.TxtUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * The Game class handles the main logic, state and properties of the TetrECS game. Methods to manipulate the game state
 * and to handle actions made by the player should take place inside this class.
 */
public class Game {

    private static final Logger logger = LogManager.getLogger(Game.class);

    /**
     * Number of rows
     */
    protected final int rows;

    /**
     * Number of columns
     */
    protected final int cols;

    /**
     * The grid model linked to the game
     */
    protected final Grid grid;

    protected final Grid choiceGrid;

    protected final Grid nextChoiceGrid;

    /**
     * The first gamePiece grid linked to the game;
     */
    protected GamePiece gamePiece;

    /**
     * The second gamePiece grid linked to the game;
     */
    protected GamePiece gamePiece2;

    protected Integer grades = 0;

    protected Integer lives = 3;

    protected Random random = new Random();

    public Clock clock;

    MediaPlayer mp3Sound;



    /**
     * Create a new game with the specified rows and columns. Creates a corresponding grid model.
     * @param cols number of columns
     * @param rows number of rows
     */
    public Game(int cols, int rows, int pieceCols, int pieceRows) {
        this.cols = cols;
        this.rows = rows;

        //Create a new grid model to represent the game state
        this.grid = new Grid(cols,rows);
        this.choiceGrid = new Grid(pieceCols,pieceRows);
        this.nextChoiceGrid = new Grid(pieceCols,pieceRows);

        int n = random.nextInt(15);
        //Create first new game piece model to represent the game piece state
        this.gamePiece = GamePiece.createPiece(n);
        this.choiceGrid.setGrid(gamePiece.getBlocks());

        //Create second new game piece model to represent the game piece state
        this.gamePiece2 = GamePiece.createPiece(getDiffNumIn15(n));
        this.nextChoiceGrid.setGrid(gamePiece2.getBlocks());


    }

    /**
     * Start the game
     */
    public void start() {
        logger.info("Starting game");
        initialiseGame();
    }

    public void restart(){
        logger.info("reStarting game");
        for(int i=0;i<cols;i++){
            for(int j=0;j<rows;j++){
                this.grid.set(i,j,0);
            }
        }
        int n = random.nextInt(15);
        //Create first new game piece model to represent the game piece state
        this.gamePiece = GamePiece.createPiece(n);
        this.choiceGrid.setGrid(gamePiece.getBlocks());

        //Create second new game piece model to represent the game piece state
        this.gamePiece2 = GamePiece.createPiece(getDiffNumIn15(n));
        this.nextChoiceGrid.setGrid(gamePiece2.getBlocks());

        this.grades = 0;
        this.lives = 3;

    }

    /**
     * Initialise a new game and set up anything that needs to be done at the start
     */
    public void initialiseGame() {
        logger.info("Initialising game");
    }

    /**
     * Handle what should happen when a particular block is clicked
     * @param gameBlock the block that was clicked
     */
    public void blockClicked(GameBlock gameBlock) {
        //Get the position of this block
        int x = gameBlock.getX();
        int y = gameBlock.getY();

        //Get the new value for this block
        int previousValue = grid.get(x,y);
        int newValue = previousValue + 1;
        if (newValue  > GamePiece.PIECES) {
            newValue = 0;
        }

        //Update the grid with the new value
        grid.set(x,y,newValue);
    }

    /**
     * Handle what should happen when a particular block is clicked
     * @param gameBlock the block that was clicked
     */
    public void blockClickedWithShape(GameBlock gameBlock) {
        //Get the position of this block
        int x = gameBlock.getX();
        int y = gameBlock.getY();

        //add the new shape for this block
        if(ableToBePlaced(x,y,gamePiece)){
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(gamePiece.getBlocks()[i][j] == gamePiece.getValue()){
                        grid.set(x+i-1,y+j-1,gamePiece.getValue());
                    }
                }
            }
        }
        else{
            System.out.println("can not be placed, your mouse is in the center of the choice");
            return;
        }

        //统计分数并且清除Grid当中的整行整列
        getGradesAndClearBlocks();

        //produce new gamePiece and swap the gamePiece and gamePiece2 in the block
        int pieceNum = gamePiece.getValue();
        gamePiece = gamePiece2;
        gamePiece2 = GamePiece.createPiece(getDiffNumIn15(pieceNum));

        choiceGrid.setGrid(gamePiece.getBlocks());
        nextChoiceGrid.setGrid(gamePiece2.getBlocks());


    }

    public void blockClickedWithRotate(GameBlock gameBlock){
        gamePiece.rotate();
        playSound("src/main/resources/sounds/rotate.wav");
        choiceGrid.setGrid(gamePiece.getBlocks());
    }

    public void blockClickedWithSwap(GameBlock gameBlock){
        GamePiece temp = gamePiece;
        gamePiece = gamePiece2;
        gamePiece2 = temp;
        choiceGrid.setGrid(gamePiece.getBlocks());
        nextChoiceGrid.setGrid(gamePiece2.getBlocks());
    }

    public Boolean ableToBePlaced(int x, int y,GamePiece gamePiece){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(gamePiece.getBlocks()[i][j] == 0 ){
                    continue;
                }
                if(!isInGrid(x+i-1,y+j-1)){
                    return false;
                }
                if(grid.get(x+i-1,y+j-1) != 0){
                    return false;
                }
            }
        }
        clock.freshTime();
        return true;
    }

    //统计分数并且清除Grid当中的整行整列
    private void getGradesAndClearBlocks(){
        int count = 0;
        int[] rows = {0,0,0,0,0};
        int[] cols = {0,0,0,0,0};

        //统计行和列情况
        for(int i=0;i<5;i++){
            if(grid.get(i,0)!=0 && grid.get(i,1)!=0 && grid.get(i,2)!=0 && grid.get(i,3)!=0 && grid.get(i,4)!=0){
                count++;
                rows[i] = 1;
            }
        }
        for(int i=0;i<5;i++){
            if(grid.get(0,i)!=0 && grid.get(1,i)!=0 && grid.get(2,i)!=0 && grid.get(3,i)!=0 && grid.get(4,i)!=0){
                count++;
                cols[i] = 1;
            }
        }

        if(count != 0){
            playSound("src/main/resources/sounds/clear.wav");
        }

        //计分
        if(count == 0){
            System.out.println("grades: "+grades);
            return;
        } else if(count == 1 ) { //有一行、列满的时候，一行一列值50
            grades += 50;
        }else if(count == 2){//有2行、列满的时候，一行一列值70，140
            grades += 140;
        }else if(count == 3){//有3行、列满的时候，一行一列值90,270
            grades += 270;
        }else if(count == 4){
            grades += 440;
        }else {
            grades += 650;
        }
        System.out.println("grades："+grades);

        //清除行和列
        for(int i=0;i< 5;i++){
            if(rows[i] == 1){
                for(int j=0;j<5;j++){
                    grid.set(i,j,16);
                }
            }

            if(cols[i] == 1){
                for(int j=0;j<5;j++){
                    grid.set(j,i,16);
                }
            }
        }

        new Thread(){
            public void run(){
                try {
                    Thread.sleep(200);
                    //清除行和列
                    for(int i=0;i< 5;i++){
                        if(rows[i] == 1){
                            for(int j=0;j<5;j++){
                                grid.set(i,j,0);
                            }
                        }

                        if(cols[i] == 1){
                            for(int j=0;j<5;j++){
                                grid.set(j,i,0);
                            }
                        }
                    }
                } catch (InterruptedException e) { }

            }

        }.start();

    }

    public void fresh(){
        if(lives == 0){
            System.out.println("you have "+ lives +" lives left");
            TxtUtils.writeTxt("src/main/scores.txt","localPlayer "+grades);
            clock.overAndSeeScores();
            return;
        }
        //Create first new game piece model to represent the game piece state
        this.gamePiece = GamePiece.createPiece(0);
        this.choiceGrid.setGrid(gamePiece.getBlocks());

        //Create second new game piece model to represent the game piece state
        this.gamePiece2 = GamePiece.createPiece(14);
        this.nextChoiceGrid.setGrid(gamePiece2.getBlocks());

        lives --;
        clock.freshTime();
        System.out.println("you have "+ lives +" lives left");
    }

    private boolean isInGrid(int x, int y){
        return x>=0 && x< grid.getCols()  && y>=0 && y<grid.getRows();
    }

    private int getDiffNumIn15(int n){
        int res = random.nextInt(15);
        if(res == n){
            res = (n+1)%15;
        }
        return res;
    }

    /**
     * Get the grid model inside this game representing the game state of the board
     * @return game grid model
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Get the number of columns in this game
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in this game
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    public Grid getChoiceGrid(){
        return choiceGrid;
    }

    public Grid getNextChoiceGrid(){
        return nextChoiceGrid;
    }

    public int getLives(){
        return lives;
    }

    public int getGrades(){
        return grades;
    }

    public String getHighest(){return TxtUtils.getMaxScores("src/main/scores.txt");}

    public Clock getClock(GameWindow gameWindow, Game game){
            clock = new Clock(gameWindow, game);
         return clock;
    }

    public Clock getClock(){
        return clock;
    }

    public void playSound(String type){
        //打开歌曲文件，此时采用相对路径，即本工程目录下
        File file1 = new File(type);
        URI uri1 = file1.toURI();		//先转成URI
        URL url1 = null;		//再转为URL
        try {
            url1 = uri1.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String Mp3play1 = url1.toString();	//将URL转换为字符串

//Media为了表示一个文件，MediaPlayer则为了将这个文件播放
        Media media1 = new Media(Mp3play1);	//新建一个Media，使它与得到的URL相关
        if(mp3Sound!= null){
            mp3Sound.stop();
        }
        mp3Sound = new MediaPlayer(media1);	//将media1传与新建的MediaPlayer
//然后播放
        mp3Sound.play();
    }


}

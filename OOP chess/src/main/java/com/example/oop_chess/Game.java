package com.example.oop_chess;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


import static com.example.oop_chess.Move.*;
import static com.example.oop_chess.Save_And_Load.obj;


public class Game implements Game_Handling {

    public static Piece currentPiece;
    public static String currentPlayer;
    public static ChessBoard chessBoard;
    private final boolean game;
    static Timer whiteTimer;
    static Timer blackTimer;
    private final Label whiteTimerLabel;
    private final Label blackTimerLabel;
    static int timer;
    static boolean casual;
    public static boolean loaded;
    static Label whiteMoveLabel;
    static Label blackMoveLabel;
    public static boolean isBot = false;
    public static Piece lastPiece = null;
    public static boolean wait = false;
    public static boolean check = false;
    public static int turn = 0;
    public static AnchorPane endGame;
    public static Label checkmateLabel;
    public static Label timeoutLabel;
    public static Label blackVictoryLabel;
    public static Label whiteVictoryLabel;
    public static Label drawLabel;
    public static Label stalemateLabel;


    public Game(GridPane chessBoard, Label whiteTimerLabel, Label blackTimerLabel ,boolean load,Label whiteMoveLabel,Label blackMoveLabel , boolean ai, AnchorPane gameEnd, Label wVictoryLabel, Label bVictoryLabel, Label timeendLabel, Label mateLabel, Label staleLabel, Label drawlabel) {
        Game.chessBoard = new ChessBoard(chessBoard);
        currentPiece = null;
        if(!load) {
            currentPlayer = "Black";
        }
        this.game = true;
        whiteTimer = new Timer();
        blackTimer = new Timer();
        Game.endGame = gameEnd;
        Game.blackVictoryLabel = bVictoryLabel;
        Game.whiteVictoryLabel = wVictoryLabel;
        Game.timeoutLabel = timeendLabel;
        Game.checkmateLabel = mateLabel;
        Game.stalemateLabel = staleLabel;
        Game.drawLabel = drawlabel;
        if(ai)isBot = true;
        this.blackTimerLabel = blackTimerLabel;
        this.whiteTimerLabel = whiteTimerLabel;
        Game.whiteMoveLabel = whiteMoveLabel;
        Game.blackMoveLabel = blackMoveLabel;
        whiteMoveLabel.setText(whiteNewMove);
        blackMoveLabel.setText(blackNewMove);
        this.loaded = load;
        if (!casual) {
            whiteTimer.start();
            blackTimer.start();
            startTimerThread();
        }



        Game_Handling.changePlayer();

        addEventHandlers(Game.chessBoard.chessBoard);

        if (load) {
            // Additional logic after loading the game can go here if needed
            Platform.runLater(Move::loadGame);
            currentPlayer = obj.player;
        }


    }

    private void updateTimerLabel(Label timerLabel, int elapsedTime, int time) {

        int minutes = 0;
        int seconds = 0;
        if (loaded) {
            minutes = (timer - (elapsedTime+time)) / 60;
            seconds = (timer - (elapsedTime+time)) % 60;
        }else{
            minutes = (timer - (elapsedTime)) / 60;
            seconds = (timer - (elapsedTime)) % 60;
        }
        timerLabel.setText(String.format("%2d:%02d", minutes, seconds));
    }


    private void startTimerThread() {
        Thread timerThread = new Thread(() -> {
            while (game) {
                try {
                    Thread.sleep(1000); // Wait for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Update the labels on the JavaFX Application Thread
                Platform.runLater(() -> updateTimerLabel(whiteTimerLabel, (int) whiteTimer.getElapsed(),obj.timeWhite));
                Platform.runLater(() -> updateTimerLabel(blackTimerLabel, (int) blackTimer.getElapsed(),obj.timeBlack));
                obj.updateTimer((int) whiteTimer.getElapsed()+obj.timeWhite,(int) blackTimer.getElapsed()+obj.timeBlack);


                if (whiteTimer.getElapsed()+obj.timeWhite >= timer && !casual) {
                    whiteTimer.stop();
                    blackTimer.stop();
                    endGame.setVisible(true);
                    blackVictoryLabel.setVisible(true);
                    timeoutLabel.setVisible(true);

                } else if (blackTimer.getElapsed()+obj.timeBlack >= timer && !casual) {
                    whiteTimer.stop();
                    blackTimer.stop();
                    endGame.setVisible(true);
                    whiteVictoryLabel.setVisible(true);
                    timeoutLabel.setVisible(true);

                }
            }
        });

        timerThread.setDaemon(true); // Make the thread a daemon to exit with the application
        timerThread.start();
    }
    public void clear() {
        chessBoard.clearBoard();
        chessBoard = null;
        currentPiece = null;
        currentPlayer = null;
        startTimerThread();
        whiteTimer = new Timer();
        blackTimer = new Timer();
        blackMoveLabel.setText("");
        whiteMoveLabel.setText("");
        whiteMovesHistory.clear();
        blackMovesHistory.clear();
        Move.blackNewMove = "";
        Move.whiteNewMove = "";
    }
    public static void stopTimer() {
        try {
            whiteTimer.stop();
            blackTimer.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.exit(0);

    }
}
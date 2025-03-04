package com.example.oop_chess;

import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

import static com.example.oop_chess.Advanced_Techniques.*;
import static com.example.oop_chess.Game.*;
import static com.example.oop_chess.Game_Handling.*;
import static com.example.oop_chess.Move.moveHistory;
import static com.example.oop_chess.Undo.undoLastMove;

public class Movements implements Game_Handling {


    public static void dropPiece(Square square) {
        if (!Move.normalMove(square)) return;


        // Check for pawn promotion after moving
        if(currentPiece instanceof Pawn) ((Pawn) currentPiece).lastMovedTurn = turn;// Check if the current piece is a pawn and if it has reached the last rank
        if (currentPiece instanceof Pawn && readyForPromotion((Pawn) currentPiece)) {
            wait = true;
            waitForPromotion((Pawn) currentPiece);
            currentPiece.setEffect(null);
            currentPiece.showAllPossibleMoves(false);
            return; // Pause further execution until promotion is complete
        }



        Visuals.deselectPiece();
        check = false;

        if (currentPlayer.equals("white")) {
            if(Checkmate.kingInCheck("black")) {

                check = true;
                lastPiece = moveHistory.get(moveHistory.size() - 1).piece;
                if(Checkmate.isCheckmate("black")){
                    whiteTimer.stop();
                    blackTimer.stop();
                    endGame.setVisible(true);
                    whiteVictoryLabel.setVisible(true);
                    checkmateLabel.setVisible(true);
                    String musicFile = "src/main/resources/game-end.wav";     // For example

                    Media sound3 = new Media(new File(musicFile).toURI().toString());
                    MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
                    mediaPlayer3.play();
                }
            }

        } else {
            if(Checkmate.kingInCheck("white")) {

                check = true;
                lastPiece = moveHistory.get(moveHistory.size() - 1).piece;
                if(Checkmate.isCheckmate("white")){
                    whiteTimer.stop();
                    blackTimer.stop();
                    endGame.setVisible(true);
                    blackVictoryLabel.setVisible(true);
                    checkmateLabel.setVisible(true);
                    String musicFile = "src/main/resources/game-end.wav";     // For example

                    Media sound3 = new Media(new File(musicFile).toURI().toString());
                    MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
                    mediaPlayer3.play();
                }
            }

        }
        if (currentPlayer.equals("white")) {
            if (Checkmate.kingInCheck("black")) {
                check = true;
                lastPiece = moveHistory.get(moveHistory.size() - 1).piece;

                if (Checkmate.isCheckmate("black")) {
                    whiteTimer.stop();
                    blackTimer.stop();
                    endGame.setVisible(true);
                    whiteVictoryLabel.setVisible(true);
                    checkmateLabel.setVisible(true);
                    String musicFile = "src/main/resources/game-end.wav";     // For example

                    Media sound3 = new Media(new File(musicFile).toURI().toString());
                    MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
                    mediaPlayer3.play();
                }
            } else if (Checkmate.isStalemate("black")) { // Check for stalemate
                whiteTimer.stop();
                blackTimer.stop();
                endGame.setVisible(true);
                stalemateLabel.setVisible(true); // Make a stalemate label visible
                drawLabel.setVisible(true);
                String musicFile = "src/main/resources/game-end.wav";     // For example

                Media sound3 = new Media(new File(musicFile).toURI().toString());
                MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
                mediaPlayer3.play();
                System.out.println("Stalemate! The game is a draw.");
            }
        } else {
            if (Checkmate.kingInCheck("white")) {
                check = true;
                lastPiece = moveHistory.get(moveHistory.size() - 1).piece;

                if (Checkmate.isCheckmate("white")) {
                    whiteTimer.stop();
                    blackTimer.stop();
                    endGame.setVisible(true);
                    blackVictoryLabel.setVisible(true);
                    checkmateLabel.setVisible(true);
                    String musicFile = "src/main/resources/game-end.wav";     // For example

                    Media sound3 = new Media(new File(musicFile).toURI().toString());
                    MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
                    mediaPlayer3.play();
                }
            } else if (Checkmate.isStalemate("white")) { // Check for stalemate
                whiteTimer.stop();
                blackTimer.stop();
                endGame.setVisible(true);
                stalemateLabel.setVisible(true); // Make a stalemate label visible
                drawLabel.setVisible(true);
                String musicFile = "src/main/resources/game-end.wav";     // For example

                Media sound3 = new Media(new File(musicFile).toURI().toString());
                MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
                mediaPlayer3.play();
                System.out.println("Stalemate! The game is a draw.");
            }
        }


        // Deselect the current piece and switch the player


        changePlayer();


    }

    public static void killPiece(Square square) {
        Piece killedPiece = (Piece) square.getChildren().get(0);
        if (!Move.killMove(square, killedPiece)) return;

        // Handle game over if the killed piece is the King
        if (killedPiece.type.equals("King")) {
            whiteTimer.stop();
            blackTimer.stop();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("WHOAAAAAAAAAAAAAH");
            alert.setHeaderText("THIS SHOULDN'T HAVE HAPPENED");
            alert.setContentText("Press OK to exit.");
            alert.showAndWait();
            System.exit(0);
        }

        if (currentPiece instanceof Pawn && readyForPromotion((Pawn) currentPiece)) {
            wait = true;
            waitForPromotion((Pawn) currentPiece);
            return; // Pause further execution until promotion is complete
        }

        Visuals.deselectPiece();

        check = false;

        if (currentPlayer.equals("white")) {
            if(Checkmate.kingInCheck("white")) {
                System.out.println("wrong move");
                undoLastMove();
            }
            if(Checkmate.kingInCheck("black")) {

                check = true;
                lastPiece = moveHistory.get(moveHistory.size() - 1).piece;
                if(Checkmate.isCheckmate("black")){
                    whiteTimer.stop();
                    blackTimer.stop();
                    endGame.setVisible(true);
                    whiteVictoryLabel.setVisible(true);
                    checkmateLabel.setVisible(true);
                    String musicFile = "src/main/resources/game-end.wav";     // For example

                    Media sound3 = new Media(new File(musicFile).toURI().toString());
                    MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
                    mediaPlayer3.play();
                }
            }

        } else {
            if(Checkmate.kingInCheck("black")) {
                System.out.println("wrong move");
                undoLastMove();
            }
            if(Checkmate.kingInCheck("white")) {

                check = true;
                lastPiece = moveHistory.get(moveHistory.size() - 1).piece;
                if(Checkmate.isCheckmate("white")){
                    whiteTimer.stop();
                    blackTimer.stop();
                    endGame.setVisible(true);
                    blackVictoryLabel.setVisible(true);
                    checkmateLabel.setVisible(true);
                    String musicFile = "src/main/resources/game-end.wav";     // For example

                    Media sound3 = new Media(new File(musicFile).toURI().toString());
                    MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
                    mediaPlayer3.play();
                }
            }

        }

        // Deselect the current piece and switch the player

        changePlayer();
    }


}
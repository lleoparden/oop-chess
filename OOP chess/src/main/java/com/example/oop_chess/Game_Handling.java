package com.example.oop_chess;

import javafx.event.EventTarget;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import java.io.File;

import static com.example.oop_chess.Advanced_Techniques.*;
import static com.example.oop_chess.Game.*;
import static com.example.oop_chess.Move.*;
import static com.example.oop_chess.Movements.dropPiece;
import static com.example.oop_chess.Movements.killPiece;
import static com.example.oop_chess.Piece.getPieceByName;
import static com.example.oop_chess.Save_And_Load.obj;

interface Game_Handling extends Visuals {


    default void addEventHandlers(GridPane chessBoard) {


        chessBoard.setOnMouseClicked(event -> {
            if(loaded){
                currentPlayer = obj.player;
                changePlayer();
                loaded = false;
            }
            if (!wait) {
                EventTarget target = event.getTarget();


                // Clicked on square
                if (target instanceof Square square) {

                    if (currentPiece instanceof King) {
                        if (!Advanced_Techniques.castling(square, currentPiece)) return;
                    }

                    if (currentPiece instanceof Pawn) {
                        if (!Advanced_Techniques.enPassant((Pawn) currentPiece, square)) return;
                    }

                    if (square.occupied) {
                        Piece newPiece = (Piece) square.getChildren().get(0);
                        // Selecting a new piece
                        if (currentPiece == null) {
                            currentPiece = newPiece;
                            if (!currentPiece.getColor().equals(currentPlayer)) {
                                currentPiece = null;
                                return;
                            }
                            selectPiece();
                        }
                        // Selecting other piece of same color || Killing a piece
                        else {
                            if (currentPiece.color.equals(newPiece.color)) {
                                Visuals.deselectPiece();
                                currentPiece = newPiece;
                                selectPiece();
                            } else {
                                killPiece(square);
                            }
                        }
                    }
                    // Dropping a piece on blank square
                    else {
                        if (currentPiece == null) return;
                        dropPiece(square);
                    }
                }
                // Clicked on piece
                else {
                    if (!(target instanceof Piece) && !(target instanceof Square)) return;
                    Piece newPiece = (Piece) target;
                    Square square = (Square) newPiece.getParent();

                    // Selecting a new piece
                    if (currentPiece == null) {
                        currentPiece = newPiece;
                        if (!currentPiece.getColor().equals(currentPlayer)) {
                            currentPiece = null;
                            return;
                        }
                        selectPiece();
                    }
                    // Selecting other piece of same color || Killing a piece
                    else {
                        if (currentPiece.color.equals(newPiece.color)) {
                            Visuals.deselectPiece();
                            currentPiece = newPiece;
                            selectPiece();
                        } else {
                            killPiece(square);
                        }
                    }

                }
            }
        });

    }

    static void changePlayer() {
        Game.turn++;
        if (currentPlayer.equals("white")) {
            currentPlayer = "black";
            whiteTimer.pause();
            blackTimer.resume();
        } else {
            currentPlayer = "white";
            blackTimer.pause();
            whiteTimer.resume();
        }


        // Trigger the bot if it's the bot's turn
        if ((currentPlayer.equals("black") && isBot)) {
            Game_Handling.botPlay();
        }
    }

    static void botPlay() {
        ChessBoard board = chessBoard; // Get the current chessboard instance
        boolean isBotMaximizing = currentPlayer.equals("White"); // Bot maximizes if it's White's turn

        System.out.println("Bot is thinking...");

        // Find the best move using Minimax
        Move bestMove = Minimax.findBestMove(board, isBotMaximizing);

        if (bestMove != null) {
            // Execute the best move
            boolean moved = Piece.movePiece(bestMove.piece, bestMove.to);

            if (!moved) Piece.capturePiece(bestMove.piece, bestMove.to);


            // Record the move in history
            moveHistory.add(bestMove);

            // Update the move label
            updateMoveLabel();

            if (bestMove.piece instanceof Pawn && readyForPromotion((Pawn) bestMove.piece)) {
                Square square = bestMove.to;
                promoteToQueen((Pawn) bestMove.piece);
                lastPiece = getPieceByName(square.name);
                // Hide the promotion button after promotion is complete
                blackPromotionPanel.setVisible(false);

                // Resume the game flow
                wait = false;// Pause further execution until promotion is complete
            }

            // Check if the game is over
            check = false;

            if (currentPlayer.equals("white")) {
                if(Checkmate.kingInCheck("black")) {

                    check = true;
                    lastPiece = moveHistory.get(moveHistory.size() - 1).piece;
                    if(Checkmate.isCheckmate("black")){
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

            // Change the turn to the other player
            Game_Handling.changePlayer();

        } else {
            if (Checkmate.isStalemate("black")) {
                endGame.setVisible(true);
                stalemateLabel.setVisible(true);
                drawLabel.setVisible(true);
                String musicFile = "src/main/resources/game-end.wav";     // For example

                Media sound3 = new Media(new File(musicFile).toURI().toString());
                MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
                mediaPlayer3.play();
            }
        }
    }


}
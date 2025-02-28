package com.example.oop_chess;


import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.util.Objects;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


import static com.example.oop_chess.Game.*;
import static com.example.oop_chess.Game_Handling.changePlayer;
import static com.example.oop_chess.Move.*;
import static com.example.oop_chess.Movements.dropPiece;
import static com.example.oop_chess.Undo.undoLastMove;

public class Advanced_Techniques {
    public static Button whiteQueenPromotionButton;
    public static Button whiteRookPromotionButton;
    public static Button whiteBishopPromotionButton;
    public static Button whiteKnightPromotionButton;
    public static Button blackQueenPromotionButton;
    public static Button blackRookPromotionButton;
    public static Button blackBishopPromotionButton;
    public static Button blackKnightPromotionButton;
    public static AnchorPane whitePromotionPanel;
    public static AnchorPane blackPromotionPanel;

    public static Piece promotedPiece;

    static Move move;

    Advanced_Techniques(Button Wqueen, Button Wrook, Button Wbishop, Button Wknight, Button Bqueen, Button Brook, Button Bbishop, Button Bknight, AnchorPane whitePromotion, AnchorPane blackPromotion) {
        whiteQueenPromotionButton = Wqueen;
        whiteRookPromotionButton = Wrook;
        whiteBishopPromotionButton = Wbishop;
        whiteKnightPromotionButton = Wknight;
        blackQueenPromotionButton = Bqueen;
        blackRookPromotionButton = Brook;
        blackBishopPromotionButton = Bbishop;
        blackKnightPromotionButton = Bknight;
        whitePromotionPanel = whitePromotion;
        blackPromotionPanel = blackPromotion;
    }

    public static void promotionLabelUpdate(char letter, Pawn pawn) {


        move = moveHistory.get(moveHistory.size() - 1);
        Move promotionMove = new Move(move.player, move.from, move.to,(Pawn) move.piece,promotedPiece);
        moveHistory.add(promotionMove);


        if (pawn.color.equals("white")) {
            whiteMovesHistory.remove(whiteMovesHistory.size() - 1);
            if (!Objects.equals(move.action, "capture"))
                whiteMovesHistory.add(move.piece.letter + convertToChessNotation(move.to) + "=" + letter);
            else whiteMovesHistory.add("x" + move.piece.letter + convertToChessNotation(move.to) + "=" + letter);
            whiteNewMove = String.join("\n", whiteMovesHistory);
            Game.whiteMoveLabel.setText(whiteNewMove);
        } else {
            blackMovesHistory.remove(blackMovesHistory.size() - 1);
            if (!Objects.equals(move.action, "capture"))
                blackMovesHistory.add(move.piece.letter + convertToChessNotation(move.to) + "=" + letter);
            else
                blackMovesHistory.add("x" + move.piece.letter + convertToChessNotation(move.to) + "=" + letter);
            blackNewMove = String.join("\n", blackMovesHistory);
            Game.blackMoveLabel.setText(blackNewMove);
        }
    }

    public static boolean readyForPromotion(Pawn pawn) {
        if (pawn.color.equals("white") && pawn.posY == 0) {
            whitePromotionPanel.setVisible(true);
            return true;
        } else if (pawn.color.equals("black") && pawn.posY == 7) {
            blackPromotionPanel.setVisible(true);
            return true;
        }
        return false;
    }

    public static void promoteToQueen(Pawn pawn) {
        promotedPiece = new Queen(pawn.color, pawn.posX, pawn.posY);
        replacePawnWithPiece(pawn, promotedPiece);
        promotionLabelUpdate('Q', pawn);
    }

    public static void promoteToRook(Pawn pawn) {
        promotedPiece = new Rook(pawn.color, pawn.posX, pawn.posY);
        replacePawnWithPiece(pawn,promotedPiece);
        promotionLabelUpdate('R', pawn);
    }

    public static void promoteToKnight(Pawn pawn) {
        promotedPiece = new Knight(pawn.color, pawn.posX, pawn.posY);
        replacePawnWithPiece(pawn,promotedPiece);
        promotionLabelUpdate('N', pawn);
    }

    public static void promoteToBishop(Pawn pawn) {
        promotedPiece = new Bishop(pawn.color, pawn.posX, pawn.posY);
        replacePawnWithPiece(pawn, promotedPiece);
        promotionLabelUpdate('B', pawn);
    }


    public static void replacePawnWithPiece(Pawn pawn, Piece promotedPiece) {

        Square promotionSquare = (Square) pawn.getParent();
        if (promotionSquare != null) {
            promotionSquare.getChildren().remove(pawn);
            promotionSquare.getChildren().add(promotedPiece);
            promotedPiece.posX = pawn.posX;
            promotedPiece.posY = pawn.posY;
        }
    }

    public static void waitForPromotion(Pawn pawn) {




        whiteQueenPromotionButton.setOnAction(event -> {
            promoteToQueen(pawn);



            whitePromotionPanel.setVisible(false);


            wait = false;
            Visuals.deselectPiece();
            changePlayer();
        });

        whiteKnightPromotionButton.setOnAction(event -> {
            promoteToKnight(pawn);


            // Hide the promotion button after promotion is complete
            whitePromotionPanel.setVisible(false);

            // Resume the game flow
            wait = false;
            Visuals.deselectPiece();
            changePlayer();
        });

        whiteBishopPromotionButton.setOnAction(event -> {
            promoteToBishop(pawn);


            // Hide the promotion button after promotion is complete
            whitePromotionPanel.setVisible(false);

            // Resume the game flow
            wait = false;
            Visuals.deselectPiece();
            changePlayer();
        });
        whiteRookPromotionButton.setOnAction(event -> {
            promoteToRook(pawn);


            // Hide the promotion button after promotion is complete
            whitePromotionPanel.setVisible(false);

            // Resume the game flow
            wait = false;
            Visuals.deselectPiece();
            changePlayer();
        });

        blackQueenPromotionButton.setOnAction(event -> {
            promoteToQueen(pawn);


            // Hide the promotion button after promotion is complete
            blackPromotionPanel.setVisible(false);

            // Resume the game flow
            wait = false;
            Visuals.deselectPiece();
            changePlayer();
        });
        blackKnightPromotionButton.setOnAction(event -> {
            promoteToKnight(pawn);


            // Hide the promotion button after promotion is complete
            blackPromotionPanel.setVisible(false);

            // Resume the game flow
            wait = false;
            Visuals.deselectPiece();
            changePlayer();
        });
        blackBishopPromotionButton.setOnAction(event -> {
            promoteToBishop(pawn);


            // Hide the promotion button after promotion is complete
            blackPromotionPanel.setVisible(false);

            // Resume the game flow
            wait = false;
            Visuals.deselectPiece();
            changePlayer();
        });
        blackRookPromotionButton.setOnAction(event -> {
            promoteToRook(pawn);


            // Hide the promotion button after promotion is complete
            blackPromotionPanel.setVisible(false);

            // Resume the game flow
            wait = false;
            Visuals.deselectPiece();
            changePlayer();
        });

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
    }


    public static boolean castling(Square square,Piece currentPiece) {
        if (!check&&currentPiece!=null) {
            if(currentPiece instanceof King) {
                if (square.x == currentPiece.posX+2 &&! ((King) currentPiece).hasMoved()){
                    King king = (King) currentPiece;
                    Square rookSquare = Piece.getSquareByName("Square"+(king.posX+3)+king.posY);
                    Rook rook = (Rook) Objects.requireNonNull(rookSquare).getChildren().get(0);
                    boolean doIt = Objects.requireNonNull(Piece.getSquareByName("Square" + (king.posX + 1) + king.posY)).occupied || rook.hasMoved;

                    if (!rook.hasMoved()) {
                        Piece.movePiece(rook, Objects.requireNonNull(Piece.getSquareByName("Square" + (king.posX + 1) + king.posY)));
                        rook.setHasMoved(true);
                    }
                    if( !doIt){
                            dropPiece(square);
                        moveHistory.remove(moveHistory.size() - 1);
                        moveHistory.add(new Move(square,king,rookSquare,rook));

                        String musicFile = "src/main/resources/castle.wav";
                        if (!muteSound) {
                            Media sound3 = new Media(new File(musicFile).toURI().toString());
                            MediaPlayer mediaPlayer3 = new MediaPlayer(sound3);
                            mediaPlayer3.play();
                        }

                        if (!loading) {
                            if (currentPlayer.equals("black")) {
                                whiteMovesHistory.remove(whiteMovesHistory.size() - 1);
                                whiteMovesHistory.add("0-0");
                                whiteNewMove = String.join("\n", whiteMovesHistory);
                                Game.whiteMoveLabel.setText(whiteNewMove);
                            }
                            else{
                                blackMovesHistory.remove(blackMovesHistory.size() - 1);
                                blackMovesHistory.add("0-0");
                                blackNewMove = String.join("\n", blackMovesHistory);
                                Game.blackMoveLabel.setText(blackNewMove);
                            }
                        }
                    }else {
                        String musicFile = "src/main/resources/error.wav";
                        Media sound4 = new Media(new File(musicFile).toURI().toString());
                        MediaPlayer mediaPlayer4 = new MediaPlayer(sound4);
                        mediaPlayer4.play();
                        return false;
                    }
                }
                else if (square.x == currentPiece.posX-2  &&! ((King) currentPiece).hasMoved() ){
                    King king = (King) currentPiece;
                    Square rookSquare = Piece.getSquareByName("Square"+(king.posX-4)+king.posY);
                    Rook rook = (Rook) Objects.requireNonNull(rookSquare).getChildren().get(0);
                    boolean doIt = (Objects.requireNonNull(Piece.getSquareByName("Square" + (king.posX - 3) + king.posY)).occupied || Objects.requireNonNull(Piece.getSquareByName("Square" + (king.posX - 1) + king.posY)).occupied) || rook.hasMoved;



                    if (!rook.hasMoved()) {
                        Piece.movePiece(rook, Objects.requireNonNull(Piece.getSquareByName("Square" + (king.posX - 1) + king.posY)));
                        rook.setHasMoved(true);
                    }

                    if(!doIt){
                        dropPiece(square);
                        moveHistory.remove(moveHistory.size() - 1);
                        moveHistory.add(new Move(square,king,rookSquare,rook));
                        if (!loading) {
                            if (currentPlayer.equals("black")) {
                                whiteMovesHistory.remove(whiteMovesHistory.size() - 1);
                                whiteMovesHistory.add("0-0-0");
                                whiteNewMove = String.join("\n", whiteMovesHistory);
                                Game.whiteMoveLabel.setText(whiteNewMove);
                            }
                            else{
                                blackMovesHistory.remove(blackMovesHistory.size() - 1);
                                blackMovesHistory.add("0-0-0");
                                blackNewMove = String.join("\n", blackMovesHistory);
                                Game.blackMoveLabel.setText(blackNewMove);
                            }
                        }
                    }else {
                        String musicFile = "src/main/resources/error.wav";
                        Media sound4 = new Media(new File(musicFile).toURI().toString());
                        MediaPlayer mediaPlayer4 = new MediaPlayer(sound4);
                        mediaPlayer4.play();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean enPassant(Pawn pawn, Square square) {
        if(pawn.possibleEnPassantMoves.contains(square.name)) {
            Square currentSquare = (Square) pawn.getParent();
            if(pawn.color.equals("white")) {
                dropPiece(square);
                Square pawnSquare = (Square) Piece.getSquareByName("Square" + square.x + (square.y + 1));
                Pawn enPassantPawn = (Pawn) Objects.requireNonNull(Piece.getSquareByName("Square" + square.x + (square.y + 1))).getChildren().get(0);
                pawnSquare.getChildren().remove(enPassantPawn);
                pawnSquare.occupied = false;
                moveHistory.remove(moveHistory.size() - 1);
                moveHistory.add(new Move(currentPlayer,currentSquare,square,pawn,enPassantPawn,pawnSquare));
                whiteMovesHistory.remove(whiteMovesHistory.size() - 1);
                whiteMovesHistory.add("x" + convertToChessNotation(square));
                whiteNewMove = String.join("\n", whiteMovesHistory);
                Game.whiteMoveLabel.setText(whiteNewMove);
            }else {
                dropPiece(square);
                Square pawnSquare = (Square) Piece.getSquareByName("Square" + square.x + (square.y - 1));
                Pawn enPassantPawn = (Pawn) Objects.requireNonNull(Piece.getSquareByName("Square" + square.x + (square.y - 1))).getChildren().get(0);
                pawnSquare.getChildren().remove(enPassantPawn);
                pawnSquare.occupied = false;
                moveHistory.remove(moveHistory.size() - 1);
                moveHistory.add(new Move(currentPlayer,currentSquare,square,pawn,enPassantPawn,pawnSquare));
                blackMovesHistory.remove(blackMovesHistory.size() - 1);
                blackMovesHistory.add("x" + convertToChessNotation(square));
                blackNewMove = String.join("\n", blackMovesHistory);
                Game.blackMoveLabel.setText(blackNewMove);

            }
            return false;
        }
        return true;
    }






}
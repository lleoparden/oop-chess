package com.example.oop_chess;


import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import static com.example.oop_chess.Advanced_Techniques.castling;
import static com.example.oop_chess.Game.*;
import static com.example.oop_chess.Piece.getSquareByName;
import static com.example.oop_chess.Save_And_Load.obj;


public class Move implements Serializable, Game_Handling {
    @Serial
    private static final long serialVersionUID = 1L;

    static boolean loading = false;

    public String player;
    public Square from;
    public Square to;
    public Piece piece;
    public Piece capture;
    public Piece promotion;
    public String action;
    public Rook rook;
    public static ArrayList<Move> moveHistory = new ArrayList<>();
    public static ArrayList<String> whiteMovesHistory = new ArrayList<String>();
    public static ArrayList<String> blackMovesHistory = new ArrayList<String>();
    public static String whiteNewMove = "";
    public static String blackNewMove = "";
    public static boolean killerMove = false;
    public static boolean muteSound = false;
    public Square enPassantSquare;
    public Pawn enPassantPawn;


    public static String convertToChessNotation(Square square) {
        char column = (char) ('a' + square.x);
        int row = 8 - square.y;
        return "" + column + row;
    }

    //normal move
    public Move(String player, Square from, Square to, Piece piece) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.action = "move";
    }

    //castling
    public Move(Square toKing, Piece king, Square from, Rook rook) {
        this.action = "castling";
        this.from = from;
        this.piece = king;
        this.to = toKing;
        this.rook = rook;
    }

    //enPassant
    public Move(String player, Square from, Square to, Pawn pawn, Pawn enPassantPawn, Square enPassantSquare) {
        this.action = "enPassant";
        this.player = player;
        this.from = from;
        this.to = to;
        this.piece = pawn;
        this.enPassantPawn = enPassantPawn;
        this.enPassantSquare = enPassantSquare;
    }

    //promotion
    public Move(String player, Square from, Square to, Pawn pawn, Piece promotion) {
        this.promotion = promotion;
        this.action = "promotion";
        this.player = player;
        this.from = from;
        this.to = to;
        this.piece = pawn;
    }

    public Move(Piece piece, Square from, Square to, Piece capture) {
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.capture = capture;
    }


    //capture move
    public Move(String player, Square from, Square to, Piece piece, Piece Captured) {
        this.capture = Captured;
        this.action = "capture";
        this.player = player;
        this.from = from;
        this.to = to;
        this.piece = piece;
    }

    public static boolean normalMove(Square square) {
        if (!currentPiece.possibleMoves.contains(square.name)) return false;

        Square initialSquare = (Square) currentPiece.getParent();
        Move moves = new Move(currentPlayer, initialSquare, square, currentPiece);
        if (!killerMove) {
            String musicFile = "src/main/resources/normal move.wav";     // For example

            if (!muteSound) {
                Media sound = new Media(new File(musicFile).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
            }
        }

        if (currentPiece instanceof King) {
            ((King) currentPiece).setHasMoved(true);
        }
        if (currentPiece instanceof Rook) {
            ((Rook) currentPiece).setHasMoved(true);
        }

        square.getChildren().add(currentPiece);
        square.occupied = true;
        initialSquare.getChildren().removeAll();
        initialSquare.occupied = false;
        currentPiece.posX = square.x;
        currentPiece.posY = square.y;
        moveHistory.add(moves);


        updateMoveLabel();
        return true;
    }

    public static void updateMoveLabel() {

        Move move = moveHistory.get(moveHistory.size() - 1);

        if (whiteMovesHistory.size() == 29) {
            whiteMovesHistory.remove(0);
        }
        if (blackMovesHistory.size() == 29) {
            blackMovesHistory.remove(0);
        }
        if (killerMove) {
            if (currentPlayer == "white") {
                whiteMovesHistory.add(move.piece.letter + "x" + convertToChessNotation(move.to));
                whiteNewMove = String.join("\n", whiteMovesHistory);
                Game.whiteMoveLabel.setText(whiteNewMove);
            } else {
                blackMovesHistory.add(move.piece.letter + "x" + convertToChessNotation(move.to));
                blackNewMove = String.join("\n", blackMovesHistory);
                Game.blackMoveLabel.setText(blackNewMove);
            }
        } else {
            if (currentPlayer == "white") {
                whiteMovesHistory.add(move.piece.letter + convertToChessNotation(move.to));
                whiteNewMove = String.join("\n", whiteMovesHistory);
                Game.whiteMoveLabel.setText(whiteNewMove);
            } else {
                blackMovesHistory.add(move.piece.letter + convertToChessNotation(move.to));
                blackNewMove = String.join("\n", blackMovesHistory);
                Game.blackMoveLabel.setText(blackNewMove);
            }
        }
        obj.updateState(ChessBoard.theme, ChessBoard.normalChess, Move.moveHistory, timer, (int) whiteTimer.getElapsed(), (int) blackTimer.getElapsed(), casual, currentPlayer, whiteNewMove, blackNewMove);

    }


    public static boolean killMove(Square square, Piece killedPiece) {
        if (!currentPiece.possibleMoves.contains(square.name)) return false;

        String musicFile = "src/main/resources/capture.wav";     // For example

        if (!muteSound) {
            Media sound2 = new Media(new File(musicFile).toURI().toString());
            MediaPlayer mediaPlayer2 = new MediaPlayer(sound2);
            mediaPlayer2.play();
        }

        killerMove = true;
        Square initialSquare = (Square) currentPiece.getParent();
        Move moves = new Move(Game.currentPlayer, initialSquare, square, currentPiece, killedPiece);

        square.occupied = false;
        square.getChildren().remove(killedPiece);
        normalMove(square);
        moveHistory.add(moves);
        killerMove = false;

        obj.updateState(ChessBoard.theme, ChessBoard.normalChess, Move.moveHistory, timer, (int) whiteTimer.getElapsed(), (int) blackTimer.getElapsed(), casual, currentPlayer, whiteNewMove, blackNewMove);

        return true;
    }

    public static void clear() {
        moveHistory.clear();
    }

    public static void loadGame() {
        // Make a copy of the moveHistory to iterate over
        List<Move> historyCopy = new ArrayList<>(obj.moveHistory);

        loading = true;
        for (Move move : historyCopy) {
            currentPiece = move.piece;
            if (move.action.equals("castling")) {
                ((King) move.piece).hasMoved = false;
                move.piece.posX = 4;
                castling(move.to, move.piece);
                continue;
            }


            Square initialSquare = getSquareByName(move.from.name);


            Piece piece1 = null;
            if (!initialSquare.getChildren().isEmpty()) piece1 = (Piece) initialSquare.getChildren().get(0);


            Square square = getSquareByName(move.to.name);


            // Restore moves based on their action type
            if (move.action.equals("capture")) {
                assert square != null;
                Piece killedPiece = null;

                if (!square.getChildren().isEmpty()) killedPiece = (Piece) square.getChildren().get(0);

                killMove(square, killedPiece, initialSquare, piece1);
            } else if (move.action.equals("move")) {
                normalMove(square, initialSquare, piece1);
            }

        }
        currentPlayer = obj.player;
        loading = false;
    }


    private static void normalMove(Square square, Square initialSquare, Piece currentPiece) {
        Move moves = new Move(currentPlayer, initialSquare, square, currentPiece);

        if (currentPiece != null) {
            square.getChildren().add(currentPiece);
            square.occupied = true;
            initialSquare.getChildren().removeAll();
            initialSquare.occupied = false;
            currentPiece.posX = square.x;
            currentPiece.posY = square.y;
            moveHistory.add(moves);

        }


        Move move = moveHistory.get(moveHistory.size() - 1);
    }


    private static void killMove(Square square, Piece killedPiece, Square initialSquare, Piece currentPiece) {
        Move moves = new Move(Game.currentPlayer, initialSquare, square, currentPiece, killedPiece);

        square.occupied = false;
        square.getChildren().remove(killedPiece);


        if (currentPiece != null) square.getChildren().add(currentPiece);


        normalMove(square, initialSquare, currentPiece);
        moveHistory.add(moves);
    }


}

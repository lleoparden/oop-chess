package com.example.oop_chess;


import javafx.event.EventHandler;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.example.oop_chess.Checkmate.kingInCheck;
import static com.example.oop_chess.ChessBoard.squares;
import static com.example.oop_chess.Game.check;
import static com.example.oop_chess.King.kingValidMoves;

public abstract class Piece extends ImageView implements Pieces, Serializable {
    String type;
    String letter;
    String color;
    int posX, posY;
    ArrayList<String> possibleMoves;
    static List<String> validMoves = new ArrayList<>();

    public Piece(String color, int posX, int posY) {
        this.color = color;
        this.posX = posX;
        this.posY = posY;
        addEventHandler();
    }

    public String getColor() {
        return this.color;
    }
    private boolean isOpposingPiece(Piece piece) {
        // Replace "yourPlayerColor" with the player's color variable (e.g., "White" or "Black").
        return !piece.color.equals(getColor());
    }


    public void setPiece(Image image) {
        this.setImage(image);
    }

    public void setImage() {
        this.setPiece(new Image("com/example/oop_chess/assets/pieces/" + this.color + "-" + this.type + ".png"));

    }

    private void addEventHandler() {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getAllPossibleMoves();
            }
        });


    }

    public abstract void getAllPossibleMoves();

    public void showAllPossibleMoves(boolean val) {
        if (val) {
            // Configure the visual effect for highlighting moves
            InnerShadow effect = new InnerShadow();
            effect.setColor(Objects.equals(ChessBoard.theme, "Glass") ? Color.BLACK : Color.GRAY);
            effect.setOffsetX(0);
            effect.setOffsetY(0);

            // Filter the possible moves to ensure validity
            filterPossibleMoves(this);

            // Highlight each valid move
            for (String move : possibleMoves) {
                Square square = getSquareByName(move);
                assert square != null;
                square.setEffect(effect);

                Piece piece = getPieceByName(move);
                if (piece != null && isOpposingPiece(piece)) {
                    // Opposing pieces are highlighted with a red border
                    square.setBorder(new Border(new BorderStroke(
                            Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.5))));
                } else {
                    // Empty squares or same-side pieces have a black border
                    square.setBorder(new Border(new BorderStroke(
                            Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.5))));
                }
            }
        } else {
            // Clear all effects and borders
            for (Square square : squares) {
//                Square square = getSquareByName(move);
//                assert square != null;
                square.setEffect(null);
                square.setBorder(new Border(new BorderStroke(
                        Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }
        }
    }


    public static void filterPossibleMoves(Piece piece) {
        if (check) {
            // Filter moves when the king is in check
            Iterator<String> iterator = piece.possibleMoves.iterator();
            while (iterator.hasNext()) {
                String move = iterator.next();
                if (piece instanceof King) {
                    if (!kingValidMoves.contains(move)) {
                        iterator.remove();
                    }
                } else {
                    if (!validMoves.contains(move)) {
                        iterator.remove();
                    }
                }
            }
        } else {
            // Filter moves based on simulation
            Iterator<String> iterator = piece.possibleMoves.iterator();
            while (iterator.hasNext()) {
                String move = iterator.next();
                if (!isSafeMove(move,piece)) {
                    iterator.remove();
                }
            }
        }
    }

    private static boolean isSafeMove(String move, Piece piece) {
        Square targetSquare = getSquareByName(move);
        Square currentSquare = (Square) piece.getParent();
        Piece originalPiece = null;

        if (Objects.requireNonNull(targetSquare).isOccupied()) {
            originalPiece = (Piece) targetSquare.getChildren().get(0);
        }

        boolean moved = movePiece(piece, targetSquare);
        boolean isInCheck = kingInCheck(piece.color);
        if (moved) undoMove(piece, currentSquare, targetSquare, originalPiece);

        return !isInCheck;
    }





    public static Square getSquareByName(String name) {
        for (Square square : squares) {
            if (square.name.equals(name)) {
                return square;
            }
        }

        return null;
    }

    public static Piece getPieceByName(String name) {
        for (Square square : squares) {
            if (square.getChildren().isEmpty()) continue;

            if (square.name.equals(name))
                return (Piece) square.getChildren().get(0);

        }
        return null;
    }


    public static boolean movePiece(Piece piece, Square targetSquare) {
        if (targetSquare.occupied) return false;
        Square currentSquare = getSquareByName("Square" + piece.posX + piece.posY);
        Objects.requireNonNull(currentSquare).occupied = false;
        currentSquare.getChildren().remove(piece);
        targetSquare.getChildren().add(piece);
        piece.posX = targetSquare.x;
        piece.posY = targetSquare.y;
        targetSquare.occupied = true;
        return true;
    }
    public static boolean capturePiece(Piece piece, Square targetSquare) {
        Square currentSquare = getSquareByName("Square" + piece.posX + piece.posY);
        Objects.requireNonNull(currentSquare).occupied = false;
        currentSquare.getChildren().remove(piece);
        Piece killPiece = (Piece)targetSquare.getChildren().get(0);
        targetSquare.getChildren().remove(killPiece);
        killPiece = null;
        targetSquare.getChildren().add(piece);
        piece.posX = targetSquare.x;
        piece.posY = targetSquare.y;
        targetSquare.occupied = true;
        return true;
    }


    public static void undoMove(Piece piece, Square originalSquare, Square targetSquare, Piece originalPiece) {
        targetSquare.getChildren().remove(piece);
        targetSquare.occupied = false;
        originalSquare.getChildren().add(piece);
        piece.posX = originalSquare.x;
        piece.posY = originalSquare.y;
        originalSquare.occupied = true;

        if (originalPiece != null) {
            targetSquare.getChildren().remove(originalPiece);
            targetSquare.getChildren().add(originalPiece);
            originalPiece.posX = targetSquare.x;
            originalPiece.posY = targetSquare.y;
            targetSquare.occupied = true;
        }
    }


    @Override
    public String toString() {
        return this.color + " " + this.type;
    }

    public ArrayList<String> getPossibleMoves() {
        return possibleMoves;
    }
}

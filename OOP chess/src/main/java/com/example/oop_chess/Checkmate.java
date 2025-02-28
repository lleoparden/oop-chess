package com.example.oop_chess;


import java.util.Objects;

import static com.example.oop_chess.ChessBoard.squares;
import static com.example.oop_chess.Game.*;
import static com.example.oop_chess.Piece.*;

public class Checkmate {

    public static Square findKingSquare(String kingColor) {
        for (Square square : squares) {
            if (square.occupied) {
                if (square.getChildren().get(0) instanceof King king) {
                    if (king.getColor().equals(kingColor)) {
                        return square;
                    }
                }
            }
        }
        return null;
    }

    public static boolean kingInCheck(String kingColor) {

        Square kingSquare = findKingSquare(kingColor);

        for (Square square : ChessBoard.squares) {

            if (!square.getChildren().isEmpty()) {

                Piece piece = (Piece) square.getChildren().get(0);

                if (!piece.color.equals(kingColor)) {

                    piece.getAllPossibleMoves();
                    String kingPosition = Objects.requireNonNull(kingSquare).name;

                    if (piece.getPossibleMoves().contains(kingPosition)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public static boolean isCheckmate(String kingColor) {


        // Find the king and its possible moves
        Square kingSquare = findKingSquare(kingColor);
        Piece king = (Piece) Objects.requireNonNull(kingSquare).getChildren().get(0);
        king.getAllPossibleMoves();
        Square checkSquare = (Square) lastPiece.getParent();
        System.out.println(checkSquare.name);

        King.kingValidMoves.clear();
        if(king.possibleMoves.contains(checkSquare.name)){
            Piece originalPiece = null;
            if (Objects.requireNonNull(checkSquare).isOccupied()) {
                originalPiece = (Piece) checkSquare.getChildren().get(0); // Temporarily remove the piece
            }
            boolean moved = capturePiece(king, checkSquare);

            // Check if the king is still in check
            boolean isStillInCheck = kingInCheck(kingColor);

            // Undo the move
            if (moved) undoMove(king, kingSquare, checkSquare, originalPiece);

            // If the king is still in check, it's checkmate
            if (!isStillInCheck) {
                King.kingValidMoves.add(checkSquare.name);
            }
        }

        // Check if the king can move to a safe square
        for (String move : king.getPossibleMoves()) {
            Square targetSquare = getSquareByName(move);

            // Simulate the move
            Piece originalPiece = null;
            if (Objects.requireNonNull(targetSquare).isOccupied()) {
                originalPiece = (Piece) targetSquare.getChildren().get(0); // Temporarily remove the piece
            }
            boolean moved = movePiece(king, targetSquare);

            // Check if the king is still in check
            boolean isStillInCheck = kingInCheck(kingColor);

            // Undo the move
            if (moved) undoMove(king, kingSquare, targetSquare, originalPiece);

            // If the king has a safe move, it's not checkmate
            if (!isStillInCheck) {
                King.kingValidMoves.add(move);
            }
        }

        validMoves.clear();

        // Check if any other piece can block
        for (Square square : ChessBoard.squares) {
            if (!square.getChildren().isEmpty()) {
                Piece piece = (Piece) square.getChildren().get(0);
                if (piece.color.equals(kingColor) && !piece.type.equals("King")) {
                    piece.getAllPossibleMoves(); // Calculate possible moves

                    for (String move : piece.getPossibleMoves()) {
                        Square targetSquare = getSquareByName(move);


                        Piece originalPiece = null;
                        if (!Objects.requireNonNull(targetSquare).getChildren().isEmpty()) {
                            originalPiece = (Piece) targetSquare.getChildren().get(0);
                        }
                        // Simulate the move
                        boolean moved = movePiece(piece, targetSquare);

                        // Check if the king is still in check
                        boolean isStillInCheck = kingInCheck(kingColor);

                        // Undo the move
                        if (moved) undoMove(piece, square, targetSquare, originalPiece);

                        // If the move blocks the check, it's not checkmate
                        if (!isStillInCheck) {
                            Piece.validMoves.add(move);
                        }
                    }
                }
            }
        }

        // Check if any allied piece can capture the threatening piece
        for (Square square : ChessBoard.squares) {
            if (!square.getChildren().isEmpty()) {
                Piece piece = (Piece) square.getChildren().get(0);
                if (piece.color.equals(kingColor) && !piece.type.equals("King")) {
                    piece.getAllPossibleMoves(); // Calculate possible moves

                    if(piece.possibleMoves.contains(checkSquare.name)){
                        Piece originalPiece = null;
                        if (Objects.requireNonNull(checkSquare).isOccupied()) {
                            originalPiece = (Piece) checkSquare.getChildren().get(0); // Temporarily remove the piece
                        }
                        boolean moved = capturePiece(piece, checkSquare);

                        // Check if the king is still in check
                        boolean isStillInCheck = kingInCheck(kingColor);

                        // Undo the move
                        if (moved) undoMove(piece, square, checkSquare, originalPiece);

                        // If the king is still in check, it's checkmate
                        if (!isStillInCheck) {
                            validMoves.add(checkSquare.name);
                        }
                    }
                }
            }
        }


        // Check if the checkmate conditions are met
        return Piece.validMoves.isEmpty() && King.kingValidMoves.isEmpty();


    }



    public static boolean isStalemate(String kingColor) {
        // Step 1: Ensure the king is NOT in check
        if (kingInCheck(kingColor)) {
            return false; // Not a stalemate if the king is in check
        }

        // Step 2: Check if the player has any legal moves
        for (Square square : ChessBoard.squares) {
            if (!square.getChildren().isEmpty()) {
                Piece piece = (Piece) square.getChildren().get(0);
                if (piece.color.equals(kingColor)) {
                    piece.getAllPossibleMoves(); // Calculate possible moves for the piece

                    // If any legal move exists, it's not a stalemate
                    for (String move : piece.getPossibleMoves()) {
                        Square targetSquare = getSquareByName(move);

                        // Simulate the move and check legality
                        if (simulateMoveAndCheck(piece, square, targetSquare, kingColor)) {
                            return false; // Found a legal move, not stalemate
                        }
                    }
                }
            }
        }

        // Step 3: If no moves and not in check, it's stalemate
        return true;
    }

    private static boolean simulateMoveAndCheck(Piece piece, Square fromSquare, Square targetSquare, String kingColor) {
        Piece originalPiece = null;
        if (targetSquare.isOccupied()) {
            originalPiece = targetSquare.getPiece();
        }

        boolean moved = movePiece(piece, targetSquare);
        boolean stillInCheck = kingInCheck(kingColor);

        if (moved) undoMove(piece, fromSquare, targetSquare, originalPiece);

        return !stillInCheck; // Move is legal if the king is not in check
    }




}
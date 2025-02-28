package com.example.oop_chess;

import java.util.Objects;

import static com.example.oop_chess.Game.*;
import static com.example.oop_chess.Move.*;
import static com.example.oop_chess.Piece.getSquareByName;

public class Undo implements Visuals {
    static boolean promotionUndo = false;

    public static void undoLastMove() {
        if (currentPiece != null) Visuals.deselectPiece();

        // Check if the move history is empty
        if (Move.moveHistory.isEmpty()) {
            System.out.println("No moves to undo.");
            return;
        }
        check = false;

        // Get the last move
        Move lastMove = Move.moveHistory.remove(Move.moveHistory.size() - 1);


        // Extract details of the move
        Square from = lastMove.from;
        Square to = lastMove.to;
        Piece movedPiece = lastMove.piece;


        from.getChildren().remove(movedPiece);
        from.getChildren().add(movedPiece);
        from.occupied = true;


        to.getChildren().remove(movedPiece);
        to.occupied = false;


        // Update the position of the moved piece
        movedPiece.posX = from.x;
        movedPiece.posY = from.y;


        // Restore the captured piece if it was a capture move
        if (lastMove.capture != null) {
            Piece capturedPiece = lastMove.capture;

            to.getChildren().add(capturedPiece);
            to.occupied = true;
            capturedPiece.posX = to.x;
            capturedPiece.posY = to.y;
            moveHistory.remove(moveHistory.size() - 1);
        }
        boolean thereIsKing = false;
        if (lastMove.piece instanceof King) {
            for (Move move : Move.moveHistory) {
                if (move.piece instanceof King) {
                    thereIsKing = true;
                    break;
                }
            }
            if (!thereIsKing) {
                ((King) lastMove.piece).setHasMoved(false);
            }
        }
        boolean thereIsRook = false;
        if (lastMove.piece instanceof Rook) {
            for (Move move : Move.moveHistory) {
                if (move.piece instanceof Rook) {
                    thereIsRook = true;
                    break;
                }
            }
            if (!thereIsRook) {
                ((Rook) lastMove.piece).setHasMoved(false);
            }
        }


        if (lastMove.rook != null) {
            if(lastMove.from.x > lastMove.to.x) {
                lastMove.to.getChildren().remove(movedPiece);
                Objects.requireNonNull(getSquareByName("Square4" + lastMove.from.y)).getChildren().add(movedPiece);
                Objects.requireNonNull(getSquareByName("Square4" + lastMove.from.y)).occupied = true;
                movedPiece.posX = 4;
                movedPiece.posY = lastMove.from.y;
                lastMove.from.getChildren().remove(lastMove.rook);
                Objects.requireNonNull(getSquareByName("Square7" + lastMove.from.y)).occupied = true;
                Objects.requireNonNull(getSquareByName("Square7" + lastMove.from.y)).getChildren().add(lastMove.rook);
                lastMove.rook.posX = 7;
                lastMove.rook.posY = lastMove.from.y;
                lastMove.rook.setHasMoved(false);
                ((King)lastMove.piece).setHasMoved(false);
                Objects.requireNonNull(getSquareByName("Square" + (lastMove.piece.posX + 1) + lastMove.from.y)).occupied = false;
                Objects.requireNonNull(getSquareByName("Square" + (lastMove.piece.posX + 2) + lastMove.from.y)).occupied = false;
            }else {
                lastMove.to.getChildren().remove(movedPiece);
                Objects.requireNonNull(getSquareByName("Square4" + lastMove.from.y)).getChildren().add(movedPiece);
                Objects.requireNonNull(getSquareByName("Square4" + lastMove.from.y)).occupied = true;
                movedPiece.posX = 4;
                movedPiece.posY = lastMove.from.y;
                lastMove.from.getChildren().remove(lastMove.rook);
                Objects.requireNonNull(getSquareByName("Square0" + lastMove.from.y)).occupied = true;
                Objects.requireNonNull(getSquareByName("Square0" + lastMove.from.y)).getChildren().add(lastMove.rook);
                lastMove.rook.posX = 0;
                lastMove.rook.posY = lastMove.from.y;
                lastMove.rook.setHasMoved(false);
                ((King)lastMove.piece).setHasMoved(false);
                Objects.requireNonNull(getSquareByName("Square" + (lastMove.piece.posX - 1) + lastMove.from.y)).occupied = false;
                Objects.requireNonNull(getSquareByName("Square" + (lastMove.piece.posX - 2) + lastMove.from.y)).occupied = false;
                Objects.requireNonNull(getSquareByName("Square" + (lastMove.piece.posX - 3) + lastMove.from.y)).occupied = false;
            }
        }


        if (lastMove.enPassantPawn != null) {
            // Revert the capturing pawn (the pawn that did the en passant capture)
            Piece capturingPawn = lastMove.piece;
            Square captureSquare = lastMove.to; // The square where the capturing pawn is now

            // Remove the capturing pawn from the capture square
            captureSquare.getChildren().remove(capturingPawn);
            captureSquare.occupied = false;

            // Place the capturing pawn back to its original position (lastMove.from)
            Square originalSquare = lastMove.from;
            if(originalSquare.getChildren().contains(capturingPawn)) originalSquare.getChildren().remove(capturingPawn);
            originalSquare.getChildren().add(capturingPawn);
            originalSquare.occupied = true;

            // Update the position of the capturing pawn
            capturingPawn.posX = originalSquare.x;
            capturingPawn.posY = originalSquare.y;

            // Now, put the captured pawn back to its original position
            Piece capturedPawn = lastMove.enPassantPawn;
            Square capturedPawnOriginalSquare;

            if (capturingPawn.color.equals("white")) {
                // The captured pawn is black, and it was captured on the rank above the capturing pawn
                capturedPawnOriginalSquare = getSquareByName("Square" + captureSquare.x + (captureSquare.y + 1));
            } else {
                // The captured pawn is white, and it was captured on the rank below the capturing pawn
                capturedPawnOriginalSquare = getSquareByName("Square" + captureSquare.x + (captureSquare.y - 1));
            }

            // Put the captured pawn back to its original square
            capturedPawnOriginalSquare.getChildren().add(capturedPawn);
            capturedPawnOriginalSquare.occupied = true;

            // Update the captured pawn's position
            capturedPawn.posX = capturedPawnOriginalSquare.x;
            capturedPawn.posY = capturedPawnOriginalSquare.y;

            lastMove.enPassantPawn.lastMovedTurn=Game.turn;


            // Clear the en passant pawn
            lastMove.enPassantPawn = null;


        }




        if (lastMove.promotion != null) {
            Piece promotionPiece = lastMove.promotion;

            to.getChildren().remove(promotionPiece);
            to.occupied = false;

            promotionUndo = true;

            undoLastMove();
        }
        if (!promotionUndo) {
            if (Objects.equals(currentPlayer, "black")) {
                whiteMovesHistory.remove(whiteMovesHistory.size() - 1);
                whiteNewMove = String.join("\n", whiteMovesHistory);
                Game.whiteMoveLabel.setText(whiteNewMove);
            }
            if (Objects.equals(currentPlayer, "white")) {
                blackMovesHistory.remove(blackMovesHistory.size() - 1);
                blackNewMove = String.join("\n", blackMovesHistory);
                Game.blackMoveLabel.setText(blackNewMove);
            }


            if(Checkmate.kingInCheck("white") || Checkmate.kingInCheck("black")) check = true;
            // Switch back the player turn
            Game_Handling.changePlayer();
        } else promotionUndo = false;
    }
}
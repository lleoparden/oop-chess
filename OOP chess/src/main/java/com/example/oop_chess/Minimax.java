package com.example.oop_chess;

import java.util.List;
import java.util.Objects;

import static com.example.oop_chess.Piece.*;

public class Minimax {

    private static final int MAX_DEPTH = 2; // Adjustable for performance vs. accuracy

    private static final int[][] PAWN_TABLE = {
            {   0,   0,   0,   0,   0,   0,   0,   0 },
            {  10,  10,  10, -20, -20,  10,  10,  10 },
            {   5,   0,   0,  10,  10,   0,   0,   5 },
            {   0,   0,   0,  20,  20,   0,   0,   0 },
            {   5,   5,  10,  25,  25,  10,   5,   5 },
            {  10,  10,  20,  30,  30,  20,  10,  10 },
            {  50,  50,  50,  50,  50,  50,  50,  50 },
            {   0,   0,   0,   0,   0,   0,   0,   0 }
    };

    private static final int[][] KNIGHT_TABLE = {
            { -50, -40, -30, -30, -30, -30, -40, -50 },
            { -40, -20,   0,   5,   5,   0, -20, -40 },
            { -30,   5,  15,  20,  20,  15,   5, -30 },
            { -30,  10,  20,  30,  30,  20,  10, -30 },
            { -30,  10,  20,  30,  30,  20,  10, -30 },
            { -30,   5,  15,  20,  20,  15,   5, -30 },
            { -40, -20,   0,   5,   5,   0, -20, -40 },
            { -50, -40, -30, -30, -30, -30, -40, -50 }
    };

    private static final int[][] BISHOP_TABLE = {
            { -20, -10, -10, -10, -10, -10, -10, -20 },
            { -10,   0,   0,   5,   5,   0,   0, -10 },
            { -10,   0,  10,  10,  10,  10,   0, -10 },
            { -10,  10,  10,  15,  15,  10,  10, -10 },
            { -10,   5,  15,  15,  15,  15,   5, -10 },
            { -10,  10,  10,  10,  10,  10,  10, -10 },
            { -10,   5,   0,   0,   0,   0,   5, -10 },
            { -20, -10, -10, -10, -10, -10, -10, -20 }
    };

    private static final int[][] ROOK_TABLE = {
            {   0,   0,   5,  10,  10,   5,   0,   0 },
            {   0,   0,   5,  10,  10,   5,   0,   0 },
            {   0,   0,   5,  10,  10,   5,   0,   0 },
            {   5,   5,   5,  10,  10,   5,   5,   5 },
            {   5,   5,   5,  10,  10,   5,   5,   5 },
            {   0,   0,   5,  10,  10,   5,   0,   0 },
            {   0,   0,   5,  10,  10,   5,   0,   0 },
            {   0,   0,   0,   5,   5,   0,   0,   0 }
    };

    private static final int[][] QUEEN_TABLE = {
            { -20, -10, -10,  -5,  -5, -10, -10, -20 },
            { -10,   0,   0,   0,   0,   0,   0, -10 },
            { -10,   0,   5,   5,   5,   5,   0, -10 },
            {  -5,   0,   5,  10,  10,   5,   0,  -5 },
            {  -5,   0,   5,  10,  10,   5,   0,  -5 },
            { -10,   0,   5,   5,   5,   5,   0, -10 },
            { -10,   0,   0,   0,   0,   0,   0, -10 },
            { -20, -10, -10,  -5,  -5, -10, -10, -20 }
    };

    private static final int[][] KING_TABLE = {
            { -30, -40, -40, -50, -50, -40, -40, -30 },
            { -30, -40, -40, -50, -50, -40, -40, -30 },
            { -30, -40, -40, -50, -50, -40, -40, -30 },
            { -30, -40, -40, -50, -50, -40, -40, -30 },
            { -20, -30, -30, -40, -40, -30, -30, -20 },
            { -10, -20, -20, -20, -20, -20, -20, -10 },
            {  20,  30,  10,   0,   0,  10,  30,  20 },
            {  20,  40,  20,   0,   0,  20,  40,  20 }
    };

    public static Move findBestMove(ChessBoard board, boolean isMaximizingPlayer) {
        // Get all possible moves
        List<Move> possibleMoves = ChessBoard.getAllPossibleMoves(isMaximizingPlayer);


        Move bestCaptureMove = getBestCaptureMove( isMaximizingPlayer);
        if (bestCaptureMove != null) {
            return bestCaptureMove; // Prioritize a strong capture
        }

        // If no good capture exists, proceed with normal minimax
        int bestValue = isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Move bestMove = null;

        for (Move move : possibleMoves) {
            simulateMove(move);
            int boardValue = minimax(board, MAX_DEPTH - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, !isMaximizingPlayer);
            undoMove(move);

            if (isMaximizingPlayer && boardValue > bestValue) {
                bestValue = boardValue;
                bestMove = move;
            } else if (!isMaximizingPlayer && boardValue < bestValue) {
                bestValue = boardValue;
                bestMove = move;
            }
        }
        return bestMove;
    }


    private static Move getBestCaptureMove( boolean isMaximizingPlayer) {
        int score = Integer.MAX_VALUE;
        Move bestCaptureMove = null;

        for (Square square : ChessBoard.squares) {
            if (!square.getChildren().isEmpty()) {
                Piece piece = (Piece) square.getChildren().get(0);

                // Consider only the current player's pieces
                if (piece.color.equals("white") == isMaximizingPlayer) {
                    piece.getAllPossibleMoves();// Ensure possibleMoves is populated
                    Piece.filterPossibleMoves(piece);
                    List<String> possibleMoves = piece.possibleMoves;
                    if (possibleMoves == null) continue;

                    for (String targetSquareName : possibleMoves) {
                        Square targetSquare = getSquareByName(targetSquareName); // Convert name to Square
                        assert targetSquare != null;
                        if (!targetSquare.getChildren().isEmpty()){
                            Piece capturedPiece = (Piece) targetSquare.getChildren().get(0);
                            boolean moved = capturePiece(piece, targetSquare);
                            boolean isSafe = underAttack(piece);
                            if (moved) Piece.undoMove(piece, square , targetSquare, capturedPiece);
                            if(getPieceValue(piece) > getPieceValue(capturedPiece)){
                                Move captureMove = new Move(piece, square, targetSquare, capturedPiece);
                                if(score>= getPieceValue(piece) - getPieceValue(capturedPiece)){
                                    score = getPieceValue(piece) - getPieceValue(capturedPiece);
                                    if (isSafe)score += 30;
                                    bestCaptureMove = captureMove;
                                }
                            }else if(getPieceValue(piece) < getPieceValue(capturedPiece)){
                                Move captureMove = new Move(piece, square, targetSquare, capturedPiece);
                                if(score>= getPieceValue(piece) - getPieceValue(capturedPiece)){
                                    score = getPieceValue(piece) - getPieceValue(capturedPiece);
                                    if (isSafe)score += 30;
                                    bestCaptureMove = captureMove;
                                }
                            }else{
                                Move captureMove = new Move(piece, square, targetSquare, capturedPiece);
                                if(score>= getPieceValue(capturedPiece) /2){
                                    score = getPieceValue(capturedPiece) *2;
                                    if (isSafe)score += 30;
                                    bestCaptureMove = captureMove;
                                }
                            }
                        }
                    }
                }
            }
        }
        return bestCaptureMove;
    }

    public static int minimax(ChessBoard board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || board.isGameOver()) {
            return evaluateBoard(board);
        }

        List<Move> possibleMoves = ChessBoard.getAllPossibleMoves(isMaximizingPlayer);

        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : possibleMoves) {
                boolean moved = simulateMove(move);
                int eval = minimax(board, depth - 1, alpha, beta, false);
                if(moved)undoMove(move);

                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break; // Alpha-beta pruning
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : possibleMoves) {
                boolean moved = simulateMove(move);
                int eval = minimax(board, depth - 1, alpha, beta, true);
                if(moved)undoMove(move);

                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break; // Alpha-beta pruning
            }
            return minEval;
        }
    }

    private static boolean simulateMove(Move move) {
        // Check and remove captured piece
        Piece capturedPiece = (Piece) move.to.getChildren().stream()
                .filter(child -> child instanceof Piece && !Objects.equals(((Piece) child).color, move.piece.color))
                .findFirst()
                .orElse(null);

        if (capturedPiece != null) {
            move.to.getChildren().remove(capturedPiece); // Remove captured piece
            move.capture = capturedPiece; // Track the captured piece
        }

        // Perform the move
        return movePiece(move.piece, move.to);
    }

    private static void undoMove(Move move) {
        // Undo the move
        Piece.undoMove(move.piece, move.from, move.to, move.capture);

        // Restore captured piece
        if (move.capture != null) {
            move.to.getChildren().add(move.capture);
            move.capture = null; // Clear the capture for safety
        }
    }

    private static boolean underAttack(Piece piece) {
        for (Square square : ChessBoard.squares) {
            if (!square.getChildren().isEmpty()) {
                Piece otherPiece = (Piece) square.getChildren().get(0);
                if (otherPiece != null && !Objects.equals(otherPiece.color, piece.color)) {
                    otherPiece.getAllPossibleMoves();

                    // Check if the other piece is under attack
                    if (otherPiece.getPossibleMoves().contains(((Square) piece.getParent()).name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }




    private static int evaluateBoard(ChessBoard board) {
        int score = 0;
        for (Piece piece : board.getAllPieces()) {
            int value = getPieceValue(piece);
            int positionValue = getPositionValue(piece);


            // Reward/deduct for material balance
            score += piece.color.equals("white") ? (value + positionValue) : -(value + positionValue);
        }
        return score;
    }


    private static int getPieceValue(Piece piece) {
        if (piece == null) return 0;

        return switch (piece.type) {
            case "Pawn" -> 10;
            case "Bishop" -> 30;
            case "Knight" -> 40;
            case "Rook" -> 50;
            case "Queen" -> 90;
            case "King" -> 900;
            default -> 0;
        };
    }

    private static int getPositionValue(Piece piece) {
        if (piece == null) return 0;
        int[][] positionTable = getPositionTable(piece);
        return positionTable != null ? positionTable[piece.posX][piece.posY] : 0;
    }

    private static int[][] getPositionTable(Piece piece) {
        return switch (piece.type) {
            case "Pawn" -> PAWN_TABLE;
            case "Knight" -> KNIGHT_TABLE;
            case "Bishop" -> BISHOP_TABLE;
            case "Rook" -> ROOK_TABLE;
            case "Queen" -> QUEEN_TABLE;
            case "King" -> KING_TABLE;
            default -> null;
        };
    }

}

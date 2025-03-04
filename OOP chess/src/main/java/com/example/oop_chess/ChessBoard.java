package com.example.oop_chess;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.example.oop_chess.Piece.getSquareByName;

public class ChessBoard {

    GridPane chessBoard;
    static String theme = "Green";
    static boolean normalChess = true;
    public static ArrayList<Square> squares = new ArrayList<>();

    public ChessBoard(GridPane chessBoard) {
        this.chessBoard = chessBoard;
        makeBoard(this.chessBoard);

    }


    private void makeBoard(GridPane chessBoard) {
        chessBoard.getChildren().clear(); // Clear any previous nodes
        chessBoard.getRowConstraints().clear();
        chessBoard.getColumnConstraints().clear();

        // Set constraints for rows and columns to ensure dynamic sizing
        for (int i = 0; i < 8; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(12.5); // Each row is 1/8 of the height
            chessBoard.getRowConstraints().add(row);

            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(12.5); // Each column is 1/8 of the width
            chessBoard.getColumnConstraints().add(col);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = new Square(i, j);
                square.setName("Square" + i + j);
                square.setMinSize(0, 0); // Allows dynamic resizing
                setTheme(square, i, j);
                chessBoard.add(square, i, j);
                squares.add(square);
            }
        }

        addPieces();
    }


    private void setTheme(Square square, int i, int j) {
        Color color1 = Color.web("#ffffff00");
        Color color2 = Color.web("#ffffff00");

        switch (theme) {
            case "Glass" -> {
                color1 = Color.web("#6b7588");
                color2 = Color.web("#292f3c");
            }
            case "Green" -> {
                color1 = Color.web("#ebecd0");
                color2 = Color.web("#739552");
            }
            case "Brown" -> {
                color1 = Color.web("#edd6b0");
                color2 = Color.web("#b88762");
            }
            case "Pink" -> {
                color1 = Color.web("#F4E4FF");
                color2 = Color.web("#F498FF");
            }
            case "Sky" -> {
                color1 = Color.web("#ccdae0");
                color2 = Color.web("#7195aa");
            }
        }

        if ((i + j) % 2 == 0) {
            square.setBackground(new Background(new BackgroundFill(color1, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            square.setBackground(new Background(new BackgroundFill(color2, CornerRadii.EMPTY, Insets.EMPTY)));
        }

    }

    private void addPiece(Square square, Piece piece) {
        square.getChildren().add(piece);
        square.occupied = true;
    }

    private void addPieces() {
        if (normalChess) {
            for (Square square : squares) {

                if (square.occupied){ square.getChildren().removeAll(); square.occupied = false; }
                if (square.y == 1) {
                    addPiece(square, new Pawn("black", square.x, square.y));
                } else if (square.y == 6) {
                    addPiece(square, new Pawn("white", square.x, square.y));
                } else if (square.y == 0) {
                    if (square.x == 4) {
                        addPiece(square, new King("black", square.x, square.y));
                    }
                    if (square.x == 3) {
                        addPiece(square, new Queen("black", square.x, square.y));
                    }
                    if (square.x == 2 || square.x == 5) {
                        addPiece(square, new Bishop("black", square.x, square.y));
                    }
                    if (square.x == 1 || square.x == 6) {
                        addPiece(square, new Knight("black", square.x, square.y));
                    }
                    if (square.x == 0 || square.x == 7) {
                        addPiece(square, new Rook("black", square.x, square.y));
                    }
                } else if (square.y == 7) {
                    if (square.x == 4) {
                        addPiece(square, new King("white", square.x, square.y));
                    }
                    if (square.x == 3) {
                        addPiece(square, new Queen("white", square.x, square.y));
                    }
                    if (square.x == 2 || square.x == 5) {
                        addPiece(square, new Bishop("white", square.x, square.y));
                    }
                    if (square.x == 1 || square.x == 6) {
                        addPiece(square, new Knight("white", square.x, square.y));
                    }
                    if (square.x == 0 || square.x == 7) {
                        addPiece(square, new Rook("white", square.x, square.y));
                    }
                }


            }
        } else {
            for (Square square : squares) {

                if (square.occupied) continue;

                Random random = new Random();
                int randomNumber = random.nextInt(6) + 1;

                if (square.y == 0 && square.x == 4) {
                    addPiece(square, new King("black", square.x, square.y));
                    continue;
                }
                if (square.y == 7 && square.x == 4) {
                    addPiece(square, new King("white", square.x, square.y));
                    continue;
                }

                if (square.y == 1 || square.y == 0) {
                    switch (randomNumber) {
                        case 1 -> addPiece(square, new Pawn("black", square.x, square.y));
                        case 2 -> addPiece(square, new Knight("black", square.x, square.y));
                        case 3 -> addPiece(square, new Bishop("black", square.x, square.y));
                        case 4 -> addPiece(square, new Rook("black", square.x, square.y));
                        case 5 -> addPiece(square, new Queen("black", square.x, square.y));
                        default -> addPiece(square, new Pawn("black", square.x, square.y)); // Default fallback.
                    }

                } else if (square.y == 6 || square.y == 7) {
                    switch (randomNumber) {
                        case 1 -> addPiece(square, new Pawn("white", square.x, square.y));
                        case 2 -> addPiece(square, new Knight("white", square.x, square.y));
                        case 3 -> addPiece(square, new Bishop("white", square.x, square.y));
                        case 4 -> addPiece(square, new Rook("white", square.x, square.y));
                        case 5 -> addPiece(square, new Queen("white", square.x, square.y));
                        default -> addPiece(square, new Pawn("white", square.x, square.y)); // Default fallback.
                    }
                }


            }
        }
    }
    public void clearBoard() {
        normalChess = true;
        squares = new ArrayList<>();
        chessBoard.getChildren().clear();
        chessBoard = null;
    }


    public static List<Move> getAllPossibleMoves(boolean isMaximizingPlayer) {
        List<Move> possible = new ArrayList<>();

        for (Square square : ChessBoard.squares) {
            if (!square.getChildren().isEmpty()) {
                Piece piece = (Piece) square.getChildren().get(0);

                // Consider only the current player's pieces
                if (piece.color.equals("white") == isMaximizingPlayer) {
                    piece.getAllPossibleMoves(); // Ensure possibleMoves is populated
                    List<String> possibleMoves = piece.possibleMoves;
                    if (possibleMoves == null) continue;

                    for (String targetSquareName : possibleMoves) {
                        Square targetSquare = getSquareByName(targetSquareName); // Convert name to Square
                        if (targetSquare == null) continue;

                        Piece capturedPiece = targetSquare.getPiece(); // Capture any piece present
                        Move move = new Move(piece, square, targetSquare, capturedPiece);

                        // Simulate the move
                        boolean moved = Piece.movePiece(move.piece, move.to);

                        if (moved) {
                            // Remove captured piece for simulation
                            if (capturedPiece != null) {
                                targetSquare.getChildren().remove(capturedPiece);
                            }

                            // Check if the move leaves the king in check
                            if (!Checkmate.kingInCheck(piece.color)) {
                                possible.add(move); // Add legal move
                            }

                            // Undo the move
                            Piece.undoMove(move.piece, move.from, move.to, capturedPiece);
                        }
                    }
                }
            }
        }

        return possible;
    }




    public List<Piece> getAllPieces() {
        List<Piece> pieces = new ArrayList<>();

        for (Square square : ChessBoard.squares) {
            if (!square.getChildren().isEmpty()) {
                Piece piece = (Piece) square.getChildren().get(0);
                pieces.add(piece);
            }
        }

        return pieces;
    }

    public boolean isGameOver() {
        // Check for checkmate or stalemate
        boolean whiteHasMoves = !getAllPossibleMoves(true).isEmpty();
        boolean blackHasMoves = !getAllPossibleMoves(false).isEmpty();

        if (!whiteHasMoves) {
            // White is in stalemate
            return true; // White is in checkmate
        }

        // Black is in stalemate
        return !blackHasMoves; // Black is in checkmate

        // Additional conditions (e.g., repetition, fifty-move rule) can be added here
    }





}

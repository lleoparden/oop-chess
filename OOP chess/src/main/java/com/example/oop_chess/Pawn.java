package com.example.oop_chess;

import java.util.ArrayList;

import static com.example.oop_chess.Game.turn;


public class Pawn extends Piece {

    ArrayList<String> possibleEnPassantMoves = new ArrayList<>();

    public int lastMovedTurn;

    // Constructor
    public Pawn(String color, int posX, int posY) {
        super(color, posX, posY);
        this.type = "Pawn";
        this.letter = "";
        this.lastMovedTurn = -1; // -1 means not moved yet
        setImage();
    }


    @Override
    public void getAllPossibleMoves() {
        int x = this.posX;
        int y = this.posY;
        ArrayList<String> moves = new ArrayList<>();
        this.possibleMoves = new ArrayList<>();

        // Moves depend on the color of the pawn
        if (color.equals("black")) {
            // Moving straight
            if (getSquareByName("Square" + x + (y + 1)) != null
                    && !getSquareByName("Square" + x + (y + 1)).occupied) {
                moves.add("Square" + x + (y + 1));
            }
            // Initial two-square move
            if (y == 1
                    && getSquareByName("Square" + x + (y + 1)) != null
                    && !getSquareByName("Square" + x + (y + 1)).occupied
                    && getSquareByName("Square" + x + (y + 2)) != null
                    && !getSquareByName("Square" + x + (y + 2)).occupied) {
                moves.add("Square" + x + (y + 2));
            }
            // Diagonal captures
            if (getSquareByName("Square" + (x + 1) + (y + 1)) != null
                    && getSquareByName("Square" + (x + 1) + (y + 1)).occupied
                    && !getPieceByName("Square" + (x + 1) + (y + 1)).getColor().equals(this.color)) {
                moves.add("Square" + (x + 1) + (y + 1));
            }
            if (getSquareByName("Square" + (x - 1) + (y + 1)) != null
                    && getSquareByName("Square" + (x - 1) + (y + 1)).occupied
                    && !getPieceByName("Square" + (x - 1) + (y + 1)).getColor().equals(this.color)) {
                moves.add("Square" + (x - 1) + (y + 1));
            }


            // en passant for black pawn
            if (y == 4) {
                // Check left square for en passant
                if (getSquareByName("Square" + (x - 1) + y) != null
                        && getSquareByName("Square" + (x - 1) + y).occupied
                        && getPieceByName("Square" + (x - 1) + y) instanceof Pawn) {
                    Pawn adjacentPawn = (Pawn) getPieceByName("Square" + (x - 1) + y);
                    // Validate en passant conditions
                    if (adjacentPawn.getColor().equals("white")
                            && adjacentPawn.posY == 4
                            && adjacentPawn.lastMovedTurn == turn-1) {
                        moves.add("Square" + (x - 1) + (y + 1)); // En passant move
                        possibleEnPassantMoves.add("Square" + (x - 1) + (y + 1));
                    }
                }
                // Repeat for the right square
                if (getSquareByName("Square" + (x + 1) + y) != null
                        && getSquareByName("Square" + (x + 1) + y).occupied
                        && getPieceByName("Square" + (x + 1) + y) instanceof Pawn) {
                    Pawn adjacentPawn = (Pawn) getPieceByName("Square" + (x + 1) + y);
                    // Validate en passant conditions
                    if (adjacentPawn.getColor().equals("white")
                            && adjacentPawn.posY == 4
                            && adjacentPawn.lastMovedTurn == turn-1) {
                        moves.add("Square" + (x +1) + (y + 1)); // En passant move
                        possibleEnPassantMoves.add("Square" + (x + 1) + (y + 1));
                    }
                }
            }

        } else { // White pawn
            // Moving straight
            if (getSquareByName("Square" + x + (y - 1)) != null
                    && !getSquareByName("Square" + x + (y - 1)).occupied) {
                moves.add("Square" + x + (y - 1));
            }
            // Initial two-square move
            if (y == 6
                    && getSquareByName("Square" + x + (y - 1)) != null
                    && !getSquareByName("Square" + x + (y - 1)).occupied
                    && getSquareByName("Square" + x + (y - 2)) != null
                    && !getSquareByName("Square" + x + (y - 2)).occupied) {
                moves.add("Square" + x + (y - 2));
            }
            // Diagonal captures
            if (getSquareByName("Square" + (x + 1) + (y - 1)) != null
                    && getSquareByName("Square" + (x + 1) + (y - 1)).occupied
                    && !getPieceByName("Square" + (x + 1) + (y - 1)).getColor().equals(this.color)) {
                moves.add("Square" + (x + 1) + (y - 1));
            }
            if (getSquareByName("Square" + (x - 1) + (y - 1)) != null
                    && getSquareByName("Square" + (x - 1) + (y - 1)).occupied
                    && !getPieceByName("Square" + (x - 1) + (y - 1)).getColor().equals(this.color)) {
                moves.add("Square" + (x - 1) + (y - 1));
            }

            // En Passant for White Pawn (moving on rank 3)
            if (y == 3) {
                // Check left square for en passant
                if (getSquareByName("Square" + (x - 1) + y) != null
                        && getSquareByName("Square" + (x - 1) + y).occupied
                        && getPieceByName("Square" + (x - 1) + y) instanceof Pawn) {
                    Pawn adjacentPawn = (Pawn) getPieceByName("Square" + (x - 1) + y);
                    // Validate en passant conditions
                    if (adjacentPawn.getColor().equals("black")
                            && adjacentPawn.posY == 3
                            && adjacentPawn.lastMovedTurn == turn-1) {
                        moves.add("Square" + (x - 1) + (y - 1)); // En passant move
                        possibleEnPassantMoves.add("Square" + (x - 1) + (y - 1));
                    }
                }
                // Repeat for the right square
                if (getSquareByName("Square" + (x + 1) + y) != null
                        && getSquareByName("Square" + (x + 1) + y).occupied
                        && getPieceByName("Square" + (x + 1) + y) instanceof Pawn) {
                    Pawn adjacentPawn = (Pawn) getPieceByName("Square" + (x + 1) + y);
                    // Validate en passant conditions
                    if (adjacentPawn.getColor().equals("black")
                            && adjacentPawn.posY == 3
                            && adjacentPawn.lastMovedTurn == turn-1) {
                        moves.add("Square" + (x + 1) + (y - 1)); // En passant move
                        possibleEnPassantMoves.add("Square" + (x + 1) + (y - 1));
                    }
                }
            }
        }

        // Update possibleMoves with the validated moves
        this.possibleMoves.addAll(moves);
    }




}
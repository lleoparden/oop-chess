package com.example.oop_chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class King extends Piece{
    public boolean hasMoved = false;
    public boolean castling = true;
    static List<String> kingValidMoves = new ArrayList<>();
    public King(String color, int posX, int posY) {
        super(color, posX, posY);;
        this.type = "King";
        this.letter = "K";
        setImage();
    }
    public boolean hasMoved() {
        return this.hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
        if (hasMoved) {
            this.castling = false; // If the piece has moved, castling is no longer possible
        }
    }

    public boolean canCastle() {
        return this.castling;
    }

    @Override
    public void getAllPossibleMoves() {
        int x = this.posX;
        int y = this.posY;
        ArrayList<String> moves = new ArrayList<>();
        this.possibleMoves = new ArrayList<>();

        moves.add("Square" + (x) + (y-1));
        moves.add("Square" + (x+1) + (y-1));
        moves.add("Square" + (x+1) + (y));
        moves.add("Square" + (x+1) + (y+1));
        moves.add("Square" + (x) + (y+1));
        moves.add("Square" + (x-1) + (y+1));
        moves.add("Square" + (x-1) + (y));
        moves.add("Square" + (x-1) + (y-1));


        boolean condition = Game.isBot && this.color.equals("black");

        if(!this.hasMoved && !condition){

            moves.add("Square" + (x+2) + (y));
            moves.add("Square" + (x-2) + (y));

        }


        for(String move : moves){
            if(getSquareByName(move) != null){
                if(Objects.requireNonNull(getSquareByName(move)).occupied && getPieceByName(move).getColor().equals(color)) continue;

                possibleMoves.add(move);

            }
        }


    }





}

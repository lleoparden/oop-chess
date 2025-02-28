package com.example.oop_chess;

import java.util.ArrayList;
import java.util.Objects;

public class Knight extends Piece{


    public Knight(String color, int posX, int posY) {
        super(color, posX, posY);
        this.type = "Knight";
        this.letter = "N";
        setImage();
    }

    @Override
    public void getAllPossibleMoves() {
        int x = this.posX;
        int y = this.posY;
        ArrayList<String> moves = new ArrayList<>();
        this.possibleMoves = new ArrayList<>();

        moves.add("Square" + (x+2) + (y+1));
        moves.add("Square" + (x+2) + (y-1));
        moves.add("Square" + (x+1) + (y+2));
        moves.add("Square" + (x-1) + (y+2));
        moves.add("Square" + (x-2) + (y+1));
        moves.add("Square" + (x-2) + (y-1));
        moves.add("Square" + (x+1) + (y-2));
        moves.add("Square" + (x-1) + (y-2));


        for(String move : moves){
            if(getSquareByName(move) != null){
                if(Objects.requireNonNull(getSquareByName(move)).occupied && getPieceByName(move).getColor().equals(color)) continue;

                possibleMoves.add(move);

            }
        }


    }

}

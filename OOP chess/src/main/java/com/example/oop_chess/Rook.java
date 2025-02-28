package com.example.oop_chess;



import java.util.ArrayList;

public class Rook extends Piece {

    public boolean hasMoved = false;
    public boolean castling = true;
    public Rook(String color, int posX, int posY) {
        super(color, posX, posY);
        this.type = "Rook";
        this.letter = "R";
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
        String name;

        this.possibleMoves = new ArrayList<>();

        for(int i=x-1; i>=0; i--){
            name = "Square" + i + y;
            if(getSquareByName(name).occupied && getPieceByName(name).getColor().equals(color)) break;

            possibleMoves.add(name);

            if(getSquareByName(name).occupied && !getPieceByName(name).getColor().equals(color)) break;
        }

        for(int i=x+1; i<8; i++){
            name = "Square" + i + y;
            if(getSquareByName(name).occupied && getPieceByName(name).getColor().equals(color)) break;

            possibleMoves.add(name);

            if(getSquareByName(name).occupied && !getPieceByName(name).getColor().equals(color)) break;
        }

        for(int j=y-1; j>=0; j--){
            name = "Square" + x + j;
            if(getSquareByName(name).occupied && getPieceByName(name).getColor().equals(color)) break;

            possibleMoves.add(name);

            if(getSquareByName(name).occupied && !getPieceByName(name).getColor().equals(color)) break;
        }

        for(int j=y+1; j<8; j++){
            name = "Square" + x + j;
            if(getSquareByName(name).occupied && getPieceByName(name).getColor().equals(color)) break;

            possibleMoves.add(name);

            if(getSquareByName(name).occupied && !getPieceByName(name).getColor().equals(color)) break;
        }


    }
}

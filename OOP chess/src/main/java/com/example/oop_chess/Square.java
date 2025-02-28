package com.example.oop_chess;


import javafx.scene.layout.StackPane;
import java.io.Serializable;

public class Square extends StackPane implements Serializable {

    int x, y;
    boolean occupied;
    String name;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
    }

    @Override
    public String toString() {
        return "Square";
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOccupied() {
        return !getChildren().isEmpty();
    }

    public Piece getPiece() {
        if (getChildren().isEmpty()) return null;
        return (Piece) getChildren().get(0);
    }


}

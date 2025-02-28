package com.example.oop_chess;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import static com.example.oop_chess.Game.currentPiece;
import static com.example.oop_chess.Game.lastPiece;
import static com.example.oop_chess.Move.moveHistory;

interface Visuals {

    default void selectPiece() {
        if(currentPiece==null) return;

        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.BLACK);
        borderGlow.setOffsetX(0f);
        borderGlow.setOffsetY(0f);
        currentPiece.setEffect(borderGlow);
        currentPiece.getAllPossibleMoves();
        currentPiece.showAllPossibleMoves(true);
    }

    static void deselectPiece() {

        currentPiece.setEffect(null);
        currentPiece.showAllPossibleMoves(false);
        currentPiece = null;
        if (!moveHistory.isEmpty()) {
            if (moveHistory.get(moveHistory.size() - 1).promotion == null) {
                Game.lastPiece = moveHistory.get(moveHistory.size() - 1).piece;
            }else{
                lastPiece = moveHistory.get(moveHistory.size() - 1).promotion;
            }
        }

    }


}

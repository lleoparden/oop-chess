package com.example.oop_chess;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class CurrentGameState implements Serializable {
   @Serial
   private static final long serialVersionUID = 1L;

   String theme;
   boolean normalChess;
   ArrayList<Move> moveHistory = new ArrayList<>();
   int timer;
   int whiteElapsedTime;
   int blackElapsedTime;
   boolean casual;
   int timeBlack = 0;
   int timeWhite = 0;
   String player;
   String whiteNewMove = "";
   String blackNewMove = "";

   void updateState(String theme, boolean normalChess, ArrayList<Move> moveHistory, int timer, int whiteElapsedTime, int blackElapsedTime, boolean casual, String player,String whiteNewMove,String blackNewMove) {
      this.theme = theme;
      this.normalChess = normalChess;
      this.moveHistory = moveHistory;
      this.timer = timer;
      this.whiteElapsedTime = whiteElapsedTime;
      this.blackElapsedTime = blackElapsedTime;
      this.casual = casual;
      this.player = player;
      this.whiteNewMove = whiteNewMove;
      this.blackNewMove = blackNewMove;
   }
   void updateTimer(int whiteElapsedTime, int blackElapsedTime) {
      this.whiteElapsedTime = whiteElapsedTime;
      this.blackElapsedTime = blackElapsedTime;
   }
}

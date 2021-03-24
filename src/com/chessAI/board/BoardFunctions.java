package com.chessAI.board;

import com.chessAI.gui.Table;

import java.util.Collections;

public class BoardFunctions {
    public static boolean willResultInDraw(Move move){
        Board testBoard = Table.get().getGameBoard().currentPlayer().makeMove(move).getTransistionBoard();
        int numberOfTimes = Collections.frequency(Table.get().getBoardHistory().getBoardList(), testBoard.toString());
        if(numberOfTimes == 2){
            System.out.println("THREEFOLD REPETITION - DRAW");
            return true;
        }else{
        return false;
        }
    }

    public static boolean willBeThreeFold(Move move){
        Board testBoard = Table.get().getGameBoard().currentPlayer().makeMove(move).getTransistionBoard();
        int numberOfTimes = Collections.frequency(Table.get().getBoardHistory().getBoardList(), testBoard.toString());
        if(numberOfTimes == 3){
            System.out.println("THREEFOLD REPETITION - DRAW");
            return true;
        }else{
            return false;
        }
    }

}

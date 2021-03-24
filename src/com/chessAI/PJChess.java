package com.chessAI;

import com.chessAI.board.Board;
import com.chessAI.gui.Table;
import com.chessAI.board.BoardVariations;
import javafx.scene.control.Tab;

public class PJChess {
    public static void main (String args[]){

        Board board;
        board = BoardVariations.createStandardBoard();

        System.out.println(board);

        Table.get().show();
    }
}

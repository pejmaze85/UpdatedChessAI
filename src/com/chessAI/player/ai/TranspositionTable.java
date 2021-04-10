package com.chessAI.player.ai;

import com.chessAI.board.Board;
import com.chessAI.board.Move;

import java.util.HashMap;

public class TranspositionTable {

    public HashMap<Integer, entry> tableList;

    TranspositionTable(){
        this.tableList = new HashMap<>();
    }

    public static class entry{
        int score;
        int depth;
        Board board;

        entry(int score, int depth, Board board){
            this.depth = depth;
            this.score = score;
            this.board = board;
        }


        public int getScore(){
            return this.score;
        }

        public int getDepth(){
            return this.depth;
        }
    }


}

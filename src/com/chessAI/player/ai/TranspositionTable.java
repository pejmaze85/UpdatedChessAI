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

        entry(int score, int depth){
            this.depth = depth;
            this.score = score;
        }


        public int getScore(){
            return this.score;
        }

        public int getDepth(){
            return this.depth;
        }
    }


}

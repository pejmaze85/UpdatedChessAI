package com.chessAI.player.ai;

import com.chessAI.board.Board;

import java.util.HashMap;

public class TranspositionTable {

    public HashMap<Long, entry> tableList;

    TranspositionTable(){

        this.tableList = new HashMap<>();

    }

    public static class entry{
        int score;
        int depth;
        int alpha;
        int beta;

        entry(int score, int depth, int alpha, int beta){
            this.alpha = alpha;
            this.beta = beta;
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

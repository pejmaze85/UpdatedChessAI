package com.chessAI.board;

import java.security.SecureRandom;

public class Zobrist {
    long pieceMap[][] = new long[64][16];
    SecureRandom rand = new SecureRandom();

    public Zobrist(){
        for(int i = 0; i < 64; i++){
            for(int x = 0; x < 16; x++){
                pieceMap[i][x] = rand.nextLong();
            }
        }
    }
}

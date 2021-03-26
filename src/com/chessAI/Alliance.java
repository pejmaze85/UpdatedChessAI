package com.chessAI;

import com.chessAI.board.BoardUtils;
import com.chessAI.player.BlackPlayer;
import com.chessAI.player.Player;
import com.chessAI.player.WhitePlayer;

public enum Alliance {
    WHITE{
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.EIGHTH_RANK[position];
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
        return whitePlayer;
        }


        @Override
        public String toString() {
            return "White";
        }

        @Override
        public int pawnBonus(final int position) {
            return W_PAWN_BONUS_TILES[position];
        }

        @Override
        public int pawnEndgameBonus(final int position){
            return W_PAWN_BONUS_ENDGAME_TILES[position];
        }

        @Override
        public int knightBonus(final int position) {
            return W_KNIGHT_BONUS_TILES[position];
        }

        @Override
        public int bishopBonus(final int position) {
            return W_BISHOP_BONUS_TILES[position];
        }

        @Override
        public int rookBonus(final int position) {
            return W_ROOK_BONUS_TILES[position];
        }

        @Override
        public int queenBonus(final int position) {
            return W_QUEEN_BONUS_TILES[position];
        }

        @Override
        public int kingBonus(final int position) {
            return W_KING_BONUS_TILES[position];
        }

        @Override
        public  int kingEndgameBonus(final int position) {return B_KING_BONUS_ENDGAME_TILES[position];}


    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.FIRST_RANK[position];
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {

            return blackPlayer;
        }

        @Override
        public int pawnBonus(final int position) {
            return B_PAWN_BONUS_TILES[position];
        }

        @Override
        public int pawnEndgameBonus(final int position){
            return B_PAWN_BONUS_ENDGAME_TILES[position];
        }

        @Override
        public int knightBonus(final int position) {
            return B_KNIGHT_BONUS_TILES[position];
        }

        @Override
        public int bishopBonus(final int position) {
            return B_BISHOP_BONUS_TILES[position];
        }

        @Override
        public int rookBonus(final int position) {
            return B_ROOK_BONUS_TILES[position];
        }

        @Override
        public int queenBonus(final int position) {
            return B_QUEEN_BONUS_TILES[position];
        }

        @Override
        public int kingBonus(final int position) {
            return B_KING_BONUS_TILES[position];
        }

        @Override
        public  int kingEndgameBonus(final int position) {return B_KING_BONUS_ENDGAME_TILES[position];}


    };

    public abstract int kingEndgameBonus(int piecePosition);

    public abstract int pawnEndgameBonus(int piecePosition);

    public abstract int getDirection();

    public abstract int getOppositeDirection();

    public abstract int pawnBonus(int position);

    public abstract int knightBonus(int position);

    public abstract int bishopBonus(int position);

    public abstract int rookBonus(int position);

    public abstract int queenBonus(int position);

    public abstract int kingBonus(int position);

    public abstract boolean isWhite();

    public abstract boolean isBlack();

    public abstract boolean isPawnPromotionSquare(int position);

    public abstract Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);

    private final static int[] W_PAWN_BONUS_TILES = {
            0,  0,  0,  0,  0,  0,  0,  0,
            75, 75, 75, 75, 75, 75, 75, 75,
            25, 25, 29, 29, 29, 29, 25, 25,
            5,  5, 10, 55, 55, 10,  5,  5,
            0,  0,  0, 40, 40,  0,  0,  0,
            5, -5,-10,  0,  0,-10, -5,  5,
            5, 10, 10,-20,-20, 10, 10,  5,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    private final static int[] B_PAWN_BONUS_TILES = {
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 10, 10,-20,-20, 10, 10,  5,
            5, -5,-10,  0,  0,-10, -5,  5,
            0,  0,  0, 40, 40,  0,  0,  0,
            5,  5, 10, 55, 55, 10,  5,  5,
            25, 25, 29, 29, 29, 29, 25, 25,
            75, 75, 75, 75, 75, 75, 75, 75,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    private final static int[] W_PAWN_BONUS_ENDGAME_TILES = {
            100,  100,  100,  100,  100,  100,  100,  100,
            50, 50, 50,50,50, 50, 50,  50,
            50, 50, 50,50,50, 50, 50,  50,
            50, 50, 50,50,50, 50, 50,  50,
            20,  20, 20, 20, 20, 20,  20,  20,
            25, 25, 29, 29, 29, 29, 25, 25,
            75, 75, 75, 75, 75, 75, 75, 75,
            0,  0,  0,  0,  0,  0,  0,  0,
    };

    private final static int[] B_PAWN_BONUS_ENDGAME_TILES = {
            0,  0,  0,  0,  0,  0,  0,  0,
            75, 75, 75, 0, 0, 75, 75, 75,
            0, 0, 0, 0, 0, 0, 0, 0,
            20,  20, 20, 20, 20, 20,  20,  20,
            50,  50,  50, 50, 50,  50,  50,  50,
            50, 50,50,  50,  50,50, 50,  50,
            50, 50, 50,50,50, 50, 50,  50,
            100,  100,  100,  100,  100,  100,  100,  100
    };

    private final static int[] W_KNIGHT_BONUS_TILES = {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50
    };

    private final static int[] B_KNIGHT_BONUS_TILES = {
            -50,-40,-30,-30,-30,-30,-40,-50,
            -40,-20,  0,  5,  5,  0,-20,-40,
            -30,  5, 10, 15, 15, 10,  5,-30,
            -30,  0, 15, 20, 20, 15,  0,-30,
            -30,  5, 15, 20, 20, 15,  5,-30,
            -30,  0, 10, 15, 15, 10,  0,-30,
            -40,-20,  0,  0,  0,  0,-20,-40,
            -50,-40,-30,-30,-30,-30,-40,-50,
    };

    private final static int[] W_BISHOP_BONUS_TILES = {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -20,-10,-10,-10,-10,-10,-10,-20
    };

    private final static int[] B_BISHOP_BONUS_TILES = {
            -20,-10,-10,-10,-10,-10,-10,-20,
            -10,  5,  0,  0,  0,  0,  5,-10,
            -10, 10, 10, 10, 10, 10, 10,-10,
            -10,  0, 10, 10, 10, 10,  0,-10,
            -10,  5,  5, 10, 10,  5,  5,-10,
            -10,  0,  5, 10, 10,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10,-10,-10,-10,-10,-20,
    };

    private final static int[] W_ROOK_BONUS_TILES = {
            0,  0,  0,  0,  0,  0,  0,  0,
            5, 20, 20, 20, 20, 20, 20,  5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            0,  0,  0,  10,  10,  0,  0,  0
    };

    private final static int[] B_ROOK_BONUS_TILES = {
            0,  0,  0,  10,  10,  0,  0,  0,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            -5,  0,  0,  0,  0,  0,  0, -5,
            5, 20, 20, 20, 20, 20, 20,  5,
            0,  0,  0,  0,  0,  0,  0,  0,
    };

    private final static int[] W_QUEEN_BONUS_TILES = {
            -20,-10,-10, -5, -5,-10,-10,-20,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -5,  0,  5,  5,  5,  5,  0, -5,
            0,  0,  5,  5,  5,  5,  0, -5,
            -10,  5,  5,  5,  5,  5,  0,-10,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -20,-10,-10, -5, -5,-10,-10,-20
    };

    private final static int[] B_QUEEN_BONUS_TILES = {
            -20,-10,-10, 0, 0,-10,-10,-20,
            -10,  0,  5,  0,  0,  0,  0,-10,
            -10,  5,  5,  5,  5,  5,  0,-10,
            0,  0,  5,  5,  5,  5,  0, -5,
            0,  0,  5,  5,  5,  5,  0, -5,
            -10,  0,  5,  5,  5,  5,  0,-10,
            -10,  0,  0,  0,  0,  0,  0,-10,
            -20,-10,-10, 0, 0,-10,-10,-20
    };

    private final static int[] W_KING_BONUS_TILES = {
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            20, 20,  0,  0,  0,  0, 20, 20,
            20, 40, 10,  0,  0, 10, 40, 20
    };

    private final static int[] B_KING_BONUS_TILES = {
            20, 40, 10,  0,  0, 10, 40, 20,
            20, 20,  0,  0,  0,  0, 20, 20,
            -10,-20,-20,-20,-20,-20,-20,-10,
            -20,-30,-30,-40,-40,-30,-30,-20,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30,
            -30,-40,-40,-50,-50,-40,-40,-30
    };

    private final static int[] B_KING_BONUS_ENDGAME_TILES = {
            -0,-0,-0,-0,-0,-0,-0,-0,
            -0,-0,  0,  0,  0,  0,-0,-0,
            -0,  0, 50, 50, 50, 50,  0,-0,
            -0,  0, 50, 100, 100, 50,  0,-0,
            -0,  0, 50, 100, 100, 50,  0,-0,
            -0,  0, 50, 50, 50, 50,  0,-0,
            -0,-0,  0,  0,  0,  0,-0,-0,
            -0,-0,-0,-0,-0,-0,-0,-0,
    };

}

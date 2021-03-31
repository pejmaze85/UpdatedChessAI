package com.chessAI.player.ai.ScorePieces;

import com.chessAI.Alliance;
import com.chessAI.board.Board;
import com.chessAI.board.BoardUtils;
import com.chessAI.piece.King;
import com.chessAI.piece.Piece;
import com.chessAI.player.Player;

import static com.chessAI.player.ai.StandardBoardEvaluator.getGameState;

public class ScoreBishop {

    private final static int CHECK_BONUS = 45;
    private final static int MOBILITY_MULTIPLIER = 5;
    private final static int CHECK_MATE_BONUS = 10000;
    private final static int DEPTH_BONUS = 100;
    private final static int CASTLE_BONUS = 60;
    private static final int ACTIVE_PIECE_BONUS = 10;
    private static int PAWNPROMOTE_BONUS = 100;
    private final static int TWO_BISHOP_BONUS = 25;
    private final static int ATTACK_MULTIPLIER = 2;
    private final static int PASSED_PAWN_BONUS = 100;

    public static int scoreBishop(Board board, Piece piece) {
        Player player = board.currentPlayer();
        int score = 0;

        return score;
    }

}

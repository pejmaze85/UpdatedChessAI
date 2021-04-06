package com.chessAI.player.ai.ScorePieces;

import com.chessAI.board.Board;
import com.chessAI.piece.Piece;
import com.chessAI.player.Player;
import com.chessAI.player.ai.StandardBoardEvaluator;
import com.chessAI.player.ai.StandardBoardEvaluator.GAMESTATE;

import static com.chessAI.player.ai.StandardBoardEvaluator.getGameState;

public class ScoreQueen {

    public static int scoreQueen(Board board, Piece piece) {
        Player player = board.currentPlayer();
        int score = 0;

        if(getGameState(board) == GAMESTATE.OPENING){
            if(piece.isFirstMove()){                // MAS'UH NO BRING THE QUEEN OUT TOO EARLY
                score += 100;
            }
        }

        return score;
    }
}

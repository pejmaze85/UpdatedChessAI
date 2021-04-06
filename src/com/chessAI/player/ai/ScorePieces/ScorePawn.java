package com.chessAI.player.ai.ScorePieces;

import com.chessAI.Alliance;
import com.chessAI.board.Board;
import com.chessAI.board.BoardUtils;
import com.chessAI.piece.King;
import com.chessAI.piece.Pawn;
import com.chessAI.piece.Piece;
import com.chessAI.player.Player;
import com.chessAI.player.ai.StandardBoardEvaluator;

import static com.chessAI.player.ai.StandardBoardEvaluator.*;
import static com.chessAI.player.ai.StandardBoardEvaluator.getGameState;

public class ScorePawn {

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

    public static int scorePawn(Board board, Piece piece) {
        Player player = board.currentPlayer();
        int score = 0;
        int oppPieceCount = board.currentPlayer().getOpponent().getActivePieces().size();
        GAMESTATE gamestate = getGameState(board);

        if (gamestate == GAMESTATE.ENDGAME) {
            PAWNPROMOTE_BONUS = 150;
        }
        if ((BoardUtils.EIGHTH_RANK[piece.getPiecePosition()] || BoardUtils.FIRST_RANK[piece.getPiecePosition()])
                && (player.calculateAttacksOnTile(piece.getPiecePosition(), player.getOpponent().getLegalMoves()).isEmpty())) {
            score = score + PAWNPROMOTE_BONUS;
        }

        if (piece.isPassedPawn()) {
            score = score + (PASSED_PAWN_BONUS);
            if (piece.isProtected(player, board)) {
                if (piece.getPieceAlliance() == Alliance.WHITE) {
                    if (piece.getPieceRow() == 6) {
                        score += 25;
                        if(oppPieceCount < 5){
                            score += 50;
                        }
                    }
                    if (piece.getPieceRow() == 7) {
                        score += 50;
                        if(oppPieceCount < 5){
                            score += 50;
                        }
                    }
                    if (piece.getPieceRow() == 8) {
                        score += 50;
                        if(oppPieceCount < 5){
                            score += 50;
                        }
                    }
                }
                if (piece.getPieceAlliance() == Alliance.BLACK) {
                    if (piece.getPieceRow() == 3) {
                        score += 25;
                        if(oppPieceCount < 5){
                            score += 50;
                        }
                    }
                    if (piece.getPieceRow() == 2) {
                        score += 50;
                        if(oppPieceCount < 5){
                            score += 50;
                        }
                    }
                    if (piece.getPieceRow() == 1) {
                        score += 50;
                        if(oppPieceCount < 5){
                            score += 50;
                        }
                    }
                }
            }
        }

        King playerKing = board.currentPlayer().getPlayerKing();
        if (playerKing.isCastled()) {                 // ADD LOCATION BONUS TO PAWNS PROTECTING A CASTLED KING
            // WHITE
            if (player.getAlliance().isWhite() &&
                    (playerKing.getPiecePosition() >= 56 && playerKing.getPiecePosition() <= 58)) {
                if ((piece.getPiecePosition() >= 48 && piece.getPiecePosition() <= 50) || piece.getPiecePosition() == 41) {
                    score += 25;
                }

            }
        } else if (player.getAlliance().isWhite() &&
                (playerKing.getPiecePosition() >= 61 && playerKing.getPiecePosition() <= 63)) {
            if ((piece.getPiecePosition() >= 53 && piece.getPiecePosition() <= 55) || piece.getPiecePosition() == 46) {
                score += 25;
            }
            // BLACK
            if (playerKing.isCastled()) {
                if (player.getAlliance().isBlack() &&
                        (playerKing.getPiecePosition() >= 0 && playerKing.getPiecePosition() <= 2)) {
                    if ((piece.getPiecePosition() >= 8 && piece.getPiecePosition() <= 10) || piece.getPiecePosition() == 17) {
                        score += 25;
                    }

                }
            } else if (player.getAlliance().isWhite() &&
                    (playerKing.getPiecePosition() >= 5 && playerKing.getPiecePosition() <= 7)) {
                if ((piece.getPiecePosition() >= 13 && piece.getPiecePosition() <= 15) || piece.getPiecePosition() == 22) {
                    score += 25;
                }
            }

        }

        return score;
    }


}

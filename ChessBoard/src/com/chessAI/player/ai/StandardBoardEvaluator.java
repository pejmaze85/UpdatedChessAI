package com.chessAI.player.ai;

import com.chessAI.board.Board;
import com.chessAI.board.BoardUtils;
import com.chessAI.board.Move;
import com.chessAI.gui.Table;
import com.chessAI.piece.Piece;
import com.chessAI.player.Player;


public final class StandardBoardEvaluator implements BoardEvaluator {

    private final static int CHECK_BONUS = 45;
    private final static int MOBILITY_MULTIPLIER = 5;
    private final static int CHECK_MATE_BONUS = 10000;
    private final static int DEPTH_BONUS = 100;
    private final static int CASTLE_BONUS = 60;
    private static int PAWNPROMOTE_BONUS = 100;
    private final static int TWO_BISHOP_BONUS = 25;
    private final static int ATTACK_MULTIPLIER = 1;

    public enum GAMESTATE{OPENING, MIDGAME, ENDGAME, UNSURE}


    @Override
    public int evaluate(final Board board, final int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) - scorePlayer(board, board.blackPlayer(), depth);
    }

    public static int getScore(final Board board, final Player player){
        return scorePlayer(board, player, 4);
    }

    public static GAMESTATE getGameState(final Board board){

        if(board.getAllPieces().size() < 14) {
            return GAMESTATE.ENDGAME;
        }else if((board.getAllPieces().size() >= 14 && board.getAllPieces().size() <= 22) || Table.get().moveCount > 15){
            return GAMESTATE.MIDGAME;
        }else if((board.getAllPieces().size() > 26 ) || Table.get().moveCount < 15){
            return GAMESTATE.OPENING;
        }

        return GAMESTATE.UNSURE;

    }

    private static int scorePlayer(final Board board, final Player player, final int depth) {

        return pieceValueAndTileScore(player, board)
        + kingThreats(player, depth)
        + attacks(player)
        + mobility(player)
        + castled(player)
        + pawnPromoteBonus(player, board)
        + pawnStructure(player);

    }


    private static int attacks(final Player player) {
        int attackScore = 0;
        for(final Move move : player.getLegalMoves()) {
            if(move.isAttack()) {
                final Piece movedPiece = move.getMovePiece();
                final Piece attackedPiece = move.getAttackedPiece();
                if(movedPiece.getPieceValue() <= attackedPiece.getPieceValue()) {
                    attackScore++;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }

    private static int pieceValueAndTileScore(final Player player, final Board board) {
        int pieceValuationScore = 0;
        int numBishops = 0;
        GAMESTATE gameState = getGameState(board);
        for (final Piece piece : player.getActivePieces()) {
            pieceValuationScore += piece.getPieceValue();
                    if(gameState != GAMESTATE.ENDGAME){
                        pieceValuationScore += piece.locationBonus();
                    }
            if(piece.getPieceType() == Piece.PieceType.BISHOP) {
                numBishops++;
            }
        }
        return pieceValuationScore + (numBishops == 2 ? TWO_BISHOP_BONUS : 0);
    }


    private static int pawnPromoteBonus(Player player, Board board) {
        if(getGameState(board) == GAMESTATE.ENDGAME) {
            PAWNPROMOTE_BONUS = 600;
        }
        int bonus = 0;
        for(Piece piece : player.getActivePieces()){
            if(piece.getPieceType() == Piece.PieceType.PAWN){
                if((BoardUtils.EIGHTH_RANK[piece.getPiecePosition()] || BoardUtils.FIRST_RANK[piece.getPiecePosition()]) && (player.calculateAttacksOnTile(piece.getPiecePosition(), player.getOpponent().getLegalMoves()).isEmpty())){
                    bonus = bonus + PAWNPROMOTE_BONUS;
                }
            }
        }
        return bonus;
    }

    private static int castled(Player player) {
        return player.getPlayerKing().isCastled() ? CASTLE_BONUS : 0;
    }

    private static int checkmate(Player player, int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) :  0;
    }

    private static int depthBonus(int depth){
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int check(Player player) {
        return player.getOpponent().isInCheck() ?  CHECK_BONUS :  0;
    }

    private static int pawnStructure(final Player player) {
        return PawnStructureAnalyzer.get().pawnStructureScore(player);
    }

    private static int kingThreats(final Player player,
                                   final int depth) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS  * depthBonus(depth) : check(player);
    }

    private static int mobility(final Player player) {
        return MOBILITY_MULTIPLIER * mobilityRatio(player);
    }

    private static int mobilityRatio(final Player player) {
        return (int)((player.getLegalMoves().size() * 10.0f) / player.getOpponent().getLegalMoves().size());
    }

}

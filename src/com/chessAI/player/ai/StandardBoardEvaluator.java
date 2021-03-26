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
        GAMESTATE gamestate = getGameState(board);
        return scorePlayer(board, board.whitePlayer(), depth, gamestate) - scorePlayer(board, board.blackPlayer(), depth, gamestate);
    }

    public static int getScore(final Board board, final Player player, GAMESTATE gamestate){
        gamestate = getGameState(board);
        return scorePlayer(board, player, 5, gamestate);
    }

    public static GAMESTATE getGameState(final Board board){

        if(board.getAllPieces().size() < 18) {
            return GAMESTATE.ENDGAME;
        }else if((board.getAllPieces().size() >= 18 && board.getAllPieces().size() <= 22) || Table.get().moveCount > 15){
            return GAMESTATE.MIDGAME;
        }else if((board.getAllPieces().size() > 22 ) || Table.get().moveCount < 15){
            return GAMESTATE.OPENING;
        }

        return GAMESTATE.UNSURE;

    }

    private static int scorePlayer(final Board board, final Player player, final int depth, GAMESTATE gameState) {

        return pieceValueAndTileScore(player, board, gameState)
        + kingThreats(player, depth)
        + attacks(player, gameState)
        + mobility(player)
        + castled(player)
        + checkmate(player, 4)
        + pawnStructure(player);

    }


    private static int attacks(final Player player, final GAMESTATE gamestate) {
        int attackScore = 0;
        int movedPieceValue;
        int attackedPieceValue;
        for(final Move move : player.getLegalMoves()) {
            if(move.isAttack()) {
                if (gamestate == GAMESTATE.ENDGAME){
                    movedPieceValue = move.getMovePiece().getPieceEndgameValue();
                    attackedPieceValue = move.getAttackedPiece().getPieceEndgameValue();
                }else {
                    movedPieceValue = move.getMovePiece().getPieceValue();
                    attackedPieceValue = move.getAttackedPiece().getPieceValue();
                }
                if(movedPieceValue <= attackedPieceValue) {
                    attackScore++;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }

    private static int pieceValueAndTileScore(final Player player, final Board board, final GAMESTATE gamestate) {
        int pieceValuationScore = 0;
        int numBishops = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValuationScore += piece.getPieceValue();
                    if(gamestate == GAMESTATE.ENDGAME){
                        pieceValuationScore += piece.endgameLocationBonus();
                    }else{
                        pieceValuationScore += piece.locationBonus();
                    }
            if(piece.getPieceType() == Piece.PieceType.BISHOP) {
                numBishops++;
            }
            if(piece.getPieceType() == Piece.PieceType.PAWN){  // PAWN BONUS
                int bonus = 0;
                if(getGameState(board) == GAMESTATE.ENDGAME) {
                    PAWNPROMOTE_BONUS = 600;
                }
                if((BoardUtils.EIGHTH_RANK[piece.getPiecePosition()] || BoardUtils.FIRST_RANK[piece.getPiecePosition()]) && (player.calculateAttacksOnTile(piece.getPiecePosition(), player.getOpponent().getLegalMoves()).isEmpty())){
                    pieceValuationScore = pieceValuationScore + bonus + PAWNPROMOTE_BONUS;
                }
            }
        }
        return pieceValuationScore + (numBishops == 2 ? TWO_BISHOP_BONUS : 0);
    }


/*    private static int pawnPromoteBonus(Player player, Board board) {
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
*/
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

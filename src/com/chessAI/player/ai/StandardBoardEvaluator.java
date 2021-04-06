package com.chessAI.player.ai;

import com.chessAI.Alliance;
import com.chessAI.board.Board;
import com.chessAI.board.BoardUtils;
import com.chessAI.board.Move;
import com.chessAI.gui.Table;
import com.chessAI.piece.Piece;
import com.chessAI.player.Player;

import static com.chessAI.player.ai.ScorePieces.ScoreBishop.*;
import static com.chessAI.player.ai.ScorePieces.ScoreKnight.*;
import static com.chessAI.player.ai.ScorePieces.ScorePawn.*;
import static com.chessAI.player.ai.ScorePieces.ScoreQueen.*;
import static com.chessAI.player.ai.ScorePieces.ScoreRook.*;


public final class StandardBoardEvaluator implements BoardEvaluator {

    private final static int CHECK_BONUS = 45;
    private final static int MOBILITY_MULTIPLIER = 5;
    private final static int CHECK_MATE_BONUS = 10000;
    private final static int DEPTH_BONUS = 100;
    private final static int CASTLE_BONUS = 60;
    private static final int ACTIVE_PIECE_BONUS = 10;
    private static final int TWO_ROOK_BONUS = 50;
    private static int PAWNPROMOTE_BONUS = 100;
    private final static int TWO_BISHOP_BONUS = 50;
    private final static int ATTACK_MULTIPLIER = 2;
    private final static int PASSED_PAWN_BONUS = 100;

    public enum GAMESTATE{OPENING, MIDGAME, ENDGAME, UNSURE}

    public static void debugScorePlayer(Board board){
        Table.get().debugWindow.addToText("===================WHITE===================\n");
        Table.get().debugWindow.addToText("Piece Value = " + pieceValueScore(board.whitePlayer(), board, getGameState(board)) + "\n");
        Table.get().debugWindow.addToText("Piece Position = " + pieceTileScore(board.whitePlayer(), board, getGameState(board)) + "\n");
        Table.get().debugWindow.addToText("Pawn Bonus = " + pawnBonus(board.whitePlayer(), board) + "\n");
        Table.get().debugWindow.addToText("King Threats = " + kingThreats(board.whitePlayer()) + "\n");
        Table.get().debugWindow.addToText("Attacks = " + attacks(board.whitePlayer(), getGameState(board)) + "\n");
        Table.get().debugWindow.addToText("Mobility = " + mobility(board.whitePlayer()) + "\n");
        Table.get().debugWindow.addToText("Active Piece Bonus = " + activatePieces(board.whitePlayer(), board) + "\n");
        Table.get().debugWindow.addToText("Castled = " + castled(board.whitePlayer()) + "\n");
        Table.get().debugWindow.addToText("Checkmate = " + checkmate(board.whitePlayer(), 4) + "\n");
        Table.get().debugWindow.addToText("Pawn Structure = " + pawnStructure(board.whitePlayer()) + "\n");

        Table.get().debugWindow.addToText("===================BLACK===================\n");
        Table.get().debugWindow.addToText("Piece Value = " + pieceValueScore(board.blackPlayer(), board, getGameState(board)) + "\n");
        Table.get().debugWindow.addToText("Piece Position = " + pieceTileScore(board.blackPlayer(), board, getGameState(board)) + "\n");
        Table.get().debugWindow.addToText("Pawn Bonus = " + pawnBonus(board.blackPlayer(), board) + "\n");
        Table.get().debugWindow.addToText("King Threats = " + kingThreats(board.blackPlayer()) + "\n");
        Table.get().debugWindow.addToText("Attacks = " + attacks(board.blackPlayer(), getGameState(board)) + "\n");
        Table.get().debugWindow.addToText("Mobility = " + mobility(board.blackPlayer()) + "\n");
        Table.get().debugWindow.addToText("Active Piece Bonus = " + activatePieces(board.blackPlayer(), board) + "\n");
        Table.get().debugWindow.addToText("Castled = " + castled(board.blackPlayer()) + "\n");
        Table.get().debugWindow.addToText("Checkmate = " + checkmate(board.blackPlayer(), 4) + "\n");
        Table.get().debugWindow.addToText("Pawn Structure = " + pawnStructure(board.blackPlayer()) + "\n");
    }

    private static int scorePlayer(final Board board, final Player player, final int depth, GAMESTATE gameState) {

        return pieceAndTileScore(player, board, gameState)
                + kingThreats(player)
                + attacks(player, gameState)
                + mobility(player)
                + activatePieces(player, board)
                + castled(player)
                + checkmate(player, depth)
                + pawnStructure(player);

    }

    @Override
    public int evaluate(final Board board, final int depth) {
        GAMESTATE gamestate = getGameState(board);
        return scorePlayer(board, board.whitePlayer(), depth, gamestate) - scorePlayer(board, board.blackPlayer(), depth, gamestate);
    }

    public static int getScore(final Board board, final Player player, GAMESTATE gamestate){
        return scorePlayer(board, player, 5, gamestate);
    }

    public static GAMESTATE getGameState(final Board board){
        int pieceCount = board.getAllPieces().size();

        if(pieceCount < 18) {
            return GAMESTATE.ENDGAME;
        }else if(pieceCount >= 18 && pieceCount <= 22){
            return GAMESTATE.MIDGAME;
        }else if(pieceCount > 22 ){
            return GAMESTATE.OPENING;
        }

        return GAMESTATE.UNSURE;

    }

    private static int attacks(final Player player, final GAMESTATE gamestate) {
        int attackScore = 0;
        int movedPieceValue;
        int attackedPieceValue;
        for(final Move move : player.getLegalMoves()) {
            if(move.isAttack()) {
                if (gamestate == GAMESTATE.ENDGAME){
                    movedPieceValue = move.getMovePiece().getEGPieceValue();
                    attackedPieceValue = move.getAttackedPiece().getEGPieceValue();
                }else {
                    movedPieceValue = move.getMovePiece().getOGPieceValue();
                    attackedPieceValue = move.getAttackedPiece().getOGPieceValue();
                }
                if(movedPieceValue <= attackedPieceValue) {
                    attackScore++;
                }
                if(move.getAttackedPiece().getPieceType() == Piece.PieceType.QUEEN){
                    attackScore += 20;
                }
            }
        }
        return attackScore * ATTACK_MULTIPLIER;
    }

    private static int pieceAndTileScore(final Player player, final Board board, final GAMESTATE gamestate){
        int pieceValuationScore = 0;
        int numBishops = 0;
        int numRooks = 0;

        for (final Piece piece : player.getActivePieces()) {
            if(gamestate == GAMESTATE.ENDGAME){                 // ENDGAME VALUE
                pieceValuationScore += piece.getEGLocationBonus();
                pieceValuationScore += piece.getEGPieceValue();
            }else if (gamestate == GAMESTATE.MIDGAME){          // MIDGAME VALUE
                pieceValuationScore += piece.getMGLocationBonus();
                pieceValuationScore += piece.getMGPieceValue();
            }else if (gamestate == GAMESTATE.OPENING){          // OPENING VALUE
                pieceValuationScore += piece.getOGLocationBonus();
                pieceValuationScore += piece.getOGPieceValue();
            }

            //=================== PAWNS ======================

            if(piece.getPieceType() == Piece.PieceType.PAWN){
                pieceValuationScore += scorePawn(board, piece);
            }

            //=================== KNIGHTS ======================

            if(piece.getPieceType() == Piece.PieceType.KNIGHT){
                pieceValuationScore += scoreKnight(board, piece);
            }

            //=================== BISHOPS ======================

            if(piece.getPieceType() == Piece.PieceType.BISHOP) {
                pieceValuationScore += scoreBishop(board, piece);
                numBishops++;
            }

            //=================== ROOKS ======================

            if(piece.getPieceType() == Piece.PieceType.ROOK) {
                pieceValuationScore += scoreRook(board, piece);
                numRooks ++;
            }

            //=================== QUEENS ======================

            if(piece.getPieceType() == Piece.PieceType.QUEEN) {
                pieceValuationScore += scoreQueen(board, piece);
                numRooks ++;
            }

        }
        return  pieceValuationScore
                + (numBishops == 2 ? TWO_BISHOP_BONUS : 0)
                + (numRooks == 2 ? TWO_ROOK_BONUS : 0);
    }

    private static int pieceValueScore(final Player player, final Board board, final GAMESTATE gamestate) {
        int pieceValuationScore = 0;
        int numBishops = 0;
        int numRooks = 0;

        for (final Piece piece : player.getActivePieces()) {
            if (gamestate == GAMESTATE.ENDGAME) {                 // ENDGAME VALUE
                pieceValuationScore += piece.getEGPieceValue();
            } else if (gamestate == GAMESTATE.MIDGAME) {          // MIDGAME VALUE
                pieceValuationScore += piece.getMGPieceValue();
            } else if (gamestate == GAMESTATE.OPENING) {          // OPENING VALUE
                pieceValuationScore += piece.getOGPieceValue();
            }
        }
        return  pieceValuationScore
                + (numBishops == 2 ? TWO_BISHOP_BONUS : 0)
                + (numRooks == 2 ? TWO_ROOK_BONUS : 0);
    }

    private static int pieceTileScore(final Player player, final Board board, final GAMESTATE gamestate) {
        int pieceValuationScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            if (gamestate == GAMESTATE.ENDGAME) {                 // ENDGAME VALUE
                pieceValuationScore += piece.getEGLocationBonus();
            } else if (gamestate == GAMESTATE.MIDGAME) {          // MIDGAME VALUE
                pieceValuationScore += piece.getMGLocationBonus();
            } else if (gamestate == GAMESTATE.OPENING) {          // OPENING VALUE
                pieceValuationScore += piece.getOGLocationBonus();
            }
        }
        return pieceValuationScore;
    }

    private static int pawnBonus(Player player, Board board){
        int score = 0;
        for(Piece piece : player.getActivePieces()){
            if(piece.getPieceType() == Piece.PieceType.PAWN){  // PAWN BONUS
                int bonus = 0;
                if(getGameState(board) == GAMESTATE.ENDGAME) {
                    PAWNPROMOTE_BONUS = 200;
                }
                if((BoardUtils.EIGHTH_RANK[piece.getPiecePosition()] || BoardUtils.FIRST_RANK[piece.getPiecePosition()]) && (player.calculateAttacksOnTile(piece.getPiecePosition(), player.getOpponent().getLegalMoves()).isEmpty())){
                    score = score + bonus + PAWNPROMOTE_BONUS;
                }

                if (piece.isPassedPawn()) {
                    score = score + (PASSED_PAWN_BONUS);
                    if (piece.isProtected(player, board)){
                        score += 100;
                        System.out.println("PROTECTED");
                    }
                    if (piece.getPieceAlliance() == Alliance.WHITE) {
                        if (piece.getPieceRow() == 6) {
                            score += 50;
                        }
                        if (piece.getPieceRow() == 7) {
                            score += 100;
                        }
                    }
                    if (piece.getPieceAlliance() == Alliance.BLACK) {
                        if (piece.getPieceRow() == 3) {
                            score += 50;
                        }
                        if (piece.getPieceRow() == 2) {
                            score += 100;
                        }
                    }
                }
            }
        }
        return score;
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

    private static int kingThreats(final Player player) {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS : check(player);
    }

    public static int activatePieces(Player player, Board board){
        int activePieces = 0;
        if(getGameState(board) == GAMESTATE.OPENING){
            for (Piece piece : player.getActivePieces()){
                if(!piece.isFirstMove()){
                    activePieces ++;
                }
            }
        }
        return activePieces * ACTIVE_PIECE_BONUS;
    }

    private static int mobility(final Player player) {
        return MOBILITY_MULTIPLIER * mobilityRatio(player);
    }

    private static int mobilityRatio(final Player player) {
        int moveCount = 0;
        for (Move move: player.getLegalMoves()){
            if (move.getMovePiece().getPieceType() != Piece.PieceType.PAWN){
                moveCount ++;
            }
        }

        return moveCount;
    }

}

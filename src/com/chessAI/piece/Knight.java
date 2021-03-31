package com.chessAI.piece;

import com.chessAI.Alliance;
import com.chessAI.board.Board;
import com.chessAI.board.BoardUtils;
import com.chessAI.board.Move;
import com.chessAI.board.Tile;
import com.chessAI.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chessAI.board.Move.*;

public class Knight extends Piece{

    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(Alliance pieceAlliance, int piecePosition) {

        super(PieceType.KNIGHT, piecePosition, pieceAlliance, true);
    }

    public Knight(Alliance pieceAlliance, int piecePosition, final boolean isFirstMove) {

        super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
           final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

                if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }

                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public int getOGLocationBonus() {
        return this.pieceAlliance.knightBonus(this.piecePosition);
    }

    @Override
    public int getMGLocationBonus() { return this.pieceAlliance.knightBonus(this.piecePosition); }

    @Override
    public int getEGLocationBonus() {
        return this.pieceAlliance.knightBonus(this.piecePosition);
    }

    @Override
    public boolean isPassedPawn() {
        return false;
    }

    @Override
    public boolean isProtected(Player player, Board board) {
        return false;
    }

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMovePiece().getPieceAlliance(), move.getDestinationCoordinate(), false);
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10) || (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));

    }
    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset == 10) || (candidateOffset == -6));

    }
    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == 17) || (candidateOffset == 10) || (candidateOffset == -6) || (candidateOffset == -15));

    }

}

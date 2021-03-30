package com.chessAI.piece;

import com.chessAI.Alliance;
import com.chessAI.board.Board;
import com.chessAI.board.Move;
import com.chessAI.board.Move.PawnAttackMove;
import com.chessAI.board.Move.PawnJump;
import com.chessAI.board.Move.PawnMove;
import com.chessAI.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chessAI.board.BoardUtils.*;
import static com.chessAI.board.Move.*;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATE = {8, 16, 7, 9};

    public Pawn(Alliance pieceAlliance, int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }

    public Pawn(Alliance pieceAlliance, int piecePosition, final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }


    @Override
    public Collection<Move> calculateLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {

            int candidateDestination = this.piecePosition + (currentCandidateOffset * this.getPieceAlliance().getDirection());

            if (!isValidTileCoordinate(candidateDestination)) {
                continue;
            }

            if (currentCandidateOffset == 8 && !board.getTile(candidateDestination).isTileOccupied()) {
                if(this.pieceAlliance.isPawnPromotionSquare(candidateDestination)){
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestination)));
                } else{
                    legalMoves.add(new PawnMove(board, this, candidateDestination));
                }

            } else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((SEVENTH_RANK[this.piecePosition] && pieceAlliance.isBlack()) ||
                    (SECOND_RANK[this.piecePosition] && pieceAlliance.isWhite()))) {

                final int behindCanDest = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if (!board.getTile(behindCanDest).isTileOccupied() &&
                        !board.getTile(candidateDestination).isTileOccupied()) {
                    legalMoves.add(new PawnJump(board, this, candidateDestination));
                }

            } else if (currentCandidateOffset == 7 &&
                    !((EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                            FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                if (board.getTile(candidateDestination).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestination).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestination)){
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestination, pieceOnCandidate)));
                        } else{
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestination, pieceOnCandidate));

                            }
                        }

                } else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
                    final Piece pieceOnCandidate = board.getEnPassantPawn();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add(
                                new PawnEnPassantAttack(board, this, candidateDestination, pieceOnCandidate));

                    }
                }

            } else if (currentCandidateOffset == 9 &&
                    !((FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                            EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                if (board.getTile(candidateDestination).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestination).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestination)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestination, pieceOnCandidate)));
                        } else {
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestination, pieceOnCandidate));
                        }
                    }
                }else if (board.getEnPassantPawn() != null && board.getEnPassantPawn().getPiecePosition() ==
                        (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
                    final Piece pieceOnCandidate = board.getEnPassantPawn();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        legalMoves.add(
                                new PawnEnPassantAttack(board, this, candidateDestination, pieceOnCandidate));

                    }
                }
            }

        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public int locationBonus() {
        return this.pieceAlliance.pawnBonus(this.piecePosition);
    }

    @Override
    public int endgameLocationBonus() {
        return this.pieceAlliance.pawnEndgameBonus(this.piecePosition);
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getMovePiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece(){

        return new Queen(this.pieceAlliance, this.piecePosition, false);
        // CHOSE PROMOTION PIECE
    }

    @Override
    public boolean isProtected(Player player, Board board){
        int col = this.getPieceFile();
        int rookRow = 0;
        int otherPieceRow = 0;
        boolean rookOnFile = false;
        boolean isProtected = true;
        for(Piece piece : player.getActivePieces()){
            if(piece.getPieceType() == PieceType.ROOK && piece.getPieceFile() == col){
                rookOnFile = true;
                rookRow = piece.getPieceRow();
                break;
            }
        }

        if(rookOnFile) {
            for (Piece piece : board.getAllPieces()) {
                if (piece.getPieceFile() == col && piece.getPieceRow() != this.getPieceRow() && piece.getPieceRow() != rookRow) {
                    otherPieceRow = piece.getPieceRow();
                    if( Math.abs((rookRow - otherPieceRow)) < Math.abs(rookRow - this.getPieceRow()) ) {
                        isProtected = false;
                        break;
                    }
                }
                isProtected = true;
            }
        }else{
            isProtected = false;
        }
        return isProtected;
    }

    @Override
    public boolean isPassedPawn(){

        int pawnFile = this.getPieceFile();
        int pawnRow = this.getPieceRow();
        List<Piece> file = getPawnsInColumn(pawnFile, this.opponentAlliance);
        List<Piece> fileMinusOne = getPawnsInColumn(pawnFile - 1, this.opponentAlliance);
        List<Piece> filePlusOne = getPawnsInColumn(pawnFile + 1, this.opponentAlliance);
        int fileCount = file.size();
        int filePlusOneCount = filePlusOne.size();
        int fileMinusOneCount = fileMinusOne.size();

        if(fileCount > 0){ return false;}

        if(fileCount == 0 && fileMinusOneCount == 0 && filePlusOneCount == 0){
            return true;
        }

        if(fileCount == 0 && fileMinusOneCount == 0) {
            for (Piece oppPawn : filePlusOne) {
                int opponentRow = oppPawn.getPieceRow();

                if (this.pieceAlliance == Alliance.WHITE) {
                    if (pawnRow >= opponentRow) {
                        return true;
                    }
                }
                if (this.pieceAlliance == Alliance.BLACK) {
                    if (pawnRow <= opponentRow) {
                        return true;
                    }
                }
            }
        }
        if(fileCount == 0 && filePlusOneCount == 0) {
            for (Piece oppPawn : fileMinusOne) {
                int opponentRow = oppPawn.getPieceRow();

                if (this.pieceAlliance == Alliance.WHITE) {
                    if (pawnRow >= opponentRow) {
                        return true;
                    }
                }
                if (this.pieceAlliance == Alliance.BLACK) {
                    if (pawnRow <= opponentRow) {
                        return true;
                    }
                }
            }
        }
        return false;

    }
}
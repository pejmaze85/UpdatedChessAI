package com.chessAI.piece;

import com.chessAI.Alliance;
import com.chessAI.board.Board;
import com.chessAI.board.BoardUtils;
import com.chessAI.board.Move;
import com.chessAI.player.Player;

import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final Alliance opponentAlliance;
    protected boolean isFirstMove;
    private final int cachedHashCode;


    Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove){
        this.pieceType = pieceType;
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.cachedHashCode = computeHashCode();
        this.isFirstMove = isFirstMove;
        if(pieceAlliance == Alliance.WHITE){ this.opponentAlliance = Alliance.BLACK;} else { this.opponentAlliance = Alliance.WHITE; }

    }

    public int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);

        return result;
    }

    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);

    @Override
    public boolean equals(final Object other){
        if(this == other) {
            return true;
        }
        if(!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType()
                && pieceAlliance == otherPiece.pieceAlliance && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }

    public int getPieceFile(){
        if(BoardUtils.FIRST_COLUMN[this.getPiecePosition()]){
            return 1;
        }
        if(BoardUtils.SECOND_COLUMN[this.getPiecePosition()]){
            return 2;
        }
        if(BoardUtils.THIRD_COLUMN[this.getPiecePosition()]){
            return 3;
        }
        if(BoardUtils.FOURTH_COLUMN[this.getPiecePosition()]){
            return 4;
        }
        if(BoardUtils.FIFTH_COLUMN[this.getPiecePosition()]){
            return 5;
        }
        if(BoardUtils.SIXTH_COLUMN[this.getPiecePosition()]){
            return 6;
        }
        if(BoardUtils.SEVENTH_COLUMN[this.getPiecePosition()]){
            return 7;
        }
        if(BoardUtils.EIGHTH_COLUMN[this.getPiecePosition()]){
            return 8;
        }
            return 0;
    }

    public int getPieceRow(){
        if(BoardUtils.FIRST_RANK[this.getPiecePosition()]){
            return 1;
        }
        if(BoardUtils.SECOND_RANK[this.getPiecePosition()]){
            return 2;
        }
        if(BoardUtils.THIRD_RANK[this.getPiecePosition()]){
            return 3;
        }
        if(BoardUtils.FOURTH_RANK[this.getPiecePosition()]){
            return 4;
        }
        if(BoardUtils.FIFTH_RANK[this.getPiecePosition()]){
            return 5;
        }
        if(BoardUtils.SIXTH_RANK[this.getPiecePosition()]){
            return 6;
        }
        if(BoardUtils.SEVENTH_RANK[this.getPiecePosition()]){
            return 7;
        }
        if(BoardUtils.EIGHTH_RANK[this.getPiecePosition()]){
            return 8;
        }
        return 0;
    }

    public PieceType getPieceType(){
        return this.pieceType;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public int getOGPieceValue(){ return this.pieceType.getOgValue(); }
    public abstract int getOGLocationBonus();

    public int getMGPieceValue(){ return this.pieceType.getMgValue(); }
    public abstract int getMGLocationBonus();

    public int getEGPieceValue() { return this.pieceType.getEgValue();}
    public abstract int getEGLocationBonus();

    public abstract boolean isPassedPawn();

    public abstract boolean isProtected(Player player, Board board);




    public enum PieceType{

        PAWN("P", 100, 100, 150){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N", 305, 300, 300){
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B", 333, 370, 400){
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R", 563, 620, 660){
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q", 950, 1100, 1200){
            @Override
            public boolean isKing() {
                return false;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K", 10000, 10000, 10000){
            @Override
            public boolean isKing() {
                return true;
            }
            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        private int ogValue;
        private int mgValue;
        private int egValue;

        PieceType(final String pieceName, final int ogValue, final int mgValue, final int egValue){
            this.pieceName = pieceName;
            this.ogValue = ogValue;
            this.mgValue = mgValue;
            this.egValue = egValue;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public int getMgValue() { return this.mgValue; }
        public int getOgValue(){
            return this.ogValue;
        }
        public int getEgValue(){
            return this.egValue;
        }
        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}

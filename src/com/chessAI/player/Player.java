package com.chessAI.player;

import com.chessAI.Alliance;
import com.chessAI.board.Board;
import com.chessAI.board.Move;
import com.chessAI.gui.Table;
import com.chessAI.piece.King;
import com.chessAI.piece.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {
        protected final Board board;
        protected final King playerKing;
        protected final Collection<Move> legalMoves;
        private final boolean isInCheck;

        Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves){
            this.board = board;
            this.playerKing = establishKing();
            this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
            this.isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
        }

        public King getPlayerKing(){
            return this.playerKing;
        }

        public Collection<Move> getLegalMoves(){
            return this.legalMoves;
        }

        public static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
            final List<Move> attackMoves = new ArrayList<>();
            for(final Move move : moves){
                if(piecePosition == move.getDestinationCoordinate()){
                    attackMoves.add(move);
                }
            }
            return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing(){
        for(final Piece piece : getActivePieces()){
            if( piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("NO VALID KING FOUND");
    }

    public boolean isMoveLegal(final Move move){
            return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
            return this.isInCheck;
    }

    public boolean isInCheckMate(){
            return this.isInCheck() && !hasEscapeMoves();
    }

    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }

    public boolean isThreeFold(){
            int numberOfTimes = Collections.frequency(Table.get().getBoardHistory().getBoardList(), board.toString());
           if(numberOfTimes == 3){
               System.out.println("THREEFOLD REPETITION - DRAW");
               return true;
           }else{
               return false;
           }
    }




    protected boolean hasEscapeMoves() {
            for (final Move move : this.legalMoves){
                final MoveTransition transition = makeMove(move);
                if(transition.getMoveStatus().isDone()){
                    return true;
                }
            }
            return false;
    }


    public MoveTransition makeMove(final Move move){
            if (!isMoveLegal(move)){
                return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
            }

            final Board transitionBoard = move.execute();

            final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),transitionBoard.currentPlayer().getLegalMoves());

            if(!kingAttacks.isEmpty()){
                return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
            }
            return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }

    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);

    protected boolean hasCastleOpportunities() {
        return !this.isInCheck && !this.playerKing.isCastled() &&
                (this.playerKing.isKingSideCastleCapable() || this.playerKing.isQueenSideCastleCapable());
    }

}


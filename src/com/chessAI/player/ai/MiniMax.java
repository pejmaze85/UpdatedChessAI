package com.chessAI.player.ai;

import com.chessAI.board.Board;
import com.chessAI.board.BoardUtils;
import com.chessAI.board.Move;
import com.chessAI.gui.Table;
import com.chessAI.player.MoveTransition;
import com.chessAI.player.ai.TranspositionTable.entry;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MiniMax implements MoveStrategy{

    private int quiescenceCount;
    private static final int MAX_QUIESCENCE = 25000;
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    private int moveCount;
    private int hashCount;
    //private final TranspositionTable tt = new TranspositionTable();
    //private final PremoveTable table = new PremoveTable();

    public MiniMax(final int searchDepth){
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;

    }

    @Override public String toString(){
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {
        return getBestMiniMaxMove(board);
    }

    public List<Move> sortMoves(Collection<Move> moveList){
        List<Move> sortedList = new ArrayList<>();

        for(Move move : moveList){
            if(move.isAttack()){
                sortedList.add(move);
            }
        }

        for(Move move : moveList){
            if (move.isCastlingMove()){
                sortedList.add(move);
            }
        }
        for(Move move : moveList){
            if(!move.isAttack() && !move.isCastlingMove()){
                sortedList.add(move);
            }
        }



        return ImmutableList.copyOf(sortedList);
    }

    public Move getBestMiniMaxMove(Board board){
        final long startTime = System.currentTimeMillis();

        Move bestMove = null;

        //String nextMove = table.getNextMove(Table.get().moveLog);

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        int numMoves = board.currentPlayer().getLegalMoves().size();

        System.out.println(board.currentPlayer().getAlliance().toString() + " thinking with depth " + this.searchDepth + " through " + numMoves + "moves.");

        for(final Move move : sortMoves(board.currentPlayer().getLegalMoves())){
            moveCount ++;
            System.out.println("Working On Move Number " + moveCount + "/" + numMoves);
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if(moveTransition.getMoveStatus().isDone()){
                currentValue = board.currentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getTransitionBoard(), this.searchDepth - 1, Integer.MIN_VALUE,Integer.MAX_VALUE) :
                        max(moveTransition.getTransitionBoard(), this.searchDepth - 1, Integer.MIN_VALUE,Integer.MAX_VALUE);

                if(board.currentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                    bestMove = move;

                }else if (board.currentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
            System.out.println("Current Best Move Is: " + bestMove);
        }

        final long executionTime = System.currentTimeMillis() - startTime;

        System.out.println("Search Took " + TimeUnit.MILLISECONDS.toSeconds(executionTime) + " Seconds");
        System.out.println("Total hashes found: " + hashCount);

            return bestMove;

    }

    public int min(final Board board, final int depth, int alpha, int beta){

        if (depth == 0  || endGame(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }

        int currentLowest = beta;

        for(final Move move : sortMoves(board.currentPlayer().getLegalMoves())){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                final Board toBoard = moveTransition.getTransitionBoard();
                //long boardHash = toBoard.computeHashCode();
                 //if(tt.tableList.containsKey(boardHash) && tt.tableList.get(boardHash).getDepth() >= depth){
                 //        hashCount ++;
                 //        currentLowest = tt.tableList.get(boardHash).beta;
                 //}else {

                     currentLowest = Math.min(currentLowest, max(toBoard,
                             calculateQuiescenceDepth(toBoard, depth), alpha, currentLowest));
                 //    entry newEntry = new entry(currentLowest, depth, alpha, beta);
                 //    tt.tableList.put(boardHash, newEntry);
                 //}
                     if (currentLowest <= alpha) {

                         break;
                     }
                 }
            }


        return currentLowest;

    }


    public int max(final Board board, final int depth, int alpha, int beta){

        if (depth == 0 || endGame(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }

        int currentHighest = alpha;

        for(final Move move : sortMoves(board.currentPlayer().getLegalMoves())){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                final Board toBoard = moveTransition.getTransitionBoard();
                //long boardHash = toBoard.computeHashCode();
                //if(tt.tableList.containsKey(boardHash)  && tt.tableList.get(boardHash).getDepth() >= depth){ //&& tt.tableList.get(boardHash).getDepth() == depth){
                //        hashCount ++;
                //        currentHighest = tt.tableList.get(boardHash).beta;
                //}else {
                    currentHighest = Math.max(currentHighest, min(toBoard,
                            calculateQuiescenceDepth(toBoard, depth), currentHighest, beta));
                //    entry newEntry = new entry(currentHighest, depth, alpha, beta);
                //    tt.tableList.put(boardHash, newEntry);
                //}
                    if (currentHighest >= beta) {

                        break;
                    }
                }
            }

        return currentHighest;
    }

    private int calculateQuiescenceDepth(final Board toBoard,
                                         final int depth) {
        if(depth == 1 && this.quiescenceCount < MAX_QUIESCENCE) {
            int activityMeasure = 0;
            if (toBoard.currentPlayer().isInCheck()) {
                activityMeasure += 1;
            }
            for(final Move move: BoardUtils.lastNMoves(toBoard, 2)) {
                if(move.isAttack()) {
                    activityMeasure += 1;
                }
            }
            if(activityMeasure >= 2) {
                this.quiescenceCount++;
                return 2;
            }
        }
        return depth - 1;
    }

    private boolean endGame(Board board) {
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStaleMate() || board.currentPlayer().isThreeFold() ;
    }

}



       /* if(willResultInDraw(bestMove)) {
            if (Table.get().getGameSetup().isAIPlayer(board.currentPlayer())) {
                int OFFSET_TO_AVOID_DRAW = 1;
                int selfScore = StandardBoardEvaluator.getScore(board, board.currentPlayer(), StandardBoardEvaluator.GAMESTATE.UNSURE);
                int opponentsScore = StandardBoardEvaluator.getScore(board, board.currentPlayer().getOpponent(), StandardBoardEvaluator.GAMESTATE.UNSURE);
                if ((selfScore + OFFSET_TO_AVOID_DRAW) > opponentsScore) {
                    bestMove = getBestMiniMaxMove(board, false);
                }
            }
        } */
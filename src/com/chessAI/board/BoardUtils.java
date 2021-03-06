package com.chessAI.board;

import com.chessAI.Alliance;
import com.chessAI.gui.Table;
import com.chessAI.piece.King;
import com.chessAI.piece.Piece;
import com.chessAI.player.MoveTransition;
import com.google.common.collect.ImmutableList;

import java.util.*;

import static com.chessAI.piece.Piece.*;

public class BoardUtils {


    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] THIRD_COLUMN = initColumn(2);
    public static final boolean[] FOURTH_COLUMN = initColumn(3);
    public static final boolean[] FIFTH_COLUMN = initColumn(4);
    public static final boolean[] SIXTH_COLUMN = initColumn(5);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final boolean[] EIGHTH_RANK = initRow(0);
    public static final boolean[] SEVENTH_RANK = initRow(8);
    public static final boolean[] SIXTH_RANK = initRow(16);
    public static final boolean[] FIFTH_RANK = initRow(24);
    public static final boolean[] FOURTH_RANK = initRow(32);
    public static final boolean[] THIRD_RANK = initRow(40);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] FIRST_RANK = initRow(56);

    public static final List<String> ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    public static final int START_TILE_INDEX = 0;
    public static final int NUM_TILES = 64;


    private static boolean[] initColumn(int colNum) {
        final boolean[] column = new boolean[NUM_TILES];
        do {
            column[colNum] = true;
            colNum += 8;
        } while (colNum < 64);
        return column;
    }

    private static boolean[] initRow(int rowNum) {
        boolean[] row = new boolean[NUM_TILES];
        for (int i = 0; i < row.length; i++) {
            row[i] = false;
        }
        do {
            row[rowNum] = true;
            rowNum++;
        } while (rowNum % 8 != 0);
        return row;
    }

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = START_TILE_INDEX; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION.get(i), i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static List<String> initializeAlgebraicNotation() {
        return Collections.unmodifiableList(Arrays.asList(
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));
    }

    public static boolean isKingPawnTrap(final Board board,
                                         final King king,
                                         final int frontTile) {
        final Piece piece = board.getTile(frontTile).getPiece();
        return piece != null &&
                piece.getPieceType() == PieceType.PAWN &&
                piece.getPieceAlliance() != king.getPieceAlliance();
    }

    private BoardUtils() {

        throw new RuntimeException("NOPE");
    }

    public static boolean isValidTileCoordinate(int coord) {
        return 0 <= coord && coord < NUM_TILES;
    }

    public static int getCoordinateAtPosition(final String position) {
        return POSITION_TO_COORDINATE.get(position);
    }

    public static String getPositionAtCoordinate(final int coordinate) {
        return ALGEBRAIC_NOTATION.get(coordinate);
    }

    public static List<Piece> getPawnsInColumn(final int file, final Alliance alliance) {
        List<Piece> pawnsInCol = new ArrayList<>();
        for (Piece piece : Table.get().getGameBoard().getAllPieces()) {
            if (piece.getPieceType() == PieceType.PAWN && piece.getPieceAlliance() == alliance) {
                if (piece.getPieceFile() == file) {
                    pawnsInCol.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(pawnsInCol);
    }

    public static List<Move> lastNMoves(final Board board, int N) {
        final List<Move> moveHistory = new ArrayList<>();
        Move currentMove = board.getTransitionMove();
        int i = 0;
        while (currentMove != Move.MoveFactory.getNullMove() && i < N) {
            moveHistory.add(currentMove);
            currentMove = currentMove.getBoard().getTransitionMove();
            i++;
        }
        return Collections.unmodifiableList(moveHistory);
    }

}
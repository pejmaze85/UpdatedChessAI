package com.chessAI.player.ai;

import com.chessAI.piece.Piece;
import com.chessAI.player.Player;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public final class PawnStructureAnalyzer {

    private static final PawnStructureAnalyzer INSTANCE = new PawnStructureAnalyzer();

    public static final int PAWN_ISLAND = -10;
    public static final int DOUBLED_PAWN = -10;

    private PawnStructureAnalyzer() {
    }

    public static PawnStructureAnalyzer get() {
        return INSTANCE;
    }

    public int pawnStructureScore(final Player player) {
        final int[] pawnsOnColumnTable = createPawnColumnTable(calculatePlayerPawns(player));
        return calculatePawnColumnStack(pawnsOnColumnTable) + calculateIsolatedPawnPenalty(pawnsOnColumnTable);
    }

    private static Collection<Piece> calculatePlayerPawns(final Player player) {
        List<Piece> pawnCollection = new ArrayList<>();

        for( Piece piece : player.getActivePieces()){
            if(piece.getPieceType() == Piece.PieceType.PAWN){
                pawnCollection.add(piece);
            }
        }

        return ImmutableList.copyOf(pawnCollection);

    }

    private static int calculatePawnColumnStack(final int[] pawnsOnColumnTable) {
        int doubledPawnCount = 0;
        for(final int pawnCount : pawnsOnColumnTable) {
            if(pawnCount > 1) {
                doubledPawnCount += pawnCount;
            }
        }
        return doubledPawnCount * DOUBLED_PAWN;
    }

    private static int calculateIsolatedPawnPenalty(final int[] pawnsOnColumnTable) {
        int numIsolatedPawns = 0;
        if(pawnsOnColumnTable[0] > 0 && pawnsOnColumnTable[1] == 0) {  //Check Pawns On First File
            numIsolatedPawns += pawnsOnColumnTable[0];
        }
        if(pawnsOnColumnTable[7] > 0 && pawnsOnColumnTable[6] == 0) { //Check Pawns On Eighth File
            numIsolatedPawns += pawnsOnColumnTable[7];
        }
        for(int i = 1; i < pawnsOnColumnTable.length - 1; i++) {        // Check All Other Files
            if((pawnsOnColumnTable[i-1] == 0 && pawnsOnColumnTable[i+1] == 0)) {
                numIsolatedPawns += pawnsOnColumnTable[i];
            }
        }
        return numIsolatedPawns * PAWN_ISLAND;
    }

    private static int[] createPawnColumnTable(final Collection<Piece> playerPawns) {
        final int[] table = new int[8];
        for(final Piece playerPawn : playerPawns) {
            table[playerPawn.getPiecePosition() % 8]++;
        }
        return table;
    }

    // TO ADD: PASSED PAWN BONUS

}
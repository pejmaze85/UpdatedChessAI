package com.chessAI.board;

import com.chessAI.Alliance;
import com.chessAI.piece.*;

public class BoardVariations {

    public static Board kingQueenEndgame(){
        final Board.Builder builder = new Board.Builder();

        builder.setPiece(new Queen(Alliance.BLACK, 16));
        builder.setPiece(new King(Alliance.BLACK, 0, true, false, true, true));

        // WHITE

        builder.setPiece(new King(Alliance.WHITE, 63, true, false, true, true));


        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();
    }

    public static Board createStandardBoard(){
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rook(Alliance.BLACK, 4));
        builder.setPiece(new Knight(Alliance.BLACK, 21));
        builder.setPiece(new Bishop(Alliance.BLACK, 45));
       // builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 6, false, true, false, false));
      //  builder.setPiece(new Bishop(Alliance.BLACK, 5));
       // builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 5));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 19));
       // builder.setPiece(new Pawn(Alliance.BLACK, 11));
        //builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        // WHITE
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 27));
        //builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        //builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 42));
        builder.setPiece(new Bishop(Alliance.WHITE, 43));
      //  builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new King(Alliance.WHITE, 62, false, true, false, false));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
      //  builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 10));

        builder.setMoveMaker(Alliance.WHITE);

        return builder.build();
    }

}

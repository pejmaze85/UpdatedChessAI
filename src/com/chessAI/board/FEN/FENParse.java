package com.chessAI.board.FEN;


import com.chessAI.Alliance;
import com.chessAI.board.Board;
import com.chessAI.board.Board.Builder;
import com.chessAI.board.BoardUtils;
import com.chessAI.piece.*;

public class FENParse {

        public static Board createGameFromFEN(final String fenString) {
            return parseFEN(fenString);
        }

        private static Board parseFEN(final String fenString) {
            final String[] fenPartitions = fenString.trim().split(" ");
            final Builder builder = new Builder();
            final boolean whiteKingSideCastle = whiteKingSideCastle(fenPartitions[2]);
            final boolean whiteQueenSideCastle = whiteQueenSideCastle(fenPartitions[2]);
            final boolean blackKingSideCastle = blackKingSideCastle(fenPartitions[2]);
            final boolean blackQueenSideCastle = blackQueenSideCastle(fenPartitions[2]);
            final String gameConfiguration = fenPartitions[0];
            final char[] boardTiles = gameConfiguration.replaceAll("/", "")
                    .replaceAll("8", "--------")
                    .replaceAll("7", "-------")
                    .replaceAll("6", "------")
                    .replaceAll("5", "-----")
                    .replaceAll("4", "----")
                    .replaceAll("3", "---")
                    .replaceAll("2", "--")
                    .replaceAll("1", "-")
                    .toCharArray();
            int i = 0;
            while (i < boardTiles.length) {
                switch (boardTiles[i]) {
                    case 'r':
                        builder.setPiece(new Rook(Alliance.BLACK, i));
                        i++;
                        break;
                    case 'n':
                        builder.setPiece(new Knight(Alliance.BLACK, i));
                        i++;
                        break;
                    case 'b':
                        builder.setPiece(new Bishop(Alliance.BLACK, i));
                        i++;
                        break;
                    case 'q':
                        builder.setPiece(new Queen(Alliance.BLACK, i));
                        i++;
                        break;
                    case 'k':
                        final boolean isCastled = !blackKingSideCastle && !blackQueenSideCastle;
                        builder.setPiece(new King(Alliance.BLACK, i, blackKingSideCastle, blackQueenSideCastle));
                        i++;
                        break;
                    case 'p':
                        builder.setPiece(new Pawn(Alliance.BLACK, i));
                        i++;
                        break;
                    case 'R':
                        builder.setPiece(new Rook(Alliance.WHITE, i));
                        i++;
                        break;
                    case 'N':
                        builder.setPiece(new Knight(Alliance.WHITE, i));
                        i++;
                        break;
                    case 'B':
                        builder.setPiece(new Bishop(Alliance.WHITE, i));
                        i++;
                        break;
                    case 'Q':
                        builder.setPiece(new Queen(Alliance.WHITE, i));
                        i++;
                        break;
                    case 'K':
                        builder.setPiece(new King(Alliance.WHITE, i, whiteKingSideCastle, whiteQueenSideCastle));
                        i++;
                        break;
                    case 'P':
                        builder.setPiece(new Pawn(Alliance.WHITE, i));
                        i++;
                        break;
                    case '-':
                        i++;
                        break;
                    default:
                        throw new RuntimeException("FEN String Wont Work ");
                }
            }
            builder.setMoveMaker(moveMaker(fenPartitions[1]));
            return builder.build();
        }

        private static Alliance moveMaker(final String moveMakerString) {
            if(moveMakerString.equals("w")) {
                return Alliance.WHITE;
            } else if(moveMakerString.equals("b")) {
                return Alliance.BLACK;
            }
            throw new RuntimeException("Invalid FEN String " +moveMakerString);
        }

        private static boolean whiteKingSideCastle(final String fenCastleString) {
            return fenCastleString.contains("K");
        }

        private static boolean whiteQueenSideCastle(final String fenCastleString) {
            return fenCastleString.contains("Q");
        }

        private static boolean blackKingSideCastle(final String fenCastleString) {
            return fenCastleString.contains("k");
        }

        private static boolean blackQueenSideCastle(final String fenCastleString) {
            return fenCastleString.contains("q");
        }

    }

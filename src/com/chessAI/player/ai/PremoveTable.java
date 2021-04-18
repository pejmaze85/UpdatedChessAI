package com.chessAI.player.ai;

import com.chessAI.gui.Table;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PremoveTable {


    public PremoveTable() {
    }

    public String getNextMove(Table.MoveLog moveLog) {
        Stream<String> gameList = null;
        List<String> lines = new ArrayList<>();
        String filename = "GameList/GameList.txt";
        try {
            gameList = Files.lines(Paths.get(filename), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String moves[];
        gameList.forEach(s -> lines.add(s));

        for (String s : lines) {

            moves = s.split(" ");
            boolean stillSet = true;

            for (int i = 0; i < moveLog.size() - 1; i++) {
                if (moves[i] != moveLog.getMoves().get(i).toString()) {
                    stillSet = false;
                }
            }
            if (stillSet) {
                return moves[moveLog.size() + 1];
            }
        }
        return null;
    }
}





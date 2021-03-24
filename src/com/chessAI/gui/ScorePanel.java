package com.chessAI.gui;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {

    private static final Dimension SCORE_PANEL_DIMENSION = new Dimension(10,20);
    public JPanel blackBoard = new JPanel();
    public JPanel whiteBoard = new JPanel();


    ScorePanel() {
        this.setPreferredSize(SCORE_PANEL_DIMENSION);
        this.setLayout(new BorderLayout());
        this.add(whiteBoard, BorderLayout.WEST);
        this.add(blackBoard, BorderLayout.EAST);
        this.setVisible(true);
    }

    public void drawScore(final int whiteScore, final int blackScore){
        int totalScore = whiteScore + blackScore;
        int whitePercent = (whiteScore / totalScore);
        int blackPercent = (blackScore / totalScore);

        whiteBoard.setBackground(Color.WHITE);
        whiteBoard.setPreferredSize(new Dimension(70,20));
        whiteBoard.setVisible(true);

        blackBoard.setBackground(Color.BLACK);
        blackBoard.setPreferredSize(new Dimension(30,20));
        whiteBoard.setVisible(true);


    }
}

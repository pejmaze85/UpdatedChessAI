package com.chessAI.gui;

import com.chessAI.Alliance;
import com.chessAI.player.Player;
import com.chessAI.gui.Table.PlayerType;

import javax.swing.*;

public class GameSetup extends JDialog {

    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;


    GameSetup() {
                whitePlayerType =  PlayerType.HUMAN;
                blackPlayerType =  PlayerType.HUMAN;
        }


    public boolean isAIPlayer(final Player player) {
        if(player.getAlliance() == Alliance.WHITE) {
            return getWhitePlayerType() == PlayerType.COMPUTER;
        }
        return getBlackPlayerType() == PlayerType.COMPUTER;
    }

    public PlayerType getWhitePlayerType() {
        return this.whitePlayerType;
    }

    public PlayerType getBlackPlayerType() {
        return this.blackPlayerType;
    }

    public void setWhitePlayerType(PlayerType type) {
        this.whitePlayerType = type;
    }

    public void setBlackPlayerType(PlayerType type) {
        this.blackPlayerType = type;
    }

}
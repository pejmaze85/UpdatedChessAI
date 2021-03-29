package com.chessAI.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Observable;

public class DebugWindow extends Observable {
    public JTextArea editArea;

public DebugWindow() {
    JPanel gui = new JPanel(new BorderLayout());
    gui.setBorder(new EmptyBorder(2,3,2,3));
    editArea = new JTextArea(40,80);
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, editArea.getFont().getSize());
    editArea.setFont(font);
    editArea.setAutoscrolls(true);
    gui.add(new JScrollPane(editArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

    JFrame debugWindow = new JFrame("Debug Window");
    debugWindow.setSize(400,400);
    debugWindow.add(gui);
    debugWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    debugWindow.setVisible(true);

    }

    public void addToText(String string){
        editArea.append(string);
    }
}
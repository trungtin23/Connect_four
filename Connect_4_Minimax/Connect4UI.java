package Connect_4_Minimax;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Connect4UI {
	public static void main(String[] args) {
        JFrame frame = new JFrame("CONNECT 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final GameState state = new GameState();
        final BoardDrawing board = new BoardDrawing(state);

        JButton button1 = new JButton("1");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                handlePlayerMove(state, board, 1);
                handleAIMove(state, board);
            }
        });

        JButton button2 = new JButton("2");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                handlePlayerMove(state, board, 2);
                handleAIMove(state, board);
            }
        });

        JButton button3 = new JButton("3");
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                handlePlayerMove(state, board, 3);
                handleAIMove(state, board);
            }
        });

        JButton button4 = new JButton("4");
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                handlePlayerMove(state, board, 4);
                handleAIMove(state, board);
            }
        });

        JButton button5 = new JButton("5");
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                handlePlayerMove(state, board, 5);
                handleAIMove(state, board);
            }
        });

        JButton button6 = new JButton("6");
        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                handlePlayerMove(state, board, 6);
                handleAIMove(state, board);
            }
        });

        JButton button7 = new JButton("7");
       button7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                handlePlayerMove(state, board, 7);
                handleAIMove(state, board);
            }
        });

        JButton buttonUndo = new JButton("Undo");
        buttonUndo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                state.undo();
                board.repaint();
            }
        });

        JButton buttonRestart = new JButton("Restart");
        buttonRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                state.restart();
                board.repaint();
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);
        buttons.add(button6);
        buttons.add(button7);
        buttons.add(buttonUndo);
        buttons.add(buttonRestart);

        frame.add(board, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);

        frame.setSize(750, 550);
        frame.setVisible(true);
    }

	private static void handlePlayerMove(GameState state, BoardDrawing board, int column) {
		if (!state.getGameOver() && state.getRedsTurn()) {
			state.move(column);
			board.repaint();
		}
	}

	private static void handleAIMove(GameState state, BoardDrawing board) {
		if (!state.getGameOver() && !state.getRedsTurn()) {
			state.moveAI();
			board.repaint();
		}
	}

}

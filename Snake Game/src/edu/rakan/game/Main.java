package edu.rakan.game;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		//Creating a Frame with these properties
		JFrame frame = new JFrame("Snake"); //the title of the window
		frame.setContentPane(new GamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //To make sure the program is not running after hit close button 
		frame.setResizable(false); //we can not change the window size
		frame.pack();
		
		frame.setPreferredSize(new Dimension(GamePanel.WIDTH,GamePanel.HEIGHT));
		frame.setLocationRelativeTo(null); //To make the window in center of screen
		frame.setVisible(true);

	}

}

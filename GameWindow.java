//Justin Baldeosingh
//816021226
//COMP 3609 - Assignment 2

import javax.swing.*;			// need this for GUI objects
import java.awt.*;			// need this for Layout Managers
import java.awt.event.*;		// need this to respond to GUI events
	
public class GameWindow extends JFrame implements ActionListener, MouseListener {
	//Declares interface buttons.
	private JButton startB;
	private JButton endB;
	private JButton restartB;
	private JButton exitB;

	private Container c;

	//Declares the different panels for the interface.
	private JPanel mainPanel;
	private GamePanel gamePanel;
	private Scoreboard scoreboard;

	@SuppressWarnings({"unchecked"})
	public GameWindow() {
 
		setTitle ("Missile Defense Command");
		setSize (612, 575);

		// Creates the main scoreboard.
		scoreboard = new Scoreboard();

		// create buttons
		startB = new JButton ("Start Game");
		endB = new JButton ("End Game");
		restartB = new JButton ("Restart Game");
		exitB = new JButton ("Exit");


		// add listener to each button (same as the current object)
		startB.addActionListener(this);
		endB.addActionListener(this);
		restartB.addActionListener(this);
		exitB.addActionListener(this);
		
		// create mainPanel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));


		// create the gamePanel for game entities

		gamePanel = new GamePanel(scoreboard);
		gamePanel.setPreferredSize(new Dimension(612, 408));

		// create buttonPanel
		GridLayout gridLayout;
		JPanel buttonPanel = new JPanel();
		gridLayout = new GridLayout(2, 2);
		buttonPanel.setLayout(gridLayout);

		// add buttons to buttonPanel
		buttonPanel.add (startB);
		buttonPanel.add (endB);
		buttonPanel.add (restartB);
		buttonPanel.add (exitB);

		// add sub-panels with GUI objects to mainPanel
		mainPanel.add(scoreboard);
		mainPanel.add(gamePanel);
		mainPanel.add(buttonPanel);
		mainPanel.setBackground(Color.BLACK);

		// set up mainPanel to respond to the mouse
		gamePanel.addMouseListener(this);

		// add mainPanel to window surface
		c = getContentPane();
		c.add(mainPanel);

		// set properties of window
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}


	// implement single method in ActionListener interface
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		if (command.equals(startB.getText()))
			gamePanel.startGame();

		if (command.equals(endB.getText()))
			gamePanel.endGame();

		if (command.equals(restartB.getText()))
			gamePanel.restartGame();

		if (command.equals(exitB.getText()))
			System.exit(0);

		mainPanel.requestFocus();
	}

	// implement methods in MouseListener interface

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {
	
	}

	public void mouseExited(MouseEvent e) {
	
	}

	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		//If the game has started, any click event on the panel should be used as game user input for firing the cannon.
		if(gamePanel.gameStarted)
			gamePanel.targetSky(x, y);
	}

	public void mouseReleased(MouseEvent e) {

	}

}
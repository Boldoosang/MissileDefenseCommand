//Justin Baldeosingh
//816021226
//COMP 3609 - Assignment 1

import javax.swing.*;			// need this for GUI objects
import java.awt.*;			    // need this for Layout Managers

import static java.lang.Integer.parseInt;


public class Scoreboard extends JPanel {
    //Declares labels for lives and score.
    private JLabel livesLabel;
    private JLabel scoreLabel;

    //Declares text fields for lives and score.
    private JTextField livesTF;
    private JTextField scoreTF;


    public Scoreboard() {
        //Creates the lives label and customizes it.
        livesLabel = new JLabel ("Lives: ");
        livesLabel.setOpaque(true);
        livesLabel.setBackground(new Color(196, 196, 196));

        //Creates the score label.
        scoreLabel = new JLabel ("Score: ");

        //Create lives and score text fields.
        livesTF = new JTextField (25);
        livesTF.setEditable(false);
        livesTF.setBackground(new Color(196, 196, 196));

        scoreTF = new JTextField (25);
        scoreTF.setEditable(false);

        // Creates the info panel and sets the layout.
        GridLayout gridLayout = new GridLayout(1, 4);
        this.setLayout(gridLayout);

        //Adds the respective labels and fields (game information) to the panel.
        this.add(livesLabel);
        this.add(livesTF);
        this.add(scoreLabel);
        this.add(scoreTF);
    }

    public void setLives(String lives) {
        this.livesTF.setText(lives);
    }

    public int getLives() {
        return parseInt(this.livesTF.getText());
    }

    public void setScore(String score) {
        this.scoreTF.setText(score);
    }

    public int getScore() {
        return parseInt(this.scoreTF.getText());
    }
}

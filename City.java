//Justin Baldeosingh
//816021226
//COMP 3609 - Assignment 2

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;


/**
 Models the city that is used to represent the player's defensive objective.
 */
public class City {
    //Declares a JPanel object for storing the panel on which the city will be drawn.
    private JPanel panel;

    //Declares the coordinate variables of the city.
    private int x;
    private int y;

    //Declares the width and height dimensions of the city.
    private int width;
    private int height;

    //Declares the image used for storing the buildings image.
    private Image buildingsImage;

    //Declares variables for storing the score and lives.
    private int score;
    private int lives;

    //Declares a variable used to store the state of the game.
    private boolean gameOver;

    //Declares a global scoreboard variable for storing the scoreboard.
    private Scoreboard scoreboard;

    /**
     * The city constructor takes the JPanel, the x, and y coordinates, and the scoreboard on which the city will be drawn and the score will be kept.
     */
    public City(JPanel p, int xPos, int yPos, Scoreboard scoreboard) {
        //Sets the drawing panel to the global panel variable.
        panel = p;

        //Sets the coordinates where the city will be drawn.
        x = xPos;
        y = yPos;

        //Sets the fixed dimensions of the city.
        width = 618;
        height = 116;

        //Assigns the global scoreboard variable.
        this.scoreboard = scoreboard;

        //The game is initially set to not be over.
        this.gameOver = false;

        //Loads the buildings images.
        buildingsImage = ImageManager.loadImage ("images/buildings.png");

        //Sets the initial score and lives of the player.
        score = 0;
        lives = 5;

        //Initializes the player's starting score and lives.
        scoreboard.setScore(Integer.toString(score));
        scoreboard.setLives(Integer.toString(lives));
    }

    /**
     * Prints the game over text to the screen once the player has lost; also prints the score the user attained.
     */
    public void gameOver(){
        if(!gameOver) {
            Graphics g = panel.getGraphics();
            Graphics2D g2 = (Graphics2D) g;

            //Sets the font, color, and draws the string to the screen.
            Font f = new Font("Bookman Old Style", Font.BOLD, 32);
            g2.setFont(f);
            g2.setColor(Color.WHITE);
            g2.drawString("Game Over!", width / 2 - 100, 180);
            g2.drawString("Score: " + score, width / 2 - 100, 225);
            gameOver = true;
        }
    }

    /**
     * Decreases the life count of the player and updates the text field.
     */
    public void loseLife(){
        lives--;
        scoreboard.setLives(Integer.toString(lives));
    }

    /**
     * Returns the current count of the lives of the player.
     */
    public int getLives(){
        return this.lives;
    }

    /**
     * Returns the current count of the lives of the player.
     */
    public boolean isGameOver(){
        return this.gameOver;
    }

    /**
     * Sets the score and updates the text field.
     */
    public void setScore(int score){
        this.score = score;
        scoreboard.setScore(Integer.toString(this.score));
    }


    /**
     * Sets the lives and updates the text field.
     */
    public void setLives(int lives){
        this.lives = lives;
        scoreboard.setLives(Integer.toString(this.lives));
    }

    /**
     * Sets the score and updates the text field.
     */
    public int getScore(){
        return this.score;
    }

    /**
     Draws the city to the image context, at the supplied coordinates using the supplied dimensions.
     */
    public void draw (Graphics2D g2) {
        g2.drawImage(buildingsImage, x, y, width, height, null);
    }


    /**
     Returns the rectangular hit-box of the city.
     */
    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double (x, y+20, width, height);
    }



}
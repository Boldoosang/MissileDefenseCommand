//Justin Baldeosingh
//816021226
//COMP 3609 - Assignment 2

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Paratrooper {
    //Sets the drawing panel to the global panel variable.
    private JPanel panel;

    //Declares the coordinate variables of the paratrooper.
    private int x;
    private int y;

    //Declares the width and height dimensions of the paratrooper.
    private int width;
    private int height;

    //Declares the image used for storing the missile paratrooper.
    private Image paratrooperImage;

    private int dx;		// increment to move along x-axis
    private int dy;		// increment to move along y-axis

    //Declares the rectangle for storing the paratrooper hitbox.
    private Rectangle2D.Double paratrooper;

    //Declares the city object for representing the player.
    private City city;

    //Declares the soundmanager for the single instance sound manager.
    private SoundManager soundManager;

    //Used to determine if the paratrooper is shown currently.
    private boolean paratrooperShown;

    public Paratrooper(JPanel p, City city) {
        //Loads the main panel, to be used as the drawing context.
        panel = p;

        //Sets the dimensions of the paratrooper.
        width = 75;
        height = 75;

        //Gets a handle on the main city representing the player's objective.
        this.city = city;

        //Loads the paratrooper image sprite.
        paratrooperImage = ImageManager.loadImage ("images/paratrooper.png");

        //Sets the single instance sound manager.
        soundManager = SoundManager.getInstance();

        //Hides the paratrooper at the start of the game.
        hideParatrooper();
    }


    //Hides the paratrooper by moving it out of the game view and setting its motion to 0.
    public void hideParatrooper() {
        x = 0 - width;
        y = 0 - height;
        dx = 0;
        dy = 0;
        paratrooperShown = false;
    }

    //Shows the paratrooper by setting its starting position and downward movement.
    public void showParatrooper(int x, int y) {
        if(!paratrooperShown) {
            this.x = x;
            this.y = y;
            dx = 0;
            dy = 8;
            paratrooperShown = true;
        }
    }


    public void draw (Graphics2D g2) {
        g2.drawImage(paratrooperImage, x, y, width, height, null);
    }

    //Moves the paratrooper
    public void move() {
        if (!panel.isVisible ())
            return;

        //Updates the x and y coordinates of the paratrooper as the paratrooper move.
        x = x + dx;
        y = y + dy;

        //Ensures that the paratrooper does not leave the screen boundaries when spawned from the jet.
        if(x <= 0){
            x = 0;
        } else if(x + width > panel.getWidth()){
            x = panel.getWidth() - width;
        }

        //Determines if the paratrooper has collided with the city.
        boolean collision = collidesWithCity();

        //If the paratrooper has collided with the city, hide the paratrooper.
        if (collision){
            hideParatrooper();
        }
    }

    //Determines if the user has clicked on the paratrooper.
    public boolean shotDown (int x, int y) {
        //Gets the hitbox of the paratrooper.
        paratrooper = getBoundingRectangle();
        //If the paratrooper has not been initialized, then return false.
        if (paratrooper == null)
            return false;

        //Determines if the paratrooper hitbox contains the coordinates that correspond to the user's click.
        boolean onParatrooper = paratrooper.contains(x, y);

        //If the user has clicked on the paratrooper and the game is not over,
        //play the 'Life Up' sound effect, add 100 to the user's score, and hide the paratrooper.
        if (onParatrooper && !city.isGameOver()){
            soundManager.playClip("lifeUp", false);
            int score = city.getScore();
            city.setScore(score + 100);
            int lives = city.getLives();
            city.setLives(lives + 1);
            hideParatrooper();
        }

        //Returns the outcome of clicking on the paratrooper.
        return onParatrooper;
    }


    //Returns the rectangular hit-box of the paratrooper.
    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double (x, y, width, height);
    }


    //Returns the outcome of the paratrooper colliding with the city.
    public boolean collidesWithCity() {
        Rectangle2D.Double paratrooperRect = getBoundingRectangle();
        Rectangle2D.Double cityRect = city.getBoundingRectangle();

        return paratrooperRect.intersects(cityRect);
    }
}
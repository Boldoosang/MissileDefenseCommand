//Justin Baldeosingh
//816021226
//COMP 3609 - Assignment 2

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;


/**
 The Cannon class models the cannon from which the player shoots the incoming missiles.
*/
public class Cannon {
    //Declares a JPanel object for storing the panel on which the cannon will be drawn.
    private JPanel panel;

    //Declares the coordinate variables of the cannon.
    private int x;
    private int y;

    //Declares the width and height dimensions of the cannon.
    private int width;
    private int height;

    //Declares the images that will be used for the cannon orientations.
    private Image cannonImage;
    private Image cannonImageLeft;
    private Image cannonImageUp;
    private Image cannonImageRight;

    //Declares the sound manager.
    private SoundManager soundManager;

    /**
     The cannon constructor takes the JPanel and the x, and y coordinates, on which the cannon will be drawn.
     */
    public Cannon(JPanel p, int xPos, int yPos) {
        //Sets the drawing panel to the global panel variable.
        panel = p;

        //Sets the fixed dimensions of the cannon.
        width = 172;
        height = 135;

        //Sets the coordinates where the cannon will be drawn.
        x = xPos;
        y = yPos;

        //Loads the images for the different orientations of the cannon.
        cannonImageLeft = ImageManager.loadImage ("images/cannon-left.png");
        cannonImageRight = ImageManager.loadImage ("images/cannon-right.png");
        cannonImageUp = ImageManager.loadImage ("images/cannon-up.png");

        //Sets the default orientation of the cannon.
        cannonImage = cannonImageUp;

        //Loads the single instance sound manager.
        soundManager = SoundManager.getInstance();
    }

    /**
     Updates the cannon orientation/image based on the direction supplied.
     */
    public void updateCannonDirection(char dir){
        if(dir == 'u')
            cannonImage = cannonImageUp;
        else if(dir == 'l')
            cannonImage = cannonImageLeft;
        else if (dir == 'r')
            cannonImage = cannonImageRight;
    }

    /**
     Draws the cannon to the image context, at the supplied coordinates using the supplied dimensions.
     */
    public void draw (Graphics2D g2) {
        g2.drawImage(cannonImage, x, y, width, height, null);
    }

    /**
     Determines the orientation of the cannon based on the input coordinates that the user has shot. Also plays the sound for firing the cannon.
     */
    public void fireCannon(int x, int y){
        soundManager.playClip("fireLaser", false);

        int screenWidth = panel.getWidth();

        if(x < screenWidth/3)
            updateCannonDirection('l');
        else if (x < 2*screenWidth/3)
            updateCannonDirection('u');
        else if (x < screenWidth)
            updateCannonDirection('r');
    }

}
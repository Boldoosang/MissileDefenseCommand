//Justin Baldeosingh
//816021226
//COMP 3609 - Assignment 2

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Jet {
    //Sets the drawing panel to the global panel variable.
    private JPanel panel;

    //Declares the coordinate variables of the jet.
    private int x;
    private int y;

    //Declares the width and height dimensions of the jet.
    private int width;
    private int height;

    private int dx;		// increment to move along x-axis
    private int dy;		// increment to move along y-axis


    //Declares the images that will be used for the jet orientations.
    private Image jetImage;
    private Image jetImageLeft;
    private Image jetImageRight;

    public Jet(JPanel p, int xPos, int yPos) {
        //Loads the main panel, to be used as the drawing context.
        panel = p;

        //Sets the dimensions of the jet.
        width = 70;
        height = 70;


        //Sets the initial x and y coordinates of the jet.
        x = xPos;
        y = yPos;

        //Jet moves across the screen only.
        dx = 25;
        dy = 0;

        //Loads the Jet image sprites.
        jetImageLeft = ImageManager.loadImage ("images/jet-l.png");
        jetImageRight = ImageManager.loadImage ("images/jet-r.png");

        //Sets the initial jet orientation.
        jetImage = jetImageRight;
    }


    public void draw (Graphics2D g2) {
        g2.drawImage(jetImage, x, y, width, height, null);
    }

    //Moves the jet
    public void move() {
        if (!panel.isVisible ()) return;

        //Updates the x and y coordinates of the jet as the jet moves.
        x = x + dx;

        if(x > panel.getWidth() - width){
            dx = -dx;
            jetImage = jetImageLeft;
        } else if(x < 0){
            dx = -dx;
            jetImage = jetImageRight;
        }
    }

    public int getJetX(){
        return x;
    }
}
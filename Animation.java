//Justin Baldeosingh
//816021226
//COMP 3609 - Assignment 2

import java.awt.Image;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;


/**
    The Animation class manages a series of images (frames) and the amount of time to display each frame.
*/
public class Animation {
    private GamePanel panel;					    // JPanel on which animation is being displayed
    private ArrayList<AnimFrame> frames;			// collection of frames for animation
    private int currFrameIndex;					    // current frame being displayed
    private long animTime;					        // time that the animation has run for already
    private long startTime;					        // start time of the animation or time since last update
    private long totalDuration;					    // total duration of the animation

    private int x;                                  //The x coordinate of the animation.
    private int y;                                  //The y coordinate of the animation.

    //Used to denote when the animation is active and if the animation should be looped.
    private boolean isActive;
    private boolean loop;

    /**
        Creates a new, empty Animation.
    */
    public Animation(GamePanel p) {
	    panel = p;
        frames = new ArrayList<AnimFrame>();
        totalDuration = 0;
        this.isActive = true;
    }


    /**
        Adds an image to the animation with the specified duration (time to display the image).
    */
    public synchronized void addFrame(Image image, long duration) {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }


    /**
        Starts this animation over from the beginning.
    */
    public synchronized void start(int x, int y, boolean loop) {
        this.x = x;
        this.y = y;
        animTime = 0;
        currFrameIndex = 0;
        startTime = System.currentTimeMillis();

        //Sets the active status of the animation, and whether the animation should loop.
        this.isActive = true;
        this.loop = loop;
    }


    /**
        Updates this animation's current image (frame), if neccesary.
    */
    public synchronized void update(){
        if(!isActive)
            return;

        long currTime = System.currentTimeMillis();
        long elapsedTime = currTime - startTime;
        startTime = currTime;

        if (frames.size() > 1) {
            animTime += elapsedTime;				    // add elapsed time to amount of time animation has run for
            if (animTime >= totalDuration) {            // if the time animation has run for > total duration
                if(!loop)
                    isActive = false;
                animTime = animTime % totalDuration;     // reset time animation has run for
                currFrameIndex = 0;                      // reset current frame to first frame
            }

            while (animTime > getFrame(currFrameIndex).endTime) {
                currFrameIndex++;				         // set frame corresponding to time animation has run for
            }
        }
    }


    /**
        Gets this Animation's current image. Returns null if this
        animation has no images.
    */
    public synchronized Image getImage() {
        if (frames.size() == 0) {
            return null;
        }
        else {
            return getFrame(currFrameIndex).image;
        }
    }

    /**
     * Draws the current frame to the graphic context if the animation is active.
     */
    public void draw (Graphics2D g2) {
        if(!isActive)
            return;

       g2.drawImage(getImage(), x, y, 150, 150, null);
    }

    /**
     * Gets the number of frames in the animation.
     */

    public int getNumFrames() {
	    return frames.size();
    }



    /**
     * Gets the i-th frame of an animation.
     */
    private AnimFrame getFrame(int i) {
        return frames.get(i);
    }


    /**
     * Inner animation frame class for each of the individual frames of an animation.
     */

    private class AnimFrame {
        Image image;
        long endTime;

        public AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }
}

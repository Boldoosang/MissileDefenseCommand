//Justin Baldeosingh
//816021226
//COMP 3609 - Assignment 2

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.util.Random;
import java.awt.Image;

public class Missile {
    //Sets the Initial Difficulty;
    private int INITIAL_DIFFICULTY = 5;

    //Sets the drawing panel to the global panel variable.
    private JPanel panel;

    //Declares the coordinate variables of the missile.
    private int x;
    private int y;

    //Declares the width and height dimensions of the missile.
    private int width;
    private int height;

    //Declares the image used for storing the missile image.
    private Image missileImage;

    private int dx;		// increment to move along x-axis
    private int dy;		// increment to move along y-axis

    private Random random;	// to position missile randomly at top of screen

    //Declares the rectangle for storing the missile hitbox.
    private Rectangle2D.Double missile;

    //Declares the city object for representing the player.
    private City city;

    //Declares animation and effect objects for detonation and missile destruction.
    private Animation detonation;
    private DisintegrateFX missileDestroyed;

    //Declares the soundmanager for the single instance sound manager.
    private SoundManager soundManager;

    public Missile(JPanel p, int xPos, int yPos, City city, Animation detonation, DisintegrateFX missileDestroyed) {
        //Loads the main panel, to be used as the drawing context.
        panel = p;

        //Sets the dimensions of the missile.
        width = 41;
        height = 70;

        //Initializes a new random object.
        random = new Random();

        //Sets the global detonation and missile destroyed effects/animations for the current missile.
        this.detonation = detonation;
        this.missileDestroyed = missileDestroyed;

        //Sets the initial x and y coordinates of the missile.
        x = xPos;
        y = yPos;

        //Sets the initial difficulty of the missiles.
        dx = 2;
        dy = INITIAL_DIFFICULTY;

        //Gets a handle on the main city representing the player's objective.
        this.city = city;

        //Loads the missile image sprite.
        missileImage = ImageManager.loadImage ("images/missile.png");

        //Sets the single instance sound manager.
        soundManager = SoundManager.getInstance();
    }


    //Randomly sets the location of a missile once it has been destroyed or has collided with the city.
    public void ResetMissileLocation() {
        int panelWidth = panel.getWidth();
        x = random.nextInt (panelWidth - width);
        y = 20;
    }


    public void draw (Graphics2D g2) {
        g2.drawImage(missileImage, x, y, width, height, null);
    }

    //Moves the missile
    public void move() {
        if (!panel.isVisible ()) return;

        //Randomizes/Adds eratic movement to missiles
        if(random.nextBoolean()){
            dx *= -1;
        }


        //Updates the x and y coordinates of the missiles as the missiles move.
        x = x + dx;
        y = y + dy;

        //Ensures that the erratic movement of the missiles do not let the missiles leave the screen boundaries.
        if(x <= 0){
            x = 0;
        } else if(x + width > panel.getWidth()){
            x = panel.getWidth() - width;
        }

        //Determines if the missile has collided with the city.
        boolean collision = collidesWithCity();



        //If the missile has collided with the city, play the detonation animation at the point of the collision,
        //reset the missile location, (dynamically speeds up the missile), and plays the explosion and panic clips.
        if (collision){
            detonation.start(x-40, y, false);
            ResetMissileLocation();
            soundManager.playClip("cityExplosion", false);
            soundManager.playClip("panic", false);

            //Takes away a life from the user if they have lives remaining.
            int playerLives = city.getLives();
            if(playerLives > 0){
                city.loseLife();
            }
        }

    }

    //Determines if the user has clicked on the missile.
    public boolean missileClicked (int x, int y) {
        //Gets the hitbox of the missile.
        missile = getBoundingRectangle();
        //If the missile has not been initialized, then return false.
        if (missile == null)
            return false;

        //Determines if the missile hitbox contains the coordinates that correspond to the user's click.
        boolean onMissile = missile.contains(x, y);

        //If the user has clicked on the missile and the game is not over, play the missile destroyed animation,
        //play the missile destroyed sound effect, add 50 to the user's score, and reset the position of the missile.
        if (onMissile && !city.isGameOver()){
            missileDestroyed.start(this.x, this.y);
            soundManager.playClip("missileExplosion", false);
            int score = city.getScore();
            city.setScore(score + 50);
            ResetMissileLocation();
        }

        //Returns the outcome of clicking on the missile.
        return onMissile;
    }


    //Returns the rectangular hit-box of the missile.
    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double (x, y, width, height);
    }


    //Returns the outcome of the missile colliding with the city.
    public boolean collidesWithCity() {
        Rectangle2D.Double missileRect = getBoundingRectangle();
        Rectangle2D.Double cityRect = city.getBoundingRectangle();

        return missileRect.intersects(cityRect);
    }

    public void updateDifficulty(int difficultyStage){
        dy = INITIAL_DIFFICULTY + difficultyStage;
    }
}
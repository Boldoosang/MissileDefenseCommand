//Justin Baldeosingh
//816021226
//COMP 3609 - Assignment 2

import javax.swing.JPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

/**
 A component that displays all the game entities
 */
public class GamePanel extends JPanel {
    // Game Panel Entity Constants
    private static int NUM_STARS = 3;
    private static int NUM_MISSILES = 4;

    //Declares the main game entities.
    private Missile[] missiles;
    private City city;
    private Cannon cannon;
    private Paratrooper paratrooper;
    private Jet jet;

    //Declares the sound manager for playing sound clips.
    private SoundManager soundManager;

    //Declares the main game thread.
    private GameThread gameThread;

    //Declares image for the background and image context.
    private BufferedImage image;
    private Image backgroundImage;

    //Declares the animations for the game.
    private Animation[] missileDetonation;
    private Animation fireAnimation;

    //Declares the effects for the entities.
    private RotateFX[] twinklingStars;
    private DisintegrateFX[] missileDestroyed;

    //Declares a global scoreboard variable for holding the main scoreboard.
    private Scoreboard scoreboard;

    //Sets the gameStarted status to false.
    public boolean gameStarted = false;

    public GamePanel (Scoreboard scoreboard) {
        //Sets the sound manager to the single instance sound manager.
        soundManager = SoundManager.getInstance();
        //Sets the main scoreboard.
        this.scoreboard = scoreboard;

        //Loads the background image for hte game.
        backgroundImage = ImageManager.loadImage ("images/Background.jpg");

        //Initializes the main drawing context for double buffering rendering.
        image = new BufferedImage (612, 408, BufferedImage.TYPE_INT_RGB);

        //Declares arrays for missile detonation animations.
        missileDetonation = new Animation[NUM_MISSILES];

        //Declares arrays for rotating effects and disintegration effects used on stars and missiles.
        twinklingStars = new RotateFX[NUM_STARS];
        missileDestroyed = new DisintegrateFX[NUM_MISSILES];

        //Declares an array for storing an array of missiles.
        missiles = new Missile[NUM_MISSILES];


        //Loads the animations from the stripfiles.
        loadFireAnimation();
        loadExplosionAnimation();
    }


    private void createGameEntities() {
        //Creates the city entity.
        city = new City(this, 0, 292, scoreboard);

        //Creates the laser cannon entity.
        cannon = new Cannon(this, 230, 250);

        //Creates the jet entity.
        jet = new Jet(this, 0, 0);

        //Creates the paratrooper entity.
        paratrooper = new Paratrooper(this, city);

        //Creates the rotating stars effects.
        for(int i = 0; i < NUM_STARS; i++){
            twinklingStars[i] = new RotateFX(this);
        }

        //Creates the disintegration effects.
        for(int i = 0; i < NUM_MISSILES; i++){
            missileDestroyed[i] = new DisintegrateFX(this);
        }

        //Creates the missiles and assigns them their detonation and destroyed effects.
        for(int i = 0; i < NUM_MISSILES; i++){
            missiles[i] = new Missile(this, 50+i*100, 15+i*25, city, missileDetonation[i], missileDestroyed[i]);
        }

        //Places a fire animation on the city.
        fireAnimation.start(100,270, true);
    }

    //Loads the fire animation from the stripfile and assigns it.
    public void loadFireAnimation() {
        Image stripImage = ImageManager.loadImage("images/fire.png");

        int imageWidth = (int) stripImage.getWidth(null) / 10;
        int imageHeight = stripImage.getHeight(null) / 6;

        fireAnimation = new Animation(this);

        for (int i=0; i<6; i++) {
            for(int j=0; j<10; j++) {
                BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) frameImage.getGraphics();

                g.drawImage(stripImage,
                        0, 0, imageWidth, imageHeight,
                        j * imageWidth, i*imageHeight, (j * imageWidth) + imageWidth, (i*imageHeight)+imageHeight,
                        null);

                fireAnimation.addFrame(frameImage, 50);
            }
        }

    }

    //Loads the explosion animation from the stripfile and assigns it.
    public void loadExplosionAnimation() {
        Image stripImage = ImageManager.loadImage("images/explosion.png");

        int imageWidth = (int) stripImage.getWidth(null) / 5;
        int imageHeight = stripImage.getHeight(null) / 4;

        //Creates the animations for the missile detonations.
        for(int i = 0; i < NUM_MISSILES; i++){
            missileDetonation[i] = new Animation(this);
        }

        for (int i=0; i<4; i++) {
            for(int j=0; j<5; j++) {
                BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = (Graphics2D) frameImage.getGraphics();

                g.drawImage(stripImage,
                        0, 0, imageWidth, imageHeight,
                        j * imageWidth, i*imageHeight, (j * imageWidth) + imageWidth, (i*imageHeight)+imageHeight,
                        null);

                //Loads the frames for the explosion into the missile detonation animation objects.
                for(int k = 0; k < NUM_MISSILES; k++){
                    missileDetonation[k].addFrame(frameImage, 50);
                }
            }
        }

    }

    //Given an x and y coordinate, fires the cannon at the coordinates.
    public boolean targetSky (int x, int y) {
        boolean onMissile = false;
        boolean hitParatrooper = false;
        //Updates the cannon animation.
        cannon.fireCannon(x, y);

        //Determines if the paratrooper is shot down.
        paratrooper.shotDown(x, y);


        //Checks to see if any of the missiles have been clicked on.
        for(int i = 0; i < NUM_MISSILES; i++){
            boolean hitMissile = missiles[i].missileClicked(x, y);
            if(hitMissile) {
                onMissile = true;
            }
        }

        //Returns whether any missile was clicked.
        return onMissile;
    }


    public void startGame() {				// initialise and start the game thread
        Thread thread;

        if (gameThread == null) {
            soundManager.playClip ("background", true);
            createGameEntities();
            gameStarted = true;
            gameThread = new GameThread (this);
            thread = new Thread (gameThread);
            thread.start();
        }
    }


    public void restartGame() {				// initialise and start a new game thread

        Thread thread;

        if (gameThread == null || !gameThread.isRunning()) {
            soundManager.playClip ("background", true);
            createGameEntities();
            gameThread = new GameThread (this);
            gameStarted = true;
            thread = new Thread (gameThread);
            thread.start();
        }
    }


    public void pauseGame() {				// pause the game (don't update game entities)
        gameThread.pauseGame();
    }


    public void endGame() {					// end the game thread
        //If the game is running and the city object is initialized, display the game over screen and end the game once the game has ended.
        if(gameThread != null) {
            if(city != null)
                city.gameOver();
            gameThread.endGame();
            gameStarted = false;
            soundManager.stopClip("background");
        }
    }


    //Updates the entities throughout the game.
    public void gameUpdate () {
        //Updates the stars.
        for(int i = 0; i < NUM_STARS; i++){
            twinklingStars[i].update();
        }


        //Updates the fire animations.
        fireAnimation.update();

        //Moves the jet
        jet.move();

        //Obtains 'checkpoints' to determine when the fire particular events.
        int difficultyStage = scoreboard.getScore() / 1000;
        int dispenseLife = scoreboard.getScore() % 800;


        //Displays and moves the paratrooper every 800 score the user obtains.
        if(dispenseLife == 0 && scoreboard.getScore() != 0) {
            paratrooper.showParatrooper(jet.getJetX(), 0);
        }

        paratrooper.move();

        //Updates the missile animations and the missile movement.
        for (int i = 0; i< NUM_MISSILES; i++) {
            missileDestroyed[i].update();
            missiles[i].move();
            missileDetonation[i].update();
            missiles[i].updateDifficulty(difficultyStage);
        }
    }


    public void gameRender () {				// draw the game objects
        Graphics2D imageContext = (Graphics2D) image.getGraphics();

        imageContext.drawImage(backgroundImage, 0, 0, null);	// draw the background image
        if (city != null) {
            city.draw(imageContext);
        }




        if(jet != null){
            jet.draw(imageContext);
        }

        if (cannon != null) {
            cannon.draw(imageContext);
        }

        for(int i = 0; i < NUM_STARS; i++){
            twinklingStars[i].draw(imageContext);
        }

        for(int i = 0; i < NUM_MISSILES; i++){
            missileDestroyed[i].draw(imageContext);
        }

        fireAnimation.draw(imageContext);


        if (missiles != null) {
            for (int i = 0; i< NUM_MISSILES; i++) {
                missiles[i].draw(imageContext);
                if (missileDetonation[i] != null)
                    missileDetonation[i].draw(imageContext);

            }
        }

        if(paratrooper != null){
            paratrooper.draw(imageContext);
        }

        Graphics2D g2 = (Graphics2D) getGraphics();	// get the graphics context for the panel
        g2.drawImage(image, 0, 0, 612, 408, null);

        imageContext.dispose();
        g2.dispose();

        //If the number of lives of the city is 0 or less, end the game.
        if(city.getLives() <= 0)
            endGame();
    }

}
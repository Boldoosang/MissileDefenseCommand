import java.util.Random;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;

public class DisintegrateFX implements ImageFX {

	private static final int WIDTH = 41;		// width of the image
	private static final int HEIGHT = 70;		// height of the image

	private GamePanel panel;

	private int x;
	private int y;

	private BufferedImage spriteImage;		// image for sprite effect
	private BufferedImage copy;			// copy of image
	private boolean isActive;

	Graphics2D g2;

	int time, timeChange;				// to control when the image is grayed


	public DisintegrateFX (GamePanel p) {
		panel = p;

		time = 0;				// range is 0 to 70
		timeChange = 1;				// how to increment time in game loop

		spriteImage = ImageManager.loadBufferedImage("images/Missile.png");
		copy = ImageManager.copyImage(spriteImage);
	}

	/**
	 * Draws the animation at the x and y coordinates, sets the animation status to true, and starts the animation from the beginning.
	 */
	public void start(int x, int y){
		this.x = x;
		this.y = y;

		isActive = true;
		time = 0;
	}


  	public void eraseImageParts(BufferedImage im, int interval) {

    		int imWidth = im.getWidth();
    		int imHeight = im.getHeight();

    		int [] pixels = new int[imWidth * imHeight];
    		im.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

			for (int i = 0; i < pixels.length; i = i + interval) {
					pixels[i] = 0;    // make transparent (or black if no alpha)
			}
  
    		im.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);
  	}


	public void draw (Graphics2D g2) {
		//If the image is not active, return without erasing or drawing.
		if(!isActive)
			return;

		if (time == 2)
			eraseImageParts(copy, 11);
		else
		if (time == 4)
			eraseImageParts(copy, 7);
		else
		if (time == 6)
			eraseImageParts(copy, 5);
		else
		if (time == 8)
			eraseImageParts(copy, 3);
		else
		if (time == 10)
			eraseImageParts(copy, 2);
		else
		if (time == 12)
			eraseImageParts(copy, 1);
		else
		if (time == 14)
			copy = ImageManager.copyImage(spriteImage);

		//Only draw the image if the time is less than 14; solve the issue where it momentarily shows the full image after complete disintegration.
		if(time < 14)
			g2.drawImage(copy, x, y, WIDTH, HEIGHT, null);

	}


	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double (x, y, WIDTH, HEIGHT);
	}


	public void update() {
		time = time + timeChange;

		//If the time is greater than 14, set the active status of the application to false; perform effect once.
		if (time > 14){
			isActive = false;
			time = 0;
		}
	}

}
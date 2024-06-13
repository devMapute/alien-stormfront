package model;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	private BufferedImage image;
	

	
	public SpriteSheet(BufferedImage image) {
		this.image = image;
	}
	
	public BufferedImage grabimage(int col, int row, int width, int height) {
		BufferedImage img = image.getSubimage((col*96)-96, (row*192)-192, width, height);
		return img;
	}
}

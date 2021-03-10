package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Plattform extends GameObject{

	public Plattform(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY);
	
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getSprite() {
		BufferedImage image = new BufferedImage(size.getWidth(), size.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g = image.createGraphics();
		
		g.setColor(Color.white);
		g.fillRect(0, 0, size.getWidth(), size.getHeight());
		g.dispose();
		return image;
	}

}

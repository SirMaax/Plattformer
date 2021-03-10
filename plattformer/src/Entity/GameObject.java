package Entity;

import java.awt.Image;
import java.awt.Rectangle;

import EntityCore.Position;
import EntityCore.Size;

public abstract class GameObject {
	
	protected Position position;
	protected Size size;

	protected int deltaX;
	protected int deltaY;
	
	protected int[] value;
	
	public GameObject(int x, int y, int sizeX, int sizeY) {
		position = new Position(x,y);
		size = new Size(sizeX,sizeY);
	}
	
	
	public abstract void tick();
	public abstract Image getSprite();
	

	public Position getPosition() {
		return position;
	}


	public void setPosition(Position position) {
		this.position = position;
	}


	public Size getSize() {
		return size;
	}


	public void setSize(Size size) {
		this.size = size;
	}
	
	public int getDeltaX() {
		return deltaX;
	}
	
	public int getDeltaY() {
		return deltaY;
	}
	public int[] getSomeValue() {
		return value;
	}
	public Rectangle getBounds() {
		return new Rectangle(position.getX(), position.getY(),size.getWidth(), size.getHeight());
	}
}

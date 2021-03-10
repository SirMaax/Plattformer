package EntityCore;

public class Size {
	private int height;
	private int width;
	
	
	public Size(int height, int width) {
			this.height = height;
			this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int widght) {
		this.width = widght;
	}
	
	public void setWidthHeight(int width, int height) {
		this.width = width;
		this.height = height;
	}
}

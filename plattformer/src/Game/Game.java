package Game;

import java.awt.Canvas;
import java.awt.List;
import java.awt.Rectangle;
import java.util.ArrayList;

import Entity.GameObject;
import Entity.Plattform;
import Entity.cube;
import EntityCore.Position;
import Input.Input;
import Input.PlayerController;
import Window.Window;

public class Game extends Canvas {
	
	

	private static final long serialVersionUID = 1L;
	
	public int WIDTH;
	public int HEIGHT;
	
	private Window window;
	private GameLoop loop;
	private Input input;
	private cube player;
	private ArrayList<GameObject> gameObjects;
	
	public Game(int WIDTH, int HEIGHT) {
		input = new Input();
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		window= new Window(WIDTH, HEIGHT, "Plattformer", this,input);
		
		
		
		gameObjects = new ArrayList<GameObject>();
		player = new cube(1,500,50,25,new PlayerController(input),this);
		gameObjects.add(player);
		gameObjects.add(new Plattform(1, 1060, 50, 1920)); 
		gameObjects.add(new Plattform(1, 1, 5, 1920)); 
		gameObjects.add(new Plattform(WIDTH-6, 1, 1080, 5)); 
		gameObjects.add(new Plattform(1, 1, 1080, 5)); 
		gameObjects.add(new Plattform(550, 750, 50, 1920));
		gameObjects.add(new Plattform(550, 450, 1000, 5)); 
		this.HEIGHT = 1080;
	}
	//Transfers GameLoop ref to Input
	public void giveRef(GameLoop loop) {
		input.giveRef(loop);
	}
	
	//Tick method for big Classes
	void tick() {
		gameObjects.forEach(gameObject-> gameObject.tick());
		
	}
	
	//Renders the graphic
	void render() {
		window.render();

	}
	
	//Returns a list with all GameObjects 
	public ArrayList<GameObject> getGameObjects(){
		return gameObjects;

	}
	public int clampX(int x, int size) {
		if(x > WIDTH-size) return WIDTH-size-1;
		if(x < 0) return 1;
		
		return x;
	}
	public int clampY(int y,int size) {
		if(y > HEIGHT-size) return HEIGHT-size-1;
		if(y < 0) return 1;
		
		return y;
	}
	public int clamp(int value, int min, int max) {
		if(value > max)return max;
		if(value < min) return min;

		return value;		
	}
	public boolean intersectsOther(Rectangle own) {
		for(GameObject object : gameObjects) {
			if(object != player) {
				if(own.intersects(object.getBounds())) {
					System.out.println("Intersects! aka True");
					return true;
				}			
			}	
		}		
		return false;	
	}
	
	public Position getNextPos (Rectangle rec, int x, int y, int oldX, int oldY) {	
		for(GameObject object : gameObjects) {
			if(object != player) {
				if(rec.intersects(object.getBounds())) {
					
					
					Position objectPos = object.getPosition();
					if(oldY < y) {//Player is moving down
						System.out.println("Move down");
						return new Position(x,objectPos.getY() - player.getSize().getHeight());
					}else if(oldY > y ) {//Player is moving up
						
						System.out.println("Return old Pos!");
						return new Position(x,objectPos.getY());
					}
				}			
			}
			return new Position(x, y);
		}
		return null;				
		
	}
	
public int[] getNextPos2 (Rectangle rec, int x, int y, int oldX, int oldY) {
	ArrayList<GameObject> tempList = new ArrayList<GameObject>();	
		
	for(int i = 0; i < gameObjects.size(); i++) {
		GameObject temp = gameObjects.get(i);
		if(temp != player) {
			
			if(rec.intersects(temp.getBounds()))
			{
				tempList.add(temp);
			}
			
		}
		
	}
	//CASE 1
	
	//CASE 2 1 intersection
	if(tempList.size()==1) {
		GameObject object = tempList.get(0);
		
		int objectY = object.getPosition().getY();
		int objectX = object.getPosition().getX();
		
		
		double[] temp = vectorSchnittpunkt(oldX, oldY, x, y, objectX , objectY, objectX , objectY + object.getSize().getHeight());
		if(temp[0] < x && temp[0] > oldX && temp[1] < y && temp[1] > oldY) {
			//intersects
		}
		
		//Movement to right
		if (oldX < x && oldY < objectY + object.getSize().getHeight() && oldY > objectY ) {
			double[] temp = vectorSchnittpunkt(oldX, oldY, x, y, objectX , objectY, objectX , objectY + object.getSize().getHeight());
			System.out.println("Right");
			return new int[] {1, (int )temp[0], (int )temp[1],0};
		}
		
		//Movement to left
		if ( oldX > x && oldY > objectY && oldY < object.getSize().getHeight() ) {
			double[] temp = vectorSchnittpunkt(oldX, oldY, x, y, objectX + object.getSize().getWidth()  , objectY , objectX + object.getSize().getWidth(), objectY + object.getSize().getHeight());
			System.out.println("Left");
			return new int[] {1, (int )temp[0], (int )temp[1],0};
		}
		
		//Movement down
		if ( oldY < y && oldX > objectX && oldX < object.getSize().getWidth() ) {
			double[] temp = vectorSchnittpunkt(oldX, oldY, x, y, objectX, objectY, objectX + object.getSize().getWidth(), objectY) ;
			System.out.println("Move down");
			return new int[] {1, (int )temp[0] - player.getSize().getHeight(), (int )temp[1],0};
			
		}
		
		//Movement up
		if ( oldY > y && oldX > objectX && oldX < object.getSize().getWidth() ) {
			double[] temp = vectorSchnittpunkt(oldX, oldY, x, y, objectX, objectY + object.getSize().getHeight(), objectX + object.getSize().getWidth(), objectY + object.getSize().getHeight());
			System.out.println("Up");
			return new int[] {1, (int )temp[0], (int )temp[1],0};
		}
	}
	System.out.println("Nothing");
	return new int[] {1,x,y};
	
	
	}
	
	public boolean checkTouchWallFromSide(int y, int oldY, GameObject object, int objectY) {
		if((y > objectY && y < objectY+object.getSize().getHeight()) 
			&& (oldY > objectY && oldY < objectY+object.getSize().getHeight()))
		{
			return true;
		}
		return false;
	}
	
	public static double[] vectorSchnittpunkt (double aX, double aY, double bX, double bY, double cX, double cY, double dX, double dY) {
		
		bX = bX - aX;
		bY = bY - aY;
		dX = dX - cX;
		dY = dY - cY;
		if(dY == 0) {
			double preX = (cY - aY) / bY;			
			double x = aX + bX * preX;
			double y = aY + bY * preX;
			return new double[] {(aX + bX * preX ),(aY + bY * preX)};
		}
		
		double y1 = (aY - cY) / dY;
		double y2 =  bY / dY;
		
		double preX = (aX - cX - (dX * y1)) / ((dX * y2) - bX);
		
		double x = aX + bX * preX ;
		double y = aY + bY * preX ;
		
		if( x < bX && x > aX) {
			//Intersects
		}
		
		return new double[] {x,y};
	}
	
	
}

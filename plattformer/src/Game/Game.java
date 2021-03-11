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
	
	private Input input;
	private cube player;
	private ArrayList<GameObject> gameObjects;
	
	public Game(int WIDTH, int HEIGHT) {
		input = new Input();
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		window= new Window(WIDTH, HEIGHT, "Plattformer", this,input);
		
		
		
		gameObjects = new ArrayList<GameObject>();
		player = new cube(50,500,50,25,new PlayerController(input),this);
		gameObjects.add(player);
		gameObjects.add(new Plattform(1, 1060, 50, 1920)); 
//		gameObjects.add(new Plattform(1, 1, 5, 1920)); 
//		gameObjects.add(new Plattform(WIDTH-6, 1, 1080, 5)); 
//		gameObjects.add(new Plattform(1, 1, 1080, 5)); 
		gameObjects.add(new Plattform(550, 650, 200,3500));
//		gameObjects.add(new Plattform(550, 450, 1000, 5)); 
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
	
	//CHECKS IF A RECTANGLE INTERSECTS WITH ANOTHER RECTANGLE (NEVER CHECKS IF PLAYER IS INTERSECTING)
	public GameObject[] checkInterSect(Rectangle rec, int x, int y, int oldX, int oldY) {
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
		if(tempList.size() == 0)return null;
		
		GameObject[] result = new GameObject[tempList.size()];
		
		for(int i = 0; i < tempList.size() ; i++) {
			result[i] = tempList.get(i);
		}	
		
		return result;
	}
	
	//GIVES THE NEXT POSITION FOR AN ENTITY 
	public int[] getNextPos3 (Rectangle rec ,int x, int y, int oldX, int oldY) {
		//CASES 1-4 : 1 = UPPER SIDE, 2 = DOWN SIDE, 3 = LEFT SIDE, 4 = RIGHT SIDE
		//CASES 5-8 : 5 = LEFT UP, 6 = LEFT RIGHT, 7 = RIGHT UP, 8 = RIGHT DOWN
		
		GameObject[] objects = checkInterSect(rec, x, y, oldX, oldY);
		
		//WHEN PLAYER IS NOT Intersecting
		if(objects == null) return new int[] {0, x, y};

		int objectX = objects[0].getPosition().getX();
		int objectY = objects[0].getPosition().getY();
		int objectYHeight = objects[0].getSize().getHeight();
		int objectYWIDTH = objects[0].getSize().getWidth();
		
		int playerHeight = player.getSize().getHeight();
		int playerWidth = player.getSize().getWidth();
		
		if(oldY <= y && oldX == x) {
			//CASE 1
			return new int[] {1, x  , objectY - playerHeight};
		}else if(oldY >= y && oldX == x) {
			//CASE 2
			return new int[] {1, x , objectY + objectYHeight};
		}else if(oldX <= x && oldY == y) {
			//CASE 3
			return new int[] {2, objectX - playerWidth, y};
		}else if(oldX >= x && oldY == y) {
			//CASE 4
			return new int[] {1, objectX + objectYWIDTH , y};
		}else if(oldX <= x && oldY <= y) {		
			//CASE 5
			if( (x - objectX) <= (y - objectY ) ) {
				//SIDE 3
				return new int[] {2, objectX - playerWidth , y};
			}else {
				//SIDE 1
				return new int[] {1, x , objectY - playerHeight};
			}
		}else if(oldX <= x && oldY >= y) {		
			//CASE 6
			if( (x - objectX) <= (objectY + objectYHeight - y ) )  {
				//SIDE 3
				return new int[] {2, objectX - playerWidth , y};
			}else {
				//SIDE 2
				return new int[] {1, x , objectY + objectYHeight};
			}
		}else if(oldX >= x && oldY <= y) {		
			//CASE 7
			if( (objectX + objectYWIDTH - x) <= (y - objectY) ) {
				//SIDE 4
				return new int[] {1, objectX + objectYWIDTH , y};
			}else {
				//SIDE 1
				return new int[] {1, x , objectY - playerHeight};
			}
		}else if(oldX >= x && oldY >= y) {		
			//CASE 8
			if( ( objectX + objectYWIDTH - x) <= (y - objectY + objectYHeight) ) {
				//SIDE 4
				return new int[] {1, objectX + objectYWIDTH , y};
			}else {
				//SIDE 2
				return new int[] {1, x , objectY + objectYHeight};
			}
		}
		return new int[] {0, 0, 0};
		
		
		
	}


	public int min(int a, int b) {
		if(a <= b)return b;
		return b;
	}

	public int max(int a, int b) {
		if(a >= b)return a;
		return b;
	}


}

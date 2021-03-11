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
		
	
	//CASE 1
	
	//CASE 2 1 intersection
	if(tempList.size()==1) {
		GameObject object = tempList.get(0);
		
		int objectY = object.getPosition().getY();
		int objectX = object.getPosition().getX();
			
			
			double[] tempLeft = null;
			double[] tempRight = null;
 		
			double[] tempDown = vectorSchnittpunkt(oldX, oldY, x, y, objectX , objectY, objectX + object.getSize().getWidth(), objectY);
			double[] tempUp = vectorSchnittpunkt(oldX, oldY, x, y, objectX  , objectY + object.getSize().getHeight(), objectX + object.getSize().getWidth(), objectY + object.getSize().getHeight());
			
			if(oldX - x != 0) {
			 tempLeft = vectorSchnittpunkt(oldX, oldY, x, y, objectX, objectY, objectX , objectY + object.getSize().getHeight()) ;
			 tempRight = vectorSchnittpunkt(oldX + 50, oldY, x, y, objectX + object.getSize().getWidth(), objectY, objectX + object.getSize().getWidth(), objectY + object.getSize().getHeight()) ;
			}else {
				tempLeft = new double[] {5000,5000};
				tempRight = new double[] {5000,5000};					
			}
			for(double ele : tempDown) {
				System.out.println("Down " + ele);
			}
			for(double ele : tempUp) {
				System.out.println("UP " + ele);
			}
			;
			for(double ele : tempLeft) {
				System.out.println("Left " + ele);
			}
			;
			for(double ele : tempRight) {
				System.out.println("Right " + ele);
			}
			System.out.println("oldX: " + oldX);
			System.out.println("newX: " + x);
			System.out.println("oldY: " + oldY);
			System.out.println("newY: " + y);
			int newX = (int) returnSmallest ( new double[] {tempDown[0],  tempUp[0], tempLeft[0], tempRight[0]}, x - player.getSize().getWidth(),x + player.getSize().getWidth(), oldX );
			int newY = (int) returnSmallest ( new double[] {tempDown[1],  tempUp[1], tempLeft[1], tempRight[1]}, y - player.getSize().getHeight(),y + player.getSize().getHeight(), oldY);
			System.out.println(newX);
			System.out.println(newY);
			System.out.println("---------");
			if(newY == tempDown[1]  ) {
				return new int[] {1, x, (int) (newY - rec.getHeight())};
			} 
			if(newY == tempUp[1]  ) {
				return new int[] {1,  (int) tempDown[0], (int) (newY)};
				
			}
			else if (newY == tempUp[1] && newX == tempRight[0] ) {
				return new int[] {1, newX, (int) (newY + object.getSize().getHeight())};
			}
			else if (newY == tempUp[1] && newX == tempLeft[0] ) {
				return new int[] {1, newX, (int) (newY + object.getSize().getHeight())};
			}
			//Left
			else if (newX == tempLeft[0] && oldY <= y ) {
				return new int[] {1, newX - player.getSize().getWidth(), (int) (y)};
			}
			else if (newX == tempLeft[0] && oldY > y ) {
				return new int[] {1, newX - player.getSize().getWidth(), (int) (y)};
			}
			//RIght
			else if (newX == tempRight[0] && oldY <= y ) {
				return new int[] {1, newX , (int) (tempRight[1])};
			}
			else if (newX == tempRight[0] && oldY > y ) {
				return new int[] {1, newX , (int) (tempRight[1])};
			}
		}
	return new int[] {0, x, y};
	}
	

	
	public static double[] vectorSchnittpunkt (double aX, double aY, double bX, double bY, double cX, double cY, double dX, double dY) {
		
		bX = bX - aX;
		bY = bY - aY;
		dX = dX - cX;
		dY = dY - cY;
		if(dY == 0) {
			double preX = (cY - aY) / bY;			
			return new double[] {(aX + bX * preX ),(aY + bY * preX)};
		}
		
		double y1 = (aY - cY) / dY;
		double y2 =  bY / dY;
		
		double preX = (aX - cX - (dX * y1)) / ((dX * y2) - bX);
		
		double x = aX + bX * preX ;
		double y = aY + bY * preX ;
		
		
		
		return new double[] {x,y};
	}
	public static double returnSmallest (double[] a, double x,double x2, double oldX) {
		double result = 0;
		for(int i = 0 ; i < 4; i++) {
			if(( a[i] >= oldX && (a[i] <= x || a[i] <=x2) ) || ((a[i] >= x || a[i] >= x2) && a[i] <= oldX)){
				result = a[i];
			}
		}
		
		double j = 1;
		if(result >= x)j = -1;
		
		for(int i = 0; i < a.length; i++) {
			if((x - result)*j > (x - a[i] )*j) {
				if(( a[i] > oldX && a[i] < x ) || (a[i] < x && a[i] > oldX))
				result =  a[i];
			}
		}		
		return result;
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




}

package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import EntityCore.Position;
import Game.Game;
import Input.Controller;

public class cube extends GameObject {
	
	private Game game;
	private Controller controller;
	
	private long currentTime = 0;					//Holds CurrentTime
	private int lastY = 0;							//Holds last Y Positon used for chekcing grounded
	
	private static long movementReduce = 50;		//Hardcoded at some point
	
	
	
	
//	private static long yMovementRate = 5;
//	private long lastYMovementUpdate = 0;
	
	//Jump Boolean
	private boolean onWall = false;
	private boolean airborn = false;				//Only tells if the player is allowedtoJump
	private boolean grounded = false;				//When True player is on ground
	//Jump Values
	private int allowedJumpDuration = 10;			//How long a Jump can possible be
	private int jumpForce = 5;						//Jump push every frame	
	private int jumpDuration = 0;					//Holds Duration of current Jump
	
	//Gravity Values
	private static int gravityUpdateRate = 25;		//How often gravity can be applied
	private long lastGravityUpdate = 0;				//Last Time Grvity was applied to the player
	private int gravityForce = 10;					//Gravity pull every frame
	private int incGravRate = 1;					//How fast gravityPull increases
	
	//LeftRight SlowDown
	private long lastXUpdate;						//Holds last X Achsis Slowdown
	private int leftRightStopForce = 5;				//How much left and right movement is slowed
	private int incLeftRightRate = 3;				//How fast leftRight slowdown increases
	
	//Left Right Acc
	private static long xMovementRate = 10;
	private int leftForce = 2;
	private int rightForce = 2;
	
	public cube(int x, int y , int sX, int sY,Controller controller, Game game) {
		super(x, y, sX, sY);
		this.controller = controller;
		this.game = game;
		deltaX = 0;
		deltaY = 0;
	}
	
	@Override
	public void tick() {
		currentTime = System.currentTimeMillis();
//			checkGrounded();
		
		
//		gravity();
		movement();
		
		
		
		int tempX = game.clampX(position.getX() + deltaX ,size.getWidth());
		int tempY = game.clampY(position.getY() + deltaY ,size.getHeight());
		
		int[] pos = game.getNextPos2(new Rectangle(tempX, tempY,size.getWidth(),size.getHeight()),tempX, tempY, position.getX(), position.getY());
		
		if(pos[0] == 0) {
			
		}
		else if(pos[0] == 1) {
			grounded = true;
			gravityUpdateRate = 25;
			
		}else if (pos[0] == 2) {
			onWall = true;
			grounded = true;
			deltaY = 0;
		}
//		if(pos[2]== 1) {
//			deltaY = 0;
//		}
//		if(pos[4]== 1) {
//			deltaX = 0;
//		}
		position.setX(pos[1]);
		position.setY(pos[2]);
		

	}
	
	//Defines look of PLayer
	public Image getSprite() {
		BufferedImage image = new BufferedImage(size.getWidth(), size.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = image.createGraphics();
		if(!grounded)g.setColor(Color.RED);
		else g.setColor(Color.CYAN);
		g.fillRect(0, 0, size.getWidth(), size.getHeight());
		g.dispose();
		
		return image;
	}
	
	public void movement() {
		//Y UP
		if(controller.isRequestingDown()) {
			
			deltaY++;
			deltaY = game.clamp(deltaY, 0, 50);
//			movementReduce=50;
		}
		//JUMP
		if(controller.isRequestingUp()) {	
			
			if(grounded) {			
				onWall = false;
				deltaY = 0;				
				jumpDuration = 0;
				grounded = false;
				airborn = true;
				
				deltaY -= jumpForce;
				deltaY = game.clamp(deltaY--, -50, 50);
				
				jumpDuration ++;
			}
			else if (airborn && jumpDuration !=0) 
			{
				deltaY -= jumpForce;
				deltaY = game.clamp(deltaY--, -50, 50);
				
				jumpDuration++;
				
				if(jumpDuration >= allowedJumpDuration) {
					jumpDuration = 0;
					airborn = false;
				}
			}
			
		}else {
			//Could count Frames where Jump Button wasnrt pressed to still give the chance to continue the jump if not pressed for only 1 frmae
			jumpDuration = 0;
			airborn = false;
		}
		
		
		//X LEFT
		if(controller.isRequestingLeft()) {
			if(currentTime - lastXUpdate > xMovementRate){			
				deltaX-= leftForce;
				deltaX = game.clamp(deltaX, -50, 50);
				movementReduce=50;
				currentTime = lastXUpdate;
				onWall = false;
			}
		}
		//X RIGHT
		if(controller.isRequestingRight()) {
			if(currentTime - lastXUpdate > xMovementRate){
				deltaX += rightForce;
				deltaX = game.clamp(deltaX, -50, 50);
				movementReduce=50;
				currentTime = lastXUpdate;
				onWall = false;
			}
		}
		 //X SLOWDOWN
		if(!controller.isRequestingRight() && !controller.isRequestingLeft()) {
			if(System.currentTimeMillis() - lastXUpdate > movementReduce) {
				movementReduce-= incLeftRightRate;
				
				if(deltaX > 0) {
					deltaX-= leftRightStopForce;
					deltaX = game.clamp(deltaX, 0, 50);
				}
				if(deltaX < 0) {
					deltaX += leftRightStopForce;
					deltaX = game.clamp(deltaX, -50, 0);	
				}
				
				lastXUpdate = System.currentTimeMillis();
			}
		}else
		{
			movementReduce = 50;
		} 
		
	}
	//Pulls the player down 
	
	public void gravity() {
		if(!airborn && !onWall) {
			if(System.currentTimeMillis() - lastGravityUpdate > gravityUpdateRate) {
				deltaY += gravityForce;
				deltaY = game.clamp(deltaY, -50, 50);
				gravityUpdateRate-= incGravRate;
				gravityUpdateRate = game.clamp(gravityUpdateRate,10,25);
				lastGravityUpdate = System.currentTimeMillis();
			}
		}else if (airborn){
			gravityUpdateRate =25;
		}
	}
	//CHECKS if player is in the air or grounded
	public void checkGrounded() {
		if(!grounded ) {
			if(position.getY() == lastY && deltaY != 0 && position.getY() > 1) {
				grounded = true;
			}else {
				grounded = false;
			}
			lastY = position.getY();		
		}
	}
	
	public int[] getSomeValue() {
		return new int[] {gravityUpdateRate, jumpDuration, position.getY() , position.getX()};
	}
}

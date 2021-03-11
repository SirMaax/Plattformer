package Window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import Entity.GameObject;
import Game.Game;
import Input.Input;

public class Window extends JFrame{
	
	private Canvas canvas;
	private JFrame frame;
	
	private int height;
	private int width;
	Game game;
	Input input;
	
	
	/*
	public Window(int height, int width, String name, Game game, Input input) {
		this.height = height;
		this.width = width;
		this.game = game;
		this.input = input;
		
		setTitle(name);
		setDefaultCloseOperation(EXIT_ON_CLOSE);


		setResizable(true);
		setLocationRelativeTo(null);
		
		GraphicsEnvironment graphics =		GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = graphics.getDefaultScreenDevice();
		
		device.getFullScreenWindow();
		
		canvas = new Canvas();

		
		canvas.setFocusable(false);
	
		canvas.setPreferredSize(new Dimension(1920,1080));

		add(canvas);
//		add(input);
		pack();
		

		canvas.createBufferStrategy(3);
		
		setVisible(true);
	}
*/	
	
	
	
	public Window (int height, int width, String name, Game game, Input input) {
		this.width = width;
		this.input = input;
		this.height = height;
		this.game = game;
		
			
	    GraphicsEnvironment graphics =
	    GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = graphics.getDefaultScreenDevice();
	
		frame = new JFrame(name);
	
		frame.setPreferredSize(new Dimension(1920, 1080)); 
//		frame.setPreferredSize(new Dimension(600, 400)); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		
		frame.setLocationRelativeTo(null);
		frame.setFocusable(false);
	
		device.setFullScreenWindow(frame);
		
//		frame.addKeyListener(input);
		frame.add(input);
		frame.add(game);
		frame.pack();
		
		
		frame.createBufferStrategy(3);
		frame.setVisible(true);
	}
	
	public void render() {
//		BufferStrategy bs = canvas.getBufferStrategy();
		BufferStrategy bs = frame.getBufferStrategy();
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.BLACK);
//		g.fillRect(0, 0,canvas.getWidth(), canvas.getHeight());
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
//		g.fillRect(0,0,600,400);

		g.setColor(Color.RED);
		
		
		game.getGameObjects().forEach(gameObject -> g.drawImage(
				gameObject.getSprite(),
				gameObject.getPosition().getX(),
				gameObject.getPosition().getY(),
				null));
		
		renderInfo(g, game.getGameObjects().get(0));
		
		g.dispose();
		bs.show();
		
	}
	
	public void renderInfo(Graphics g, GameObject object) {
		g.setColor(Color.WHITE);
		g.drawString("SpeedX: " + object.getDeltaX(), 50, 50);
//		g.drawString("y Cord: " + object.getSomeValue()[2], 50, 70);
		g.drawString("X Cord: " + object.getSomeValue()[3], 50, 90);
		g.drawString("SpeedY: " + object.getDeltaY(), 150, 50);
		g.drawString("GRate: " + object.getSomeValue()[0], 250, 50);
		g.drawString("JumpDur: " + object.getSomeValue()[1], 350, 50);	
		g.drawString("New y CO: " + object.getPosition().getY(), 50, 70);
		
		Rectangle r = new Rectangle(0,0,25,25);
		Rectangle s = new Rectangle(0,0,25,25);
		
		r.intersects(s);
		
	}
	

	
	
}

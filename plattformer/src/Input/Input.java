package Input;

import java.awt.event.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import Game.GameLoop;


	public class Input extends JPanel{

	boolean[] pressed;		//Array for PLayer
	boolean[] gPressed;		//Array for general Game
		
	private long lastTime;
	private long pause = 3000;
	
	private GameLoop loop;
	
		public Input (){
		
		
		pressed = new boolean[4];
		gPressed = new boolean[1];
		
		
		InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();
			
			
		//Player Movement pressed
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "wPressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "sPressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "aPressed");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "dPressed");
		
		//Player movement released
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "wReleased");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "sReleased");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "aReleased");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "dReleased");
		//Organize Stuff
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "ESCpressed");

		
		am.put("wPressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pressed[0] = true;
//				System.out.println("W pressed!");
			}
		});
		am.put("sPressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pressed[1] = true;
//				System.out.println("S pressed!");
			}
		});
		am.put("aPressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pressed[2] = true;
//				System.out.println("A pressed!");
			}
		});
		am.put("dPressed", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pressed[3] = true;
//				System.out.println("D pressed!");
			}
		});
		am.put("wReleased", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pressed[0] = false;
//				System.out.println("W released!");
			}
		});
		am.put("sReleased", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pressed[1] = false;
//				System.out.println("S released!");
			}
		});
		am.put("aReleased", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pressed[2] = false;
//				System.out.println("S released!");
			}
		});
		am.put("dReleased", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pressed[3] = false;
//				System.out.println("S released!");
			}
		});
		//ESC Button
		am.put("ESCpressed", new AbstractAction() {
			
			public void actionPerformed(ActionEvent e) {
//				gPressed[0] = true;
				loop.stop();
			}
		});
		
		setFocusable(true);
		requestFocusInWindow();
		
		}
		

		//Zombie Method For Printing out Info
		public void tick() {
			if(System.currentTimeMillis() - lastTime > pause) {
			for(int i = 0; i < pressed.length ; i++) {
				System.out.println("Button "+ i + " is pressed " + pressed[i]);
					
				}
			lastTime = System.currentTimeMillis();
			}
		}
		//Returns the bool from the isPressed List which tells you if a certain key is currently pressed
		public boolean isPressed(int num) {			
			return pressed[num];
		}
		//Used to give Input the gameLoop reference
		public void giveRef(GameLoop loop) {
			this.loop = loop;
		}
	}
		

		

	
	

	

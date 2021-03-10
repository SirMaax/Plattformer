package plattformer;

import Game.Game;
import Game.GameLoop;
import Input.Input;

public class Launcher {
	
	private GameLoop loop;
	private Input input;
	
		public static void main(String args[]) {
			
		
			new Thread(new GameLoop(new Game(1920, 1080)));
					
		}
}

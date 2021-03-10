package Game;

public class GameLoop implements Runnable {
	
	Game game;
	private boolean running = false;
	private Thread thread;
	
	//For second game Loop
	private final double updateRate = 1.0d/60.0d;			//Defines how often per second the game updates 
	
	private final long updateInfoRate = 1000l;
	private long lastInfoUpdate = System.currentTimeMillis() + 1000;
	
	private int fps = 0;
	
	public GameLoop (Game game) {
		this.game= game;
		game.giveRef(this);
		running = true;
		run();
		
	}
		
	

	//STARTS THREADS
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
		}

	//STOPS THREADS
	public synchronized void stop() {
		System.exit(0);
	}
		
	//Game Loop
	public void run() {
		int fps = 0;
		running = true;
		double accumulator = 0;
		long currentTime, lastUpdate = System.currentTimeMillis();
			
		while(running) {			
			currentTime = System.currentTimeMillis();
			double lastRenderTime = (currentTime - lastUpdate) / 1000d;
			accumulator += lastRenderTime;
			lastUpdate = currentTime;
			

	
			if(accumulator >= updateRate) {
				while(accumulator >= updateRate) {				
					game.tick();
					accumulator -= updateRate;					
				}
				render();
			}
//		printInfo();
		}
		
	}
		
	//Calls Render Method in Game
	private void render() {
		game.render();
		fps++;
	}
	
	//Prints FPS on CONSOLE	
	private void printInfo() {
		if(System.currentTimeMillis() - lastInfoUpdate > updateInfoRate) {
			System.out.println("FPS : "+ fps);
			lastInfoUpdate = System.currentTimeMillis();
			fps = 0;
		}
		
	}
}

package Client;

public class GameLogic {
	private boolean running;

	private ServerConnection server;
	private Snake snake;
	private GameWindow gameWindow;

	public GameLogic() {
		running = true;

		this.server = new ServerConnection();
		this.gameWindow = new GameWindow();
		this.snake = new Snake();

		gameLoop();
	}

	private void gameLoop() {
		while (running){

		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
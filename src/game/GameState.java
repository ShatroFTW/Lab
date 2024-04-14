package game;

public class GameState {
	public static final GameState INSTANCE = new GameState();
	private int points;
	
	public int getPoints() { return points; }
	public void setPoints(int points) { this.points = points; }
	
	public GameState() {
		points = 0;
	}
	
	public void increaseScore() {
		points++;
	}
	
	public boolean loadGame() {
		//TODO: not implemented
		return false;
	}
	
	public boolean saveGame() {
		//TODO: not implemented
		return false;
	}
}

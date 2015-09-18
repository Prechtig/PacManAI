package behaviour.tree;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DataContext {
	
	// Singleton
	private static DataContext instance;
	private DataContext() {}
	public static DataContext getInstance() {
		if(instance == null)
			instance = new DataContext();
		return instance;
	}
	
	private Game game;
	private GHOST closeGhost;
	private GHOST closeEdibleGhost;
	private MOVE nextMove;
	
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public GHOST getCloseGhost() {
		return closeGhost;
	}
	public void setCloseGhost(GHOST closeGhost) {
		this.closeGhost = closeGhost;
	}
	public GHOST getCloseEdibleGhost() {
		return closeEdibleGhost;
	}
	public void setCloseEdibleGhost(GHOST closeEdibleGhost) {
		this.closeEdibleGhost = closeEdibleGhost;
	}
	public MOVE getNextMove() {
		return nextMove;
	}
	public void setNextMove(MOVE nextMove) {
		this.nextMove = nextMove;
	}
}

package behaviour.tree;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DataContext {
	
	private static Game game;
	private static GHOST closestGhost;
	private static GHOST closeEdibleGhost;
	private static MOVE nextMove;
	private static int closestPowerPillNodeIndex;
	
	public static Game getGame() {
		return game;
	}
	public static void setGame(Game game) {
		DataContext.game = game;
	}
	public static GHOST getClosestGhost() {
		return closestGhost;
	}
	public static void setClosestGhost(GHOST closeGhost) {
		DataContext.closestGhost = closeGhost;
	}
	public static GHOST getCloseEdibleGhost() {
		return closeEdibleGhost;
	}
	public static void setCloseEdibleGhost(GHOST closeEdibleGhost) {
		DataContext.closeEdibleGhost = closeEdibleGhost;
	}
	public static MOVE getNextMove() {
		return nextMove;
	}
	public static void setNextMove(MOVE nextMove) {
		DataContext.nextMove = nextMove;
	}
	public static int getClosestPowerPillNodeIndex() {
		return closestPowerPillNodeIndex;
	}
	public static void setClosestPowerPillNodeIndex(int closestPowerPillNodeIndex) {
		DataContext.closestPowerPillNodeIndex = closestPowerPillNodeIndex;
	}
}

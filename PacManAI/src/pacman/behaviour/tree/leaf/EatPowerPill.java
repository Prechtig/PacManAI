package pacman.behaviour.tree.leaf;

import pacman.behaviour.tree.DataContext;
import pacman.behaviour.tree.INode;
import pacman.behaviour.tree.Status;
import pacman.game.Game;
import pacman.game.Constants.DM;

public class EatPowerPill implements INode {

	@Override
	public Status run(Game game) {
		int pacmanCurrentNodeIndex = game.getPacmanCurrentNodeIndex();
		
		int[] activePowerPillsIndices = game.getActivePowerPillsIndices();
		DataContext.setNextMove(game.getNextMoveTowardsTarget(pacmanCurrentNodeIndex,game.getClosestNodeIndexFromNodeIndex(pacmanCurrentNodeIndex,activePowerPillsIndices,DM.MANHATTAN),DM.MANHATTAN));
		
		return Status.SUCCESS;
	}
}
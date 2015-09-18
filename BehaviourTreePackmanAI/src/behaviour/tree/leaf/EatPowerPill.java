package behaviour.tree.leaf;

import pacman.game.Game;
import pacman.game.Constants.DM;
import behaviour.tree.DataContext;
import behaviour.tree.INode;
import behaviour.tree.Status;

public class EatPowerPill implements INode {

	@Override
	public Status run(Game game) {
		int pacmanCurrentNodeIndex = game.getPacmanCurrentNodeIndex();
		
		int[] activePowerPillsIndices = game.getActivePowerPillsIndices();
		DataContext.setNextMove(game.getNextMoveTowardsTarget(pacmanCurrentNodeIndex,game.getClosestNodeIndexFromNodeIndex(pacmanCurrentNodeIndex,activePowerPillsIndices,DM.MANHATTAN),DM.MANHATTAN));
		
		return Status.SUCCESS;
	}
}
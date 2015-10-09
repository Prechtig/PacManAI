package appo.pacman.behaviour.tree.leaf;

import appo.pacman.behaviour.tree.DataContext;
import appo.pacman.behaviour.tree.INode;
import appo.pacman.behaviour.tree.Status;
import pacman.game.Constants.DM;
import pacman.game.Game;

/**
 * A task for eating power pills
 */
public class EatPowerPill implements INode {

	@Override
	public Status run(Game game) {
		int pacmanCurrentNodeIndex = game.getPacmanCurrentNodeIndex();
		
		int[] activePowerPillsIndices = game.getActivePowerPillsIndices();
		DataContext.setNextMove(game.getNextMoveTowardsTarget(pacmanCurrentNodeIndex,game.getClosestNodeIndexFromNodeIndex(pacmanCurrentNodeIndex,activePowerPillsIndices,DM.MANHATTAN),DM.MANHATTAN));
		
		return Status.SUCCESS;
	}
}
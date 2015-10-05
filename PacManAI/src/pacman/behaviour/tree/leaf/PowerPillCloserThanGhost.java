package pacman.behaviour.tree.leaf;

import pacman.behaviour.tree.DataContext;
import pacman.behaviour.tree.INode;
import pacman.behaviour.tree.Status;
import pacman.game.Constants.DM;
import pacman.game.Game;

public class PowerPillCloserThanGhost implements INode {

	@Override
	public Status run(Game game) {
		int pacmanCurrentNodeIndex = game.getPacmanCurrentNodeIndex();
		
		int[] activePowerPillsIndices = game.getActivePowerPillsIndices();
		if(activePowerPillsIndices.length == 0) {
			return Status.FAILURE;
		}
		
		int closestPowerPillNodeIndex = game.getClosestNodeIndexFromNodeIndex(pacmanCurrentNodeIndex, activePowerPillsIndices, DM.MANHATTAN);
		int closestGhostNodeIndex = game.getGhostCurrentNodeIndex(DataContext.getClosestGhost());
		
		double distanceToClosestPowerPill = game.getDistance(pacmanCurrentNodeIndex, closestPowerPillNodeIndex, DM.MANHATTAN);
		double distanceToClosestGhost = game.getDistance(pacmanCurrentNodeIndex, closestGhostNodeIndex, DM.MANHATTAN);
		
		DataContext.setClosestPowerPillNodeIndex(closestPowerPillNodeIndex);
		
		if(distanceToClosestPowerPill < distanceToClosestGhost)
			return Status.SUCCESS;
		return Status.FAILURE;
	}

}

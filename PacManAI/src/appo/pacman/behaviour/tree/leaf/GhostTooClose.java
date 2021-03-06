package appo.pacman.behaviour.tree.leaf;

import appo.pacman.behaviour.tree.DataContext;
import appo.pacman.behaviour.tree.INode;
import appo.pacman.behaviour.tree.Status;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

/**
 * Detects if a ghost is too close
 */
public class GhostTooClose implements INode {
	
	protected final int minDistance=5;

	@Override
	public Status run(Game game) {
		int current=game.getPacmanCurrentNodeIndex();
		
		GHOST closestGhost = null;
		int closestGhostDistance = Integer.MAX_VALUE;
		
		for(GHOST ghost : GHOST.values())
			if(game.getGhostEdibleTime(ghost)==0 && game.getGhostLairTime(ghost)==0) {
				int distanceFromGhost = game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost));
				if(distanceFromGhost < minDistance) {
					if(distanceFromGhost < closestGhostDistance) {
						closestGhost = ghost;
						DataContext.setClosestGhost(closestGhost);
					}
					
				}
			}
		if(closestGhost != null)
			return Status.SUCCESS;
		return Status.FAILURE;
	}

}

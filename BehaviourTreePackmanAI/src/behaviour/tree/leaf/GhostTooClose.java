package behaviour.tree.leaf;

import pacman.game.Constants.GHOST;
import pacman.game.Game;
import behaviour.tree.DataContext;
import behaviour.tree.INode;
import behaviour.tree.Status;

public class GhostTooClose implements INode {
	
	protected final int minDistance=20;

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
						DataContext.getInstance().setCloseGhost(closestGhost);
					}
					
				}
			}
		if(closestGhost != null)
			return Status.SUCCESS;
		return Status.FAILURE;
	}

}

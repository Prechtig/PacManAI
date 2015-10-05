package pacman.behaviour.tree.leaf;

import pacman.behaviour.tree.DataContext;
import pacman.behaviour.tree.INode;
import pacman.behaviour.tree.Status;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class CanEatGhost implements INode {

	@Override
	public Status run(Game game) {
		int current = game.getPacmanCurrentNodeIndex();
		
		int minDistance=Integer.MAX_VALUE;
		
		GHOST minGhost=null;		
		
		for(GHOST ghost : GHOST.values()) {
			if(game.getGhostEdibleTime(ghost)>0) {
				int distance=game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost));
				
				if(distance<minDistance) {
					minDistance=distance;
					minGhost=ghost;
				}
			}
		}
		
		if(minGhost!=null) { //we found an edible ghost
			DataContext.setCloseEdibleGhost(minGhost);
			return Status.SUCCESS;
		}
		return Status.FAILURE;
	}
}
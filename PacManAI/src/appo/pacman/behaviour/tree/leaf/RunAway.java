package appo.pacman.behaviour.tree.leaf;

import appo.pacman.behaviour.tree.DataContext;
import appo.pacman.behaviour.tree.INode;
import appo.pacman.behaviour.tree.Status;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * A task for running away from the ghosts
 */
public class RunAway implements INode {

	@Override
	public Status run(Game game) {
		GHOST ghost = DataContext.getClosestGhost();
		MOVE nextMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost),DM.PATH);
		DataContext.setNextMove(nextMove);
		return Status.SUCCESS;
	}

}

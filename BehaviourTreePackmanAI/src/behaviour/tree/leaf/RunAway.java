package behaviour.tree.leaf;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import behaviour.tree.DataContext;
import behaviour.tree.INode;
import behaviour.tree.Status;

public class RunAway implements INode {

	@Override
	public Status run(Game game) {
		GHOST ghost = DataContext.getInstance().getCloseGhost();
		MOVE nextMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost),DM.PATH);
		DataContext.getInstance().setNextMove(nextMove);
		return Status.SUCCESS;
	}

}

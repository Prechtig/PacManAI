package behaviour.tree.leaf;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import behaviour.tree.DataContext;
import behaviour.tree.INode;
import behaviour.tree.Status;

public class EatGhost implements INode {

	@Override
	public Status run(Game game) {
		GHOST ghost = DataContext.getInstance().getCloseEdibleGhost();
		MOVE nextMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost),DM.PATH);
		DataContext.getInstance().setNextMove(nextMove);
		return Status.SUCCESS;
	}

}

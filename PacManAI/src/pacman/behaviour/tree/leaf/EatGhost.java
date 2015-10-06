package pacman.behaviour.tree.leaf;

import pacman.behaviour.tree.DataContext;
import pacman.behaviour.tree.INode;
import pacman.behaviour.tree.Status;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class EatGhost implements INode {

	@Override
	public Status run(Game game) {
		GHOST ghost = DataContext.getCloseEdibleGhost();
		MOVE nextMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(ghost),DM.PATH);
		DataContext.setNextMove(nextMove);
		return Status.SUCCESS;
	}

}

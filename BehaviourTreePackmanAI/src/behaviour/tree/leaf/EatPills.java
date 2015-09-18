package behaviour.tree.leaf;

import pacman.game.Constants.DM;
import pacman.game.Game;
import behaviour.tree.DataContext;
import behaviour.tree.INode;
import behaviour.tree.Status;

public class EatPills implements INode {

	@Override
	public Status run(Game game) {
		int current = game.getPacmanCurrentNodeIndex();
		
		int[] pills=game.getActivePillsIndices();
		int[] powerPills=game.getActivePowerPillsIndices();		
		
		int[] targetsArray=new int[pills.length + powerPills.length];
		System.arraycopy(pills, 0, targetsArray, 0, pills.length);
		System.arraycopy(powerPills, 0, targetsArray, pills.length, powerPills.length);
		
		//return the next direction once the closest target has been identified
		DataContext.setNextMove(game.getNextMoveTowardsTarget(current,game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.MANHATTAN),DM.MANHATTAN));
		return Status.SUCCESS;
	}
}
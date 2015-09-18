package behaviour.tree.leaf;

import java.util.ArrayList;

import pacman.game.Game;
import pacman.game.Constants.DM;
import behaviour.tree.DataContext;
import behaviour.tree.INode;
import behaviour.tree.Status;

public class EatPills implements INode {

	@Override
	public Status run(Game game) {
		int current = game.getPacmanCurrentNodeIndex();
		
		int[] pills=game.getPillIndices();
		int[] powerPills=game.getPowerPillIndices();		
		
		ArrayList<Integer> targets=new ArrayList<Integer>();
		
		for(int i=0;i<pills.length;i++)					//check which pills are available			
			if(game.isPillStillAvailable(i))
				targets.add(pills[i]);
		
		for(int i=0;i<powerPills.length;i++)			//check with power pills are available
			if(game.isPowerPillStillAvailable(i))
				targets.add(powerPills[i]);				
		
		int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array
		
		for(int i=0;i<targetsArray.length;i++)
			targetsArray[i]=targets.get(i);
		
		//return the next direction once the closest target has been identified
		DataContext.getInstance().setNextMove(game.getNextMoveTowardsTarget(current,game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH),DM.PATH));
		return Status.SUCCESS;
	}

}

package behaviour.tree.controller;

import java.util.ArrayList;
import java.util.List;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import behaviour.tree.DataContext;
import behaviour.tree.INode;
import behaviour.tree.Status;
import behaviour.tree.composite.Selector;
import behaviour.tree.composite.Sequence;
import behaviour.tree.leaf.CanEatGhost;
import behaviour.tree.leaf.EatGhost;
import behaviour.tree.leaf.EatPills;
import behaviour.tree.leaf.GhostTooClose;
import behaviour.tree.leaf.RunAway;

public class BehaviourTreePacMan extends Controller<MOVE> {
	
	private final INode root;
	
	
	
	/**
	 * GhostToClose
	 * Selector
	 * Sequence RunAway
	 * PillCloserThanGhost EatPill
	 * 
	 * 
	 */
	
	public BehaviourTreePacMan() {
		List<INode> runAwaySequenceList = new ArrayList<INode>();
		runAwaySequenceList.add(new GhostTooClose());
		runAwaySequenceList.add(new RunAway());
		
		Sequence runAwaySequence = new Sequence(runAwaySequenceList);
		
		List<INode> eatGhostSequenceList = new ArrayList<INode>();
		eatGhostSequenceList.add(new CanEatGhost());
		eatGhostSequenceList.add(new EatGhost());
		
		Sequence eatGhostSequence = new Sequence(eatGhostSequenceList);
		
		List<INode> children = new ArrayList<INode>();
		children.add(runAwaySequence);
		children.add(eatGhostSequence);
		children.add(new EatPills());
		
		root = new Selector(children);
		
		DataContext.getInstance().setNextMove(MOVE.LEFT);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		DataContext.getInstance().setGame(game);
		
		Status status = root.run(game);
		
//		if(status != Status.SUCCESS)
//			throw new IllegalStateException("Should always return SUCCESS at this point");
			
		
		return DataContext.getInstance().getNextMove();
	}
}
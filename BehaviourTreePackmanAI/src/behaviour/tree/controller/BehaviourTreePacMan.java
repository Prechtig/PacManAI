package behaviour.tree.controller;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import behaviour.tree.DataContext;
import behaviour.tree.INode;
import behaviour.tree.composite.Selector;
import behaviour.tree.composite.Sequence;
import behaviour.tree.leaf.CanEatGhost;
import behaviour.tree.leaf.EatGhost;
import behaviour.tree.leaf.EatPills;
import behaviour.tree.leaf.EatPowerPill;
import behaviour.tree.leaf.GhostTooClose;
import behaviour.tree.leaf.PowerPillCloserThanGhost;
import behaviour.tree.leaf.RunAway;

public class BehaviourTreePacMan extends Controller<MOVE> {
	
	private final INode root;
	
	/**
	 * GhostTooClose
	 * Selector
	 * Sequence RunAway
	 * PillCloserThanGhost EatPill
	 * 
	 * 
	 */
	public BehaviourTreePacMan() {
		Sequence runAwaySequence = new Sequence(new GhostTooClose(), new RunAway());
		
		Sequence eatGhostSequence = new Sequence(new CanEatGhost(), new EatGhost());
		
		root = new Selector(runAwaySequence, eatGhostSequence, new EatPills());
		
		DataContext.setNextMove(MOVE.LEFT);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		DataContext.setGame(game);
		
		root.run(game);
		
		return DataContext.getNextMove();
	}
	
	public void asd() {
		new Sequence(new PowerPillCloserThanGhost(), new EatPowerPill());
		
	}
}
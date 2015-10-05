package pacman.behaviour.tree.controller;

import pacman.behaviour.tree.DataContext;
import pacman.behaviour.tree.INode;
import pacman.behaviour.tree.composite.Selector;
import pacman.behaviour.tree.composite.Sequence;
import pacman.behaviour.tree.leaf.CanEatGhost;
import pacman.behaviour.tree.leaf.EatGhost;
import pacman.behaviour.tree.leaf.EatPills;
import pacman.behaviour.tree.leaf.EatPowerPill;
import pacman.behaviour.tree.leaf.GhostTooClose;
import pacman.behaviour.tree.leaf.PowerPillCloserThanGhost;
import pacman.behaviour.tree.leaf.RunAway;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

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
		Sequence runAwayOrEatPowerPillIfCloser = new Sequence(new GhostTooClose(), new Selector(new Sequence(new PowerPillCloserThanGhost(), new EatPowerPill()), new RunAway()));
		
		Sequence eatGhostSequence = new Sequence(new CanEatGhost(), new EatGhost());
		
		root = new Selector(runAwayOrEatPowerPillIfCloser, eatGhostSequence, new EatPills());
		
		DataContext.setNextMove(MOVE.LEFT);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		DataContext.setGame(game);
		
		root.run(game);
		
		return DataContext.getNextMove();
	}
	
	public void asd() {
		
		
	}
}
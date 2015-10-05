package pacman.mcts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MCTS extends Controller<MOVE> {
	
	private static Controller<EnumMap<GHOST,MOVE>> ghostController = new StarterGhosts();
	private static final double c = Math.sqrt(2d);
	private static final int depth = 25;
	
	private MOVE currentDirection = MOVE.LEFT;
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		if(isJunction(game)) {
			currentDirection = search(game, timeDue);;
		} else {
			currentDirection = getMoveTowardsCurrentJunction(game);
		}
		return currentDirection;
	}
	
	private MOVE getMoveTowardsCurrentJunction(Game game) {
		List<MOVE> possibleMoves = new ArrayList<MOVE>(Arrays.asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex())));
		if(possibleMoves.contains(currentDirection)) {
			return currentDirection;
		}
		possibleMoves.remove(currentDirection.opposite());
		Collections.shuffle(possibleMoves);
		return possibleMoves.get(0);
	}
	
	private boolean isJunction(Game game) {
		return game.isJunction(game.getPacmanCurrentNodeIndex());
	}
	
	public MOVE search(Game game, long timeDue) {
		Node node = new Node(game);
		
		while(System.currentTimeMillis() < timeDue-5) {
			node = treePolicy(node);
			int score = defaultPolicy(node, timeDue);
			backup(node, score);
		}
		return bestChild(node).move();
	}
	
	private void backup(Node node, int score) {
		while(node != null) {
			node.incrementVisits();
			node.addScore(score);
			node = node.parent();
		}
	}
	
	private int defaultPolicy(Node node, long timeDue) {
		Game game = node.game().copy();
		int currentDepth = 0;
		MOVE localCurrentDirection = null;
		while(currentDepth < depth) {
			if(currentDepth == 0 || isJunction(game)) {
				ArrayList<MOVE> moves = new ArrayList<MOVE>(Arrays.asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex())));
				Collections.shuffle(moves);
				localCurrentDirection = moves.get(0);
			}
			game.advanceGame(localCurrentDirection, ghostController.getMove(game, timeDue));
			
			currentDepth++;
		}
		return game.getScore();
	}
	
	private Node treePolicy(Node node) {
		while(!node.game().gameOver()) {
			if(!node.fullyExpanded()) {
				return node.expand();
			} else {
				node = bestChild(node);
			}
		}
		return node;
	}
	
	private Node bestChild(Node node) {
		double bestValue = 0d;
		Node selected = node;
		
		for(Node n : node.children()) {
			double uctValue =	(n.totalScore() / n.visits()) +
								c * Math.sqrt(Math.log(node.visits()) / n.visits());
			if(bestValue < uctValue) {
				selected = n;
				bestValue = uctValue;
			}
		}
		return selected;
	}
}

package appo.pacman.mcts;

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

public class Node {
	
	private final Game game;
	private final MOVE move;
	private final Node parent;
	private int visits;
	private int totalScore;
	private List<Node> children;
	private Controller<EnumMap<GHOST,MOVE>> ghostController = new StarterGhosts();
	
	public Node(Game game, MOVE move, Node parent) {
		this.game = game;
		this.move = move;
		this.parent = parent;
		children = new ArrayList<Node>();
	}
	
	public Node(Game game) {
		this(game, null, null);
	}
	
	/**
	 * Expand this node returning the expanded child 
	 */
	public Node expand() {
		List<MOVE> possibleMoves = Arrays.asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex()));
		Collections.shuffle(possibleMoves);
		
		Game copy = game.copy();
		for(MOVE move : possibleMoves) {
			if(!moveTried(move)) {
				copy.advanceGame(move, ghostController.getMove(copy, 0));
				Node child = new Node(copy, move, this);
				children.add(child);
				return child;
			}
		}
		throw new RuntimeException("No untried moves but not fully expanded. Should not happen.");
	}
	
	private boolean moveTried(MOVE move) {
		for(Node child : children) {
			if(child.move == move) {
				return true;
			}
		}
		return false;
	}
	
	public boolean fullyExpanded() {
		return children.size() == game.getPossibleMoves(game.getPacmanCurrentNodeIndex()).length;
	}
	
	public void incrementVisits() {
		visits++;
	}
	
	public int visits() {
		return visits;
	}
	
	public void addScore(int score) {
		totalScore += score;
	}
	
	public int totalScore() {
		return totalScore;
	}
	
	public List<Node> children() {
		return children;
	}
	
	public Game game() {
		return game;
	}
	
	public Node parent() {
		return parent;
	}
	
	public MOVE move() {
		return move;
	}
}
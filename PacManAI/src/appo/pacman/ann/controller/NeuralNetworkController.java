package appo.pacman.ann.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import appo.pacman.ann.evolution.Generation;
import appo.pacman.ann.io.IOManager;
import appo.pacman.ann.network.Network;
import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class NeuralNetworkController extends Controller<MOVE> {
	
	protected static final double MAX_DISTANCE = 255.0;
	protected static final int INITIAL_RUN_NUMBER = 1; 
	protected Network network;
	protected Generation currentGeneration;
	protected int currentRunNumber;

	public NeuralNetworkController() {
		this(IOManager.loadLatestGeneration().getNetworksSortedByFitness(1).get(0));
	}

	public NeuralNetworkController(Network network) {
		super();
		this.network = network;
		currentRunNumber = INITIAL_RUN_NUMBER;
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		network.updateScore(currentRunNumber, game.getScore());
		return getBestMove(game);
	}
	
	/**
	 * Returns the best possible move 
	 */
	protected MOVE getBestMove(Game game) {
		int[] neighbours = game.getNeighbouringNodes(game.getPacmanCurrentNodeIndex());
		
		int highestNeighbour = -1;
		double highestNeighbourScore = -1.0;
		for(int i = 0; i < neighbours.length; i++) {
			double evaluation = evaluateNode(neighbours[i], game);
			if(evaluateNode(neighbours[i], game) > highestNeighbourScore) {
				highestNeighbourScore = evaluation;
				highestNeighbour = neighbours[i];
			}
		}
		
		if(highestNeighbour == -1) {
			throw new IllegalStateException("Should not happen, revise code");
		}
		
		return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), highestNeighbour, DM.MANHATTAN);
	}
	
	/**
	 * Evaluate the goodness of a given game state 
	 */
	protected double evaluateNode(int node, Game game) {
		return network.calculateOutput(getInputFromGameStateAndNode(game, node))[0];
	}
	
	/**
	 * Constructs the input used by the ANN 
	 */
	protected double[] getInputFromGameStateAndNode(Game game, int node) {
		List<Double> input = new ArrayList<Double>();
		addDistancesToGhosts(game, input, node);
		addIsGhostsEdible(game, input);
		addDistanceToNearestPowerPill(game, input, node);
		addDistanceToNearestPill(game, input, node);
		
		return ArrayUtils.toPrimitive(input.toArray(new Double[input.size()]));
	}
	
	/**
	 * For each ghost adds the distance from Pac-Man to the ghost 
	 */
	protected void addDistancesToGhosts(Game game, List<Double> input, int node) {
		int pacmanCurrentNodeIndex = game.getPacmanCurrentNodeIndex();
		int currentGhostCurrentNodeIndex;
		for(GHOST ghost : GHOST.values()) {
			currentGhostCurrentNodeIndex = game.getGhostCurrentNodeIndex(ghost);
			double distanceToGhost = game.getDistance(pacmanCurrentNodeIndex, currentGhostCurrentNodeIndex, DM.MANHATTAN);
			input.add(scaleDistance(distanceToGhost));
		}
	}
	
	/**
	 * For each ghost adds whether or not the ghost is edible 
	 */
	protected void addIsGhostsEdible(Game game, List<Double> input) {
		for(GHOST ghost : GHOST.values()) {
			input.add(toZeroOrOne(game.isGhostEdible(ghost)));
		}
	}
	
	/**
	 * Adds the distance to the nearest power pill to the input 
	 */
	protected void addDistanceToNearestPowerPill(Game game, List<Double> input, int node) {
		int[] powerPills = game.getActivePowerPillsIndices();
		if(powerPills.length == 0) { // No power pills
			input.add(1d);
		} else {
			int clostedPowerPillNodeIndex = game.getClosestNodeIndexFromNodeIndex(node, powerPills, DM.MANHATTAN);
			input.add(scaleDistance(game.getDistance(node, clostedPowerPillNodeIndex, DM.MANHATTAN)));
		}
	}
	
	/**
	 * Adds the distance to the nearest pill to the input
	 */
	protected void addDistanceToNearestPill(Game game, List<Double> input, int node) {
		int[] pills = game.getActivePillsIndices();
		if(pills.length == 0) {
			input.add(1d);
		} else {
			int clostedPillNodeIndex = game.getClosestNodeIndexFromNodeIndex(node, pills, DM.MANHATTAN);
			input.add(scaleDistance(game.getDistance(node, clostedPillNodeIndex, DM.MANHATTAN)));
		}
	}
	
	protected double scaleDistance(double distance) {
		return distance/MAX_DISTANCE;
	}
	
	protected double toZeroOrOne(boolean bool) {
		return bool ? 1d : 0d;
	}
	
	public void setNetwork(Network network) {
		this.network = network;
	}
	
	public void setCurrentGeneration(Generation currentGeneration) {
		this.currentGeneration = currentGeneration;
	}
	
	public void incrementCurrentRunNumber() {
		currentRunNumber++;
	}
	
	public void resetCurrentRunNumber() {
		currentRunNumber = INITIAL_RUN_NUMBER;
	}
}
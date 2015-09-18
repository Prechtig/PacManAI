package controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import network.Network;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class SimpleNeuralNetworkController extends NeuralNetworkController {
	
	protected static final double MAX_DISTANCE = 255.0;

	public SimpleNeuralNetworkController(Network network) {
		super(network);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		network.updateScore(currentRunNumber, game.getScore());
//		currentGeneration.addFitnessToNetwork(0, game.getScore(), network);
		return getBestMove(game);
	}
	
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
	
	protected double evaluateNode(int node, Game game) {
		return network.calculateOutput(getInputFromGameStateAndNode(game, node))[0];
	}
	
	protected double[] getInputFromGameStateAndNode(Game game, int node) {
		List<Double> input = new ArrayList<Double>();
		addDistancesToGhosts(game, input, node);
		addIsGhostsEdible(game, input);
		addDistanceToNearestPowerPill(game, input, node);
		addDistanceToNearestPill(game, input, node);
		
		return ArrayUtils.toPrimitive(input.toArray(new Double[input.size()]));
	}
	
	protected void addDistancesToGhosts(Game game, List<Double> input, int node) {
		int pacmanCurrentNodeIndex = game.getPacmanCurrentNodeIndex();
		int currentGhostCurrentNodeIndex;
		for(GHOST ghost : GHOST.values()) {
			currentGhostCurrentNodeIndex = game.getGhostCurrentNodeIndex(ghost);
			double distanceToGhost = game.getDistance(pacmanCurrentNodeIndex, currentGhostCurrentNodeIndex, DM.MANHATTAN);
			input.add(scaleDistance(distanceToGhost));
		}
	}
	
	protected void addIsGhostsEdible(Game game, List<Double> input) {
		for(GHOST ghost : GHOST.values()) {
			input.add(toZeroOrOne(game.isGhostEdible(ghost)));
		}
	}
	
	protected void addDistanceToNearestPowerPill(Game game, List<Double> input, int node) {
		int[] powerPills = game.getActivePowerPillsIndices();
		if(powerPills.length == 0) { // No power pills
			input.add(1d);
		} else {
			int clostedPowerPillNodeIndex = game.getClosestNodeIndexFromNodeIndex(node, powerPills, DM.MANHATTAN);
			input.add(scaleDistance(game.getDistance(node, clostedPowerPillNodeIndex, DM.MANHATTAN)));
		}
	}
	
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
}
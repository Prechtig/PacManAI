package controller;

import java.util.Arrays;
import java.util.List;

import network.Network;
import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import evolution.Generation;

public abstract class NeuralNetworkController extends Controller<MOVE> {

	protected Network network;
	protected Generation currentGeneration;
	protected int currentRunNumber = 1;

	protected final static double MAX_X = 120.0;
	protected final static double MAX_Y = 120.0;


	public NeuralNetworkController(Network network) {
		super();
		this.network = network;
	}

	protected double toZeroOrOne(boolean bool) {
		return bool ? 1d : 0d;
	}
	
	protected void fillWithZeroOrOne(List<Double> input, boolean oneOrZero) {
		input.add(toZeroOrOne(oneOrZero));
	}
	
	protected void fillWithCoordinatesOfNode(List<Double> input, int node, Game game) {
		input.add(scaleXCoordinate(game.getNodeXCood(node)));
		input.add(scaleYCoordinate(game.getNodeYCood(node)));
	}
	
	private double scaleXCoordinate(int node) {
		return ((double) node) / MAX_X;
	}
	
	private double scaleYCoordinate(int node) {
		return ((double) node) / MAX_Y;
	}
	
	protected void fillWithPossibleMoves(List<Double> input, Game game) {
		List<MOVE> moves = Arrays.asList(game.getPossibleMoves(game.getPacmanCurrentNodeIndex()));
		for(MOVE move : MOVE.values()) {
			addZeroOrOneToInput(input, moves, move);
		}
	}
	
	protected void addZeroOrOneToInput(List<Double> input, List<MOVE> possibleMoves, MOVE expectedMove) {
		if(possibleMoves.contains(expectedMove)) {
			input.add(1.0);
		} else {
			input.add(0.0);
		}
	}
	
	protected MOVE getMoveFromOutput(double[] output) {
		int indexWithHighestValue = 0;
		double highestValue = Double.MIN_VALUE;
		
		for(int i = 0; i < output.length; i++) {
			double currentValue = output[i];
			if(currentValue > highestValue) {
				highestValue = currentValue;
				indexWithHighestValue = i;
			}
		}
		
		return MOVE.values()[indexWithHighestValue];
	}
	
	protected boolean isNearestGhostEdible(Game game) {
		int[] ghostNodes = new int[4];
		for(int i = 0; i < GHOST.values().length; i++) {
			ghostNodes[i] = game.getGhostCurrentNodeIndex(GHOST.values()[i]);
		}
		int nearestGhostNode = game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), ghostNodes, DM.MANHATTAN);
		for(GHOST ghost : GHOST.values()) {
			if(game.getGhostCurrentNodeIndex(ghost) == nearestGhostNode)
				return game.isGhostEdible(ghost);
		}
		throw new IllegalStateException("Should not happen, revise code");
	}
	
	protected int[] parseIntArray(int[] intArray) {
		if(intArray.length == 0) {
			return new int[] { 0 };
		}
		return intArray;
	}
	
	public Network getNetwork() {
		return network;
	}
	
	public void setNetwork(Network network) {
		this.network = network;
	}
	
	public Generation getCurrentGeneration() {
		return currentGeneration;
	}
	
	public void setCurrentGeneration(Generation currentGeneration) {
		this.currentGeneration = currentGeneration;
	}
	
	public void incrementCurrentRunNumber() {
		currentRunNumber++;
	}
	
	public void resetCurrentRunNumber() {
		currentRunNumber = 1;
	}
}

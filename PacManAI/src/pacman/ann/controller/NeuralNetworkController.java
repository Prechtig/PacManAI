package pacman.ann.controller;

import pacman.ann.evolution.Generation;
import pacman.ann.network.Network;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;

public abstract class NeuralNetworkController extends Controller<MOVE> {

	protected Network network;
	protected Generation currentGeneration;
	protected int currentRunNumber;
	protected final int initialRunNumber = 1; 

	public NeuralNetworkController(Network network) {
		super();
		this.network = network;
		currentRunNumber = initialRunNumber;
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
		currentRunNumber = initialRunNumber;
	}
}
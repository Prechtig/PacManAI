package evolution;

import java.util.EnumMap;

import network.Network;
import pacman.Executor;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import controller.NeuralNetworkController;
import controller.SimpleNeuralNetworkController;

public class Evovler {

	private static int inputNeurons, hiddenLayerNeurons, outputNeurons;
	private static NeuralNetworkController pacmanController;
	private static Controller<EnumMap<GHOST, MOVE>> ghostController;
	private static int numberOfEvaluations;
	private static int generationNumber, generationSize;
	private static int numberOfElitists;
	private static int numberOfParents;
	private static double mutationChance, mutationIntensity;
	private static double initialWeight, initialBias;
	private static boolean runInfinite;
	private static double terminalAverageFitness;
	
	private static void setDefaultArgs() {
		// General
		numberOfEvaluations = 25;
		runInfinite = true;
		terminalAverageFitness = 1500;
		
		// Neurons
		inputNeurons = 10;
		hiddenLayerNeurons = 10;
		outputNeurons = 1;

		// Generation
		generationNumber = 1;
		generationSize = 100;

		// Mutation
		numberOfElitists = 10;
		numberOfParents = 4;
		mutationChance = 0.1;
		mutationIntensity = 0.2;

		// Network
		initialWeight = 0.1;
		initialBias = 0.1;

		// Controllers
		ghostController = new StarterGhosts();
	}

	public static void main(String[] args) {
		setDefaultArgs();

		Network network = new Network(inputNeurons, hiddenLayerNeurons, outputNeurons);
		network.setWeightsAndBiases(initialWeight, initialBias);
		pacmanController = new SimpleNeuralNetworkController(network);

		Generation generation = new Generation(generationNumber, network, generationSize, mutationChance, mutationIntensity);
		do {
			evaluateGeneration(generation);
			
			printStatusToTerminal(generation);
			generationNumber++;
			
			generation = new Generation(generationNumber, generationSize, numberOfElitists, numberOfParents, generation, mutationChance, mutationIntensity);
		} while (keepEvolving(generation));
	}

	private static void evaluateGeneration(Generation generation) {
		pacmanController.setCurrentGeneration(generation);
		for (Network network : generation.getNetworks()) {
			pacmanController.setNetwork(network);
			pacmanController.resetCurrentRunNumber();
			for(int i = 0; i < numberOfEvaluations; i++) {
				new Executor().runGame(pacmanController, ghostController, false, 0);
				pacmanController.incrementCurrentRunNumber();
			}
		}
	}

	private static void printStatusToTerminal(Generation currentGeneration) {
		System.out.println("Generation: " + currentGeneration.getGenerationNumber());
		System.out.println("Average fitness: " + averageFitness(currentGeneration));
		System.out.println("Highest fitness: " + highestFitness(currentGeneration));
		System.out.println("Highest single fitness: " + currentGeneration.highestSingleFitness());
	}
	
	private static boolean keepEvolving(Generation generation) {
		if(runInfinite) return true;
		return averageFitness(generation) < terminalAverageFitness;
	}
	
	private static double averageFitness(Generation generation) {
		return generation.averageTotalFitness() / (double) numberOfEvaluations;
	}
	
	private static double highestFitness(Generation generation) {
		return (double) generation.highestTotalFitness() / (double) numberOfEvaluations;
	}
}
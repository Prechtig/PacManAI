package evolution;

import io.IOManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import network.Network;
import tool.CollectionTool;
import tool.MathTool;

public class Generation {
	private Map<Network, Integer> networksWithFitness;
	private List<Network> networks;
	private int generationNumber;

	public Generation(int number, int size, int numberOfElitists, int numberOfParents, double chanceOfMutation, double intensity) {
		List<Network> inputNetworks = new ArrayList<Network>();
		List<Network> parents = new ArrayList<Network>();

		int childrenPerParent = (size - numberOfElitists) / numberOfParents;
		int additionalChildren = (size - numberOfElitists) % numberOfParents;
		for (Network parent : parents) {
			for (int i = 0; i < childrenPerParent; i++) {
				inputNetworks.add(spawnChild(parent, chanceOfMutation, intensity));
			}
		}
		for (int i = 0; i < additionalChildren; i++) {
			inputNetworks.add(spawnChild(parents.get(0), chanceOfMutation, intensity));
		}

		if (inputNetworks.size() != size) {
			throw new IllegalArgumentException("Construction parameters did not add up.");
		}
		constructGeneration(number, inputNetworks);
	}

	public Generation(int number, int size, int numberOfElitists, int numberOfParents, Generation previousGeneration, SpawnCriteria spawn,
			double chanceOfMutation, double intensity, double crossoverProbability) {

		List<Network> inputNetworks = new ArrayList<Network>();
		List<Network> parents = new ArrayList<Network>();

		inputNetworks.addAll(previousGeneration.getNetworksSortedByFitness(numberOfElitists));

		parents.addAll(previousGeneration.getNetworksSortedByFitness(numberOfParents));

		if (spawn == SpawnCriteria.Mutation) {
			int childrenPerParent = (size - numberOfElitists) / numberOfParents;
			int additionalChildren = (size - numberOfElitists) % numberOfParents;
			for (Network parent : parents) {
				for (int i = 0; i < childrenPerParent; i++) {
					inputNetworks.add(spawnChild(parent, chanceOfMutation, intensity));
				}
			}
			for (int i = 0; i < additionalChildren; i++) {
				inputNetworks.add(spawnChild(parents.get(0), chanceOfMutation, intensity));
			}
		}
		if (spawn == SpawnCriteria.Crossover) {
			int numberOfChildren = size - numberOfElitists;
			for (int i = 0; i < numberOfChildren; i++) {
				if (MathTool.getDoubleBetweenZeroAnd(1) < crossoverProbability) {
					inputNetworks.add(spawnChild(CollectionTool.getRandomElement(parents), CollectionTool.getRandomElement(parents)));
				} else {
					inputNetworks.add(spawnChild(CollectionTool.getRandomElement(parents), chanceOfMutation, intensity));
				}
			}
		}

		if (inputNetworks.size() != size) {
			throw new IllegalArgumentException("Construction parameters did not add up.");
		}
		constructGeneration(number, inputNetworks);
	}

	public Generation(int number, List<Network> parents, int size, double chanceOfMutation, double intensity) {
		List<Network> inputNetworks = new ArrayList<Network>();
		inputNetworks.addAll(parents);

		int childrenPerParent = (size - parents.size()) / parents.size();
		int additionalChildren = (size - parents.size()) % parents.size();
		for (Network parent : parents) {
			for (int i = 0; i < childrenPerParent; i++) {
				inputNetworks.add(spawnChild(parent, chanceOfMutation, intensity));
			}
		}
		for (int i = 0; i < additionalChildren; i++) {
			inputNetworks.add(spawnChild(parents.get(0), chanceOfMutation, intensity));
		}

		constructGeneration(number, inputNetworks);
	}

	/***
	 * Seeds a generation from a single parent.
	 */
	public Generation(int number, Network parent, int size, double chanceOfMutation, double intensity) {
		List<Network> inputNetworks = new ArrayList<Network>(size);
		for (int i = 0; i < size - 1; i++) {
			inputNetworks.add(parent.cloneAndMutate(chanceOfMutation, intensity));
		}
		inputNetworks.add(parent);

		assert inputNetworks.size() == size;
		networks = inputNetworks;
	}

	/***
	 * Seeds a generation from a predefined list of networks.
	 */
	public Generation(int number, List<Network> inputNetworks) {
		constructGeneration(number, inputNetworks);
	}

	public Generation(int generationNumber, int generationSize, int numberOfElitists, int numberOfParents, Generation previousGeneration, double mutationChance, double mutationIntensity) {
		List<Network> inputNetworks = new ArrayList<Network>();
		List<Network> parents = new ArrayList<Network>();
		
		inputNetworks.addAll(previousGeneration.getNetworksSortedByFitness(numberOfElitists));
		
		parents.addAll(previousGeneration.getNetworksSortedByFitness(numberOfParents));
		
		int childrenPerParent = (generationSize-numberOfElitists)/numberOfParents;
		int additionalChildren = (generationSize-numberOfElitists)%numberOfParents;
		for(Network parent: parents) {
			for(int i = 0; i < childrenPerParent; i++) {
				inputNetworks.add(parent.cloneAndMutate(mutationChance, mutationIntensity));
			}
		}
		// Spawn the rest using the best as parent
		for(int i = 0; i<additionalChildren; i++) {
			inputNetworks.add(inputNetworks.get(0).cloneAndMutate(mutationChance, mutationIntensity));
		}
		
		if(inputNetworks.size() != generationSize) {
			throw new IllegalArgumentException("Construction parameters did not add up.");
		}
		
		this.generationNumber = generationNumber;
		networks = inputNetworks;
	}

	private void constructGeneration(int number, List<Network> inputNetworks) {
		this.generationNumber = number;
		networksWithFitness = new HashMap<Network, Integer>();
		for (Network network : inputNetworks) {
			networksWithFitness.put(network, 0);
		}
	}

	private Network spawnChild(Network parent, double chanceOfMutation, double intensity) {
		return parent.cloneAndMutate(chanceOfMutation, intensity);
	}

	private Network spawnChild(Network father, Network mother) {
		// Genome combinedGenome =
		// parent1.getGenome().crossover(parent2.getGenome());
		// return new Network(combinedGenome, parent1.getSpecies());
		return father.crossover(mother);
	}

	public void addFitnessToNetwork(int tryNumber, int fitness, Network network) {
		if (!networksWithFitness.containsKey(network)) {
			throw new IllegalStateException("Tried to add fitness to a network that did not exist in the generation");
		}
		networksWithFitness.replace(network, fitness);
	}

	// public double getAverageFitnessOfNetwork(Network network) {
	// return networksWithFitness.get(network).getAverageFitness();
	// }

	public List<Network> getNetworksSortedByFitness(int topN) {
		return new ArrayList<Network>(getNetworksSortedByFitness().subList(0, topN));
	}

	public List<Network> getNetworksSortedByFitness() {
		ArrayList<Network> sortedNetworks = new ArrayList<Network>(networks);

		sortedNetworks.sort(Collections.reverseOrder(new Comparator<Network>() {
			@Override
			public int compare(Network o1, Network o2) {
				return o1.getTotalScore().compareTo(o2.getTotalScore());
			}
		}));
		return sortedNetworks;
	}

	// public Network selectParentToNextGenerationBasedOnFitness() {
	// if (networksWithFitness.keySet().size() == 0) {
	// throw new IllegalStateException(
	// "The generation must have networks to select the fittest");
	// }
	// for (Evaluation e : networksWithFitness.values()) {
	// if (e.getAverageFitness() < 0.0) {
	// System.out.println(Arrays.toString(networksWithFitness.values()
	// .toArray(new Integer[0])));
	// throw new IllegalStateException(
	// "Not all fitness values have been updated");
	// }
	// }
	//
	// double totalFitness = 0.0;
	// for (Evaluation e : networksWithFitness.values()) {
	// totalFitness += e.getAverageFitness();
	// }
	//
	// double selector = MathTool.getDoubleBetweenZeroAnd(totalFitness);
	// double accumulatedFitness = 0;
	// for (Entry<Network, Evaluation> entry : networksWithFitness.entrySet()) {
	// if (isIncludedInInterval(selector, accumulatedFitness,
	// accumulatedFitness + entry.getValue().getAverageFitness())) {
	// return entry.getKey();
	// } else {
	// accumulatedFitness += entry.getValue().getAverageFitness();
	// }
	// }
	//
	// throw new IllegalStateException("Should not happen, revise code");
	// }

	// public Network selectParentToNextGenerationBasedOnRank() {
	// if (networksWithFitness.keySet().size() == 0) {
	// throw new IllegalStateException(
	// "The generation must have networks to select the fittest");
	// }
	//
	// for (Evaluation e : networksWithFitness.values()) {
	// if (e.getAverageFitness() < 0.0) {
	// System.out.println(Arrays.toString(networksWithFitness.values()
	// .toArray(new Integer[0])));
	// throw new IllegalStateException(
	// "Not all fitness values have been updated");
	// }
	// }
	//
	// networksWithFitness = CollectionTool.sortByValue(networksWithFitness);
	// // More tun if need be
	// int i = 0;
	// for (Entry<Network, Evaluation> entry : networksWithFitness.entrySet()) {
	// if (MathTool.getDoubleBetweenZeroAnd(1) < 0.5
	// || i == networksWithFitness.entrySet().size() - 1)
	// return entry.getKey();
	// i++;
	// }
	//
	// throw new IllegalStateException("Should not happen, revise code");
	// }

	private boolean isIncludedInInterval(double input, double lower, double upper) {
		return input >= lower && input <= upper;
	}

	// public void evenAllFitnessValues() {
	// for (Entry<Network, Evaluation> ne : networksWithFitness.entrySet()) {
	// addFitnessToNetwork(1, 1, ne.getKey());
	// }
	// }

	public void saveGeneration() {
		// IOManager.saveMultipleGenomesToFile(number,
		// Network.networksToGenomes(getTopNetworksWithHighestFitness(getSize())));
		IOManager.saveToFile(getNetworksSortedByFitness(getSize()), generationNumber);
		IOManager.saveGenerationToFile(this, generationNumber);
	}
	
	public Integer highestSingleFitness() {
		int highestScore = 0;
		for(Network network : networks) {
			for(Integer score : network.getScores()) {
				if(highestScore < score ) {
					highestScore = score;
				}
			}
		}
		return highestScore;
	}

	public Integer highestTotalFitness() {
		return getNetworksSortedByFitness().get(0).getTotalScore();
	}

	public double averageTotalFitness() {
		Integer sum = 0;
		for(Network network : networks) {
			sum += network.getTotalScore();
		}
		return (double) sum / (double) getSize();
//		int result = 0;
//		for (Entry<Network, Integer> entry : networksWithFitness.entrySet()) {
//			result += entry.getValue();
//		}
//		return result;
	}

	public double averageFitness() {
		return ((double) averageTotalFitness()) / ((double) getNetworks().size());
	}

	public int getSize() {
		return networks.size();
//		return networksWithFitness.size();
	}

	public List<Network> getNetworks() {
		return networks;
//		return new ArrayList<Network>(Arrays.asList(networksWithFitness.keySet().toArray(new Network[0])));
	}

	public Network getNetwork(int index) {
		return getNetworks().get(index);
	}

	public int getGenerationNumber() {
		return generationNumber;
	}
}

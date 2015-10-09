package appo.pacman.ann.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import appo.pacman.ann.network.Network;

public class Generation {
	private List<Network> networks;
	private int generationNumber;
	
	public Generation(List<Network> networks, int generationNumber) {
		this.networks = networks;
		this.generationNumber = generationNumber;
	}

	/***
	 * Seeds a generation from a single parent.
	 */
	public Generation(int generationNumber, Network parent, int size, double chanceOfMutation, double intensity) {
		this.generationNumber = generationNumber;
		
		List<Network> inputNetworks = new ArrayList<Network>(size);
		for (int i = 0; i < size - 1; i++) {
			inputNetworks.add(parent.cloneAndMutate(chanceOfMutation, intensity));
		}
		inputNetworks.add(parent);

		assert inputNetworks.size() == size;
		networks = inputNetworks;
	}

	public Generation(int generationNumber, int generationSize, int numberOfElitists, int numberOfParents, Generation previousGeneration, double mutationChance, double mutationIntensity) {
		this.generationNumber = generationNumber;
		
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
		
		networks = inputNetworks;
	}

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
	}

	public int getSize() {
		return networks.size();
	}

	public List<Network> getNetworks() {
		return networks;
	}

	public int getGenerationNumber() {
		return generationNumber;
	}
}

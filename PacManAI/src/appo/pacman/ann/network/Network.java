package appo.pacman.ann.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {
	
	private List<Layer> layers;
	private Map<Integer, Integer> scores = new HashMap<Integer, Integer>();
	
	public Network(Integer... numberOfNeuronsAtLayer) {
		construct(numberOfNeuronsAtLayer);
	}
	
	protected Network(List<Layer> layers) {
		this.layers = new ArrayList<Layer>();
		for(Layer layer : layers) {
			this.layers.add(layer);
		}
	}
	
	/**
	 * Constructs a network with the given number of neurons at the different layers
	 * @param numberOfNeuronsAtLayer
	 */
	private void construct(Integer... numberOfNeuronsAtLayer) {
		layers = new ArrayList<Layer>(numberOfNeuronsAtLayer.length);
		
		// Construct the "fake" first layer
		layers.add(new Layer(0, numberOfNeuronsAtLayer[0]));
		
		// Construct all subsequent layers
		for(int i = 1; i < numberOfNeuronsAtLayer.length; i++) {
			int inputNeurons = numberOfNeuronsAtLayer[i-1];
			int outputNeurons = numberOfNeuronsAtLayer[i];
			
			layers.add(new Layer(inputNeurons, outputNeurons)); 
		}
	}
	
	/**
	 * Calculates the output of the given network
	 * @param input Given this input
	 * @return The output of the network
	 */
	public double[] calculateOutput(double[] input) {
		double[] output = layers.get(0).activateInput(input);
		
		Layer currentLayer;
		for(int i = 1; i < layers.size(); i++) {
			currentLayer = layers.get(i);
			output = currentLayer.calculateOutput(output);
		}
		return output;
	}
	
	/**
	 * @return This network with all layers mutated
	 */
	public Network mutateLayers(double chanceOfMutation, double intensity) {
		for(Layer layer : layers) {
			layer.mutate(chanceOfMutation, intensity);
		}
		return this;
	}
	
	/**
	 * @return Copies the network and mutates its layers
	 */
	public Network cloneAndMutate(double chanceOfMutation, double intensity) {
		return clone().mutateLayers(chanceOfMutation, intensity);
	}
	
	/**
	 * Sets all weights and biases in the given network to some value
	 * @param weight The value of the weight
	 * @param bias The value of the bias
	 */
	public void setWeightsAndBiases(double weight, double bias) {
		for(Layer layer : layers) {
			layer.setWeights(weight);
			layer.setBiases(bias);
		}
	}
	
	@Override
	public Network clone() {
		List<Layer> copyOfLayers = new ArrayList<Layer>(layers.size());
		for(Layer layer : layers) {
			copyOfLayers.add(layer.clone());
		}
		return new Network(copyOfLayers);
		
	}
	
	public Collection<Integer> getScores() {
		return scores.values();
	}
	
	public void updateScore(int run, int score) {
		if(!scores.containsKey(run)) {
			scores.put(run, score);
		} else {
			scores.replace(run, score);
		}
	}
	
	public Integer getTotalScore() {
		Integer sum = 0;
		for(Integer score : scores.values()) {
			sum += score;
		}
		return sum;
	}
}
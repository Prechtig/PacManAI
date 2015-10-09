package appo.pacman.ann.network;

import appo.pacman.ann.tool.MathTool;

public class Layer {
	
	private final int inputNeurons;
	private final int outputNeurons;
	private final double[][] weights;
	private final double[] biases;
	
	/**
	 * Defines a hidden layer or the output layer
	 * @param inputNeurons Amount of input neurons
	 * @param outputNeurons Amount of output neurons
	 * @param weights the weights of the connections
	 * @param biases the biases of the output neurons
	 */
	public Layer(int inputNeurons, int outputNeurons, double[][] weights, double[] biases) {
		this.inputNeurons = inputNeurons;
		this.outputNeurons = outputNeurons;
		this.weights = weights;
		this.biases = biases;
	}
	
	/**
	 * Defines the input layer
	 * @param inputNeurons Amount of input neurons
	 * @param outputNeurons Amount of output neurons
	 */
	public Layer(int inputNeurons, int outputNeurons) {
		this.inputNeurons = inputNeurons;
		this.outputNeurons = outputNeurons;
		this.weights = new double[inputNeurons][outputNeurons];
		this.biases = new double[outputNeurons];
	}
	
	/**
	 * Calculates the output of the input layer. Used to feed the first hidden layer or the output layer.
	 * @param inputValues The values given to the input neurons
	 * @return The calculated output
	 */
	public double[] activateInput(double[] inputValues) {
		double[] activatedInput = new double[outputNeurons];
		
		for(int currentOutputNeuronIndex = 0; currentOutputNeuronIndex < outputNeurons; currentOutputNeuronIndex++) {
			double inputValue = inputValues[currentOutputNeuronIndex];
			double bias = biases[currentOutputNeuronIndex];
			activatedInput[currentOutputNeuronIndex] = MathTool.sigmoid(inputValue + bias);
		}
		return activatedInput;
	}
	
	/**
	 * Calculates the output of a hidden layer or the output layer
	 * @param inputValues The values given to the input neurons
	 * @return The calculated output
	 */
	public double[] calculateOutput(double[] inputValues) {
		double[] output = new double[outputNeurons];
		
		for(int currentOutputNeuronIndex = 0; currentOutputNeuronIndex < outputNeurons; currentOutputNeuronIndex++) {
			// Loop through every input neuron adding the value of that
			double sum = 0d;
			for(int currentInputNeuronIndex = 0; currentInputNeuronIndex < inputNeurons; currentInputNeuronIndex++) {
				double weight = weights[currentInputNeuronIndex][currentOutputNeuronIndex];
				double inputValue = inputValues[currentInputNeuronIndex];
				sum += (inputValue * weight);
			}
			double bias = biases[currentOutputNeuronIndex];
			output[currentOutputNeuronIndex] = MathTool.sigmoid(sum + bias);
		}
		return output;
	}
	
	/**
	 * Mutate a given layer
	 * @param chanceOfMutation
	 * @param intensity
	 * @return The layer mutated
	 */
	public Layer mutate(double chanceOfMutation, double intensity) {
		for(int outputNeuron = 0; outputNeuron < outputNeurons; outputNeuron++) {
			biases[outputNeuron] += (MathTool.normalDistribution() * intensity);
			for(int inputNeuron = 0; inputNeuron < inputNeurons; inputNeuron++) {
				weights[inputNeuron][outputNeuron] += (MathTool.normalDistribution() * intensity);
			}
		}
		return this;
	}
	
	/**
	 * @return A copy of the layer mutated 
	 */
	public Layer copyAndMutate(double chanceOfMutation, double intensity) {
		return new Layer(inputNeurons, outputNeurons, weights.clone(), biases.clone()).mutate(chanceOfMutation, intensity);
	}
	
	public void setWeights(double value) {
		for(int inputNeuron = 0; inputNeuron < inputNeurons; inputNeuron++) {
			for(int outputNeuron = 0; outputNeuron < outputNeurons; outputNeuron++) {
				weights[inputNeuron][outputNeuron] = value;
			}
		}
	}
	
	public void setWeight(int inputNeuronIndex, int outputNeuronIndex, double value) {
		weights[inputNeuronIndex][outputNeuronIndex] = value;
	}
	
	public void setBiases(double value) {
		for(int outputNeuron = 0; outputNeuron < outputNeurons; outputNeuron++) {
			biases[outputNeuron] = value;
		}
	}
	
	public void setBias(int outputNeuronIndex, double value) {
		biases[outputNeuronIndex] = value;
	}
	
	@Override
	public Layer clone() {
		double[][] newWeights = weights.clone();
		for(int i = 0; i < weights.length; i++) {
			newWeights[i] = weights[i].clone();
		}
		return new Layer(inputNeurons, outputNeurons, newWeights, biases.clone());
	}
}     
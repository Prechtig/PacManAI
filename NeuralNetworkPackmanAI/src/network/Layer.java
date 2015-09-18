package network;

import tool.MathTool;

public class Layer {
	
	private final int inputNeurons;
	private final int outputNeurons;
	private final double[][] weights;
	private final double[] biases;
	
	public Layer(int inputNeurons, int outputNeurons, double[][] weights, double[] biases) {
		this.inputNeurons = inputNeurons;
		this.outputNeurons = outputNeurons;
		this.weights = weights;
		this.biases = biases;
	}
	
	public Layer(int inputNeurons, int outputNeurons) {
		this.inputNeurons = inputNeurons;
		this.outputNeurons = outputNeurons;
		this.weights = new double[inputNeurons][outputNeurons];
		this.biases = new double[outputNeurons];
	}
	
	public double[] activateInput(double[] inputValues) {
		double[] activatedInput = new double[outputNeurons];
		
		for(int currentOutputNeuronIndex = 0; currentOutputNeuronIndex < outputNeurons; currentOutputNeuronIndex++) {
			double inputValue = inputValues[currentOutputNeuronIndex];
			double bias = biases[currentOutputNeuronIndex];
			activatedInput[currentOutputNeuronIndex] = MathTool.sigma(inputValue + bias);
		}
		return activatedInput;
	}
	
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
			output[currentOutputNeuronIndex] = MathTool.sigma(sum + bias);
		}
		return output;
	}
	
	public Layer mutate(double chanceOfMutation, double intensity) {
		for(int outputNeuron = 0; outputNeuron < outputNeurons; outputNeuron++) {
			biases[outputNeuron] += (MathTool.normalDistribution() * intensity);
			for(int inputNeuron = 0; inputNeuron < inputNeurons; inputNeuron++) {
				weights[inputNeuron][outputNeuron] += (MathTool.normalDistribution() * intensity);
			}
		}
		return this;
	}
	
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
	
//	public Genome crossover(Genome other) {
//		if(other.getGenCode().length != genCode.length) {
//			throw new IllegalArgumentException("genCodes have to be of equal length");
//		}
//		
//		double[] resultGenCode = new double[genCode.length];
//		for(int i = 0; i < resultGenCode.length; i++) {
//			if(MathTool.getNumberBetweenZeroAndOne() < 0.5) {
//				resultGenCode[i] = genCode[i];
//			} else {
//				resultGenCode[i] = other.getGenCode()[i];
//			}
//		}
//		
//		return new Genome(resultGenCode);
//	}
	
	public Layer crossover(Layer other) {
		double[] crossoverBiases = new double[biases.length];
		double[][] crossoverWeights = new double[weights.length][weights[0].length];
		
		for(int outputNeuron = 0; outputNeuron < outputNeurons; outputNeuron++) {
			if(MathTool.nextNumberZeroToOne() < 0.5) {
				crossoverBiases[outputNeuron] = this.biases[outputNeuron];
			} else {
				crossoverBiases[outputNeuron] = other.biases[outputNeuron];
			}
			for(int inputNeuron = 0; inputNeuron < inputNeurons; inputNeuron++) {
				if(MathTool.nextNumberZeroToOne() < 0.5) {
					crossoverWeights[inputNeuron][outputNeuron] = this.weights[inputNeuron][outputNeuron];
				} else {
					crossoverWeights[inputNeuron][outputNeuron] = other.weights[inputNeuron][outputNeuron];
				}
			}
		}
		return new Layer(inputNeurons, outputNeurons, crossoverWeights, crossoverBiases);
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
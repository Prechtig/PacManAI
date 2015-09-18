package test;

import network.Network;

import org.testng.Assert;
import org.testng.annotations.Test;

public class NeuralNetworkTest {
	
	private static final double DELTA = 0.000001;
	
	@Test
	public void testANN1() {
////		Species s = new Species(2, 2, 1);
//		Genome g = new Genome(s, 0.1, 0.1);
//		Network n = new Network(g, s);
//		
//		List<Double> inputs = new ArrayList<Double>();
//		inputs.add(2.0);
//		inputs.add(-1.0);
//		List<Double> outputs = n.activateInputs(inputs);
//		
//		System.out.println(outputs.get(0));
//		
//		Assert.assertTrue(isCloseEnough(0.5525197575, outputs.get(0)));
		
		Network n = new Network(2, 2, 1);
		n.setWeightsAndBiases(0.1, 0.1);
		
		double[] input = new double[] { 2.0, -1,0 };
		double[] outputs = n.calculateOutput(input);
		
		Assert.assertEquals(outputs[0], 0.5525197575, DELTA);
	}
	
	@Test
	public void testANN2() {
//		Species s = new Species(2, 2, 1);
//		Genome g = new Genome(s, 0.1, 0.1);
//		Network n = new Network(g, s);
//		n.getAllNeurons().get(0).get(0).setBias(0.2);
//		
//		List<Double> inputs = new ArrayList<Double>();
//		inputs.add(2.0);
//		inputs.add(-1.0);
//		List<Double> outputs = n.activateInputs(inputs);
//		
//		Assert.assertTrue(isCloseEnough(0.5525311749, outputs.get(0)));
		
		Network n = new Network(2, 2, 1);
		n.setWeightsAndBiases(0.1, 0.1);
		n.getLayers().get(0).setBias(0, 0.2);
		
		double[] input = new double[] { 2.0, -1,0 };
		double[] outputs = n.calculateOutput(input);
		
		Assert.assertEquals(outputs[0], 0.5525311749, DELTA);
	}
	
	@Test
	public void testANN3() {
//		Species s = new Species(2, 2, 1);
//		Genome g = new Genome(s, 0.1, 0.1);
//		Network n = new Network(g, s);
//		n.getAllNeurons().get(0).get(0).setBias(0.2);
//		n.getAllNeurons().get(0).get(1).setBias(0.9);
//		n.getAllNeurons().get(0).get(0).getOutputSynapsis().get(0).setWeight(0.7);
//		
//		List<Double> inputs = new ArrayList<Double>();
//		inputs.add(2.0);
//		inputs.add(-1.0);
//		List<Double> outputs = n.activateInputs(inputs);
//		
//		AssertJUnit.assertTrue(isCloseEnough(0.5558727625, outputs.get(0)));
		
		Network n = new Network(2, 2, 1);
		n.setWeightsAndBiases(0.1, 0.1);
		n.getLayers().get(0).setBias(0, 0.2);
		n.getLayers().get(0).setBias(1, 0.9);
		n.getLayers().get(1).setWeight(0, 0, 0.7);
		
		double[] input = new double[] { 2.0, -1,0 };
		double[] outputs = n.calculateOutput(input);
		
		Assert.assertEquals(outputs[0], 0.5558727625, DELTA);
	}
}

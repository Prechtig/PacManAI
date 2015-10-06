package test;

import org.testng.Assert;
import org.testng.annotations.Test;

import pacman.ann.network.Network;

public class NeuralNetworkTest {
	
	private static final double DELTA = 0.000001;
	
	@Test
	public void testANN1() {
		Network n = new Network(2, 2, 1);
		n.setWeightsAndBiases(0.1, 0.1);
		
		double[] input = new double[] { 2.0, -1,0 };
		double[] outputs = n.calculateOutput(input);
		
		Assert.assertEquals(outputs[0], 0.5525197575, DELTA);
	}
	
	@Test
	public void testANN2() {
		Network n = new Network(2, 2, 1);
		n.setWeightsAndBiases(0.1, 0.1);
		n.getLayers().get(0).setBias(0, 0.2);
		
		double[] input = new double[] { 2.0, -1,0 };
		double[] outputs = n.calculateOutput(input);
		
		Assert.assertEquals(outputs[0], 0.5525311749, DELTA);
	}
	
	@Test
	public void testANN3() {
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

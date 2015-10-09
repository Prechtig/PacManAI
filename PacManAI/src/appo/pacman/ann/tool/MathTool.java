package appo.pacman.ann.tool;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;

public class MathTool {
	
	private static Random rng = new Random();
	public static NormalDistribution ndg = new NormalDistribution(0, 1);

	public static double sigmoid(double inputValue) {
		return 1 / (1 + FastMath.pow(Math.E, -inputValue));
	}
	
	public static double nextNumberZeroToOne() {
		return rng.nextDouble();
	}
	
	public static double normalDistribution() {
		return ndg.sample();
	}
	
	public static double getDoubleBetweenZeroAnd(double upperBound) {
		return rng.nextDouble()*upperBound;
	}
	
	public static int getIntFromZeroIncludingUpperExcludingZero(int upperBound) {
		return rng.nextInt(upperBound) + 1;
	}
	
	public static int getIntFromZeroAndIncludingZeroExcludingUpper(int upperBound) {
		return rng.nextInt(upperBound);
	}
}
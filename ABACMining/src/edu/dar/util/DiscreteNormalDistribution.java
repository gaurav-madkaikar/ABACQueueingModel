/**
 *  Mining Attribute-Based Access Control Policies
 * Copyright (C) 2014 Zhongyuan Xu
 * Copyright (C) 2014 Scott D. Stoller
 * Copyright (c) 2014 Stony Brook University
 * Copyright (c) 2014 Research Foundation of SUNY
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package edu.dar.util;

import java.util.ArrayList;
import java.util.Random;

//discrete version of normal distribution with the specified mean and
//standard deviation. it is a probability distribution on integers in the
//range 0 .. N-1.
public class DiscreteNormalDistribution implements ProbabilityDistribution {
	private org.apache.commons.math3.distribution.NormalDistribution normalDist;
	private double scale;
	// lower bound and upper bound
	private int N1, N2;
	// cumulative distribution function
	private double[] cdf;
	private Random randomGen = new java.util.Random();

	public DiscreteNormalDistribution(int N1, int N2, double mean, double sigma) {
		this.N1 = N1;
		this.N2 = N2;
		normalDist = new org.apache.commons.math3.distribution.NormalDistribution(
				mean, sigma);
		// compute scale factor.
		scale = 0;
		for (int i = N1; i <= N2; i++) {
			scale += normalDist.density(i);
		}
		// tabulate the cumulative distribution function, for more efficient
		// sampling later.
		cdf = new double[N2 - N1 + 1];
		cdf[0] = this.getProbability(N1);
		for (int i = 1; i <= N2 - N1; i++) {
			cdf[i] = cdf[i - 1] + this.getProbability(i + N1);
		}
		// cdf[N2 - N1] should equal 1.0, but it might not, due to rounding.
		// to avoid problems during sampling, set it to 1.0.
		cdf[N2 - N1] = 1.0;
	}

	public double getProbability(int i) {
		return (normalDist.density(i)) / scale;
	}

	// set seed of pseudorandom number generator used in nextValue().
	public void setSeed(long seed) {
		randomGen.setSeed(seed);
	}

	public int getNextDistVal() {
		double rnd = randomGen.nextDouble();
		int i = N1;
		// find the "bucket" in the cdf that rnd is in.
		while (rnd > cdf[i - N1] && i < N2) {
			i++;
		}
		return i;
	}

	// return next value, restricted to be one of the values in vals.
	public int nextValue(ArrayList<Integer> vals) {
		if (vals == null || vals.isEmpty()) {
			System.err.println("Passed in null or empty ArrayList for function nextValue()");
			return -1; 
		}
		double totalProb = 0;
		for (int val : vals) {
			totalProb += this.getProbability(val);
		}
		double rnd = randomGen.nextDouble();
		// scale rnd so it is between 0 and totalProb
		rnd = rnd * totalProb;
		// find the "bucket" that rnd is in.
		double sum = 0.0;
		int result = vals.get(0);
		for (int i : vals) {
			result = i;
			sum += getProbability(i);
			if (rnd <= sum) {
				break;
			}
		}
		return result;
	}

	public static void main(String[] args) {

		DiscreteNormalDistribution d = new DiscreteNormalDistribution(1, 4, 3,
				1);
		double sum = 0;
		for (int i = 1; i <= 4; i++) {
			sum += d.getProbability(i);
		}
		System.out.println(sum); // check that the sum is close to 1.0
		
		int values[] = new int[4];
		for (int i = 1; i <= 100; i++) {
			values[d.getNextDistVal() - 1]++;
		}
		for (int i = 0;i < 4; i++) {
			System.out.println(values[i]);
		}
	}
}

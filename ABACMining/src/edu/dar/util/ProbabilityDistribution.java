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

import java.util.Random;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.ExponentialDistributionImpl;
import org.apache.commons.math.distribution.WeibullDistributionImpl;

/**
 * ProabilityDistribution is an interface for defining various distributions.
 * 
 * @author Zhongyuan Xu
 * 
 */

public interface ProbabilityDistribution {
	public int getNextDistVal();

	public double getProbability(int rank);
}

class ExponentialDistrib implements ProbabilityDistribution {
	double lambda;
	private ExponentialDistributionImpl edmp;

	public ExponentialDistrib(double mean) {
		edmp = new ExponentialDistributionImpl(mean);
	}

	public double sample() {
		try {
			return edmp.sample();
		} catch (MathException e) {
			e.printStackTrace();
		}

		return -1.0;
	}

	@Override
	public int getNextDistVal() {
		try {
			return (int) edmp.sample();
		} catch (MathException e) {
			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public double getProbability(int rank) {
		return 0;
	}
}

class UniformDistrib implements ProbabilityDistribution {
	private Random rnd;
	private int size;

	public UniformDistrib(int size, long seed) {
		rnd = new Random(seed);
		if (size > 0)
			this.size = size;
		else
			this.size = 5;
	}

	public int getNextDistVal() {
		return rnd.nextInt(size) + 2;
	}

	@Override
	public double getProbability(int rank) {
		return 1.0 / (double) size;
	}
}

class MixedDistrib implements ProbabilityDistribution {
	private double probability;
	private Random rnd;
	private UniformDistrib uniformDist;

	public MixedDistrib(double probability, int size, long seed) {
		uniformDist = new UniformDistrib(size, seed);
		rnd = new Random(seed);
		this.probability = probability;
	}

	@Override
	public int getNextDistVal() {
		double d = rnd.nextDouble();
		if (d <= probability)
			return 1;
		else
			return uniformDist.getNextDistVal() + 2;

	}

	@Override
	public double getProbability(int rank) {
		return 0;
	}

}

class ConstantDistrib implements ProbabilityDistribution {
	private int value;

	public ConstantDistrib(int value) {
		this.value = value;
	}

	@Override
	public int getNextDistVal() {
		return value;
	}

	@Override
	public double getProbability(int rank) {
		return 1.0;
	}
}

class WeibullDistrib implements ProbabilityDistribution {

	private WeibullDistributionImpl wimp;

	public WeibullDistrib(double shape, double scale) {
		wimp = new WeibullDistributionImpl(shape, scale);
	}

	@Override
	public int getNextDistVal() {
		try {
			return (int) wimp.sample();
		} catch (MathException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public double getProbability(int rank) {
		return 1.0;
	}
}



/*
 * http://code.google.com/p/haggle/source/browse/android/LuckyMe/src/org/haggle/
 * LuckyMe
 * /Zipf.java?spec=svnf77395981c6e37ed2818509222364331001fd86e&r=092516d3d
 * 6672fe662051b1ef625345c265408af
 */
// Based on code by Hyunsik Choi
// http://diveintodata.org/2009/09/zipf-distribution-generator-in-java/
class ZipfDistrib implements ProbabilityDistribution {
	private Random rnd;
	private int size;
	private double skew;
	private double bottom = 0;

	/*
	 * @size: the number of elements
	 * 
	 * @skew: value of exponent characterizing the distribution, (assume 1 for
	 * now)
	 * 
	 * @seed: seed for Zipfian Distribution
	 */
	public ZipfDistrib(int size, double skew, long seed) {
		this.rnd = new Random(seed);
		this.size = size;
		this.skew = skew;

		for (int i = 1; i < size; i++) {
			this.bottom += (1 / Math.pow(i, this.skew));
		}
	}

	public void setSeed(long seed) {
		rnd = new Random(seed);
	}

	// the next() method returns an rank id. The frequency of returned rank ids
	// are follows Zipf distribution.
	public int getNextDistVal() {
		int rank;
		double frequency = 0;
		double dice;

		rank = rnd.nextInt(size);
		frequency = (1.0d / Math.pow(rank, this.skew)) / this.bottom;
		dice = rnd.nextDouble();

		while (!(dice < frequency)) {
			rank = rnd.nextInt(size);
			frequency = (1.0d / Math.pow(rank, this.skew)) / this.bottom;
			dice = rnd.nextDouble();
		}

		return rank;
	}

	// This method returns a probability that the given rank occurs.
	public double getProbability(int rank) {
		return (1.0d / Math.pow(rank, this.skew)) / this.bottom;
	}
}

class NormalDistribution implements ProbabilityDistribution {
	  private org.apache.commons.math3.distribution.NormalDistribution normalDist;
	  private double min, max;

	  public NormalDistribution(double min, double max, double mean, double sigma) {
	    normalDist = 
	      new org.apache.commons.math3.distribution.NormalDistribution(mean, sigma);
	    this.min = min;
	    this.max = max;
	  }

	  // set seed of pseudorandom number generator used in nextValue().
	  public void setSeed(long seed) {
	    normalDist.reseedRandomGenerator(seed);
	  }
	  
	  public double nextValue() {
	    double x = normalDist.sample();
	    while (x < min || x > max) {
	      x = normalDist.sample();
	    }
	    return x;
	  }
	  
	  @Override
	  //Not Implemented
	  public int getNextDistVal(){
		   return 0;
	  }
	  
	  @Override
	  //Not Implemented
	  public double getProbability(int rank) {
		  return 0;
	  }
}



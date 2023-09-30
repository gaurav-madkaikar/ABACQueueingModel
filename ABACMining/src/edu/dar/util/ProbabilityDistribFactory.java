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

/**
 * ProabilityDistribFactory is an interface for defining various distribution factories.
 * @author Zhongyuan Xu
 *
 */
public interface ProbabilityDistribFactory {
public ProbabilityDistribution create(int size, long seed);
}

class UniformDistribFactory implements  ProbabilityDistribFactory {

	  public UniformDistribFactory() {
	  }

	  public ProbabilityDistribution create(int size, long seed) {
	    return new UniformDistrib(size, seed);
	  }
}

class ConstantDistribFactory implements ProbabilityDistribFactory {
	public ConstantDistribFactory() {
		
	}
	public ProbabilityDistribution create(int value, long seed) {
		return new ConstantDistrib(value);
	}
}

class ExponentialDistribFactory implements ProbabilityDistribFactory {
	private double lambda; 
	public ExponentialDistribFactory(double lambda) {
		this.lambda = lambda;
	}
	public ProbabilityDistribution create(int value, long seed) {
		return new ExponentialDistrib(lambda);
	}
}


class ZipfDistribFactory implements ProbabilityDistribFactory {
	private double skew;

	public ZipfDistribFactory(double skew) {
		this.skew = skew;
	}

	public ProbabilityDistribution create(int size, long seed) {
		return new ZipfDistrib(size, skew, seed);
	}
}

class WeibullDistribFactory implements ProbabilityDistribFactory {
	
	public ProbabilityDistribution create(double shape, double scale){
		return new WeibullDistrib(shape, scale);
	}

	@Override
	public ProbabilityDistribution create(int size, long seed) {
		return null;
	}
}

class MixedDistribFactory implements ProbabilityDistribFactory{
	private double probability;
	
	public MixedDistribFactory(){
		this.probability = 0.5;
	}
	
	public MixedDistribFactory(double probability){
		this.probability = probability;
	}
	
	@Override
	public ProbabilityDistribution create(int size, long seed) {
		return new MixedDistrib(probability, size , seed);
	}
}



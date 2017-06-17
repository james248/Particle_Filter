package com.jcdeck.particlefilter;

/**
 * Contains multiple data points from the re-sampling process.
 * 
 * @author James C Decker
 *
 */
public class ResampleData {
	
	/**
	 * Constructs a data object to hold multiple values. The three parameters passed are
	 * the only ones this object holds. None of the values can be modified after the
	 * object is constructed
	 * 
	 * @param numRemoved the number of particles removed.
	 * @param largestWeight the largest weight of al the particles
	 * @param largestNormalizedWeight the largest normalized weight of the particles; i.e. the
	 * largest weight devided by the sum of the weights
	 */
	ResampleData(int numRemoved, double largestWeight, double largestNormalizedWeight){
		this.particlesRemoved = numRemoved;
		this.largestWeight = largestWeight;
		this.largetNormalizedWeight = largestNormalizedWeight;
	}
	
	/**
	 * The number of particles that were removed after re-sampling
	 */
	public final int particlesRemoved;
	
	/**
	 * The largest weight of all the particle. This should be from the particle that
	 * was duplicated most.
	 */
	public final double largestWeight;
	
	/**
	 * The normalized value of the largest weight. After normalizing the particles
	 * all the weights should add up to one.
	 */
	public final double largetNormalizedWeight;
	
	/**
	 * Returns the number of particles removed on the re-sampling and the
	 * largest normalized weight.
	 */
	@Override
	public String toString(){
		return "Resampling Data: "+this.particlesRemoved+
				" particles removed; Largest Normalized Weight: "+this.largetNormalizedWeight;
	}
	
}

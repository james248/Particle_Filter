package com.jcdeck.particlefilter;

/**
 * 
 * This is a particle that represents a possible state of the world. As measurements
 * are taken, particles that are more accurate representations of the world are kept
 * and duplicated and less accurate particles are removed. Each update, particles
 * are mutated to represent different outcomes from the action taken that update.
 * 
 * @author James C Decker
 *
 */
public abstract class Particle {
	
	/**
	 * how likely the particle is based on measurements from the real world
	 * 
	 */
	private double probability;
	
	/**
	 * Returns the weight of the particle
	 * 
	 * @return The weight of the particle
	 */
	public final double getProbability(){
		return this.probability;
	}
	
	/**
	 * Sets the probability of this particle being chosen.
	 * 
	 * @param newWeight new probability for the particle
	 */
	final void setProbability(double newWeight){
		this.probability = newWeight;
	}
	
	/**
	 * Multiplies the probability of this particle being chosen by {@code modifier}.
	 * 
	 * @param modifier what the probability will by multiplied with
	 */
	public void adjustProbability(double modifier){
		this.probability *= modifier;
	}
	
	/**
	 * Returns the value of a gaussian distribution given the center of the
	 * gaussian ({@code mu}) and when to evaluate at ({@code x}). It will use
	 * the static variable {@code senseNoise} defined in the Filter class as the
	 * width of the gaussian (sigma).
	 * 
	 * @param mu the center of the gaussian. This should be the real measurement.
	 * @param sigma the width of the gaussian. The noise of the reading. Not squared
	 * @param x where to evaluate. This should be the corresponding measurement
	 * of the particle
	 * @return the weight for the particle for one measurement
	 */
	protected final static double gaussian(final double mu, double sigma, final double x){
		
		//square sigma
		sigma *= sigma;
		
		//the difference between the the real value and the sensed value 
		final double dif = mu - x;
		
		//square it
		final double diffSquared = dif * dif;
		
		//calculate the numerator
		final double n = ((diffSquared / sigma) / 2.0) * -1;
		//calculate the denominator
		final double d = Math.sqrt(2.0 * Math.PI * sigma);
		
		//return the final result
		return Math.exp(n/d);
		
	}
	
	/**
	 * Multiplies the probability by a gaussian distribution given the center of the
	 * gaussian ({@code mu}) and when to evaluate at ({@code x}). The gaussian
	 * will have a width of {@code sigma}. A larger width 
	 * 
	 * @param mu the center of the gaussian. This should be the real measurement.
	 * @param sigma the width of the gaussian. The noise of the reading. Not squared
	 * @param x where to evaluate. This should be the corresponding measurement
	 * of the particle
	 */
	protected final void adjustWithGaussian(final double mu, double sigma, final double x){
		this.adjustProbability(Particle.gaussian(mu, sigma, x));
	}
	
	
	/**
	 * This should set the probability variable based on {@code data}. It should used
	 * the {@code gaussian()} function defined in this class. If there are multiple
	 * measurements in {@code data}, then {@code probability} should be multiplied by the
	 * gaussian of each of the measurements. The probability is set to 1 before calling
	 * this method.
	 * 
	 * @param data An object containing measurements from the real world
	 */
	public abstract void updateProb(World world, Data data);
	
	/**
	 * Performs Action {@code a} on this particle. The particle should
	 * add noise to the action so that all particles are different after
	 * taking the action.
	 * 
	 * @param a the action that is being taken by the real world
	 */
	public abstract void takeAction(Action a);
	
	/**
	 * Return the difference between this particle
	 * and the {@code realParticle}. Used for debugging to find
	 * the average error of all the particles
	 * 
	 * @param realParticle A particle representing the actual state of the world
	 * @return The error of this particle
	 * @see com.jcdeck.particlefilter.Filter#getError(Particle)
	 */
	public abstract double calculateError(Particle realParticle);
	
	/**
	 * Returns a copy of the particle for re-sampling
	 * 
	 * @return a new particle object equal to this particle
	 */
	public abstract Particle copy();
	
	/**
	 * Returns the probability of the particle
	 */
	@Override
	public String toString(){
		return "Particle probability: "+this.getProbability();
	}
	
}
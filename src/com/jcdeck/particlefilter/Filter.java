package com.jcdeck.particlefilter;

/**
 * 
 * This Filter holds all the particles in the particle filter. It is designed to
 * weight and re-sample the particles. It is also able to calculate the average
 * error of all the particles given the true data of the world.
 * 
 * @author James C Decker
 *
 */
public class Filter {
	
	/**
	 * all the particles in the filter
	 */
	private Particle[] particles;
	
	/**
	 * Constructs the Particle Filter. It takes an array of randomized
	 * particles to manipulate.
	 * 
	 * @param particles an array of randomized Particle objects
	 */
	public Filter(Particle[] particles){
		this.particles = particles;
	}
	
	/**
	 * Constructs a particle filter with "numberOfParticles" particles. Each
	 * particle is a copy of {@code particle}.
	 * 
	 * @param particle the particle to copy for each initial particle in the filter
	 * @param numberOfParticles the number of particles to instantiate the filter with
	 * 
	 * 
	 * @throws NullPointerExeption
	 */
	public Filter(Particle particle, int numberOfParticles) throws NullPointerException{
		this.particles = new Particle[numberOfParticles];
		for(int i = 0; i<this.getNumOfParticles(); i++)
			this.particles[i] = particle.copy();
		
	}
	
	/**
	 * Constructs a new particle filter the same as {@code other}. Each particle in {@code other} is
	 * copied to create the new {@code Filter};
	 * 
	 * @param other the particle filter to copy
	 */
	public Filter(Filter other){
		
		Particle[] others = other.getParticles();
		
		this.particles = new Particle[others.length];
		for(int i = 0; i<this.getNumOfParticles(); i++)
			this.particles[i] = others[i].copy();
		
	}
	
	
	
	
	/**
	 * Calls {@link com.jcdeck.particlefilter.Particle#takeAction(Action a) takeAction} on all the particles in the
	 * filter, passing {@code a} as a parameter. This is used when an action is
	 * being taken in the world and the particles should change to reflect
	 * possible outcomes of that action.
	 * 
	 * @param a the action taken in the real world
	 */
	public void takeAction(Action a){
		for(Particle particle : particles)
			particle.takeAction(a);
	}
	
	
	
	
	/**
	 * Updates the probability of each particle given {@code data}. The
	 * data should be a set of observations of the real world. If the state
	 * a particle represents would give similar data to the observations
	 * passed in {@code data}, then it is given a higher weight/probability. If
	 * it would not match the data from the real world it is given a lower
	 * weight.
	 * 
	 * @param data a set of observations from the real world
	 */
	public void updateParticleWeights(World world, Data data){
		
		for(Particle particle : particles){
			particle.setProbability(1);
			particle.updateProb(world, data);
		}
		
	}
	
	
	
	
	/**
	 * Re-samples the particles based on the probability of each particle. It
	 * will change the number of particles to equal the value passed to it.
	 * 
	 * @param numOfParticles the new number of particles there will be after
	 * re-sampling
	 * @return data about the weights of the particles
	 */
	private ResampleData resampleParticlesNum(int numOfParticles){
		
		Particle[] newParticles = new Particle[numOfParticles];
		
		//keeps track of which particles are being used
		boolean[] particlesCopied = new boolean[numOfParticles];
		
		int index = (int) (Math.random() * this.getNumOfParticles());
		double beta = 0;
		//set the max
		double max = 0;
		for(Particle particle : particles)
			max = Math.max(max, particle.getProbability());
		
		for(int i = 0; i<numOfParticles; i++){
			beta += Math.random() * 2 * max;
			while(beta > this.particles[index].getProbability()){
				beta -= this.particles[index].getProbability();
				index++;
				index %= this.getNumOfParticles();
			}
			newParticles[i] = this.particles[index].copy();
			//mark this particle as copied
			particlesCopied[index] = true;
		}
		
		this.particles = newParticles;
		
		//calculate the number of particles removed and return that value
		int count = 0;
		double sumOfWeights = 0;
		double largestWeight = 0;
		for(int i = 0; i<particlesCopied.length; i++){
			//count removed
			if(!particlesCopied[i])
				count++;
			//increase sum of weights
			sumOfWeights = this.particles[i].getProbability();
			//calculate the largest weight
			largestWeight = Math.max(largestWeight, this.particles[i].getProbability());
		}
		
		return new ResampleData(count, largestWeight, largestWeight/sumOfWeights);
		
	}
	
	/**
	 * Re-samples the particles based on the probability of each particle.
	 * It will change the number of particles based on the value passed to it.
	 * It 1 is passed then there will be the same number of particles as before.
	 * 
	 * @param percentOfParticles the percent of the number of existing particles
	 * that should exist after re-sampling. This should be a decimal representation
	 * of a percent
	 * @return data about the weights of the particles
	 */
	public ResampleData resampleParticles(double percentOfParticles){
		return resampleParticlesNum((int) (this.getNumOfParticles()*percentOfParticles));
	}
	
	/**
	 * Re-samples the particles based on the probability of each particle.
	 * There will be the same number of particles after re-sampling.
	 * 
	 * @return data about the weights of the particles
	 */
	public ResampleData resampleParticles(){
		return resampleParticlesNum(this.getNumOfParticles());
	}
	
	
	
	
	
	
	/**
	 * Returns the average of the errors form all the particles. This can
	 * only be used if the actual location is known. e.g. it is a virtual
	 * test; the values can be measured and given to the computer to debug.
	 * 
	 * @param realParticle A particle that represents the actual state of
	 * the world instead of a possible state
	 * @return The average error of all the particles
	 */
	public double getError(Particle realParticle){
		double sum = 0;
		for(Particle particle : particles)
			sum += particle.calculateError(realParticle);
		return sum/this.getNumOfParticles();
	}
	
	/**
	 * Returns all the particles in the particle filter. They can be used
	 * to find the average of the particles. This average would then act
	 * as the best estimate of the world.
	 * 
	 * @return All of the particle in the particle filter
	 */
	public Particle[] getParticles(){
		return this.particles;
	}
	
	/**
	 * Returns the number of particles in the filter. This can change during
	 * resampling.
	 * 
	 * @return  The number of particles being used.
	 */
	public int getNumOfParticles(){
		return this.particles.length;
	}
	
	/**
	 * Returns the number of particles
	 */
	@Override
	public String toString(){
		return "ParticleFilter: "+this.getNumOfParticles()+" particles";
	}
	
}

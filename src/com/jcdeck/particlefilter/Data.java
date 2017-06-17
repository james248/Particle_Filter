package com.jcdeck.particlefilter;

/**
 * Marks objects that contains real readings from the world. These readings
 * are used to update the probability of the particles.
 * 
 * <p>
 * 
 * The interface exists so the filter can move any readings around. It does not
 * matter what the readings are. The weights are updated in overridden abstract
 * methods of the particle.
 * 
 * @author James C Decker
 *
 */
public interface Data {

}

# Particle_Filter

Each Particle represents a posible state of the world. When Actions are taken, the particles randomly spread
out showing that the program is less certaint about the state of the world. When measurements are taken,
particles that closely match the measurements are duplicated and particles that do not are removed. After this
resampling, the particles will be clustered together showing that the program is more certaint about the state
of the world.

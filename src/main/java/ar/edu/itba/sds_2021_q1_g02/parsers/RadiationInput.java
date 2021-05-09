package ar.edu.itba.sds_2021_q1_g02.parsers;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;

public class RadiationInput {
    private final Particle[][] particles;
    private final double V0;
    private final double yOffset;

    public RadiationInput(Particle[][] particles, double v0, double yOffset) {
        this.particles = particles;
        this.V0 = v0;
        this.yOffset = yOffset;
    }

    public Particle[][] getParticles() {
        return this.particles;
    }

    public double getV0() {
        return this.V0;
    }

    public double getyOffset() {
        return this.yOffset;
    }
}

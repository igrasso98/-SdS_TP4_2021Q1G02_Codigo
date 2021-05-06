package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.Map;

public class Step {
    private final Map<Integer, Particle> particleMap;
    private final Map<Particle, Position> previousParticlesPosition;
    private final Map<Particle, Velocity> previousParticlesVelocity;
    private final double deltaTime;
    private final double absoluteTime;
    private final int step;
    private final IntegrationAlgorithm integrationAlgorithm;

    public Step(Map<Integer, Particle> particleMap, Map<Particle, Position> previousParticlesPosition, Map<Particle, Velocity> previousParticlesVelocity, double deltaTime, double absoluteTime, int step, IntegrationAlgorithm integrationAlgorithm) {
        this.particleMap = particleMap;
        this.previousParticlesPosition = previousParticlesPosition;
        this.previousParticlesVelocity = previousParticlesVelocity;

        this.deltaTime = deltaTime;
        this.absoluteTime = absoluteTime;
        this.step = step;
        this.integrationAlgorithm = integrationAlgorithm;
    }

    public double getRelativeTime() {
        return this.deltaTime;
    }

    public double getAbsoluteTime() {
        return this.absoluteTime;
    }

    public int getStep() {
        return this.step;
    }

    public IntegrationAlgorithm getIntegrationAlgorithm() {
        return this.integrationAlgorithm;
    }

    public Position getPreviousPosition(Particle particle) {
        return this.previousParticlesPosition.get(particle);
    }

    public Velocity getPreviousVelocity(Particle particle) {
        return this.previousParticlesVelocity.get(particle);
    }
}

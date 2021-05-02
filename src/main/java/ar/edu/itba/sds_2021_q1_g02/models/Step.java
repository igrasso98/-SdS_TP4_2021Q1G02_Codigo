package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;
import java.util.Map;

public class Step {
    private final Map<Integer, Particle> particleMap;
    private final Map<Particle, Position> previousParticlesPosition;
    private final Map<Particle, Velocity> previousParticlesVelocity;
    private final BigDecimal deltaTime;
    private final BigDecimal absoluteTime;
    private final int step;

    public Step(Map<Integer, Particle> particleMap, Map<Particle, Position> previousParticlesPosition, Map<Particle, Velocity> previousParticlesVelocity, BigDecimal deltaTime, BigDecimal absoluteTime, int step) {
        this.particleMap = particleMap;
        this.previousParticlesPosition = previousParticlesPosition;
        this.previousParticlesVelocity = previousParticlesVelocity;

        this.deltaTime = deltaTime;
        this.absoluteTime = absoluteTime;
        this.step = step;
    }

    public BigDecimal getRelativeTime() {
        return this.deltaTime;
    }

    public BigDecimal getAbsoluteTime() {
        return this.absoluteTime;
    }

    public int getStep() {
        return this.step;
    }

    public Position getPreviousPosition(Particle particle) {
        return this.previousParticlesPosition.get(particle);
    }

    public Velocity getPreviousVelocity(Particle particle) {
        return this.previousParticlesVelocity.get(particle);
    }
}

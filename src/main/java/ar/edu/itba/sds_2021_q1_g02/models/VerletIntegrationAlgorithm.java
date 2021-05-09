package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

public class VerletIntegrationAlgorithm implements IntegrationAlgorithm {
    private final ForceCalculator forceCalculator;
    private final IntegrationAlgorithm euler;

    public VerletIntegrationAlgorithm(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
        this.euler = new EulerIntegrationAlgorithm(forceCalculator);
    }

    @Override
    public Pair<Position, Velocity> perform(Particle particle, Step step) {
        Particle previousParticleState = particle.copy();

        if (!step.containsPreviousPosition(particle)) {
            Step previousStep = step.copy();
            previousStep.setAbsoluteTime(previousStep.getAbsoluteTime().subtract(previousStep.getRelativeTime()));
            previousStep.setRelativeTime(previousStep.getRelativeTime().negate());
            Pair<Position, Velocity> previousParticlePositionAndVelocity = this.euler.perform(previousParticleState,
                    previousStep);
            previousParticleState.setPosition(previousParticlePositionAndVelocity.getKey());
        } else {
            previousParticleState.setPosition(step.getPreviousPosition(particle));
        }

        Position nextPosition = this.calculatePositions(step, particle, previousParticleState);
        Velocity nextVelocity = this.calculateVelocities(step, previousParticleState, nextPosition);

        return new Pair<>(nextPosition, nextVelocity);
    }

    @Override
    public String getName() {
        return "Verlet";
    }

    private Position calculatePositions(Step step, Particle particle, Particle previousParticleState) {
        Pair<Double, Double> force = this.forceCalculator.calculatePair(particle);

        double positionX = this.calculatePosition(step.getRelativeTime().doubleValue(), particle.getPosition().getX(),
                previousParticleState.getPosition().getX(), force.getKey(),
                particle.getMass());

        double positionY = this.calculatePosition(step.getRelativeTime().doubleValue(), particle.getPosition().getY(),
                previousParticleState.getPosition().getY(), force.getValue(),
                particle.getMass());

        return new Position(positionX, positionY);
    }

    private Velocity calculateVelocities(Step step, Particle previousParticleState, Position nextPosition) {
        double velocityX = this.calculateVelocity(step.getRelativeTime().doubleValue(),
                previousParticleState.getPosition().getX(), nextPosition.getX());
        double velocityY = this.calculateVelocity(step.getRelativeTime().doubleValue(),
                previousParticleState.getPosition().getY(), nextPosition.getY());

        return new Velocity(velocityX, velocityY);
    }


    private double calculatePosition(double dt, double position, double previousPosition,
                                     double force, double mass) {
        return (2.0 * position) - previousPosition + (Math.pow(dt, 2.0) * (force / mass));
    }

    public double calculateVelocity(double dt, double previousPosition, double nextPosition) {
        return (nextPosition - previousPosition) / (dt * 2);
    }
}

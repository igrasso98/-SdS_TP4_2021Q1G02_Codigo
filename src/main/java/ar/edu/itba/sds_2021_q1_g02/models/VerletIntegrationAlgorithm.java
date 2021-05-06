package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

public class VerletIntegrationAlgorithm implements IntegrationAlgorithm {
    private final ForceCalculator forceCalculator;

    public VerletIntegrationAlgorithm(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
    }

    @Override
    public Pair<Position, Velocity> perform(Particle particle, Step step) {
        Position nextPosition = this.calculatePositions(step, particle);
        Velocity nextVelocity = this.calculateVelocities(step, particle, nextPosition);

        return new Pair<>(nextPosition, nextVelocity);
    }

    @Override
    public String getName() {
        return "Verlet";
    }

    private Position calculatePositions(Step step, Particle particle) {
        double positionX = this.calculatePosition(step.getRelativeTime(), particle.getPosition().getX(),
                step.getPreviousPosition(particle).getX(), this.forceCalculator.calculateX(particle),
                particle.getMass());

        double positionY = this.calculatePosition(step.getRelativeTime(), particle.getPosition().getY(),
                step.getPreviousPosition(particle).getY(), this.forceCalculator.calculateY(particle),
                particle.getMass());

        return new Position(positionX, positionY);
    }

    private Velocity calculateVelocities(Step step, Particle currentParticleState, Position nextPosition) {
        double velocityX = this.calculateVelocity(step.getRelativeTime(),
                step.getPreviousPosition(currentParticleState).getX(), nextPosition.getX());
        double velocityY = this.calculateVelocity(step.getRelativeTime(),
                step.getPreviousPosition(currentParticleState).getY(), nextPosition.getY());

        return new Velocity(velocityX, velocityY);
    }


    private double calculatePosition(double dt, double position, double previousPosition,
                                     double force, double mass)
    {
        return position * 2
                - previousPosition
                + (Math.pow(dt, 2) / mass) * force;
    }

    public double calculateVelocity(double dt, double previousPosition, double nextPosition) {
        return (nextPosition - previousPosition) / (dt * 2);
    }
}

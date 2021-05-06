package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;


public class EulerIntegrationAlgorithm implements IntegrationAlgorithm {
    private final ForceCalculator forceCalculator;

    public EulerIntegrationAlgorithm(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
    }

    @Override
    public Pair<Position, Velocity> perform(Particle particle, Step step) {
        Position newPosition = new Position(
                this.calculatePosition(particle, particle.getVelocity().getxSpeed(), particle.getPosition().getX(), step.getRelativeTime(), this.forceCalculator.calculateX(particle)),
                this.calculatePosition(particle, particle.getVelocity().getySpeed(), particle.getPosition().getY(), step.getRelativeTime(), this.forceCalculator.calculateY(particle))
        );
        Velocity newVelocity = new Velocity(
                this.calculateVelocity(particle, particle.getVelocity().getxSpeed(), step.getRelativeTime(), this.forceCalculator.calculateX(particle)),
                this.calculateVelocity(particle, particle.getVelocity().getySpeed(), step.getRelativeTime(), this.forceCalculator.calculateY(particle))
        );

        return new Pair<>(newPosition, newVelocity);
    }

    @Override
    public String getName() {
        return "Euler";
    }

    private double calculatePosition(Particle particle, double velocity, double position, double dt, double force) {
        return position
                + dt * velocity
                + (Math.pow(dt, 2) / (particle.getMass() * 2)) * force;
    }

    private double calculateVelocity(Particle particle, double velocity, double dt, double force) {
        return velocity + (dt / particle.getMass()) * force;
    }
}

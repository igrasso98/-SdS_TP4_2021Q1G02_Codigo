package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

import java.math.BigDecimal;

public class EulerVelocityIntegrationAlgorithm implements IntegrationAlgorithm {
    private final ForceCalculator forceCalculator;

    public EulerVelocityIntegrationAlgorithm(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
    }

    @Override
    public Pair<Position, Velocity> perform(Particle particle, Step step) {
        Velocity newVelocity = new Velocity(
                this.calculateVelocity(particle, particle.getVelocity().getxSpeed(), step.getRelativeTime(), this.forceCalculator.calculateX(particle)),
                this.calculateVelocity(particle, particle.getVelocity().getySpeed(), step.getRelativeTime(), this.forceCalculator.calculateY(particle))
        );
        Position newPosition = new Position(
                this.calculatePosition(particle, newVelocity.getxSpeed(), particle.getPosition().getX(), step.getRelativeTime(), this.forceCalculator.calculateX(particle)),
                this.calculatePosition(particle, newVelocity.getySpeed(), particle.getPosition().getY(), step.getRelativeTime(), this.forceCalculator.calculateY(particle))
        );

        return new Pair<>(newPosition, newVelocity);
    }

    private BigDecimal calculatePosition(Particle particle, BigDecimal velocity, BigDecimal position, BigDecimal step, BigDecimal force) {
        return position
                .add(step.multiply(velocity))
                .add(BigDecimalDivision.divide(step.pow(2), particle.getMass().multiply(BigDecimal.valueOf(2))).multiply(force));
    }

    private BigDecimal calculateVelocity(Particle particle, BigDecimal velocity, BigDecimal step, BigDecimal force) {
        return velocity
                .add(BigDecimalDivision.divide(step, particle.getMass()).multiply(force));
    }
}

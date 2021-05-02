package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;

public class EulerIntegrationAlgorithm implements IntegrationAlgorithm {
    private final ForceCalculator forceCalculator;

    public EulerIntegrationAlgorithm(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
    }

    @Override
    public Position calculatePosition(Particle particle, BigDecimal time, BigDecimal step) {
        return new Position(
                this.calculatePosition(particle, particle.getVelocity().getxSpeed(), particle.getPosition().getX(), step, this.forceCalculator.calculateX(particle)),
                this.calculatePosition(particle, particle.getVelocity().getySpeed(), particle.getPosition().getY(), step, this.forceCalculator.calculateY(particle))
        );
    }

    @Override
    public Velocity calculateVelocity(Particle particle, BigDecimal time, BigDecimal step) {
        return new Velocity(
                this.calculateVelocity(particle, particle.getVelocity().getxSpeed(), step, this.forceCalculator.calculateX(particle)),
                this.calculateVelocity(particle, particle.getVelocity().getySpeed(), step, this.forceCalculator.calculateY(particle))
        );
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

package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;

public class VerletIntegrationAlgorithm implements IntegrationAlgorithm {
    private final ForceCalculator forceCalculator;

    public VerletIntegrationAlgorithm(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
    }

    @Override
    public Position calculatePosition(Particle particle, Step step) {
        return new Position(
                this.calculatePosition(
                        particle,
                        particle.getPosition().getX(),
                        step.getRelativeTime(),
                        step.getPreviousPosition(particle).getX(),
                        this.forceCalculator.calculateX(particle)
                ),
                this.calculatePosition(
                        particle,
                        particle.getPosition().getY(),
                        step.getRelativeTime(),
                        step.getPreviousPosition(particle).getY(),
                        this.forceCalculator.calculateY(particle)
                )
        );
    }

    @Override
    public Velocity calculateVelocity(Particle particle, Step step) {
        Position nextPosition = this.calculatePosition(particle, step);

        return new Velocity(
                this.calculateVelocity(
                        step.getPreviousPosition(particle).getX(),
                        nextPosition.getX(),
                        step.getRelativeTime(),
                        this.forceCalculator.calculateX(particle)
                ),
                this.calculateVelocity(
                        step.getPreviousPosition(particle).getY(),
                        nextPosition.getY(),
                        step.getRelativeTime(),
                        this.forceCalculator.calculateY(particle)
                )
        );
    }

    private BigDecimal calculatePosition(Particle particle, BigDecimal position, BigDecimal step, BigDecimal previousPosition, BigDecimal force) {
        return position
                .multiply(BigDecimal.valueOf(2))
                .subtract(previousPosition)
                .add(BigDecimalDivision.divide(step.pow(2), particle.getMass()).multiply(force));
    }

    private BigDecimal calculateVelocity(BigDecimal previousPosition, BigDecimal nextPosition, BigDecimal step, BigDecimal force) {
        return BigDecimalDivision.divide(
                nextPosition.subtract(previousPosition),
                step.multiply(BigDecimal.valueOf(2))
        );
    }
}

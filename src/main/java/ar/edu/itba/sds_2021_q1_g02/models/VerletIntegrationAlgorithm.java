package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

import java.math.BigDecimal;

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

    private Position calculatePositions(Step step, Particle particle) {
        BigDecimal positionX = this.calculatePosition(step.getRelativeTime(), particle.getPosition().getX(),
                step.getPreviousPosition(particle).getX(), this.forceCalculator.calculateX(particle),
                particle.getMass());

        BigDecimal positionY = this.calculatePosition(step.getRelativeTime(), particle.getPosition().getY(),
                step.getPreviousPosition(particle).getY(), this.forceCalculator.calculateY(particle),
                particle.getMass());

        return new Position(positionX, positionY);
    }

    private Velocity calculateVelocities(Step step, Particle currentParticleState, Position nextPosition) {
        BigDecimal velocityX = this.calculateVelocity(step.getRelativeTime(),
                step.getPreviousPosition(currentParticleState).getX(), nextPosition.getX());
        BigDecimal velocityY = this.calculateVelocity(step.getRelativeTime(),
                step.getPreviousPosition(currentParticleState).getY(), nextPosition.getY());

        return new Velocity(velocityX, velocityY);
    }


    private BigDecimal calculatePosition(BigDecimal step, BigDecimal position, BigDecimal previousPosition,
                                         BigDecimal force, BigDecimal mass) {
        return position
                .multiply(BigDecimal.valueOf(2))
                .subtract(previousPosition)
                .add(BigDecimalDivision.divide(step.pow(2), mass).multiply(force));
    }

    public BigDecimal calculateVelocity(BigDecimal step, BigDecimal previousPosition, BigDecimal nextPosition) {
        return BigDecimalDivision.divide(
                nextPosition.subtract(previousPosition),
                step.multiply(BigDecimal.valueOf(2)));
    }
}

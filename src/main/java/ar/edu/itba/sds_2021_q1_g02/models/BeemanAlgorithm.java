package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.geometry.Pos;
import javafx.util.Pair;

import java.math.BigDecimal;

public class BeemanAlgorithm implements IntegrationAlgorithm {
    private final ForceCalculator forceCalculator;

    public BeemanAlgorithm(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
    }

    @Override
    public Pair<Position, Velocity> perform(Particle particle, Step step) {
        Particle previousParticleState = particle.copy();
        previousParticleState.setPosition(step.getPreviousPosition(particle));
        previousParticleState.setVelocity(step.getPreviousVelocity(particle));

        Position newPosition = this.calculatePositions(step.getRelativeTime(), particle, previousParticleState);

        Particle nextParticleState = particle.copy();
        nextParticleState.setPosition(newPosition);

        if (this.forceCalculator.isVelocityDependant()) {
            Velocity predictedVelocity = this.calculatePredictedVelocities(step.getRelativeTime(), particle,
                    previousParticleState);
            nextParticleState.setVelocity(predictedVelocity);
        }

        Velocity correctedVelocity = this.calculateCorrectedVelocities(step.getRelativeTime(), particle,
                previousParticleState, nextParticleState);

        return new Pair<>(newPosition, correctedVelocity);
    }

    private Position calculatePositions(BigDecimal step, Particle currentParticleState,
                                        Particle previousParticleState) {

        BigDecimal positionX = this.calculatePosition(step, currentParticleState.getPosition().getX(),
                currentParticleState.getVelocity().getxSpeed(), this.forceCalculator.calculateX(currentParticleState)
                , this.forceCalculator.calculateX(previousParticleState), currentParticleState.getMass());

        BigDecimal positionY = this.calculatePosition(step, currentParticleState.getPosition().getY(),
                currentParticleState.getVelocity().getySpeed(), this.forceCalculator.calculateY(currentParticleState)
                , this.forceCalculator.calculateY(previousParticleState), currentParticleState.getMass());

        return new Position(positionX, positionY);
    }

    private Velocity calculateCorrectedVelocities(BigDecimal step, Particle currentParticleState,
                                                  Particle previousParticleState, Particle nextParticleState) {
        BigDecimal velocityX = this.calculateCorrectedVelocity(step, currentParticleState.getVelocity().getxSpeed(),
                this.forceCalculator.calculateX(currentParticleState),
                this.forceCalculator.calculateX(previousParticleState),
                this.forceCalculator.calculateX(nextParticleState),
                currentParticleState.getMass());

        BigDecimal velocityY = this.calculateCorrectedVelocity(step, currentParticleState.getVelocity().getySpeed(),
                this.forceCalculator.calculateY(currentParticleState),
                this.forceCalculator.calculateY(previousParticleState),
                this.forceCalculator.calculateY(nextParticleState),
                currentParticleState.getMass());

        return new Velocity(velocityX, velocityY);
    }

    private Velocity calculatePredictedVelocities(BigDecimal step, Particle currentParticleState,
                                                  Particle previousParticleState) {
        BigDecimal velocityX = this.calculatePredictedVelocity(step, currentParticleState.getVelocity().getxSpeed(),
                this.forceCalculator.calculateX(currentParticleState),
                this.forceCalculator.calculateX(previousParticleState), currentParticleState.getMass());

        BigDecimal velocityY = this.calculatePredictedVelocity(step, currentParticleState.getVelocity().getySpeed(),
                this.forceCalculator.calculateY(currentParticleState),
                this.forceCalculator.calculateY(previousParticleState), currentParticleState.getMass());

        return new Velocity(velocityX, velocityY);
    }

    private BigDecimal calculatePosition(BigDecimal step, BigDecimal position, BigDecimal velocity, BigDecimal force,
                                         BigDecimal previousForce, BigDecimal mass) {
        return position
                .add(step.multiply(velocity))
                .add((step.pow(2)).multiply(BigDecimal.valueOf(2 / 3)).multiply(BigDecimalDivision.divide(force, mass)))
                .subtract(BigDecimal.valueOf(1 / 6).multiply(previousForce).multiply(step.pow(2)));
    }

    private BigDecimal calculatePredictedVelocity(BigDecimal step, BigDecimal velocity, BigDecimal force,
                                                  BigDecimal previousForce, BigDecimal mass) {
        return velocity
                .add(BigDecimal.valueOf(3 / 2).multiply(BigDecimalDivision.divide(force, mass)).multiply(step))
                .subtract(BigDecimal.valueOf(1 / 2).multiply(BigDecimalDivision.divide(previousForce, mass)).multiply(step));
    }

    private BigDecimal calculateCorrectedVelocity(BigDecimal step, BigDecimal velocity, BigDecimal force,
                                                  BigDecimal previousForce, BigDecimal nextForce, BigDecimal mass) {
        return velocity
                .add(BigDecimal.valueOf(1 / 3).multiply(BigDecimalDivision.divide(nextForce, mass)).multiply(step))
                .add(BigDecimal.valueOf(5 / 6).multiply(BigDecimalDivision.divide(force, mass)).multiply(step))
                .subtract(BigDecimal.valueOf(1 / 6).multiply(previousForce).multiply(step));


    }
}

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

    @Override
    public String getName() {
        return "Beeman";
    }

    private Position calculatePositions(double step, Particle currentParticleState,
                                        Particle previousParticleState) {
        double positionX = this.calculatePosition(step, currentParticleState.getPosition().getX(),
                currentParticleState.getVelocity().getxSpeed(), this.forceCalculator.calculateX(currentParticleState)
                , this.forceCalculator.calculateX(previousParticleState), currentParticleState.getMass());

        double positionY = this.calculatePosition(step, currentParticleState.getPosition().getY(),
                currentParticleState.getVelocity().getySpeed(), this.forceCalculator.calculateY(currentParticleState)
                , this.forceCalculator.calculateY(previousParticleState), currentParticleState.getMass());

        return new Position(positionX, positionY);
    }

    private Velocity calculateCorrectedVelocities(double step, Particle currentParticleState,
                                                  Particle previousParticleState, Particle nextParticleState) {
        double velocityX = this.calculateCorrectedVelocity(step, currentParticleState.getVelocity().getxSpeed(),
                this.forceCalculator.calculateX(currentParticleState),
                this.forceCalculator.calculateX(previousParticleState),
                this.forceCalculator.calculateX(nextParticleState),
                currentParticleState.getMass());

        double velocityY = this.calculateCorrectedVelocity(step, currentParticleState.getVelocity().getySpeed(),
                this.forceCalculator.calculateY(currentParticleState),
                this.forceCalculator.calculateY(previousParticleState),
                this.forceCalculator.calculateY(nextParticleState),
                currentParticleState.getMass());

        return new Velocity(velocityX, velocityY);
    }

    private Velocity calculatePredictedVelocities(double step, Particle currentParticleState,
                                                  Particle previousParticleState) {
        double velocityX = this.calculatePredictedVelocity(step, currentParticleState.getVelocity().getxSpeed(),
                this.forceCalculator.calculateX(currentParticleState),
                this.forceCalculator.calculateX(previousParticleState), currentParticleState.getMass());

        double velocityY = this.calculatePredictedVelocity(step, currentParticleState.getVelocity().getySpeed(),
                this.forceCalculator.calculateY(currentParticleState),
                this.forceCalculator.calculateY(previousParticleState), currentParticleState.getMass());

        return new Velocity(velocityX, velocityY);
    }

    private double calculatePosition(double step, double position, double velocity, double force,
                                         double previousForce, double mass) {
        return position
                + step * velocity
                + Math.pow(step, 2) * (2.0 / 3) * (force / mass)
                - (1.0 / 6) * previousForce * Math.pow(step, 2);
    }

    private double calculatePredictedVelocity(double step, double velocity, double force,
                                              double previousForce, double mass) {
        return velocity
                + (3.0 / 2) * (force / mass) * step
                - (1.0 / 2) * (previousForce / mass) * step;
    }

    private double calculateCorrectedVelocity(double step, double velocity, double force,
                                              double previousForce, double nextForce, double mass) {
        return velocity
                + (1.0 / 3) * (nextForce / mass) * step
                + (5.0 / 6) * (force / mass) * step
                - (1.0 / 6) * previousForce * step;
    }
}

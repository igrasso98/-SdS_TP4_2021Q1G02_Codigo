package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

public class BeemanAlgorithm implements IntegrationAlgorithm {
    private final ForceCalculator forceCalculator;
    private final IntegrationAlgorithm euler;

    public BeemanAlgorithm(ForceCalculator forceCalculator) {
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
            previousParticleState.setVelocity(previousParticlePositionAndVelocity.getValue());
        } else {
            previousParticleState.setPosition(step.getPreviousPosition(particle));
            previousParticleState.setVelocity(step.getPreviousVelocity(particle));
        }

        Position newPosition = this.calculatePositions(step.getRelativeTime().doubleValue(), particle, previousParticleState);

        Particle nextParticleState = particle.copy();
        nextParticleState.setPosition(newPosition);

        if (this.forceCalculator.isVelocityDependant()) {
            Velocity predictedVelocity = this.calculatePredictedVelocities(step.getRelativeTime().doubleValue(), particle,
                    previousParticleState);
            nextParticleState.setVelocity(predictedVelocity);
        }

        Velocity correctedVelocity = this.calculateCorrectedVelocities(step.getRelativeTime().doubleValue(), particle,
                previousParticleState, nextParticleState);

        return new Pair<>(newPosition, correctedVelocity);
    }

    @Override
    public String getName() {
        return "Beeman";
    }

    private Position calculatePositions(double dt, Particle currentParticleState,
                                        Particle previousParticleState) {
        Pair<Double, Double> previousForce = this.forceCalculator.calculatePair(previousParticleState);
        Pair<Double, Double> currentForce = this.forceCalculator.calculatePair(currentParticleState);

        double positionX = this.calculatePosition(dt, currentParticleState.getPosition().getX(),
                currentParticleState.getVelocity().getxSpeed(), currentForce.getKey(),
                previousForce.getKey(), currentParticleState.getMass());

        double positionY = this.calculatePosition(dt, currentParticleState.getPosition().getY(),
                currentParticleState.getVelocity().getySpeed(), currentForce.getValue(),
                previousForce.getValue(), currentParticleState.getMass());

        return new Position(positionX, positionY);
    }

    private Velocity calculateCorrectedVelocities(double dt, Particle currentParticleState,
                                                  Particle previousParticleState, Particle nextParticleState) {
        Pair<Double, Double> previousForce = this.forceCalculator.calculatePair(previousParticleState);
        Pair<Double, Double> currentForce = this.forceCalculator.calculatePair(currentParticleState);
        Pair<Double, Double> nextForce = this.forceCalculator.calculatePair(nextParticleState);

        double velocityX = this.calculateCorrectedVelocity(dt, currentParticleState.getVelocity().getxSpeed(),
                currentForce.getKey(),
                previousForce.getKey(),
                nextForce.getKey(),
                currentParticleState.getMass());

        double velocityY = this.calculateCorrectedVelocity(dt, currentParticleState.getVelocity().getySpeed(),
                currentForce.getValue(),
                previousForce.getValue(),
                nextForce.getValue(),
                currentParticleState.getMass());

        return new Velocity(velocityX, velocityY);
    }

    private Velocity calculatePredictedVelocities(double dt, Particle currentParticleState,
                                                  Particle previousParticleState) {
        Pair<Double, Double> previousForce = this.forceCalculator.calculatePair(previousParticleState);
        Pair<Double, Double> currentForce = this.forceCalculator.calculatePair(currentParticleState);

        double velocityX = this.calculatePredictedVelocity(
                dt,
                currentParticleState.getVelocity().getxSpeed(),
                currentForce.getKey(),
                previousForce.getKey(),
                currentParticleState.getMass()
        );

        double velocityY = this.calculatePredictedVelocity(
                dt,
                currentParticleState.getVelocity().getySpeed(),
                currentForce.getValue(),
                previousForce.getValue(),
                currentParticleState.getMass()
        );

        return new Velocity(velocityX, velocityY);
    }

    private double calculatePosition(double dt, double position, double velocity, double force,
                                     double previousForce, double mass) {
        return position
                + dt * velocity
                + Math.pow(dt, 2) * (2.0 / 3) * (force / mass)
                - (1.0 / 6) * (previousForce / mass) * Math.pow(dt, 2);
    }

    private double calculatePredictedVelocity(double st, double velocity, double force,
                                              double previousForce, double mass) {
        return velocity
                + (3.0 / 2) * (force / mass) * st
                - (1.0 / 2) * (previousForce / mass) * st;
    }

    private double calculateCorrectedVelocity(double dt, double velocity, double force,
                                              double previousForce, double nextForce, double mass) {
        return velocity
                + (1.0 / 3) * (nextForce / mass) * dt
                + (5.0 / 6) * (force / mass) * dt
                - (1.0 / 6) * (previousForce / mass) * dt;
    }
}

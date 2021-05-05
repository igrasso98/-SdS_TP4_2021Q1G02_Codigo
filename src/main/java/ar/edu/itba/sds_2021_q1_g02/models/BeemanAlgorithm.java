package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

import java.math.BigDecimal;

public class BeemanAlgorithm implements IntegrationAlgorithm {
    private final IntegrationAlgorithm eulerIntegration;
    private final ForceCalculator forceCalculator;
    private Acceleration lastAcceleration;
    private Position lastPosition;
    private Velocity lastVelocity;
    public BeemanAlgorithm(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
        this.lastAcceleration = new Acceleration(null, null);
        this.lastPosition = new Position(null, null);
        this.lastVelocity = new Velocity(null, null);
        this.eulerIntegration = new EulerIntegrationAlgorithm(forceCalculator);
    }

    @Override
    public Pair<Position, Velocity> perform(Particle particle, Step step) {

        return null;
    }




    public Position calculatePosition(Particle particle, Step step) {
        if(this.lastAcceleration.getxAcceleration() == null){
            calculateLastAcceleration(particle, step);
        }
        return new Position(
                this.calculatePosition(
                        particle,
                        particle.getVelocity().getxSpeed(),
                        particle.getPosition().getX(),
                        step.getRelativeTime(),
                        this.forceCalculator.calculateX(particle),
                        this.lastAcceleration.getxAcceleration()),
                this.calculatePosition(
                        particle,
                        particle.getVelocity().getySpeed(),
                        particle.getPosition().getY(),
                        step.getRelativeTime(),
                        this.forceCalculator.calculateY(particle),
                        this.lastAcceleration.getyAcceleration()));
    }

    @Override
    public Velocity calculateVelocity(Particle particle, Step step) {
        return new Velocity(
                this.calculateVelocity(
                        particle,
                        particle.getVelocity().getxSpeed(),
                        step.getRelativeTime(),
                        this.forceCalculator.calculateX(particle),
                        this.lastAcceleration.getxAcceleration()),
                this.calculateVelocity(
                        particle,
                        particle.getVelocity().getySpeed(),
                        step.getRelativeTime(),
                        this.forceCalculator.calculateY(particle),
                        this.lastAcceleration.getyAcceleration())
        );
    }

    private BigDecimal calculatePosition(Particle particle, BigDecimal velocity, BigDecimal position, BigDecimal step, BigDecimal force, BigDecimal lastAcceleration) {
        return position
                .add(step.multiply(velocity))
                .add((step.pow(2)).multiply(BigDecimal.valueOf(2/3)).multiply(BigDecimalDivision.divide(force, particle.getMass())))
                .subtract(BigDecimal.valueOf(1/6).multiply(lastAcceleration).multiply(step.pow(2)));
    }

//    private BigDecimal calculateVelocity(Particle particle, BigDecimal velocity, BigDecimal step, BigDecimal force, BigDecimal lastAcceleration) {
//        BigDecimal predictedVel = calculatePredictedVelocity(particle, velocity, step, force, lastAcceleration);
////        Medio perdida aca
//        return velocity
//                .add(BigDecimal.valueOf(1/3));
//    }


    private BigDecimal calculateCorrectedVelocity() {

    }

    private BigDecimal calculatePredictedVelocity(Particle particle, BigDecimal velocity, BigDecimal step, BigDecimal force, BigDecimal lastAcceleration) {
        return velocity
                .add(BigDecimal.valueOf(2/3).multiply(BigDecimalDivision.divide(force, particle.getMass())).multiply(step)).
                subtract(BigDecimal.valueOf(1/2).multiply(lastAcceleration).multiply(step));
    }
}

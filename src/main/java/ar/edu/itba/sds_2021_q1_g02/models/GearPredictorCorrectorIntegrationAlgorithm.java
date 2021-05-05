package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

public class GearPredictorCorrectorIntegrationAlgorithm implements IntegrationAlgorithm {
    static private final int ZERO_DERIVATIVE = 0;
    static private final int FIRST_DERIVATIVE = 1;
    static private final int SECOND_DERIVATIVE = 2;
    static private final int THIRD_DERIVATIVE = 3;
    static private final int FOURTH_DERIVATIVE = 4;
    static private final int[] factorials = {1, 1, 2, 6, 24, 120};
    static private double[] alphas = {3.0 / 20.0, 251.0 / 360.0, 1.0, 11.0 / 18.0, 1.0 / 6.0, 1.0 / 60.0};
    private final ForceCalculator forceCalculator;

    public GearPredictorCorrectorIntegrationAlgorithm(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
    }

    @Override
    public Pair<Position, Velocity> perform(Particle particle, Step step) {
        List<Pair<BigDecimal, BigDecimal>> corrections = this.predictAndCorrect(particle, step);
        Position newPosition = new Position(corrections.get(0).getKey(), corrections.get(0).getValue());
        Velocity newVelocity = new Velocity(corrections.get(1).getKey(), corrections.get(1).getValue());
        return new Pair<>(newPosition, newVelocity);
    }

    private List<Pair<BigDecimal, BigDecimal>> predictAndCorrect(Particle particle, Step step) {
        List<Pair<BigDecimal, BigDecimal>> derivatives = this.calculateDerivatives(particle);
        List<Pair<BigDecimal, BigDecimal>> predictions = this.predict(derivatives, step.getRelativeTime());

        BigDecimal accelerationX =
                derivatives.get(2).getKey().subtract(predictions.get(2).getKey()).multiply(BigDecimalDivision.divide(step.getRelativeTime().pow(2), BigDecimal.valueOf(2)));
        BigDecimal accelerationY =
                derivatives.get(2).getValue().subtract(predictions.get(2).getValue()).multiply(BigDecimalDivision.divide(step.getRelativeTime().pow(2), BigDecimal.valueOf(2)));

        return this.correct(predictions, accelerationX, accelerationY, step.getRelativeTime());
    }

    private List<Pair<BigDecimal, BigDecimal>> calculateDerivatives(Particle particle) {
        List<Pair<BigDecimal, BigDecimal>> derivatives = new ArrayList<>();
        derivatives.add(new Pair<>(particle.getPosition().getX(), particle.getPosition().getY()));
        derivatives.add(new Pair<>(particle.getVelocity().getxSpeed(), particle.getVelocity().getySpeed()));
        derivatives.add(new Pair<>(
                this.forceCalculator.calculate(derivatives.get(ZERO_DERIVATIVE).getKey(),
                        derivatives.get(FIRST_DERIVATIVE).getKey()),
                this.forceCalculator.calculate(derivatives.get(ZERO_DERIVATIVE).getValue(),
                        derivatives.get(FIRST_DERIVATIVE).getValue())));
        derivatives.add(new Pair<>(
                this.forceCalculator.calculate(derivatives.get(FIRST_DERIVATIVE).getKey(),
                        derivatives.get(SECOND_DERIVATIVE).getKey()),
                this.forceCalculator.calculate(derivatives.get(FIRST_DERIVATIVE).getValue(),
                        derivatives.get(SECOND_DERIVATIVE).getValue())));
        derivatives.add(new Pair<>(
                this.forceCalculator.calculate(derivatives.get(SECOND_DERIVATIVE).getKey(),
                        derivatives.get(THIRD_DERIVATIVE).getKey()),
                this.forceCalculator.calculate(derivatives.get(SECOND_DERIVATIVE).getValue(),
                        derivatives.get(THIRD_DERIVATIVE).getValue())));
        derivatives.add(new Pair<>(
                this.forceCalculator.calculate(derivatives.get(THIRD_DERIVATIVE).getKey(),
                        derivatives.get(FOURTH_DERIVATIVE).getKey()),
                this.forceCalculator.calculate(derivatives.get(THIRD_DERIVATIVE).getValue(),
                        derivatives.get(FOURTH_DERIVATIVE).getValue())));

        return derivatives;
    }

    private List<Pair<BigDecimal, BigDecimal>> correct(List<Pair<BigDecimal, BigDecimal>> predictions,
                                                       BigDecimal accelerationX, BigDecimal accelerationY,
                                                       BigDecimal step) {
        if (this.forceCalculator.isVelocityDependant()) {
            alphas[0] = 3.0 / 16.0;
        }
        List<Pair<BigDecimal, BigDecimal>> corrections = new ArrayList<>();
        for (int i = 0; i < predictions.size(); i++) {
            corrections.add(new Pair<>(
                    predictions.get(i).getKey().add(BigDecimal.valueOf(alphas[i]).multiply(accelerationX).multiply(BigDecimalDivision.divide(BigDecimal.valueOf(factorials[i]), step.pow(i)))),
                    predictions.get(i).getValue().add(BigDecimal.valueOf(alphas[i]).multiply(accelerationY).multiply(BigDecimalDivision.divide(BigDecimal.valueOf(factorials[i]), step.pow(i))))
            ));
        }
        return corrections;
    }

    private List<Pair<BigDecimal, BigDecimal>> predict(List<Pair<BigDecimal, BigDecimal>> derivatives,
                                                       BigDecimal step) {
        List<Pair<BigDecimal, BigDecimal>> predictions = new ArrayList<>();
        for (int i = 0; i < derivatives.size(); i++) {
            predictions.add(this.calculatePredictedDerivative(derivatives.subList(i, derivatives.size()), step));
        }
        return predictions;
    }

    private Pair<BigDecimal, BigDecimal> calculatePredictedDerivative(List<Pair<BigDecimal, BigDecimal>> derivatives,
                                                                      BigDecimal step) {
        BigDecimal predictedDerivativeX = new BigDecimal(0);
        BigDecimal predictedDerivativeY = new BigDecimal(0);
        int idx = 0;
        while (idx < derivatives.size()) {
            predictedDerivativeX =
                    predictedDerivativeX.add(derivatives.get(idx).getKey().multiply(BigDecimalDivision.divide(step.pow(idx),
                            BigDecimal.valueOf(factorials[idx]))));
            predictedDerivativeY =
                    predictedDerivativeY.add(derivatives.get(idx).getValue().multiply(BigDecimalDivision.divide(step.pow(idx),
                            BigDecimal.valueOf(factorials[idx]))));
            idx++;
        }
        return new Pair<>(predictedDerivativeX, predictedDerivativeY);
    }
}

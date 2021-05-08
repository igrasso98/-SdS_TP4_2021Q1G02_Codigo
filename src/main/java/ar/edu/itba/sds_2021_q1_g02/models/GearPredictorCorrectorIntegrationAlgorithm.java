package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class GearPredictorCorrectorIntegrationAlgorithm implements IntegrationAlgorithm {
    static private final int ZERO_DERIVATIVE = 0;
    static private final int FIRST_DERIVATIVE = 1;
    static private final int SECOND_DERIVATIVE = 2;
    static private final int THIRD_DERIVATIVE = 3;
    static private final int FOURTH_DERIVATIVE = 4;
    static private final double[] factorials = {1.0, 1.0, 2.0, 6.0, 24.0, 120.0};
    static private double[] alphas = {3.0 / 20.0, 251.0 / 360.0, 1.0, 11.0 / 18.0, 1.0 / 6.0, 1.0 / 60.0};
    private final ForceCalculator forceCalculator;

    public GearPredictorCorrectorIntegrationAlgorithm(ForceCalculator forceCalculator) {
        this.forceCalculator = forceCalculator;
    }

    @Override
    public Pair<Position, Velocity> perform(Particle particle, Step step) {
        List<Pair<Double, Double>> corrections = this.predictAndCorrect(particle, step);
        Position newPosition = new Position(corrections.get(0).getKey(), corrections.get(0).getValue());
        Velocity newVelocity = new Velocity(corrections.get(1).getKey(), corrections.get(1).getValue());
        return new Pair<>(newPosition, newVelocity);
    }

    @Override
    public String getName() {
        return "Gear Prediction Corrector orden 5";
    }

    private List<Pair<Double, Double>> predictAndCorrect(Particle particle, Step step) {

        List<Pair<Double, Double>> derivatives = this.calculateDerivatives(particle);

        List<Pair<Double, Double>> predictions = this.predict(derivatives, step.getRelativeTime());


        Pair<Double, Double> deltaR2 = this.calculateDeltaR2(predictions, particle.getMass(), step.getRelativeTime());

        List<Pair<Double, Double>> corrections = this.correct(predictions, deltaR2,
                step.getRelativeTime());

        return corrections;
    }

    private Pair<Double, Double> calculateDeltaR2(List<Pair<Double, Double>> predictions, double mass,
                                                  double step) {
        Double accelerationPX = this.forceCalculator.calculate(predictions.get(0).getKey(),
                predictions.get(1).getKey()) / mass;
        Double accelerationPY = this.forceCalculator.calculate(predictions.get(0).getValue(),
                predictions.get(1).getValue()) / mass;

        double accelerationX = (accelerationPX - predictions.get(2).getKey()) * (Math.pow(step, 2) / 2);
        double accelerationY = (accelerationPY - predictions.get(2).getValue()) * (Math.pow(step, 2) / 2);

        return new Pair<>(accelerationX, accelerationY);

    }

    private List<Pair<Double, Double>> calculateDerivatives(Particle particle) {
        List<Pair<Double, Double>> derivatives = new ArrayList<>();
        derivatives.add(new Pair<>(particle.getPosition().getX(), particle.getPosition().getY()));
        derivatives.add(new Pair<>(particle.getVelocity().getxSpeed(), particle.getVelocity().getySpeed()));
        derivatives.add(new Pair<>(
                this.forceCalculator.calculate(derivatives.get(ZERO_DERIVATIVE).getKey(),
                        derivatives.get(FIRST_DERIVATIVE).getKey()) / particle.getMass(),
                this.forceCalculator.calculate(derivatives.get(ZERO_DERIVATIVE).getValue(),
                        derivatives.get(FIRST_DERIVATIVE).getValue()) / particle.getMass()));
        derivatives.add(new Pair<>(
                this.forceCalculator.calculate(derivatives.get(FIRST_DERIVATIVE).getKey(),
                        derivatives.get(SECOND_DERIVATIVE).getKey()) / particle.getMass(),
                this.forceCalculator.calculate(derivatives.get(FIRST_DERIVATIVE).getValue(),
                        derivatives.get(SECOND_DERIVATIVE).getValue()) / particle.getMass()));
        derivatives.add(new Pair<>(
                this.forceCalculator.calculate(derivatives.get(SECOND_DERIVATIVE).getKey(),
                        derivatives.get(THIRD_DERIVATIVE).getKey()) / particle.getMass(),
                this.forceCalculator.calculate(derivatives.get(SECOND_DERIVATIVE).getValue(),
                        derivatives.get(THIRD_DERIVATIVE).getValue()) / particle.getMass()));
        derivatives.add(new Pair<>(
                this.forceCalculator.calculate(derivatives.get(THIRD_DERIVATIVE).getKey(),
                        derivatives.get(FOURTH_DERIVATIVE).getKey()) / particle.getMass(),
                this.forceCalculator.calculate(derivatives.get(THIRD_DERIVATIVE).getValue(),
                        derivatives.get(FOURTH_DERIVATIVE).getValue()) / particle.getMass()));

        return derivatives;
    }

    private List<Pair<Double, Double>> correct(List<Pair<Double, Double>> predictions, Pair<Double, Double> deltaR2,
                                               double dt) {
        if (this.forceCalculator.isVelocityDependant()) {
            alphas[0] = 3.0 / 16.0;
        }
        List<Pair<Double, Double>> corrections = new ArrayList<>();
        for (int i = 0; i < predictions.size(); i++) {
            corrections.add(new Pair<>(
                    predictions.get(i).getKey() + (alphas[i] * deltaR2.getKey() * (factorials[i] / Math.pow(dt, i))),
                    predictions.get(i).getValue() + (alphas[i] * deltaR2.getValue() * (factorials[i] / Math.pow(dt, i)))
            ));
        }
        return corrections;
    }

    private List<Pair<Double, Double>> predict(List<Pair<Double, Double>> derivatives,
                                               double dt) {
        List<Pair<Double, Double>> predictions = new ArrayList<>();
        for (int i = 0; i < derivatives.size(); i++) {
            predictions.add(this.calculatePredictedDerivative(derivatives.subList(i, derivatives.size()), dt));
        }
        return predictions;
    }

    private Pair<Double, Double> calculatePredictedDerivative(List<Pair<Double, Double>> derivatives,
                                                              double dt) {
        double predictedDerivativeX = 0;
        double predictedDerivativeY = 0;
        int idx = 0;
        while (idx < derivatives.size()) {
            predictedDerivativeX =
                    predictedDerivativeX + (derivatives.get(idx).getKey() * (Math.pow(dt, idx) / factorials[idx]));
            predictedDerivativeY =
                    predictedDerivativeY + (derivatives.get(idx).getValue() * (Math.pow(dt, idx) / factorials[idx]));
            idx++;
        }
        return new Pair<>(predictedDerivativeX, predictedDerivativeY);
    }
}

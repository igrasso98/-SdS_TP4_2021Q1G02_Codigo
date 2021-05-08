package ar.edu.itba.sds_2021_q1_g02.models;


import javafx.util.Pair;

public interface ForceCalculator {
    Pair<Double, Double> calculatePair(Particle particle);

    double calculate(double rn_2, double rn_1);

    boolean isVelocityDependant();
}

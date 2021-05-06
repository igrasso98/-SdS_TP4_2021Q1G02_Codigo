package ar.edu.itba.sds_2021_q1_g02.models;


public interface ForceCalculator {
    double calculateX(Particle particle);

    double calculateY(Particle particle);

    double calculate(double rn_2, double rn_1);

    boolean isVelocityDependant();
}

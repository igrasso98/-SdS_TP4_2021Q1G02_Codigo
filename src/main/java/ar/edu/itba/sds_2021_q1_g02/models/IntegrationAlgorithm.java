package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

public interface IntegrationAlgorithm {
    Pair<Position, Velocity> perform(Particle particle, Step step);
}

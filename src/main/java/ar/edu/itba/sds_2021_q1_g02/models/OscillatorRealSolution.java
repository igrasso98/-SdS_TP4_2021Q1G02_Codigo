package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

public class OscillatorRealSolution implements IntegrationAlgorithm {
    @Override
    public Pair<Position, Velocity> perform(Particle particle, Step step) {
        return new Pair<>(
                new Position(
                        Math.exp(-(50 / particle.getMass()) * step.getAbsoluteTime())
                                * Math.cos(Math.sqrt((1e4 / particle.getMass()) - (10000 / (4 * Math.pow(particle.getMass(), 2)))) * step.getAbsoluteTime()),
                        0
                ),
                new Velocity(
                        0,
                        0
                )
        );
    }

    @Override
    public String getName() {
        return "Solucion Analitica";
    }
}

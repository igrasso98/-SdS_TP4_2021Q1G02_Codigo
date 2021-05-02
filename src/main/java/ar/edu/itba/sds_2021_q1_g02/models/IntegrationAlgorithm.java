package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;

public interface IntegrationAlgorithm {
    Position calculatePosition(Particle particle, Step step);

    Velocity calculateVelocity(Particle particle, Step step);
}

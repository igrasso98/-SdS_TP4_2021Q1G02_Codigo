package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;

public interface IntegrationAlgorithm {
    Position calculatePosition(Particle particle, BigDecimal time, BigDecimal step);

    Velocity calculateVelocity(Particle particle, BigDecimal time, BigDecimal step);
}

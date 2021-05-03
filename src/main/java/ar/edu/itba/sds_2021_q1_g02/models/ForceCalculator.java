package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;

public interface ForceCalculator {
    BigDecimal calculateX(Particle particle);

    BigDecimal calculateY(Particle particle);

    BigDecimal calculate(BigDecimal rn_2, BigDecimal rn_1);

    boolean isVelocityDependant();
}

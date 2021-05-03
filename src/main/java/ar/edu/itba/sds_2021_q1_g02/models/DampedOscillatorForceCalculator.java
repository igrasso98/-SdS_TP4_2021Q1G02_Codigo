package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;

public class DampedOscillatorForceCalculator implements ForceCalculator {

    private final double k;
    private final double gamma;

    public DampedOscillatorForceCalculator(double k, double gamma) {
        this.k = k;
        this.gamma = gamma;
    }

    // f = m.a = m.r2 = -k.r - gamma.r1
    @Override
    public BigDecimal calculateX(Particle particle) {
        return BigDecimal.valueOf(-1).multiply(BigDecimal.valueOf(this.k).multiply(particle.getPosition().getX()).add(BigDecimal.valueOf(this.gamma).multiply(particle.getVelocity().getxSpeed())));
    }

    @Override
    public BigDecimal calculateY(Particle particle) {
        return BigDecimal.valueOf(-1).multiply(BigDecimal.valueOf(this.k).multiply(particle.getPosition().getY()).add(BigDecimal.valueOf(this.gamma).multiply(particle.getVelocity().getySpeed())));
    }

    public BigDecimal calculate(BigDecimal rn_2, BigDecimal rn_1) {
        return BigDecimal.valueOf(-1).multiply(BigDecimal.valueOf(this.k).multiply(rn_2).add(BigDecimal.valueOf(this.gamma).multiply(rn_1)));
    }

    @Override
    public boolean isVelocityDependant() {
        return true;
    }
}

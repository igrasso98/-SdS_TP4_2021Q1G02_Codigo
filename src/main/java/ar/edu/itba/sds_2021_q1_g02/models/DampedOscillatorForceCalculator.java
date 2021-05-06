package ar.edu.itba.sds_2021_q1_g02.models;


public class DampedOscillatorForceCalculator implements ForceCalculator {
    private final double k;
    private final double gamma;

    public DampedOscillatorForceCalculator(double k, double gamma) {
        this.k = k;
        this.gamma = gamma;
    }

    // f = m.a = m.r2 = -k.r - gamma.r1
    @Override
    public double calculateX(Particle particle) {
        return -(this.k * particle.getPosition().getX() + this.gamma * particle.getVelocity().getxSpeed());
    }

    @Override
    public double calculateY(Particle particle) {
        return -(this.k * particle.getPosition().getY() + this.gamma * particle.getVelocity().getySpeed());
    }

    public double calculate(double rn_2, double rn_1) {
        return -(this.k * rn_2 + this.gamma * rn_1);
    }

    @Override
    public boolean isVelocityDependant() {
        return true;
    }
}

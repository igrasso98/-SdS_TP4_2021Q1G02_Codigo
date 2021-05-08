package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

import java.util.Collection;

public class ParticleElectrostaticEnergyCalculator implements EnergyCalculator {
    private static final double K = Math.pow(10, 10);

    private final Collection<Particle> particles;

    public ParticleElectrostaticEnergyCalculator(Collection<Particle> particles) {
        this.particles = particles;
    }

    @Override
    public double calculateTotalEnergy(Particle particle) {
        double potentialEnergy = this.calculatePotentialEnergy(particle);
        double kineticEnergy = this.calculateKineticEnergy(particle);
        return potentialEnergy + kineticEnergy;

    }

    private double calculateKineticEnergy(Particle particle) {
        double vx = particle.getVelocity().getxSpeed();
        double vy = particle.getVelocity().getySpeed();
        double V = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
        return Constants.RADIATION_PARTICLE_MASS * Math.pow(V, 2) / 2;
    }

    private double calculatePotentialEnergy(Particle particle) {
        double sum = this.sum(particle);

        double kCharge = K * particle.getCharge().getCharge();
        return kCharge * sum;
    }

    private double sum(Particle target) {
        double sum = 0;

        for (Particle particle : this.particles) {
            if (!particle.equals(target)) {
                double newValues = this.sumTerm(particle, target);
                sum += newValues;
            }
        }
        return sum;
    }

    private double sumTerm(Particle particle, Particle target) {
        double r = particle.distanceTo(target);
        return particle.getCharge().getCharge() / r;
    }
}

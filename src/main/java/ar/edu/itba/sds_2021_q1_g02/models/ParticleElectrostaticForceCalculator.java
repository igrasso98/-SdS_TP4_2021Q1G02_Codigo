package ar.edu.itba.sds_2021_q1_g02.models;


import javafx.util.Pair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;

public class ParticleElectrostaticForceCalculator implements ForceCalculator {
    private static final double K = Math.pow(10, 10);

    private final Collection<Particle> particles;

    public ParticleElectrostaticForceCalculator(Collection<Particle> particles) {
        this.particles = particles;
    }

    @Override
    public Pair<Double, Double> calculatePair(Particle particle) {
        Pair<Double, Double> sum = this.sum(particle);

        double kCharge = K * particle.getCharge().getCharge();
        return new Pair<>(
                kCharge * sum.getKey(),
                kCharge * sum.getValue()
        );
    }

    @Override
    public boolean isVelocityDependant() {
        return false;
    }

    @Override
    public double calculate(double rn_2, double rn_1) {
        throw new NotImplementedException();
    }

    private Pair<Double, Double> sum(Particle target) {
        ModifiablePair<Double, Double> sum = new ModifiablePair<>(0d, 0d);

        for (Particle particle : this.particles) {
            if (!particle.equals(target)) {
                Pair<Double, Double> newValues = this.sumTerm(particle, target);

                sum.setKey(sum.getKey() + newValues.getKey());
                sum.setValue(sum.getValue() + newValues.getValue());
            }
        }

        return sum.toPair();
    }

    private Pair<Double, Double> sumTerm(Particle particle, Particle target) {
        double magnitude = particle.distanceTo(target);
        double abs = particle.getCharge().getCharge() / magnitude;

        return new Pair<>(
                abs * (particle.getPosition().getX() - target.getPosition().getX()),
                abs * (particle.getPosition().getY() - target.getPosition().getY())
        );
    }

    private static class ModifiablePair<K, V> {
        private K key;
        private V value;

        public ModifiablePair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return this.value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Pair<K, V> toPair() {
            return new Pair<>(this.key, this.value);
        }
    }
}

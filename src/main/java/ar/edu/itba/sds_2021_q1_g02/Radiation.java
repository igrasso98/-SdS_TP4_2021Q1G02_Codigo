package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Radiation extends Simulation {
    private final Particle[][] particlesMatrix;
    private final Collection<Particle> particles;
    private final Particle impactParticle;
    private final IntegrationAlgorithm integrationAlgorithm;
    private final EnergyCalculator energyCalculator;
    private final double dt;
    private boolean impactParticleInsideMaterial = false;

    public Radiation(Particle[][] particlesMatrix, Particle impactParticle, IntegrationAlgorithm integrationAlgorithm
            , double dt) {
        this.particlesMatrix = particlesMatrix;
        this.impactParticle = impactParticle;
        this.integrationAlgorithm = integrationAlgorithm;
        this.dt = dt;

        this.particles = new ArrayList<>(Constants.N_PARTICLES_TOTAL + 1);
        for (Particle[] row : this.particlesMatrix) {
            Collection<Particle> rowCollection = Arrays.asList(row);
            this.particles.addAll(rowCollection);
        }
        this.particles.add(impactParticle);
        this.energyCalculator = new ParticleElectrostaticEnergyCalculator(this.particles);
    }

    @Override
    public void simulate() {
        this.serializeSystem(this.particles, this.integrationAlgorithm);
        double energyT0 = this.energyCalculator.calculateTotalEnergy(this.impactParticle);
        System.out.println(energyT0);
        Step step = this.calculateFirstStep();
        this.serialize(this.particles, step);

        while (this.continueSimulation()) {
            step = this.simulateStep(step);
            double energy = this.energyCalculator.calculateTotalEnergy(this.impactParticle);
            // TODO: HAY QUE GRAFICAR LOS VALORES QUE IMPRIMO ABAJO -> energyT0-energy
            System.out.println(Math.abs(energyT0 - energy));
            if (!this.continueSimulation())
                step.setLastStep(true);
            if (!this.impactParticleInsideMaterial && this.impactParticle.getPosition().getX() >= Constants.D) {
                this.impactParticleInsideMaterial = true;
            }

            this.serialize(this.particles, step);
        }
    }

    private Step simulateStep(Step previousStep) {
        Step newStep = new Step(
                this.particles.stream().collect(Collectors.toMap(particle -> particle, Particle::getPosition)),
                this.particles.stream().collect(Collectors.toMap(particle -> particle, Particle::getVelocity)),
                this.dt,
                previousStep.getAbsoluteTime() + this.dt,
                previousStep.getStep() + 1,
                this.integrationAlgorithm
        );

        Pair<Position, Velocity> newVelocityPositions = this.integrationAlgorithm.perform(this.impactParticle,
                previousStep);

        this.impactParticle.setPosition(newVelocityPositions.getKey());
        this.impactParticle.setVelocity(newVelocityPositions.getValue());

        return newStep;
    }

    private Step calculateFirstStep() {
        return new Step(
                this.particles.stream().collect(Collectors.toMap(particle -> particle, Particle::getPosition)),
                this.particles.stream().collect(Collectors.toMap(particle -> particle, Particle::getVelocity)),
                this.dt,
                0,
                0,
                this.integrationAlgorithm
        );
    }

    private boolean continueSimulation() {
        return this.isImpactParticleInside() || this.isImpactParticleTooClose();
    }

    private boolean isImpactParticleInside() {
        return this.impactParticle.getPosition().getX() >= (this.impactParticleInsideMaterial ? Constants.D : 0)
                && this.impactParticle.getPosition().getX() <= (Constants.L + Constants.D)
                && this.impactParticle.getPosition().getY() >= 0
                && this.impactParticle.getPosition().getY() <= Constants.L;
    }

    private boolean isImpactParticleTooClose() {
        for (Particle particle : this.particles) {
            if (!particle.equals(this.impactParticle)) {
                if (this.impactParticle.distanceTo(particle) < 0.01 * Constants.D) {
                    return true;
                }
            }
        }

        return false;
    }
}

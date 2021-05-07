package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.serializer.Serializable;
import javafx.util.Pair;

import java.util.Collections;

public class Oscillator extends Serializable {
    private final static double EPSILON = 1e-10;

    private final Particle particle;
    private final IntegrationAlgorithm integrationAlgorithm;
    private final double dt;
    private final double maxTime;

    public Oscillator(Particle particle, IntegrationAlgorithm integrationAlgorithm, double dt, double maxTime) {
        this.particle = particle;
        this.integrationAlgorithm = integrationAlgorithm;
        this.dt = dt;
        this.maxTime = maxTime;
    }

    public void simulate() {
        this.serializeSystem(Collections.singletonList(this.particle), this.integrationAlgorithm);
        Step step = this.calculateFirstStep();
        this.serialize(Collections.singletonList(this.particle), step);

        while (step.getAbsoluteTime() < this.maxTime) {
            step = this.simulateStep(step);
            if (step.getAbsoluteTime() >= this.maxTime)
                step.setLastStep(true);

            this.serialize(Collections.singletonList(this.particle), step);
        }
    }

    private Step simulateStep(Step previousStep) {
        Step newStep = new Step(
                Collections.singletonMap(this.particle.getId(), this.particle),
                Collections.singletonMap(this.particle, this.particle.getPosition()),
                Collections.singletonMap(this.particle, this.particle.getVelocity()),
                this.dt,
                previousStep.getAbsoluteTime() + this.dt,
                previousStep.getStep() + 1,
                this.integrationAlgorithm
        );

        Pair<Position, Velocity> newVelocityPositions = this.integrationAlgorithm.perform(this.particle, previousStep);

        this.particle.setPosition(newVelocityPositions.getKey());
        this.particle.setVelocity(newVelocityPositions.getValue());

        return newStep;
    }

    private Step calculateFirstStep() {
        return new Step(
                Collections.singletonMap(this.particle.getId(), this.particle),

                Collections.emptyMap(),
                Collections.emptyMap(),
//                Collections.singletonMap(this.particle, this.particle.getPosition()),
//                Collections.singletonMap(this.particle, this.particle.getVelocity()),
                this.dt,
                this.dt,
                0,
                this.integrationAlgorithm
        );
    }
}

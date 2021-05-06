package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.serializer.Serializer;
import javafx.util.Pair;

import java.util.*;

public class Oscillator {
    private final static double EPSILON = 1e-10;

    private final Particle particle;
    private final List<Serializer> serializers;
    private final IntegrationAlgorithm integrationAlgorithm;
    private final double dt;

    public Oscillator(Particle particle, IntegrationAlgorithm integrationAlgorithm, double dt) {
        this.particle = particle;
        this.serializers = new LinkedList<>();
        this.integrationAlgorithm = integrationAlgorithm;
        this.dt = dt;
    }

    public void addSerializer(Serializer serializer) {
        this.serializers.add(serializer);
    }

    public void simulate() {
        this.serializeSystem();
        Step step = this.calculateFirstStep();
        this.serialize(step);

        while (!this.particle.getVelocity().isZero(EPSILON) || !step.getPreviousVelocity(this.particle).isZero(EPSILON)) {
            step = this.simulateStep(step);
            this.serialize(step);
        }
    }

    private Step simulateStep(Step previousStep) {
        Pair<Position, Velocity> newVelocityPositions = this.integrationAlgorithm.perform(this.particle, previousStep);

        this.particle.setPosition(newVelocityPositions.getKey());
        this.particle.setVelocity(newVelocityPositions.getValue());

        return new Step(
                Collections.singletonMap(this.particle.getId(), this.particle),
                Collections.singletonMap(this.particle, this.particle.getPosition()),
                Collections.singletonMap(this.particle, this.particle.getVelocity()),
                this.dt,
                previousStep.getAbsoluteTime() + this.dt,
                previousStep.getStep() + 1
        );
    }

    private Step calculateFirstStep() {
        return new Step(
                Collections.singletonMap(this.particle.getId(), this.particle),
                Collections.singletonMap(this.particle, this.particle.getPosition()),
                Collections.singletonMap(this.particle, this.particle.getVelocity()),
                this.dt,
                this.dt,
                0
        );
    }

    private void serializeSystem() {
        for (Serializer serializer : this.serializers) {
            serializer.serializeSystem(Collections.singletonList(this.particle), this.integrationAlgorithm);
        }
    }

    private void serialize(Step step) {
        for (Serializer serializer : this.serializers) {
            serializer.serialize(Collections.singletonList(this.particle), step);
        }
    }
}

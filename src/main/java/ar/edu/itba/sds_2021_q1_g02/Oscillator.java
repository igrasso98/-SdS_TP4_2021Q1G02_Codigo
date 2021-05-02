package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.serializer.Serializer;
import javafx.util.Pair;

import java.util.*;

public class Oscillator {
    private final Particle particle;
    private final List<Serializer> serializers;

    public Oscillator(Particle particle) {
        this.particle = particle;
        this.serializers = new LinkedList<>();
    }

    public void addSerializer(Serializer serializer) {
        this.serializers.add(serializer);
    }

    public void simulate() {
        this.serializeSystem();
        Step step = this.calculateFirstStep();
        this.serialize(step);

        while (!this.halfOccupationFactor(step)) {
            step = this.simulateStep(step, false);
            if (step == null)
                return;

            this.serialize(step);
        }

        if (this.halfOccupationFactor(step)) {
            BigDecimal t = step.getAbsoluteTime() + this.configuration.getDt();
            while (step.getAbsoluteTime() < t) {
                step = this.simulateStep(step, true);
                if (step == null)
                    return;
            }
        }

        System.out.println("Pressure: " + this.systemPressure + "; collisions: " + this.pressureParticlesCollided);
    }

    private Step simulateStep(Step previousStep) {
    }

    private void serializeSystem() {
        for (Serializer serializer : this.serializers) {
            serializer.serializeSystem(this.particle);
        }
    }

    private void serialize(Step step) {
        for (Serializer serializer : this.serializers) {
            serializer.serialize(this.particle, step);
        }
    }
}

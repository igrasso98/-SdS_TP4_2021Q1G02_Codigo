package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.IntegrationAlgorithm;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Step;

import java.math.BigDecimal;
import java.util.Collection;

public abstract class Serializer {
    protected final double serializeEvery;
    private BigDecimal lastSerialized;

    public Serializer(double serializeEvery) {
        this.serializeEvery = serializeEvery;
        this.restartCount();
    }

    public void serializeSystem(Collection<Particle> particles, IntegrationAlgorithm integrationAlgorithm) {}

    public abstract void serialize(Collection<Particle> particles, Step step);

    protected boolean serialize(Step step) {
        if (step.getStep() == 0)
            return true;

        if (step.getAbsoluteTime().subtract(this.lastSerialized).doubleValue() >= this.serializeEvery || step.isLastStep()) {
            this.lastSerialized = step.getAbsoluteTime();
            return true;
        }

        return false;
    }

    protected void restartCount() {
        this.lastSerialized = BigDecimal.ZERO;
    }
}

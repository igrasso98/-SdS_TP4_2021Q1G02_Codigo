package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.IntegrationAlgorithm;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Step;

import java.math.BigDecimal;
import java.util.Collection;

public abstract class Serializer {
    private final BigDecimal serializeEvery;
    private BigDecimal lastSerialized = BigDecimal.ZERO;

    public Serializer(BigDecimal serializeEvery) {
        this.serializeEvery = serializeEvery;
    }

    public void serializeSystem(Collection<Particle> particles, IntegrationAlgorithm integrationAlgorithm) {}

    public abstract void serialize(Collection<Particle> particles, Step step);

    protected boolean serialize(Step step) {
        if (step.getStep() == 0)
            return true;

        this.lastSerialized = this.lastSerialized.add(step.getRelativeTime());
        if (this.lastSerialized.compareTo(this.serializeEvery) >= 0) {
            this.lastSerialized = BigDecimal.ZERO;
            return true;
        }

        return false;
    }
}

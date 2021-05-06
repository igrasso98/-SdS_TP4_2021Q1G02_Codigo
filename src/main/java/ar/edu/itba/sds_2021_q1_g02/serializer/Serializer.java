package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.IntegrationAlgorithm;
import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Step;

import java.util.Collection;

public abstract class Serializer {
    public static final double INFINITE_TIME = Double.POSITIVE_INFINITY;

    private final double serializeEvery;
    private double lastSerialized = 0;
    private double maxTime;

    public Serializer(double serializeEvery, double maxTime) {
        this.serializeEvery = serializeEvery;
        this.maxTime = maxTime;
    }

    public void serializeSystem(Collection<Particle> particles, IntegrationAlgorithm integrationAlgorithm) {}

    public abstract void serialize(Collection<Particle> particles, Step step);

    protected boolean serialize(Step step) {
        if (step.getStep() == 0)
            return true;

        this.lastSerialized = this.lastSerialized + step.getRelativeTime();
        if (this.lastSerialized - this.serializeEvery >= 0 || (this.maxTime < Serializer.INFINITE_TIME && step.getAbsoluteTime() + step.getRelativeTime() >= this.maxTime)) {
            this.restartCount();
            return true;
        }

        return false;
    }

    protected void restartCount() {
        this.lastSerialized = 0;
    }
}

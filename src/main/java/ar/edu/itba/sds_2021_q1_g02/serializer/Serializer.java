package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Step;

import java.util.Collection;

public interface Serializer {
    default void serializeSystem(Collection<Particle> particles) {}

    void serialize(Collection<Particle> particles, Step step);
}

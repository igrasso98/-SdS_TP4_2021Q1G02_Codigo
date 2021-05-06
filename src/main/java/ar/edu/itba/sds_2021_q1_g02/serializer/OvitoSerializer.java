package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.*;

import java.io.*;
import java.util.Collection;
import java.util.List;

public class OvitoSerializer extends Serializer {
    private final StepFormatter stepFormatter;
    private final ParticleFormatter particleFormatter;
    private final FileFormatter fileFormatter;

    public OvitoSerializer(StepFormatter stepFormatter, ParticleFormatter particleFormatter, FileFormatter fileFormatter, double serializeEvery, double maxTime) {
        super(serializeEvery, maxTime);

        this.stepFormatter = stepFormatter;
        this.particleFormatter = particleFormatter;
        this.fileFormatter = fileFormatter;
    }

    public OvitoSerializer(StepFormatter stepFormatter, ParticleFormatter particleFormatter, FileFormatter fileFormatter, double serializeEvery) {
        this(stepFormatter, particleFormatter, fileFormatter, serializeEvery, Serializer.INFINITE_TIME);
    }

    @Override
    public void serialize(Collection<Particle> particles, Step step) {
        if (!this.serialize(step))
            return;

        File file = new File(this.fileFormatter.formatFilename(step.getStep()));
        if (file.exists() && !file.delete())
            throw new RuntimeException("Couldn't delete file: " + file.getName());

        try {
            file.getParentFile().mkdirs();
            if (!file.createNewFile())
                throw new RuntimeException("Couldn't create file: " + file.getName());

            FileWriter writer = new FileWriter(file);

            writer.write(this.stepFormatter.format(particles, step));
            writer.write("\n");
            for (Particle p : particles) {
                writer.write(this.particleFormatter.format(p, step));
                writer.write("\n");
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

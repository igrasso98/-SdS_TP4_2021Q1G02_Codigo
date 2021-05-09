package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Step;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class RadiationEscapeProportionSerializer extends Serializer {
    private final FileFormatter fileFormatter;
    private TreeMap<Double, Integer> v0Runs;
    private TreeMap<Double, Integer> v0Escaped;

    public RadiationEscapeProportionSerializer(FileFormatter fileFormatter, double serializeEvery) {
        super(serializeEvery);

        this.fileFormatter = fileFormatter;
        this.reset();
    }

    public void finish() {
        File file = new File(this.fileFormatter.formatFilename(0));
        if (file.exists() && !file.delete())
            throw new RuntimeException("Couldn't delete file: " + file.getName());

        try {
            file.getParentFile().mkdirs();
            if (!file.createNewFile())
                throw new RuntimeException("Couldn't create file: " + file.getName());

            FileWriter writer = new FileWriter(file);

            // "v0 total_runs escaped"
            writer.write("v0\ttotal_runs\tescaped\n");

            for (Map.Entry<Double, Integer> v0RunsEntry : this.v0Runs.entrySet()) {
                StringBuilder builder = new StringBuilder();
                builder.append(v0RunsEntry.getKey());
                builder.append("\t");
                builder.append(v0RunsEntry.getValue());
                builder.append("\t");
                builder.append(this.v0Escaped.get(v0RunsEntry.getKey()));

                writer.write(builder + "\n");
            }

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.reset();
    }

    @Override
    public void serialize(Collection<Particle> particles, Step step) {
        if (step.getStep() == 0) {
            this.v0Runs.putIfAbsent(step.getV0(), 0);
            this.v0Escaped.putIfAbsent(step.getV0(), 0);
            return;
        } else if (!step.isLastStep()) {
            return;
        }

        this.v0Runs.computeIfPresent(step.getV0(), (aDouble, count) -> count + 1);
        if (step.hasImpactParticleEscaped())
            this.v0Escaped.computeIfPresent(step.getV0(), (aDouble, count) -> count + 1);
    }

    public void reset() {
        this.v0Runs = new TreeMap<>();
        this.v0Escaped = new TreeMap<>();
        this.restartCount();
    }
}

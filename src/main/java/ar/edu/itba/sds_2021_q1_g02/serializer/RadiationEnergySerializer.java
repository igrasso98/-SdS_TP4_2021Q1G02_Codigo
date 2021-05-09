package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Step;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class RadiationEnergySerializer extends Serializer {
    private final FileFormatter fileFormatter;
    private TreeMap<BigDecimal, Map<Double, Double>> absDtEnergyDifferences;
    private List<Double> dts;

    public RadiationEnergySerializer(FileFormatter fileFormatter, double serializeEvery) {
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

            // "abs dt1 ... dtn";
            StringBuilder builder = new StringBuilder();
            builder.append("abs");
            for (Double dt : this.dts) {
                builder.append("\t\"");
                builder.append(dt);
                builder.append("\"");
            }
            writer.write(builder + "\n");

            // "abs sist1 sist2 sist3 sist4";
            for (Map.Entry<BigDecimal, Map<Double, Double>> absDtEnergyDifference : this.absDtEnergyDifferences.entrySet()) {
                builder = new StringBuilder();
                builder.append(absDtEnergyDifference.getKey());
                for (Double dt : this.dts) {
                    builder.append("\t");
                    builder.append(absDtEnergyDifference.getValue().get(dt));
                }

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
            this.restartCount();
            this.dts.add(step.getRelativeTime().doubleValue());
        } else if (!this.serialize(step)) {
            return;
        }

        this.absDtEnergyDifferences.computeIfAbsent(step.getAbsoluteTime(), abs -> new HashMap<>()).put(step.getRelativeTime().doubleValue(), step.getEnergyDifference());
    }

    public void reset() {
        this.dts = new LinkedList<>();
        this.absDtEnergyDifferences = new TreeMap<>();
        this.restartCount();
    }
}

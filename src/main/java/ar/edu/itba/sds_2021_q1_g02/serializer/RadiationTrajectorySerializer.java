package ar.edu.itba.sds_2021_q1_g02.serializer;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Step;
import javafx.util.Pair;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class RadiationTrajectorySerializer extends Serializer {
    private final FileFormatter fileFormatter;
    private TreeMap<Double, TreeMap<BigDecimal, LinkedList<Double>>> v0AbsTrajectoryRuns;

    public RadiationTrajectorySerializer(FileFormatter fileFormatter, double serializeEvery) {
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

            TreeMap<Double, TreeMap<BigDecimal, Pair<Double, Double>>> v0AbsRStd = this.reduceResults();

            // "abs rv0 std0 rv1 std1 ... rvn stdn";
            StringBuilder builder = new StringBuilder();
            builder.append("abs");
            LinkedList<Double> v0s = new LinkedList<>();
            for (Double v0 : v0AbsRStd.keySet()) {
                builder.append("\t\"");
                builder.append(v0);
                builder.append("\"\t\"std_");
                builder.append(v0);
                builder.append("\"");

                v0s.add(v0);
            }
            writer.write(builder + "\n");

            TreeMap<BigDecimal, TreeMap<Double, Pair<Double, Double>>> absV0RStd = this.flipReducedResults(v0AbsRStd);

            // "abs rv0 std0 rv1 std1 ... rvn stdn";
            for (Map.Entry<BigDecimal, TreeMap<Double, Pair<Double, Double>>> absV0RStdEntry : absV0RStd.entrySet()) {
                builder = new StringBuilder();
                builder.append(absV0RStdEntry.getKey());
                for (Double v0 : v0s) {
                    Pair<Double, Double> rStd = absV0RStdEntry.getValue().get(v0);
                    if (rStd != null) {
                        builder.append("\t");
                        builder.append(rStd.getKey()); // Mean
                        builder.append("\t");
                        builder.append(rStd.getValue()); // Std
                    } else {
                        builder.append("\tnull\tnull");
                    }
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
        Particle impactParticle = this.findImpactParticle(particles);
        assert impactParticle != null;

        if (step.getStep() == 0) {
            this.restartCount();
            this.v0AbsTrajectoryRuns.computeIfAbsent(step.getV0(), v0 -> new TreeMap<>());
        } else if (!this.serialize(step)) {
            return;
        }

        this.v0AbsTrajectoryRuns.get(step.getV0()).computeIfAbsent(step.getAbsoluteTime(), abs -> new LinkedList<>()).add(step.getImpactParticleTotalTrajectory().doubleValue());
    }

    public void reset() {
        this.v0AbsTrajectoryRuns = new TreeMap<>();
        this.restartCount();
    }

    private Particle findImpactParticle(Collection<Particle> particles) {
        for (Particle particle : particles) {
            if (particle.getId() == 0)
                return particle;
        }
        return null;
    }

    private TreeMap<Double, TreeMap<BigDecimal, Pair<Double, Double>>> reduceResults() {
        TreeMap<Double, TreeMap<BigDecimal, Pair<Double, Double>>> v0AbsRStd = new TreeMap<>();

        for (Map.Entry<Double, TreeMap<BigDecimal, LinkedList<Double>>> v0absRRunEntry : this.v0AbsTrajectoryRuns.entrySet()) {
            v0AbsRStd.put(v0absRRunEntry.getKey(), this.reduceRuns(v0absRRunEntry.getValue()));
        }

        return v0AbsRStd;
    }

    private TreeMap<BigDecimal, Pair<Double, Double>> reduceRuns(TreeMap<BigDecimal, LinkedList<Double>> absRRuns) {
        TreeMap<BigDecimal, Pair<Double, Double>> absRStd = new TreeMap<>();

        for (Map.Entry<BigDecimal, LinkedList<Double>> absRRunEntry : absRRuns.entrySet()) {
            double[] runs = RadiationTrajectorySerializer.collectionToDoubleArray(absRRunEntry.getValue());

            Mean meanCalculator = new Mean();
            double mean = meanCalculator.evaluate(runs);
            absRStd.put(absRRunEntry.getKey(), new Pair<>(mean, new StandardDeviation().evaluate(runs, mean)));
        }

        return absRStd;
    }

    private static double[] collectionToDoubleArray(Collection<Double> doubles) {
        double[] array = new double[doubles.size()];

        int i = 0;
        for (Double d: doubles) {
            array[i++] = d;
        }

        return array;
    }

    private TreeMap<BigDecimal, TreeMap<Double, Pair<Double, Double>>> flipReducedResults(TreeMap<Double, TreeMap<BigDecimal, Pair<Double, Double>>> v0AbsRStd) {
        TreeMap<BigDecimal, TreeMap<Double, Pair<Double, Double>>> absV0RStd = new TreeMap<>();

        for (TreeMap<BigDecimal, Pair<Double, Double>> absRStd : v0AbsRStd.values()) {
            for (BigDecimal abs : absRStd.keySet()) {
                absV0RStd.computeIfAbsent(abs, time -> new TreeMap<>());
            }
        }

        for (Map.Entry<Double, TreeMap<BigDecimal, Pair<Double, Double>>> v0AbsRStdEntry : v0AbsRStd.entrySet()) {
            for (Map.Entry<BigDecimal, Pair<Double, Double>> absRStdEntry : v0AbsRStdEntry.getValue().entrySet()) {
                absV0RStd.computeIfAbsent(absRStdEntry.getKey(), time -> new TreeMap<>()).put(v0AbsRStdEntry.getKey(), absRStdEntry.getValue());
            }
        }

        return absV0RStd;
    }
}

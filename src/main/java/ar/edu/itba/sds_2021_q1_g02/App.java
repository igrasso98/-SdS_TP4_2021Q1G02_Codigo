package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.parsers.CommandParser;
import ar.edu.itba.sds_2021_q1_g02.parsers.ParticleParser;
import ar.edu.itba.sds_2021_q1_g02.serializer.ConsoleSerializer;
import ar.edu.itba.sds_2021_q1_g02.serializer.OvitoSerializer;
import javafx.util.Pair;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) throws ParseException, IOException {
//        CommandParser.getInstance().parse(args);

        App.oscillatorSimulation();

//        GD.addSerializer(new OvitoSerializer(
//                (systemParticles, step) -> (systemParticles.size() + 2) + "\n" + "Properties=id:R:1:radius:R:1:pos:R" +
//                        ":2:Velocity:R:2:mass:R:1:color:R:3:transparency:R:1",
//                (particle, step) -> {
//                    // id (1), radius (1), pos (2), size (1), color (3, RGB)";
//                    String s = particle.getId() + "\t" +
//                            particle.getRadius() + "\t" +
//                            particle.getPosition().getX() + "\t" +
//                            particle.getPosition().getY() + "\t" +
//                            particle.getVelocity().getxSpeed() + "\t" +
//                            particle.getVelocity().getySpeed() + "\t" +
//                            particle.getMass() + "\t";
//
//                    if (particle.getId() >= 0) {
//                        Color color = getParticleColor(particle, dimen);
//                        s += color.getRed() + "\t" +
//                                color.getGreen() + "\t" +
//                                color.getBlue() + "\t" +
//                                "0.0";
//                    } else {
//                        s += "0.0\t0.0\t0.0\t1.0";
//                    }
//
//                    return s;
//                },
//                step -> "output/output_" + step + ".xyz",
//                dimen
//        ));

//        GD.addSerializer(new ConsoleSerializer(
//                (systemParticles, configuration) -> {
//                    return "Height = " + String.format("%.5f",
//                            configuration.getDimen().getYvf() - configuration.getDimen().getYvi()) +
//                            "m; Width = " + String.format("%.5f",
//                            configuration.getDimen().getXvf() - configuration.getDimen().getXvi()) +
//                            "m; Aperture size = " + String.format("%.5f",
//                            configuration.getDimen().getApertureYvf() - configuration.getDimen().getApertureYvi()) +
//                            "m; Aperture X position = " + String.format("%.5f",
//                            configuration.getDimen().getApertureX()) +
//                            "m; Occupation factor tolerance = " + String.format("%.5f",
//                            configuration.getOccupationFactor());
//                },
//                (stepParticles, step) -> {
//                    return "** Step = " + step.getStep() +
//                            "; dT = " + String.format("%.5fs", step.getRelativeTime()) +
//                            "; abs = " + String.format("%.5fs", step.getAbsoluteTime()) +
//                            "; fp = " + String.format("%.5f", step.getLeftOccupationFactor());
//                },
//                (particle, step) -> {
//                    return particle.getId() + " | " +
//                            String.format("(%.5f, %.5f)m", particle.getPosition().getX(),
//                                    particle.getPosition().getY()) + " | " +
//                            String.format("(%.5f, %.5f)m/s", particle.getVelocity().getxSpeed(),
//                                    particle.getVelocity().getySpeed());
//                }
//        ));
    }

    private static void oscillatorSimulation() {
        Oscillator oscillator = new Oscillator(
                new Particle(
                        1,
                        1,
                        70,
                        new Position(
                                1,
                                0
                        ),
                        new Velocity(
                                -50,
                                0
                        )
                ),
                new EulerIntegrationAlgorithm(new DampedOscillatorForceCalculator(10e4, 100)),
                0.001
        );

        App.putOscillatorSerializers(oscillator);

        System.out.println("Running oscillator");
        oscillator.simulate();
        System.out.println("Ended oscillator");
    }

    private static Color getParticleColor(Particle particle) {
        if (particle.getCharge() == null || particle.getCharge().equals(ParticleCharge.NEGATIVE)) {
            return new Color(1.0, 0, 0);
        } else {
            return new Color(0, 1.0, 0);
        }
    }

    private static void putOscillatorSerializers(Oscillator oscillator) {
        oscillator.addSerializer(new ConsoleSerializer(
                (systemParticles, integrationAlgorithm) -> {
                    return "Using integration algorithm: " + integrationAlgorithm.getClass().getName();
                },
                (stepParticles, step) -> {
                    return "** Step = " + step.getStep() +
                            "; dT = " + String.format("%.8fs", step.getRelativeTime());
                },
                (particle, step) -> {
                    return particle.getId() + " | " +
                            String.format("%.5fm", particle.getPosition().getX()) +
                            " | " +
                            String.format("%.5fm/s", particle.getVelocity().getxSpeed());
                },
                1
        ));

        oscillator.addSerializer(new OvitoSerializer(
                (systemParticles, step) -> systemParticles.size() + "\n" + "Properties=id:R:1:radius:R:1:pos:R" +
                        ":2:Velocity:R:2:mass:R:1:color:R:3:transparency:R:1",
                (particle, step) -> {
                    // id (1), radius (1), pos (2), size (1), color (3, RGB)";
                    String s = particle.getId() + "\t" +
                            particle.getRadius() + "\t" +
                            particle.getPosition().getX() + "\t" +
                            particle.getPosition().getY() + "\t" +
                            particle.getVelocity().getxSpeed() + "\t" +
                            particle.getVelocity().getySpeed() + "\t" +
                            particle.getMass() + "\t";

                    if (particle.getId() >= 0) {
                        Color color = getParticleColor(particle);
                        s += color.getRed() + "\t" +
                                color.getGreen() + "\t" +
                                color.getBlue() + "\t" +
                                "0.0";
                    } else {
                        s += "0.0\t0.0\t0.0\t1.0";
                    }

                    return s;
                },
                step -> "R:/output/output_" + step + ".xyz",
                0.1
        ));
    }

//    private void testGearPredictorCorrector() {
//        GearPredictorCorrectorIntegrationAlgorithm integrationAlgorithm =
//                new GearPredictorCorrectorIntegrationAlgorithm(new DampedOscillatorForceCalculator(Math.pow(10, 4),
//                        100));
//        integrationAlgorithm.calculatePosition(new Particle(1, 1, 1,
//                        new Position(1, 0),
//                        new Velocity(-(1.0 * 100.0 / (2.0 * 1.0)), 0)),
//                0.01);
//    }
}

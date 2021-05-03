package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.parsers.CommandParser;
import ar.edu.itba.sds_2021_q1_g02.parsers.ParticleParser;
import javafx.util.Pair;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class App {
    public static void main(String[] args) throws ParseException, IOException {
        CommandParser.getInstance().parse(args);


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

//        GD.addSerializer(new CSVSerializer(
//                (systemParticles, step) -> "id;\"radius [m]\";\"x [m]\";\"y [m]\";\"mass [kg]\";\"vx [m/s]\";\"vy " +
//                        "[m/s]\";\"dt [s]\";\"t [s]\";fp",
//                (particle, step) -> {
//                    // id (1), radius (1), pos (2), size (1), speed (2), dt (1), t (1)";
//                    return particle.getId() + ";" +
//                            particle.getRadius() + ";" +
//                            particle.getPosition().getX() + ";" +
//                            particle.getPosition().getY() + ";" +
//                            particle.getMass() + ";" +
//                            particle.getVelocity().getxSpeed() + ";" +
//                            particle.getVelocity().getySpeed() + ";" +
//                            step.getRelativeTime() + ";" +
//                            step.getAbsoluteTime() + ";" +
//                            step.getLeftOccupationFactor();
//                },
//                step -> "output/output_" + step + ".csv"
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
        Oscillator oscillator = new Oscillator(new Particle(
//                1,
//                BigDecimal.valueOf(1),
//                BigDecimal.valueOf(70),
//                new Position(
//                        BigDecimal.valueOf()
//                )
        ));

        System.out.println("Running simulation 1");
        oscillator.simulate();
    }

    private static Color getParticleColor(Particle particle) {
        if (particle.getCharge() == null || particle.getCharge().equals(ParticleCharge.NEGATIVE)) {
            return new Color(1.0, 0, 0);
        } else {
            return new Color(0, 1.0, 0);
        }
    }

    private void testGearPredictorCorrector() {
        GearPredictorCorrectorIntegrationAlgorithm integrationAlgorithm =
                new GearPredictorCorrectorIntegrationAlgorithm(new DampedOscillatorForceCalculator(Math.pow(10, 4),
                        100));
        integrationAlgorithm.calculatePosition(new Particle(1, BigDecimal.valueOf(1), BigDecimal.valueOf(1),
                        new Position(BigDecimal.valueOf(1), BigDecimal.valueOf(0)),
                        new Velocity(BigDecimal.valueOf(-(1.0 * 100.0) / (2.0 * 1.0)), BigDecimal.valueOf(0))),
                BigDecimal.valueOf(0.01));
    }
}

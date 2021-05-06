package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.serializer.OscillatorSerializer;
import ar.edu.itba.sds_2021_q1_g02.serializer.OvitoSerializer;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

public class App {
    private static OscillatorSerializer OSCILLATOR_SERIALIZER = new OscillatorSerializer(
            step -> "R:/output/oscillator.tsv",
            0.01
    );

    public static void main(String[] args) throws ParseException, IOException {
//        CommandParser.getInstance().parse(args);

        System.out.println("------------- OSCILLATOR -------------");
        App.oscillatorSimulation();
        System.out.println("--------------------------------------");

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
        Oscillator eulerOscillator = App.getOscillatorSystem(new EulerIntegrationAlgorithm(new DampedOscillatorForceCalculator(10e4, 100)));
        Oscillator eulerVelocityOscillator = App.getOscillatorSystem(new EulerVelocityIntegrationAlgorithm(new DampedOscillatorForceCalculator(10e4, 100)));
        Oscillator gearPredictorOscillator = App.getOscillatorSystem(new GearPredictorCorrectorIntegrationAlgorithm(new DampedOscillatorForceCalculator(10e4, 100)));
        Oscillator realOscillator = App.getOscillatorSystem(new OscillatorRealSolution());

        App.putOscillatorSerializers(eulerOscillator);
        App.putOscillatorSerializers(eulerVelocityOscillator);
        App.putOscillatorSerializers(gearPredictorOscillator);
        App.putOscillatorSerializers(realOscillator);

        System.out.println("Simulating euler");
        eulerOscillator.simulate();
        System.out.println("Simulating euler velocity");
        eulerVelocityOscillator.simulate();
        System.out.println("Simulating gear predictor");
        gearPredictorOscillator.simulate();
        System.out.println("Simulating real");
        realOscillator.simulate();

        System.out.println("Dumping results");
        App.dumpOscillatorResults();
    }

    private static Color getParticleColor(Particle particle) {
        if (particle.getCharge() == null || particle.getCharge().equals(ParticleCharge.NEGATIVE)) {
            return new Color(1.0, 0, 0);
        } else {
            return new Color(0, 1.0, 0);
        }
    }

    private static void putOscillatorSerializers(Oscillator oscillator) {
//        oscillator.addSerializer(new OvitoSerializer(
//                (systemParticles, step) -> systemParticles.size() + "\n" + "Properties=id:R:1:radius:R:1:pos:R" +
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
//                        Color color = getParticleColor(particle);
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
//                step -> "R:/output/output_" + step + ".xyz",
//                0.01
//        ));

        oscillator.addSerializer(App.OSCILLATOR_SERIALIZER);
    }

    private static void dumpOscillatorResults() {
        App.OSCILLATOR_SERIALIZER.finish();
    }

    private static Particle getOscillatorParticle() {
        return new Particle(
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
        );
    }

    private static Oscillator getOscillatorSystem(IntegrationAlgorithm integrationAlgorithm) {
        return new Oscillator(
                App.getOscillatorParticle(),
                integrationAlgorithm,
                0.001,
                5
        );
    }
}

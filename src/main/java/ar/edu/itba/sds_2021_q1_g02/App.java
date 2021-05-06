package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.serializer.OscillatorSerializer;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

public class App {
    private static final double OSCILLATOR_DT = 0.0001;
    private static final double OSCILLATOR_SERIALIZE_EVERY = 0.01;
    private static final OscillatorSerializer OSCILLATOR_SERIALIZER = new OscillatorSerializer(
            step -> "R:/output/oscillator.tsv",
            App.OSCILLATOR_SERIALIZE_EVERY
    );
    private static final DampedOscillatorForceCalculator DAMPED_FORCE = new DampedOscillatorForceCalculator(10000, 100);

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
        Oscillator eulerOscillator = App.getOscillatorSystem(new EulerIntegrationAlgorithm(App.DAMPED_FORCE));
        Oscillator eulerVelocityOscillator = App.getOscillatorSystem(new EulerVelocityIntegrationAlgorithm(App.DAMPED_FORCE));
        Oscillator beemanOscillator = App.getOscillatorSystem(new BeemanAlgorithm(App.DAMPED_FORCE));
        Oscillator verletOscillator = App.getOscillatorSystem(new VerletIntegrationAlgorithm(App.DAMPED_FORCE));
        Oscillator gearPredictorOscillator = App.getOscillatorSystem(new GearPredictorCorrectorIntegrationAlgorithm(App.DAMPED_FORCE));
        Oscillator realOscillator = App.getOscillatorSystem(new OscillatorRealSolution());

        App.putOscillatorSerializers(eulerOscillator);
        App.putOscillatorSerializers(eulerVelocityOscillator);
        App.putOscillatorSerializers(beemanOscillator);
        App.putOscillatorSerializers(verletOscillator);
        App.putOscillatorSerializers(gearPredictorOscillator);
        App.putOscillatorSerializers(realOscillator);

        System.out.println("Simulating euler");
        eulerOscillator.simulate();
        System.out.println("Simulating euler velocity");
        eulerVelocityOscillator.simulate();
        System.out.println("Simulating beeman");
        beemanOscillator.simulate();
        System.out.println("Simulating verlet");
        verletOscillator.simulate();
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
                        -0.7142857142857,
                        0
                )
        );
    }

    private static Oscillator getOscillatorSystem(IntegrationAlgorithm integrationAlgorithm) {
        return new Oscillator(
                App.getOscillatorParticle(),
                integrationAlgorithm,
                App.OSCILLATOR_DT,
                5
        );
    }
}

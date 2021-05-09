package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import ar.edu.itba.sds_2021_q1_g02.parsers.CommandParser;
import ar.edu.itba.sds_2021_q1_g02.parsers.ParticleParser;
import ar.edu.itba.sds_2021_q1_g02.parsers.RadiationInput;
import ar.edu.itba.sds_2021_q1_g02.serializer.OscillatorSerializer;
import ar.edu.itba.sds_2021_q1_g02.serializer.RadiationEnergySerializer;
import ar.edu.itba.sds_2021_q1_g02.serializer.RadiationEscapeProportionSerializer;
import ar.edu.itba.sds_2021_q1_g02.serializer.RadiationTrajectorySerializer;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class App {
    private static final double OSCILLATOR_DT = 0.0001;
    private static final double OSCILLATOR_SERIALIZE_EVERY = 0.01;
    private static final OscillatorSerializer OSCILLATOR_SERIALIZER = new OscillatorSerializer(
            step -> "R:/output/oscillator_1.tsv",
            App.OSCILLATOR_SERIALIZE_EVERY
    );
    private static final DampedOscillatorForceCalculator DAMPED_FORCE = new DampedOscillatorForceCalculator(10000, 100);
    private static final double[] OSCILLATOR_DTS = {0.01, 0.001, 1e-4, 1e-5, 1e-6};

    private static final double RADIATION_DT = 1e-14;
    private static final double RADIATION_RADIUS = 0.2;
    private static final double RADIATION_SERIALIZE_EVERY = 1e-14;
    private static final double[] RADIATION_DTS = {1e-14, 1e-15, 1e-16, 1e-17, 1e-18};
    private static final double[] RADIATION_V0S = {10e3, 20e3, 30e3, 40e3, 50e3, 60e3, 70e3, 80e3, 90e3, 100e3};
    private static final double[] RADIATION_Y_OFFSETS = {-1, -.9, -.8, -.7, -.6, -.5, -.4, -.3, -.2, -.1, 0, .1, .2, .3, .4, .5, .6, .7, .8, .9, 1};
    private static final RadiationEnergySerializer RADIATION_2_ENERGY_SERIALIZER = new RadiationEnergySerializer(
            step -> "R:/output/radiation_2.tsv",
            App.RADIATION_SERIALIZE_EVERY
    );
    private static final RadiationTrajectorySerializer RADIATION_3_TRAJECTORY_SERIALIZER = new RadiationTrajectorySerializer(
            step -> "R:/output/radiation_3.tsv",
            App.RADIATION_SERIALIZE_EVERY
    );
    private static final RadiationEscapeProportionSerializer RADIATION_4_FACTOR_SERIALIZER = new RadiationEscapeProportionSerializer(
            step -> "R:/output/radiation_4.tsv",
            App.RADIATION_SERIALIZE_EVERY
    );

    public static void main(String[] args) throws ParseException, IOException {
        CommandParser.getInstance().parse(args);

        System.out.println("------------- OSCILLATOR -------------");
        App.oscillatorSimulation();
        System.out.println("--------------------------------------");

        System.out.println("------------- RADIATION -------------");
        App.radiationSimulation();
        System.out.println("--------------------------------------");
    }

    private static void oscillatorSimulation() {
        App.oscillatorSimulation1();
        App.oscillatorSimulation2();
    }

    private static void oscillatorSimulation1() {
        Oscillator eulerOscillator = App.getOscillatorSystem(new EulerIntegrationAlgorithm(App.DAMPED_FORCE), App.OSCILLATOR_DT);
        Oscillator eulerVelocityOscillator = App.getOscillatorSystem(new EulerVelocityIntegrationAlgorithm(App.DAMPED_FORCE), App.OSCILLATOR_DT);
        Oscillator beemanOscillator = App.getOscillatorSystem(new BeemanAlgorithm(App.DAMPED_FORCE), App.OSCILLATOR_DT);
        Oscillator verletOscillator = App.getOscillatorSystem(new VerletIntegrationAlgorithm(App.DAMPED_FORCE), App.OSCILLATOR_DT);
        Oscillator gearPredictorOscillator = App.getOscillatorSystem(new GearPredictorCorrectorIntegrationAlgorithm(App.DAMPED_FORCE), App.OSCILLATOR_DT);
        Oscillator realOscillator = App.getOscillatorSystem(new OscillatorRealSolution(), App.OSCILLATOR_DT);

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

        System.out.println("Dumping results 1.1/1.2");
        App.dumpOscillatorResults();
    }

    private static void oscillatorSimulation2() {
        for (double dt : App.OSCILLATOR_DTS) {
            OscillatorSerializer serializer = new OscillatorSerializer(
                    step -> "R:/output/oscillator_2_" + dt + ".tsv",
                    App.OSCILLATOR_SERIALIZE_EVERY
            );

            Oscillator eulerOscillator = App.getOscillatorSystem(new EulerIntegrationAlgorithm(App.DAMPED_FORCE), dt);
            Oscillator eulerVelocityOscillator = App.getOscillatorSystem(new EulerVelocityIntegrationAlgorithm(App.DAMPED_FORCE), dt);
            Oscillator beemanOscillator = App.getOscillatorSystem(new BeemanAlgorithm(App.DAMPED_FORCE), dt);
            Oscillator verletOscillator = App.getOscillatorSystem(new VerletIntegrationAlgorithm(App.DAMPED_FORCE), dt);
            Oscillator gearPredictorOscillator = App.getOscillatorSystem(new GearPredictorCorrectorIntegrationAlgorithm(App.DAMPED_FORCE), dt);
            Oscillator realOscillator = App.getOscillatorSystem(new OscillatorRealSolution(), dt);

            eulerOscillator.addSerializer(serializer);
            eulerVelocityOscillator.addSerializer(serializer);
            beemanOscillator.addSerializer(serializer);
            verletOscillator.addSerializer(serializer);
            gearPredictorOscillator.addSerializer(serializer);
            realOscillator.addSerializer(serializer);

            System.out.println("Simulating 1.3 with dt = " + dt);
            eulerOscillator.simulate();
            eulerVelocityOscillator.simulate();
            beemanOscillator.simulate();
            verletOscillator.simulate();
            gearPredictorOscillator.simulate();
            realOscillator.simulate();

            System.out.println("Dumping 1.3");
            serializer.finish();
        }
    }

    private static void putOscillatorSerializers(Oscillator oscillator) {
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

    private static Oscillator getOscillatorSystem(IntegrationAlgorithm integrationAlgorithm, double dt) {
        return new Oscillator(
                App.getOscillatorParticle(),
                integrationAlgorithm,
                dt,
                5
        );
    }

    private static void radiationSimulation() throws IOException {
        App.radiationSimulation1_2();
        App.radiationSimulation3_4();
    }

    private static void radiationSimulation1_2() throws IOException {
        App.RADIATION_2_ENERGY_SERIALIZER.reset();

        for (double dt : App.RADIATION_DTS) {
            App.radiationSimulation1_2(dt);
        }

        App.RADIATION_2_ENERGY_SERIALIZER.finish();
    }

    private static void radiationSimulation1_2(double dt) throws IOException {
        RadiationInput radiationInput = ParticleParser.parseParticles(CommandParser.getInstance().getInputPath(), new Position(Constants.D, 0));
        Particle impactParticle = App.getImpactParticle(radiationInput.getV0(), radiationInput.getyOffset());

        Collection<Particle> particles = new ArrayList<>(Constants.N_PARTICLES_TOTAL);
        for (Particle[] row : radiationInput.getParticles()) {
            particles.addAll(Arrays.asList(row));
        }

        Radiation radiation = new Radiation(
                radiationInput.getParticles(),
                impactParticle,
                new BeemanAlgorithm(new ParticleElectrostaticForceCalculator(particles)),
                dt
        );

//        radiation.addSerializer(new OvitoSerializer(
//                (systemParticles, step) -> systemParticles.size() + "\n" + "Properties=id:R:1:radius:R:1:pos:R" +
//                        ":2:Velocity:R:2:color:R:3",
//                (particle, step) -> {
//                    // id (1), radius (1), pos (2), size (1), color (3, RGB)";
//                    String s = particle.getId() + "\t" +
//                            App.RADIATION_RADIUS + "\t" +
//                            particle.getPosition().getX() / Constants.D + "\t" +
//                            particle.getPosition().getY() / Constants.D + "\t" +
//                            particle.getVelocity().getxSpeed() / Constants.D + "\t" +
//                            particle.getVelocity().getySpeed() / Constants.D + "\t";
//
//                    Color color = getParticleColor(particle);
//                    s += color.getRed() + "\t" +
//                            color.getGreen() + "\t" +
//                            color.getBlue();
//
//                    return s;
//                },
//                step -> "R:/output/radiation_1_" + dt + "_" + step + ".xyz",
//                App.RADIATION_SERIALIZE_EVERY
//        ));
        radiation.addSerializer(App.RADIATION_2_ENERGY_SERIALIZER);

        System.out.println("Simulating radiation with dt = " + dt);
        radiation.simulate();
    }

    private static void radiationSimulation3_4() throws IOException {
        App.RADIATION_3_TRAJECTORY_SERIALIZER.reset();
        App.RADIATION_4_FACTOR_SERIALIZER.reset();

        for (double v0 : App.RADIATION_V0S) {
            System.out.println("Simulating radiation 3 with v0 = " + v0);
            for (double yOffset: App.RADIATION_Y_OFFSETS) {
                App.radiationSimulation3_4(v0, yOffset * Constants.D);
            }
        }

        App.RADIATION_3_TRAJECTORY_SERIALIZER.finish();
        App.RADIATION_4_FACTOR_SERIALIZER.finish();
    }

    private static void radiationSimulation3_4(double V0, double yOffset) throws IOException {
        RadiationInput radiationInput = ParticleParser.parseParticles(CommandParser.getInstance().getInputPath(), new Position(Constants.D, 0));
        Particle impactParticle = App.getImpactParticle(V0, yOffset);

        Collection<Particle> particles = new ArrayList<>(Constants.N_PARTICLES_TOTAL);
        for (Particle[] row : radiationInput.getParticles()) {
            particles.addAll(Arrays.asList(row));
        }

        Radiation radiation = new Radiation(
                radiationInput.getParticles(),
                impactParticle,
                new BeemanAlgorithm(new ParticleElectrostaticForceCalculator(particles)),
                App.RADIATION_DT
        );

        radiation.addSerializer(App.RADIATION_3_TRAJECTORY_SERIALIZER);
        radiation.addSerializer(App.RADIATION_4_FACTOR_SERIALIZER);
        radiation.simulate();
    }

    private static Particle getImpactParticle(double V0, double yOffset) {
        return new Particle(
                0,
                0,
                Constants.RADIATION_PARTICLE_MASS,
                Constants.RADIATION_PARTICLE_POSITION.add(new Position(0, yOffset)),
                new Velocity(V0, 0),
                ParticleCharge.POSITIVE
        );
    }

    private static Color getParticleColor(Particle particle) {
        if (particle.getCharge() == null || particle.getCharge().equals(ParticleCharge.NEGATIVE)) {
            return new Color(1.0, 0, 0);
        } else {
            return new Color(0, 1.0, 0);
        }
    }
}

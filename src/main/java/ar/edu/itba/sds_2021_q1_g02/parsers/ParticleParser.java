package ar.edu.itba.sds_2021_q1_g02.parsers;

import ar.edu.itba.sds_2021_q1_g02.models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public abstract class ParticleParser {
    public static RadiationInput parseParticles(String filePath, Position offset) throws IOException {
        Particle[][] particles = new Particle[Constants.N_PARTICLES_SIDE][Constants.N_PARTICLES_SIDE];

        int i = 1;
        double V0;
        double yOffset;
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);

            String data = myReader.nextLine();
            V0 = Double.parseDouble(data);

            data = myReader.nextLine();
            yOffset = Double.parseDouble(data);

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                String[] info = data.split("\t");

                Position matrixPosition = new Position(Double.parseDouble(info[0]), Double.parseDouble(info[1]));
                Particle particle = new Particle(i, 0, Constants.RADIATION_PARTICLE_MASS, matrixPosition.multiply(Constants.D).add(offset), new Velocity(0, 0), ParticleCharge.fromInteger(Integer.parseInt(info[2])));
                particles[matrixPosition.getRoundedX()][matrixPosition.getRoundedY()] = particle;

                i++;
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            throw e;
        }

        return new RadiationInput(
                particles,
                V0,
                yOffset
        );
    }
}

package ar.edu.itba.sds_2021_q1_g02.parsers;

import ar.edu.itba.sds_2021_q1_g02.models.Particle;
import ar.edu.itba.sds_2021_q1_g02.models.Position;
import ar.edu.itba.sds_2021_q1_g02.models.Velocity;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class ParticleParser {
    public static Pair<List<Particle>, Integer> parseParticles(String filePath) throws IOException {
        List<Particle> particles;

        int i = 0, M;
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);

            String data = myReader.nextLine();
            M = Integer.parseInt(data);

            particles = new ArrayList<>(M);

            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                String[] info = data.split("\t");
                // int id, BigDecimal radius,BigDecimal mass,Position position, Velocity velocity
                Particle particle = new Particle(i, BigDecimal.valueOf(Double.parseDouble(info[0])), BigDecimal.valueOf(Double.parseDouble(info[1])), new Position(BigDecimal.valueOf(Double.parseDouble(info[2])), BigDecimal.valueOf(Double.parseDouble(info[3]))), new Velocity(BigDecimal.valueOf(Double.parseDouble(info[4])), BigDecimal.valueOf(Double.parseDouble(info[5]))));
                particles.add(particle);
                i++;
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            throw e;
        }

        return new Pair<>(particles, M);
    }
}

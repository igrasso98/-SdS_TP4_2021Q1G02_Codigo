package ar.edu.itba.sds_2021_q1_g02.models;


public class Acceleration {
    private final double xAcceleration;
    private final double yAcceleration;

    public Acceleration(double xAcceleration, double yAcceleration) {
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
    }

    public double getxAcceleration() {
        return this.xAcceleration;
    }

    public double getyAcceleration() {
        return this.yAcceleration;
    }

    public Acceleration copy() {
        return new Acceleration(this.xAcceleration, this.yAcceleration);
    }

    @Override
    public String toString() {
        return String.format("(%.5f, %.5f)", this.xAcceleration, this.yAcceleration);
    }
}

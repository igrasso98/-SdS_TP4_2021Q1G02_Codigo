package ar.edu.itba.sds_2021_q1_g02.models;

public abstract class Constants {
    public static final double D = Math.pow(10, -8);
    public static final int N_PARTICLES_SIDE = 16;
    public static final int N_PARTICLES_TOTAL = N_PARTICLES_SIDE * N_PARTICLES_SIDE;
    public static final double L = (N_PARTICLES_SIDE - 1) * D;
    public static final double RADIATION_PARTICLE_MASS = Math.pow(10, -27);
    public static final Position RADIATION_PARTICLE_POSITION = new Position(0, L / 2);
}

package ar.edu.itba.sds_2021_q1_g02.models;

public enum ParticleCharge {
    POSITIVE(1),
    NEGATIVE(-1);

    private final double charge;

    ParticleCharge(int charge) {
        this.charge = charge * Math.pow(10, -19);
    }

    public double getCharge() {
        return this.charge;
    }

    public static ParticleCharge fromInteger(int parseInt) {
        return parseInt < 0 ? NEGATIVE : POSITIVE;
    }
}
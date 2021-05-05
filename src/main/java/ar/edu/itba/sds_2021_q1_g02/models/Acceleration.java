package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;

public class Acceleration {
    private final BigDecimal xAcceleration;
    private final BigDecimal yAcceleration;

    public Acceleration(BigDecimal xAcceleration, BigDecimal yAcceleration) {
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
    }

    public BigDecimal getxAcceleration() {
        return this.xAcceleration;
    }

    public BigDecimal getyAcceleration() {
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

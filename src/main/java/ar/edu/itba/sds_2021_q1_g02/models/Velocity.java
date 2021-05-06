package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;

public class Velocity {
    private final BigDecimal xSpeed;
    private final BigDecimal ySpeed;

    public Velocity(BigDecimal xSpeed, BigDecimal ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public BigDecimal getxSpeed() {
        return this.xSpeed;
    }

    public BigDecimal getySpeed() {
        return this.ySpeed;
    }

    public Velocity copy() {
        return new Velocity(this.xSpeed, this.ySpeed);
    }

    @Override
    public String toString() {
        return String.format("(%.5f, %.5f)", this.xSpeed, this.ySpeed);
    }

    public boolean isZero() {
        return this.xSpeed.equals(BigDecimal.ZERO) && this.ySpeed.equals(BigDecimal.ZERO);
    }
}

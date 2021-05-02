package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;

public class Position {
    private BigDecimal x;
    private BigDecimal y;

    public Position(BigDecimal x, BigDecimal y) {
        this.x = x;
        this.y = y;
    }

    public BigDecimal getX() {
        return this.x;
    }
    public void setX(BigDecimal x) { this.x  = x;}

    public BigDecimal getY() {
        return this.y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public Position copy() {
        return new Position(this.x, this.y);
    }

    @Override
    public String toString() {
        return String.format("(%.5f, %.5f)", this.x, this.y);
    }
}

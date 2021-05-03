package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;

public class Color {
    private final BigDecimal red;
    private final BigDecimal green;
    private final BigDecimal blue;

    public Color(BigDecimal red, BigDecimal green, BigDecimal blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public BigDecimal getRed() {
        return this.red;
    }

    public BigDecimal getGreen() {
        return this.green;
    }

    public BigDecimal getBlue() {
        return this.blue;
    }
}

package ar.edu.itba.sds_2021_q1_g02.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class BigDecimalDivision {
    private static final int BIG_DECIMAL_PRECISION = 20;

    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return a.divide(b, BIG_DECIMAL_PRECISION, RoundingMode.HALF_UP);
    }
}

package com.parkit.parkingsystem.constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Fare {
  public static final BigDecimal BIKE_RATE_PER_HOUR = new BigDecimal("1").setScale(2, RoundingMode.HALF_EVEN);
  public static final BigDecimal CAR_RATE_PER_HOUR = new BigDecimal("1.5").setScale(2, RoundingMode.HALF_EVEN);
  public static final BigDecimal REDUCTION_FIVE_POURCENT = new BigDecimal("0.95").setScale(2, RoundingMode.HALF_EVEN);
  public static final BigDecimal FREE_FARE = new BigDecimal("0").setScale(2, RoundingMode.HALF_EVEN);
}

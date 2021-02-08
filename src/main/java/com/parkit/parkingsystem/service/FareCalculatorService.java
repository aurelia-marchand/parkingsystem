package com.parkit.parkingsystem.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

  /**
   * Calculate the fare.
   * 
   * @param ticket
   * 
   * @see com.parkit.parkingsystem.service.ParkingService#processExitingVehicle()
   * 
   */
  public void calculateFare(Ticket ticket) {

    // Check that the vehicle exit time is not null and is not less than the
    // entry time
    if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
      throw new IllegalArgumentException(
          "Out time provided is incorrect:" + ticket.getOutTime().toString());
    }

    // Convert into hours
    double inHour = (ticket.getInTime().getTime()) / 3600000.0;
    double outHour = (ticket.getOutTime().getTime()) / 3600000.0;

    // Calculate the difference in order to obtain the duration
    double duree = (outHour - inHour);

    // Convert double into bigDecimal for the rest of the monetary calculations
    BigDecimal duration = new BigDecimal(duree);

    // Create a variable representing the limit of a free half hour
    BigDecimal demiHeure = new BigDecimal("0.5");

    // Test if the user stayed less than 30 minutes
    int limiteDemiHeure = duration.compareTo(demiHeure);

    // If test == -1 it means that the duration was much less than 30 minutes
    if (limiteDemiHeure == -1) {
      duration = Fare.FREE_FARE;
    }

    // Test if the getRecurrent method returns to true and if so the user
    // benefits from the reduction otherwise the calculation of the normal price is
    // performed
    switch (ticket.getParkingSpot().getParkingType()) {
      case CAR: {
        if (ticket.getRecurrent()) {
          ticket.setPrice((duration.multiply(Fare.CAR_RATE_PER_HOUR
              .multiply(Fare.REDUCTION_FIVE_POURCENT).setScale(2, RoundingMode.HALF_EVEN))));
        } else {
          ticket.setPrice(
              duration.multiply(Fare.CAR_RATE_PER_HOUR).setScale(2, RoundingMode.HALF_EVEN));
        }
        break;
      }
      case BIKE: {
        if (ticket.getRecurrent()) {
          ticket.setPrice((duration.multiply(Fare.BIKE_RATE_PER_HOUR
              .multiply(Fare.REDUCTION_FIVE_POURCENT).setScale(2, RoundingMode.HALF_EVEN))));
        } else {
          ticket.setPrice(
              duration.multiply(Fare.BIKE_RATE_PER_HOUR).setScale(2, RoundingMode.HALF_EVEN));
        }
        break;
      }
      default:
        throw new IllegalArgumentException("Unkown Parking Type");
    }
  }
}
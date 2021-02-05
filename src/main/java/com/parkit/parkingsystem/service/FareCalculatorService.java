package com.parkit.parkingsystem.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

  public void calculateFare(Ticket ticket) {
    
    if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
      throw new IllegalArgumentException(
          "Out time provided is incorrect:" + ticket.getOutTime().toString());
    }

    // change int to double et getHours to getTime / 36000000.000 pour conversion
    // heure.00
    // essayer classe BigDecimal
    // int inHour = ticket.getInTime().getHours();
    // int outHour = ticket.getOutTime().getHours();
    
    double inHour = (ticket.getInTime().getTime()) ;
    double outHour =(ticket.getOutTime().getTime());
    
    double duree = (outHour  - inHour);
    double dureeHeure = duree /3600000.0;
    


    
    BigDecimal duration = new BigDecimal(dureeHeure);
    
    BigDecimal demiHeure = new BigDecimal("0.5");
    int test = duration.compareTo(demiHeure);

    //System.out.println("duration :  "+ duration + "demiheure : " + demiHeure + " test : " + test);
    
    // stationnement gratuit moins de 30 minutes
    if ( test == -1) {
      duration = Fare.FREE_FARE;
    }
    

    switch (ticket.getParkingSpot().getParkingType()) {
      case CAR: {
        if (ticket.getRecurrent()) {
          ticket.setPrice((duration.multiply(Fare.CAR_RATE_PER_HOUR.multiply(Fare.REDUCTION_FIVE_POURCENT).setScale(2, RoundingMode.HALF_EVEN))));
        } else {
          ticket.setPrice(duration.multiply(Fare.CAR_RATE_PER_HOUR).setScale(2, RoundingMode.HALF_EVEN));
        }
        break;
      }
      case BIKE: {
        if (ticket.getRecurrent()) {
          ticket.setPrice((duration.multiply(Fare.BIKE_RATE_PER_HOUR.multiply(Fare.REDUCTION_FIVE_POURCENT).setScale(2, RoundingMode.HALF_EVEN))));
        } else {
          ticket.setPrice(duration.multiply(Fare.BIKE_RATE_PER_HOUR).setScale(2, RoundingMode.HALF_EVEN));
        }
        break;
      }
      default:
        throw new IllegalArgumentException("Unkown Parking Type");
    }
  }
}
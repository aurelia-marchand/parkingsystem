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
   */
  public void calculateFare(Ticket ticket) {

    // On vérifie que l'heure de sortie du véhicule n'est pas null et n'est pas
    // inférieure à l'heure d'entrée
    if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
      throw new IllegalArgumentException(
          "Out time provided is incorrect:" + ticket.getOutTime().toString());
    }
    // On convertit en heures
    double inHour = (ticket.getInTime().getTime()) / 3600000.0;
    double outHour = (ticket.getOutTime().getTime()) / 3600000.0;
    // On calcule la différence afin d'obtenir la durée
    double duree = (outHour - inHour);
    // On convertit notre double en bigDecimal pour la suite des calculs monétaires
    BigDecimal duration = new BigDecimal(duree);
    // On crée une variable représentant la limite d'une demi-heure gratuite
    BigDecimal demiHeure = new BigDecimal("0.5");
    // On test si l'utilisateur est resté moins de 30 minutes
    int test = duration.compareTo(demiHeure);

    // Si notre test == -1 c'est que la durée était bien inférieure à 30 minutes
    // Le Fare.FREE_FARE nous renverra donc un prix à 0 car multiplié par 0
    if (test == -1) {
      duration = Fare.FREE_FARE;
    }

    // On test si la méthode getRecurrent nous revient à true et si oui
    // l'utilisateur bénéficie de la réduction
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
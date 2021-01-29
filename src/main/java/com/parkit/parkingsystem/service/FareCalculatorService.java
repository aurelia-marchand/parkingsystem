package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

     // change int to double et getHours to getTime / 36000000.000 pour conversion
     		// heure.00
     		// essayer classe BigDecimal
     		// int inHour = ticket.getInTime().getHours();
     		// int outHour = ticket.getOutTime().getHours();
     		double inHour = (ticket.getInTime().getTime()) / 3600000.0;
     		double outHour = (ticket.getOutTime().getTime()) / 3600000.0;

     		// TODO: Some tests are failing here. Need to check if this logic is correct
     		double duration = outHour - inHour;
     		
     	// stationnement gratuit moins de 30 minutes
    		if (duration < 0.5) {
    			duration = 0;
    		}

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
    public void calculateFareRecurrent(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		double inHour = (ticket.getInTime().getTime()) / 3600000.0;
		double outHour = (ticket.getOutTime().getTime()) / 3600000.0;
		double duration = outHour - inHour;

		// stationnement gratuit moins de 30 minutes
		if (duration < 0.5) {
			duration = 0;
		}

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice((duration * Fare.CAR_RATE_PER_HOUR)*0.95);
			break;
		}
		case BIKE: {
			ticket.setPrice((duration * Fare.BIKE_RATE_PER_HOUR)*0.95);
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}		
	}
}
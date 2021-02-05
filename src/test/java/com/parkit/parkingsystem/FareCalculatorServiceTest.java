package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

  private static FareCalculatorService fareCalculatorService;
  private Ticket ticket;
  private BigDecimal fortyFiveMinutesTimes = new BigDecimal("0.75");
  private BigDecimal oneDay = new BigDecimal("24");
  private BigDecimal free = new BigDecimal("0");

  @BeforeAll
  private static void setUp() {
    fareCalculatorService = new FareCalculatorService();
  }

  @BeforeEach
  private void setUpPerTest() {
    ticket = new Ticket();
  }

  @Test
  public void calculateFareCar() {
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    
    Date outTime = new Date();
    

    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Boolean recurrent = false;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(recurrent);
    fareCalculatorService.calculateFare(ticket);
    assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
  }

  @Test
  public void calculateFareBike() {
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    Boolean recurrent = false;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(recurrent);
    fareCalculatorService.calculateFare(ticket);
    assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
  }

  @Test
  public void calculateFareUnkownType() {
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
    Boolean recurrent = false;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(recurrent);
    assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
  }

  @Test
  public void calculateFareBikeWithFutureInTime() {
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    Boolean recurrent = false;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(recurrent);
    assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
  }

  @Test
  public void calculateFareBikeWithLessThanOneHourParkingTime() {
    Date inTime = new Date();
    // 45 minutes parking time should give 3/4th parking fare
    inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    Boolean recurrent = false;
    
   System.out.println("calculatefarcar : " + inTime);
   System.out.println("calculatefarcar in time : " + inTime.getTime());
   System.out.println("calculatefarcar out time : " + outTime.getTime());


    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(recurrent);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((Fare.BIKE_RATE_PER_HOUR.multiply(fortyFiveMinutesTimes).setScale(2, RoundingMode.HALF_EVEN)), ticket.getPrice());
  }

  @Test
  public void calculateFareCarWithLessThanOneHourParkingTime() {
    Date inTime = new Date();
    // 45 minutes parking time should give 3/4th parking fare
    inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Boolean recurrent = false;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(recurrent);
    fareCalculatorService.calculateFare(ticket);
        
    
    assertEquals(Fare.CAR_RATE_PER_HOUR.multiply(fortyFiveMinutesTimes).setScale(2, RoundingMode.HALF_EVEN), ticket.getPrice());
  }

  @Test
  public void calculateFareCarWithMoreThanOneDayParkingTime() {
    Date inTime = new Date();
    // 24 hours parking time should give 24 * parking fare per hour
    inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Boolean recurrent = false;
    

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(recurrent);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((Fare.CAR_RATE_PER_HOUR.multiply(oneDay).setScale(2, RoundingMode.HALF_EVEN)), ticket.getPrice());
  }

  // test calculateFareCar et BikeWithLessThan30MinParkingTime
  @Test
  public void calculateFareBikeWithLessThanhalfAnHourParkingTime() {
    Date inTime = new Date();
    // 29 minutes parking time should give free parking fare
    inTime.setTime(System.currentTimeMillis() - (29 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    Boolean recurrent = false;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(recurrent);
    fareCalculatorService.calculateFare(ticket);
    assertEquals(free.setScale(2, RoundingMode.HALF_EVEN), ticket.getPrice());
  }

  @Test
  public void calculateFareCarWithLessThanhalfAnHourParkingTime() {
    Date inTime = new Date();
    // 29 minutes parking time should give free parking fare
    inTime.setTime(System.currentTimeMillis() - (29 * 60 * 1000));
    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Boolean recurrent = false;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(recurrent);
    fareCalculatorService.calculateFare(ticket);
    System.out.println("free : " + free + "car " + "  ticket prix : "+ ticket.getPrice());
    assertEquals(free.setScale(2, RoundingMode.HALF_EVEN), ticket.getPrice());
    

  }

  // tests calculer prix avec reduction utilisateurs recurrent
  @Test
  public void calculateFareBikeRecurrent() {
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));

    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
    Boolean ticketOld = true;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(ticketOld);
    fareCalculatorService.calculateFare(ticket);
    assertEquals((Fare.BIKE_RATE_PER_HOUR.multiply(Fare.REDUCTION_FIVE_POURCENT).setScale(2, RoundingMode.HALF_EVEN)), ticket.getPrice());
  }

  @Test
  public void calculateFareCarRecurrent() {
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));

    Date outTime = new Date();
    ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
    Boolean ticketOld = true;

    ticket.setInTime(inTime);
    ticket.setOutTime(outTime);
    ticket.setParkingSpot(parkingSpot);
    ticket.setRecurrent(ticketOld);
    fareCalculatorService.calculateFare(ticket);
    assertEquals(Fare.CAR_RATE_PER_HOUR.multiply(Fare.REDUCTION_FIVE_POURCENT).setScale(2, RoundingMode.HALF_EVEN), ticket.getPrice());
  }

}

package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

class ParkingSpotTest {

  
  @Test
  public void testGetter() {
    // ARRANGE
    ParkingSpot parkingSpot = new ParkingSpot(3, ParkingType.CAR, true);
    // ACT
    parkingSpot.getParkingType();
    parkingSpot.getId();
    parkingSpot.isAvailable();
    // ASSERT
    assertEquals(ParkingType.CAR, parkingSpot.getParkingType());
    assertEquals(3, parkingSpot.getId());
    assertEquals(true, parkingSpot.isAvailable());


  }

}

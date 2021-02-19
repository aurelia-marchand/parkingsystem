package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.RoundingMode;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
class ParkingFareCalculatorRecurrentDataBaseIT {

  private static ParkingService parkingService;
  private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
  private static ParkingSpotDAO parkingSpotDAO;
  private static TicketDAO ticketDAO;
  private static DataBasePrepareService dataBasePrepareService;
  private static Ticket ticket = new Ticket();

  @Mock
  private static InputReaderUtil inputReaderUtil;

  @BeforeAll
  private static void setUp() throws Exception {
    parkingSpotDAO = new ParkingSpotDAO();
    parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
    ticketDAO = new TicketDAO();
    ticketDAO.dataBaseConfig = dataBaseTestConfig;

  }

  @BeforeEach
  private void setUpPerTest() throws Exception {
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

    dataBasePrepareService = new DataBasePrepareService();
    parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

  }

  @AfterAll
  private static void tearDown() {
    dataBasePrepareService.clearDataBaseEntries();
  }

  @Test
  public void testRecurrentUserInDataBase1() {
    // first time
    parkingService.processIncomingVehicle();
    parkingService.processExitingVehicle();

    // Second time for create recurrent
    parkingService.processIncomingVehicle();

    ticket = ticketDAO.getTicket("ABCDEF");
    Date inTime = new Date();
    inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
    ticket.setInTime(inTime);
    ticketDAO.saveTicket(ticket);
    parkingService.processExitingVehicle();

    Ticket ticket = ticketDAO.getCompletTicket("ABCDEF");

    assertEquals(Fare.CAR_RATE_PER_HOUR.multiply(Fare.REDUCTION_FIVE_POURCENT).setScale(2,
        RoundingMode.HALF_EVEN), ticket.getPrice());
  }

}

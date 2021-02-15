package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
class ParkingSpotDaoIT {

  private static ParkingSpotDAO parkingSpotDAO;

  private static ParkingService parkingService;

  @Mock
  private static InputReaderUtil inputReaderUtil;

  private static TicketDAO ticketDAO;

  private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

  private static DataBasePrepareService dataBasePrepareService;

  @BeforeAll
  private static void setUp() throws Exception {
    parkingSpotDAO = new ParkingSpotDAO();
    parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
    ticketDAO = new TicketDAO();
    ticketDAO.dataBaseConfig = dataBaseTestConfig;

  }

  @BeforeEach
  private void setUpPerTest() throws Exception {
    dataBasePrepareService = new DataBasePrepareService();
  }

  @AfterAll
  private static void tearDown() {
    dataBasePrepareService.clearDataBaseEntries();
  }

  @Test
  public void testGetNextAvailableSlotCarFirstIsOne() throws Exception {
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("CAR01");

    parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

    parkingService.processIncomingVehicle();
    Ticket ticket = ticketDAO.getCompletTicket("CAR01");

    assertEquals(1, ticket.getParkingSpot().getId());
  }

  @Test
  public void testGetNextAvailableSlotBikeFirstIsFour() throws Exception {
    when(inputReaderUtil.readSelection()).thenReturn(2);
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("BIKE01");

    parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

    parkingService.processIncomingVehicle();
    Ticket ticket = ticketDAO.getCompletTicket("BIKE01");
    assertEquals(4, ticket.getParkingSpot().getId());

  }

}

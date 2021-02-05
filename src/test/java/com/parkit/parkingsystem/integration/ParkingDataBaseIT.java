package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDao;
import com.parkit.parkingsystem.dao.TicketDao;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

  private static ParkingService parkingService;
  private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
  private static ParkingSpotDao parkingSpotDAO;
  private static TicketDao ticketDAO;
  private static DataBasePrepareService dataBasePrepareService;
  //private static Ticket ticket;

  @Mock
  private static InputReaderUtil inputReaderUtil;
  


  @BeforeAll
  private static void setUp() throws Exception {
    parkingSpotDAO = new ParkingSpotDao();
    parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
    ticketDAO = new TicketDao();
    ticketDAO.dataBaseConfig = dataBaseTestConfig;
    dataBasePrepareService = new DataBasePrepareService();
    


  }

  @BeforeEach
  private void setUpPerTest() throws Exception {
    when(inputReaderUtil.readSelection()).thenReturn(1);
    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
    
    //Mockito.doReturn(new Date(System.currentTimeMillis() - (3*60 * 60 * 1000))).when(ticket.setOutTime());
    

    parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
  }

  @AfterAll
  private static void tearDown() {
    dataBasePrepareService.clearDataBaseEntries();
  }


  @Test
  public void testParkingACar() {
    parkingService.processIncomingVehicle();
    // check that a ticket is actualy saved in DB and Parking table is updated
    // with availability
    // j'utilise la méthode créer pour savoir si un utilisateur est déjà venu, me renvoi true si le
    // ticket existe et donc cela veut dire qu'il est bien enregistré
    assertThat(ticketDAO.getOldTicket("ABCDEF")).isTrue();
    // je récupère le ticket et la place de parking pour vérifier que la place est bien passée à false
    Ticket ticket = ticketDAO.getTicket("ABCDEF");
    ParkingSpot parkingSpot = ticket.getParkingSpot();
    assertThat(parkingSpot.isAvailable()).isFalse();

  }

  @Test
  public void testParkingLotExit() throws InterruptedException {
    parkingService.processIncomingVehicle();
    
    //when(ticket.getOutTime().getTime()).thenReturn(System.currentTimeMillis() + (3*60 * 60 * 1000));
    //ticket.setInTime(new Date(System.currentTimeMillis() - (3*60 * 60 * 1000)));
    parkingService.processExitingVehicle();
    
    // check that the fare generated and out time are populated correctly in
    // the database
    Ticket ticket = ticketDAO.getCompletTicket("ABCDEF");
    BigDecimal price = ticket.getPrice();
    Date outTime = ticket.getOutTime();
    // problème, le test est trop court et le prix reste à 0
    // problème : assertion compare primitives with null
    assertThat(price).isNotNull();
    assertThat(outTime).isNotNull();

  }

}

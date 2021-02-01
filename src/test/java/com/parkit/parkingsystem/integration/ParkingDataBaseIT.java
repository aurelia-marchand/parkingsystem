package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static ParkingService parkingService;
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
		

	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
	
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
		assertThat(ticketDAO.getOldTicket("ABCDEF")).isTrue();
		Ticket ticket = ticketDAO.getTicket("ABCDEF");
		System.out.println("ticket11 : " + ticket);
		ParkingSpot parkingSpot = ticket.getParkingSpot();
		assertThat(parkingSpot.isAvailable()).isFalse();

	}

	
	@Test
	public void testParkingLotExit() throws InterruptedException {
		parkingService.processIncomingVehicle();
		Thread.sleep(2000);
		parkingService.processExitingVehicle();
		// check that the fare generated and out time are populated correctly in
		// the database
		Ticket ticket = ticketDAO.getCompletTicket("ABCDEF");
		double price = ticket.getPrice();
		Date outTime = ticket.getOutTime();

		assertThat(price).isNotNull();
		assertThat(outTime).isNotNull();

	}

}

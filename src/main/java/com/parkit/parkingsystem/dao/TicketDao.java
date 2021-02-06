package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDao {

  private static final Logger logger = LogManager.getLogger("TicketDAO");

  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  /**
   * 
   * Save ticket in first time
   * 
   * @param ticket ticket with spot number, vehicle registration number, price, in time, out time
   * @return boolean
   */
  public boolean saveTicket(Ticket ticket) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.SAVE_TICKET);
      // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
      // ps.setInt(1,ticket.getId());
      ps.setInt(1, ticket.getParkingSpot().getId());
      ps.setString(2, ticket.getVehicleRegNumber());
      ps.setBigDecimal(3, ticket.getPrice());
      ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
      ps.setTimestamp(5,
          (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
      ps.setBoolean(6, (ticket.getRecurrent()));
      return ps.execute();
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
      return false;
    } finally {
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);

    }
  }

  /**
   * 
   * Get ticket before exiting
   * 
   * @param vehicleRegNumber
   * @return ticket
   */
  public Ticket getTicket(String vehicleRegNumber) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Ticket ticket = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.GET_TICKET);
      // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
      ps.setString(1, vehicleRegNumber);
      rs = ps.executeQuery();
      if (rs.next()) {
        ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1),
            ParkingType.valueOf(rs.getString(6)), false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setId(rs.getInt(2));
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(rs.getBigDecimal(3));
        ticket.setInTime(rs.getTimestamp(4));
        ticket.setOutTime(rs.getTimestamp(5));
        ticket.setRecurrent(rs.getBoolean(7));
      }
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);

    }
    return ticket;
  }

  /**
   * 
   * Check existing ticket
   * 
   * @param ticket
   * @return true if exist
   */
  public boolean updateTicket(Ticket ticket) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
      ps.setBigDecimal(1, ticket.getPrice());
      ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
      ps.setInt(3, ticket.getId());
      ps.execute();
      return true;
    } catch (Exception ex) {
      logger.error("Error saving ticket info", ex);
    } finally {
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
    return false;
  }

  /**
   * 
   * Check old ticket for calculate recurrent user
   * 
   * @param vehicleRegNumber
   * @return boolean true if recurrent
   * 
   * @see ParkingService.processIncomingVehicle()
   */
  public boolean getOldTicket(String vehicleRegNumber) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.GET_OLD_TICKET);
      ps.setString(1, vehicleRegNumber);
      rs = ps.executeQuery();
      if (rs.next()) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      logger.error("Error get old ticket", ex);
      return false;
    } finally {
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
  }

  /**
   * 
   * @param vehicleRegNumber
   * @return complete ticket with all information needed for testing
   */
  public Ticket getCompletTicket(String vehicleRegNumber) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Ticket ticket = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.GET_COMPLET_TICKET);
      // ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
      ps.setString(1, vehicleRegNumber);
      rs = ps.executeQuery();
      if (rs.next()) {
        ticket = new Ticket();
        ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1),
            ParkingType.valueOf(rs.getString(6)), false);
        ticket.setParkingSpot(parkingSpot);
        ticket.setId(rs.getInt(2));
        ticket.setVehicleRegNumber(vehicleRegNumber);
        ticket.setPrice(rs.getBigDecimal(3));
        ticket.setInTime(rs.getTimestamp(4));
        ticket.setOutTime(rs.getTimestamp(5));
      }
    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);

    }
    return ticket;
  }

}

package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDao {
  private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

  public DataBaseConfig dataBaseConfig = new DataBaseConfig();

  /**
   * The use of parking available
   * 
   * @param parkingType Car or Bike
   * @return parking spot number
   * 
   * @see com.parkit.parkingsystem.service.ParkingService#getNextParkingNumberIfAvailable()
   * 
   *
   */
  public int getNextAvailableSlot(ParkingType parkingType) {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    int result = -1;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
      ps.setString(1, parkingType.toString());
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getInt(1);
        ;
      }

    } catch (Exception ex) {
      logger.error("Error fetching next available slot", ex);
    } finally {
      dataBaseConfig.closeResultSet(rs);
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
    return result;
  }

  /**
   * Update the availability of that parking slot
   * 
   * @param parkingSpot parking spot number
   * 
   * @see com.parkit.parkingsystem.service.ParkingService#processIncomingVehicle()
   * @see com.parkit.parkingsystem.service.ParkingService#processExitingVehicle()
   */
  public boolean updateParking(ParkingSpot parkingSpot) {
    Connection con = null;
    PreparedStatement ps = null;
    try {
      con = dataBaseConfig.getConnection();
      ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
      ps.setBoolean(1, parkingSpot.isAvailable());
      ps.setInt(2, parkingSpot.getId());
      int updateRowCount = ps.executeUpdate();
      dataBaseConfig.closePreparedStatement(ps);
      return (updateRowCount == 1);
    } catch (Exception ex) {
      logger.error("Error updating parking info", ex);
      return false;
    } finally {
      dataBaseConfig.closePreparedStatement(ps);
      dataBaseConfig.closeConnection(con);
    }
  }

}

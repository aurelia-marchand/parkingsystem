package com.parkit.parkingsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBaseConfig {

  private static final Logger logger = LogManager.getLogger("DataBaseConfig");
  private String url = "jdbc:mysql://localhost:3306/prod?serverTimezone=Europe/Paris";
  private String user = "root";
  private String password = "rootroot";

  /**
   * Connection to the DataBase
   * 
   * @return a connection to the URL
   * @throws ClassNotFoundException - when no definition of the class with the
   *                                specified name could be found
   * @throws SQLException           - if a database access error occurs or the url
   *                                is null
   */
  public Connection getConnection() throws ClassNotFoundException, SQLException {
    logger.info("Create DB connection");
    Class.forName("com.mysql.cj.jdbc.Driver");

    return DriverManager.getConnection(url, user, password);
  }

  /**
   * Close connection to the DataBase
   * 
   * @param con
   */
  public void closeConnection(Connection con) {
    if (con != null) {
      try {
        con.close();
        logger.info("Closing DB connection");
      } catch (SQLException e) {
        logger.error("Error while closing connection", e);
      }
    }
  }

  /**
   * Close prepared Statement
   * 
   * @param ps
   */
  public void closePreparedStatement(PreparedStatement ps) {
    if (ps != null) {
      try {
        ps.close();
        logger.info("Closing Prepared Statement");
      } catch (SQLException e) {
        logger.error("Error while closing prepared statement", e);
      }
    }
  }

  /**
   * Close Result Set
   * 
   * @param rs
   */
  public void closeResultSet(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
        logger.info("Closing Result Set");
      } catch (SQLException e) {
        logger.error("Error while closing result set", e);
      }
    }
  }
}

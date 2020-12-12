package DAO;

import Model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.logging.Logger;

/**
 * FLD DB class collects a list of FLD objects from DB
 */
public class FirstLevelDivisionDB {
  public static ObservableList<FirstLevelDivision> allFirstLevelDivisions = FXCollections.observableArrayList();

  //Create a list of all appointments
  public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() throws SQLException {
    allFirstLevelDivisions.clear();
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rb = conn.createStatement().executeQuery("SELECT * FROM first_level_divisions");
      while (rb.next()) {
        allFirstLevelDivisions.add(new FirstLevelDivision(
                rb.getInt("Division_ID"),
                rb.getString("Division"),
                rb.getTimestamp("Create_Date").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                rb.getString("Created_By"),
                rb.getTimestamp("Last_Update").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                rb.getString("Last_Updated_By"),
                rb.getInt("Country_ID")));
      }
      return allFirstLevelDivisions;
    } catch (SQLException e) {
      Logger.getLogger(e.toString());
    }
    return null;
  }
  public static ObservableList<FirstLevelDivision> usFilteredFirstLevelDivisions = FXCollections.observableArrayList();

  //Create a list of all appointments
  public static ObservableList<FirstLevelDivision> getUSFilteredFirstLevelDivisions() throws SQLException {
    usFilteredFirstLevelDivisions.clear();
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rb = conn.createStatement().executeQuery("SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 1");
      while (rb.next()) {
        usFilteredFirstLevelDivisions.add(new FirstLevelDivision(
                rb.getInt("Division_ID"),
                rb.getString("Division"),
                rb.getTimestamp("Create_Date").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                rb.getString("Created_By"),
                rb.getTimestamp("Last_Update").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                rb.getString("Last_Updated_By"),
                rb.getInt("Country_ID")));
      }
      return usFilteredFirstLevelDivisions;
    } catch (SQLException e) {
      Logger.getLogger(e.toString());
    }
    return null;
  }

  public static ObservableList<FirstLevelDivision> ukFilteredFirstLevelDivisions = FXCollections.observableArrayList();

  //Create a list of all appointments
  public static ObservableList<FirstLevelDivision> getUKFilteredFirstLevelDivisions() throws SQLException {
    ukFilteredFirstLevelDivisions.clear();
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rb = conn.createStatement().executeQuery("SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 2");
      while (rb.next()) {
        ukFilteredFirstLevelDivisions.add(new FirstLevelDivision(
                rb.getInt("Division_ID"),
                rb.getString("Division"),
                rb.getTimestamp("Create_Date").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                rb.getString("Created_By"),
                rb.getTimestamp("Last_Update").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                rb.getString("Last_Updated_By"),
                rb.getInt("Country_ID")));
      }
      return ukFilteredFirstLevelDivisions;
    } catch (SQLException e) {
      Logger.getLogger(e.toString());
    }
    return null;
  }

  public static ObservableList<FirstLevelDivision> canadaFilteredFirstLevelDivisions = FXCollections.observableArrayList();

  //Create a list of all appointments
  public static ObservableList<FirstLevelDivision> getCanadaFilteredFirstLevelDivisions() throws SQLException {
    canadaFilteredFirstLevelDivisions.clear();
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rb = conn.createStatement().executeQuery("SELECT * FROM first_level_divisions WHERE COUNTRY_ID = 3");
      while (rb.next()) {
        canadaFilteredFirstLevelDivisions.add(new FirstLevelDivision(
                rb.getInt("Division_ID"),
                rb.getString("Division"),
                rb.getTimestamp("Create_Date").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                rb.getString("Created_By"),
                rb.getTimestamp("Last_Update").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                rb.getString("Last_Updated_By"),
                rb.getInt("Country_ID")));
      }
      return canadaFilteredFirstLevelDivisions;
    } catch (SQLException e) {
      Logger.getLogger(e.toString());
    }
    return null;
  }

}

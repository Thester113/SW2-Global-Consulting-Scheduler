package DAO;

import Model.Appointment;
import Model.Contacts;
import Model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ReportDB {
  private static Contacts newContactSchedule;
  private static Customers newCustomerSchedule;


  public static void sendContactSelection(Contacts contactSchedule) {
    newContactSchedule = contactSchedule;
  }

  public static ObservableList<Appointment> contactSchedule = FXCollections.observableArrayList();


  public static ObservableList<Appointment> getContactSchedule() throws SQLException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    contactSchedule.clear();
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rb = conn.createStatement().executeQuery("SELECT * FROM appointments WHERE Contact_ID=" + newContactSchedule.getContactID());
      if (rb.next()) {
        do {
          contactSchedule.add(new Appointment(
                  rb.getInt("Appointment_ID"),
                  rb.getString("Title"),
                  rb.getString("Description"),
                  rb.getString("Type"),
                  rb.getTimestamp("Start").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                  rb.getTimestamp("End").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                  rb.getInt("Customer_ID")));

        } while (rb.next());
      }
      return contactSchedule;
    } catch (SQLException e) {
      java.util.logging.Logger.getLogger(e.toString());
    }
    return null;
  }

  public static void sendCustomerSelection(Customers customerSchedule) {
    newCustomerSchedule = customerSchedule;
  }

  public static ObservableList<Appointment> customerSchedule = FXCollections.observableArrayList();


  public static ObservableList<Appointment> getCustomerSchedule() throws SQLException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    customerSchedule.clear();
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rb = conn.createStatement().executeQuery("SELECT * FROM appointments WHERE Customer_ID=" + newCustomerSchedule.getCustomerID());
      if (rb.next()) {
        do {
          customerSchedule.add(new Appointment(
                  rb.getInt("Appointment_ID"),
                  rb.getString("Title"),
                  rb.getString("Description"),
                  rb.getString("Type"),
                  rb.getTimestamp("Start").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                  rb.getTimestamp("End").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                  rb.getInt("Customer_ID")));

        } while (rb.next());
      }
      return customerSchedule;
    } catch (SQLException e) {
      java.util.logging.Logger.getLogger(e.toString());
    }
    return null;
  }
}

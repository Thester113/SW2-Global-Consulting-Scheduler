package DAO;
/**
 * Class is used to generate reports for customers and contacts
 */

import Model.Appointments;
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

  /**
   * From the selection of the ComboBox from the Contact Schedules Report
   */
  public static void sendContactSelection(Contacts contactSchedule) {
    newContactSchedule = contactSchedule;
  }

  public static ObservableList<Appointments> contactSchedule = FXCollections.observableArrayList();


  public static ObservableList<Appointments> getContactSchedule() throws SQLException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    contactSchedule.clear();
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet resultBack = conn.createStatement().executeQuery("SELECT * FROM appointments WHERE Contact_ID=" + newContactSchedule.getContactID());
      if (resultBack.next()) {
        do {
          contactSchedule.add(new Appointments(
                  resultBack.getInt("Appointment_ID"),
                  resultBack.getString("Title"),
                  resultBack.getString("Description"),
                  resultBack.getString("Type"),
                  resultBack.getTimestamp("Start").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                  resultBack.getTimestamp("End").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                  resultBack.getInt("Customer_ID")));

        } while (resultBack.next());
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

  public static ObservableList<Appointments> customerSchedule = FXCollections.observableArrayList();


  public static ObservableList<Appointments> getCustomerSchedule() throws SQLException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    customerSchedule.clear();
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet resultBack = conn.createStatement().executeQuery("SELECT * FROM appointments WHERE Customer_ID=" + newCustomerSchedule.getCustomerID());
      if (resultBack.next()) {
        do {
          customerSchedule.add(new Appointments(
                  resultBack.getInt("Appointment_ID"),
                  resultBack.getString("Title"),
                  resultBack.getString("Description"),
                  resultBack.getString("Type"),
                  resultBack.getTimestamp("Start").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                  resultBack.getTimestamp("End").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                  resultBack.getInt("Customer_ID")));

        } while (resultBack.next());
      }
      return customerSchedule;
    } catch (SQLException e) {
      java.util.logging.Logger.getLogger(e.toString());
    }
    return null;
  }
}

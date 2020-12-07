package DAO;

import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class AppointmentDB {


    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();


    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        allAppointments.clear();
        try {
            Connection connection = DBConnection.startConnection();
            ResultSet rb = connection.createStatement().executeQuery("SELECT * FROM appointments");
            while (rb.next()) {
                allAppointments.add(new Appointment(
                        rb.getInt("Appointment_ID"),
                        rb.getString("Title"),
                        rb.getString("Description"),
                        rb.getString("Location"),
                        rb.getString("Type"),
                        rb.getTimestamp("Start").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                        rb.getTimestamp("End").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                        rb.getTimestamp("Create_Date").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                        rb.getString("Created_By"),
                        rb.getTimestamp("Last_Update").toInstant().atOffset(ZoneOffset.from(ZonedDateTime.now())).toLocalDateTime(),
                        rb.getString("Last_Updated_By"),
                        rb.getInt("Customer_ID"),
                        rb.getInt("User_ID"),
                        rb.getInt("Contact_ID")));
            }
            return allAppointments;
        } catch (SQLException e) {
            Logger.getLogger(e.toString());
        }
        return null;
    }


    public static boolean editAppointment(Integer appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, Integer customerID, Integer userID, Integer contactID) throws SQLException {
        try {
            Statement statement = DBConnection.startConnection().createStatement();
            statement.executeUpdate("UPDATE appointments SET Title='"+title+"',Description='"+description+"', Location='"+location+"', Type='"+type+"', Start='"+start+"', End='"+end+"', Create_Date='"+createDate+"',  Created_By='"+createdBy+"', Last_Update='"+lastUpdate+"', Last_Updated_By='"+lastUpdatedBy+"', Customer_ID='"+customerID+"', User_ID='"+userID+"', Contact_ID="+contactID+" WHERE Appointment_ID ="+appointmentID);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean addAppointment(Integer appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, Integer customerID, Integer userID, Integer contactID) {

        try {
            Statement statement = DBConnection.startConnection().createStatement();
            statement.executeUpdate("INSERT INTO appointments SET Appointment_ID='"+appointmentID+"',Title='"+title+"',Description='"+description+"', Location='"+location+"', Type='"+type+"', Start='"+start+"', End='"+end+"', Create_Date='"+createDate+"',  Created_By='"+createdBy+"', Last_Update='"+lastUpdate+"', Last_Updated_By='"+lastUpdatedBy+"', Customer_ID='"+customerID+"', User_ID='"+userID+"', Contact_ID="+contactID);
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteAppointment(int id) {

        try {
            Statement statement = DBConnection.startConnection().createStatement();
            String query = "DELETE FROM appointments WHERE Appointment_ID=" + id;
            statement.executeUpdate(query);
            System.out.println(query);

        } catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
}
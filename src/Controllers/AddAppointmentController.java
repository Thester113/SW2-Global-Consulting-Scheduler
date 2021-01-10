package Controllers;
/**
 * AddAppointmentController is used to add a new appointment to the DB
 */

import DAO.AppointmentDB;
import DAO.DBConnection;
import DAO.Logger;
import Model.Appointments;
import Model.Contacts;
import Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static java.lang.Integer.valueOf;
/**
 * AddAppointmentController is used to add a new appointment to the DB
 */

public class AddAppointmentController implements Initializable {
    /**
     * This is the formatter for properly setting the DTG
     * Using offsetToUTC to display the time difference between the users OS time zone and UTC
     */

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    Long offsetToUTC = (long) (ZonedDateTime.now().getOffset()).getTotalSeconds();
    ObservableList<Contacts> contactList = FXCollections.observableArrayList();
    @FXML
    private TextField aptIDtext;
    @FXML
    private TextField aptTitleText;
    @FXML
    private TextField aptDescrText;
    @FXML
    private TextField aptLocText;
    @FXML
    private TextField aptTypeText;
    @FXML
    private TextField aptCreateByText;
    @FXML
    private TextField aptLstUpdByText;
    @FXML
    private TextField aptCustIDText;
    @FXML
    private TextField aptUIDText;
    @FXML
    private TextField aptContIDText;
    @FXML
    private TextField aptStartText;
    @FXML
    private TextField aptEndText;
    @FXML
    private TextField aptCreateDateText;
    @FXML
    private TextField aptLastUpdateText;
    @FXML
    private ComboBox<Contacts> contactName;
    @FXML
    private Button AddAppointmentBtn;
    @FXML
    private Button ExitBtn;

    /**
     * Using the comboBox adds the contacts to the list
     *
     * @throws SQLException
     */


    public AddAppointmentController() throws SQLException {

        try {
            Connection conn = DBConnection.startConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM contacts");
            if (rs.next()) {
                do {

                    contactList.add(new Contacts(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));
                } while (rs.next());
            }
        } catch (SQLException ce) {
            Logger.getLogger(ce.toString());
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         *Created By field is auto-populated with the value of a user login that is valid
         */

        aptCreateByText.setText(String.valueOf(Users.getUserName()));
        aptLstUpdByText.setText(String.valueOf(Users.getUserName()));
        try {
            /**
             * Connects to the DB
             */

            Connection conn = DBConnection.startConnection();
            /**
             * Uses the Max appointment ID and sets it as the highestID with the var tempHighID
             */

            ResultSet rs = conn.createStatement().executeQuery("SELECT MAX(Appointment_ID) AS highestID FROM appointments");
            while (rs.next()) {

                int tempHighID = rs.getInt("highestID");
                /**
                 * Increments tempHighID by 1
                 */

                aptIDtext.setText(String.valueOf(tempHighID + 1));
                System.out.println(rs.getInt(tempHighID));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        contactName.setItems(contactList);
    }

    /**
     *  Sets the ID field for an appointment based on the choices of the combobox using the list of contacts.
     * @param event ActionEvent
     * @throws IOException
     */

    @FXML
    void SetContactID(ActionEvent event) throws IOException {
        if (contactName.getSelectionModel().isEmpty()) {
        } else {
            Contacts c = contactName.getSelectionModel().getSelectedItem();
            aptContIDText.setText(String.valueOf(c.getContactID()));
        }
    }

    @FXML
    void ExitToMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/Views/Appointment.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Times entered by the user will default to local tz and then based of the time of the user we get from the offset variable it will set the time to UTC for storage in the DB
     * Alerts have been set up based off of the requirements section
     * @param event
     * @see AppointmentDB#addAppointment(Integer, String, String, String, String, LocalDateTime, LocalDateTime, LocalDateTime, String, LocalDateTime, String, Integer, Integer, Integer)
     * @return
     * @throws IOException
     * @throws SQLException
     */

    @FXML
    boolean OnActionAddAppointment(ActionEvent event) throws IOException, SQLException {
        try {

            //Gets users timezone and OffSets


            TimeZone est = TimeZone.getTimeZone("est");
            long offsetToEST = est.getOffset(new Date().getTime()) / 1000 / 60;
            Integer appointmentID = valueOf(aptIDtext.getText());
            String title = aptTitleText.getText();
            String description = aptDescrText.getText();
            String location = aptLocText.getText();
            String type = aptTypeText.getText();
            //Works when going behind, uses EST as per requirements

            LocalDateTime start = LocalDateTime.parse(aptStartText.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            LocalDateTime end = LocalDateTime.parse(aptEndText.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            LocalDateTime createDate = LocalDateTime.parse(aptCreateDateText.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            String createdBy = aptCreateByText.getText();
            LocalDateTime lastUpdate = LocalDateTime.parse(aptLastUpdateText.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            String lastUpdatedBy = aptLstUpdByText.getText();
            Integer customerID = valueOf(aptCustIDText.getText());
            Integer userID = valueOf(aptUIDText.getText());
            Integer contactID = valueOf(aptContIDText.getText());

            /**
             * Compare Local time to Business hours converts. This converts the hours.
             *          Get local time of User and Convert to UTC
             */

            LocalDateTime startTime = LocalDateTime.parse(aptStartText.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));

            /**
             * Sets the start time to EST
             */

            startTime = startTime.plus(Duration.ofMinutes(offsetToEST));

            /**
             *Gets Users local time and converts to UTC
             */

            LocalDateTime endTime = LocalDateTime.parse(aptEndText.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));

            /**
             *Set to EST
             */

            endTime = endTime.plus(Duration.ofMinutes(offsetToEST));

            /**
             * Check for 8-22 business hours startTime and endTime
             */


            LocalTime businessHoursStart = LocalTime.of(8, 00);
            LocalTime businessHoursEnd = LocalTime.of(22, 00);
            /**
             * Check if time falls between other appointments and avoids conflict
             */

            LocalDateTime startDateTime = LocalDateTime.parse(aptStartText.getText(), formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(aptEndText.getText(), formatter);

            /**
             * Uses observable list to make sure sure no overlapping in appointments
             */


            ObservableList<Appointments> allAppointments = AppointmentDB.allAppointments;
            for (int i = 0, allAppointmentsSize = allAppointments.size(); i < allAppointmentsSize; i++) {
                Appointments appointments = allAppointments.get(i);
                if ((startDateTime.isEqual(appointments.getStart()) || startDateTime.isAfter(appointments.getStart()) && startDateTime.isBefore(appointments.getEnd()))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("CONFLICT OF TIME");
                    alert.setContentText("Please enter a time for the start and end time of the appointment that is not already taken");
                    alert.showAndWait();
                    return false;
                }
            }

            if (startTime.toLocalTime().isBefore(businessHoursStart) || endTime.toLocalTime().isAfter(businessHoursEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("TIME NOT AVAILABLE");
                alert.setContentText("Please enter a time after business opening hour of 0800 EST and before business closing hours of 1000 EST");
                alert.showAndWait();

            } else if (!title.equals("") && !type.equals("") && !description.equals("") && !location.equals("")) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Views/Appointment.fxml"));
                Parent parent = loader.load();

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();

                return AppointmentDB.addAppointment(
                        appointmentID,
                        title,
                        description,
                        location,
                        type, start,
                        end,
                        createDate,
                        createdBy,
                        lastUpdate,
                        lastUpdatedBy,
                        customerID,
                        userID,
                        contactID);
            }

        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing selection");
            alert.setContentText("Please ensure all date and time fields are formatted YYYY-MM-DD HH:MM prior to adding an appointment");
            alert.showAndWait();
        }
        return false;
    }

}

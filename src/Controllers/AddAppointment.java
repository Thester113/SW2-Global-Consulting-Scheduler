package Controllers;

import DAO.AppointmentDB;
import DAO.DBConnection;
import Model.Appointment;
import Model.Contacts;
import Model.User;
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
import java.util.logging.Logger;

import static java.lang.Integer.valueOf;

public class AddAppointment implements Initializable {
    /**
     * formatter for configuring the DTG to be set properly and getters.
     * offsetToUTC is used to get the time difference between UTC and the user's operating system time zone
     */
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    Long offsetToUTC = Long.valueOf((ZonedDateTime.now().getOffset()).getTotalSeconds());

    @FXML
    private TextField aptIDtxt;

    @FXML
    private TextField aptTitleTxt;

    @FXML
    private TextField aptDescrTxt;

    @FXML
    private TextField aptLocTxt;

    @FXML
    private TextField aptTypeTxt;

    @FXML
    private TextField aptCreateByTxt;

    @FXML
    private TextField aptLstUpdByTxt;

    @FXML
    private TextField aptCustIDTxt;

    @FXML
    private TextField aptUIDTxt;

    @FXML
    private TextField aptContIDTxt;

    @FXML
    private TextField aptStartTxt;

    @FXML
    private TextField aptEndTxt;

    @FXML
    private TextField aptCreateDateTxt;

    @FXML
    private TextField aptLastUpdateTxt;

    @FXML
    private ComboBox<Contacts> contactName;

    @FXML
    private Button AddAppointmentBtn;

    @FXML
    private Button ExitBtn;

    /**
     * Used to set the ID field for an appointment based on the selection of the combobox that has the list of contacts
     * @param event
     * @throws IOException
     */
    @FXML
    private void SetContactID (ActionEvent event) throws IOException {
        if (contactName.getSelectionModel().isEmpty()) {
            return;
        }
        else {
            Contacts c = contactName.getSelectionModel().getSelectedItem();
            aptContIDTxt.setText(String.valueOf(c.getContactID()));
        }
    }

    @FXML
    void ExitToMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/AppointmentMain.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    ObservableList<Contacts> contactList = FXCollections.observableArrayList();

    /**
     * All times entered by the user will be assumed local and then based of users time that we get from the offset variable it will set the time to UTC for storage in the DB
     * There are a few alerts set up that are detailed below based off of the requirements section
     * @param event
     * @see AppointmentDB#addAppointment(Integer, String, String, String, String, LocalDateTime, LocalDateTime, LocalDateTime, String, LocalDateTime, String, Integer, Integer, Integer)
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    boolean OnActionAddAppointment(ActionEvent event) throws IOException, SQLException {
        try {
            //get the users TimeZone offsetToUTC to

            TimeZone est = TimeZone.getTimeZone("America/New_York");
            Long offsetToEST = Long.valueOf(est.getOffset(new Date().getTime()) /1000 /60);
            Integer appointmentID = valueOf(aptIDtxt.getText());
            String title = aptTitleTxt.getText();
            String description = aptDescrTxt.getText();
            String location = aptLocTxt.getText();
            String type = aptTypeTxt.getText();
            //Works when going behind, used my current TZ EST and ahead, used India Standard Time which is 5:30 ahead of UTC
            LocalDateTime start = LocalDateTime.parse(aptStartTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            LocalDateTime end = LocalDateTime.parse(aptEndTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            LocalDateTime createDate = LocalDateTime.parse(aptCreateDateTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            String createdBy = aptCreateByTxt.getText();
            LocalDateTime lastUpdate = LocalDateTime.parse(aptLastUpdateTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            String lastUpdatedBy = aptLstUpdByTxt.getText();
            Integer customerID = valueOf(aptCustIDTxt.getText());
            Integer userID = valueOf(aptUIDTxt.getText());
            Integer contactID = valueOf(aptContIDTxt.getText());
            /**
             * Compare Local time to Business hours convert text field to z and set business hours to z time
             *             Get the time entered (user local) and set it to utc
             */
            LocalDateTime startTime = LocalDateTime.parse(aptStartTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            /**
             * Set the start time to EST
             */
            startTime = startTime.plus(Duration.ofMinutes(offsetToEST));
            /**
             *Get the time entered (user local) and set it to utc
             */
            LocalDateTime endTime = LocalDateTime.parse(aptEndTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            /**
             *Set the end time to EST
             */
            endTime = endTime.plus(Duration.ofMinutes(offsetToEST));

            /**
             * Compare startTime and endTime between business hours of 8-22
             */

            LocalTime businessHoursStart = LocalTime.of(8, 00);
            LocalTime businessHoursEnd = LocalTime.of(22, 00);

            /**
             * Use to check if date time falls between other scheduled appointments
             */
            LocalDateTime startDateTime = LocalDateTime.parse(aptStartTxt.getText(), formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(aptEndTxt.getText(), formatter);

            /**
             * Check for overlapping appointment times
             */

            for (Appointment appointment : AppointmentDB.allAppointments) {
                if((startDateTime.isEqual(appointment.getStart()) || startDateTime.isAfter(appointment.getStart()) && startDateTime.isBefore(appointment.getEnd()))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("CONFLICT");
                    alert.setContentText("Please enter a time for the start and end time of the appointment that is not already taken");
                    alert.showAndWait();
                    return false;
                }
            }

            /**
             * Check if time of start and end are within the business hours
             */

            if (startTime.toLocalTime().isBefore(businessHoursStart) || endTime.toLocalTime().isAfter(businessHoursEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("TOO EARLY!");
                alert.setContentText("Please enter a time after business opening hour of 0800 EST and before business closing hours of 1000 EST");
                alert.showAndWait();

            } else if (!title.equals("") && !type.equals("") && !description.equals("") && !location.equals("")) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/View/AppointmentMain.fxml"));
                Parent parent = loader.load();

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();

                return AppointmentDB.addAppointment(appointmentID, title, description, location, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, customerID, userID, contactID);}

        }
        catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing selection");
            alert.setContentText("Please ensure all date and time fields are formatted YYYY-MM-DD HH:MM prior to adding an appointment");
            alert.showAndWait();
        }
        return false;
    }

    /**
     * Adding contacts to a list to be used in the combobox
     * @throws SQLException
     */
    public AddAppointment() throws SQLException {

        try {
            Connection conn = DBConnection.startConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM contacts");
            while (rs.next()) {
                /**
                 * Create Country Objects vice Strings for Country selection
                 *                 columnLabel corresponds to Column! not the attribute of the object
                 */
                contactList.add(new Contacts(rs.getInt("Contact_ID"),rs.getString("Contact_Name"),rs.getString("Email")));
            }
        } catch (SQLException ce) {
            Logger.getLogger(ce.toString());
        }
    }



    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         *Auto-populate Created By field with the value of a valid user log in that is stored in the User object
         */
        aptCreateByTxt.setText(String.valueOf(User.getUsername()));
        aptLstUpdByTxt.setText(String.valueOf(User.getUsername()));
        try {
            /**
             * Connection to the database
             */
            Connection conn = DBConnection.startConnection();
            /**
             * Select the max Appointment ID from appointments table and set it as highestID
             */
            ResultSet rs = conn.createStatement().executeQuery("SELECT MAX(Appointment_ID) AS highestID FROM appointments");
            while (rs.next()) {
                /**
                 * Create a temporary var for appointment ID
                 */
                int tempID = rs.getInt("highestID");
                /**
                 * Set the temp var appointment ID to  increment by 1
                 */
                aptIDtxt.setText(String.valueOf(tempID + 1));
                System.out.println(rs.getInt(tempID));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        contactName.setItems(contactList);
    }
}

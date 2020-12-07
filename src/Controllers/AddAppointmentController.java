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
import javafx.scene.input.MouseEvent;
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

public class AddAppointmentController implements Initializable {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-mm-dd");
    Long offsetToUTC = (long) (ZonedDateTime.now().getOffset()).getTotalSeconds();

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

    @FXML
    private void SetContactID (MouseEvent event) throws IOException {
        if (contactName.getSelectionModel().isEmpty()) {
            return;
        } else {
            Contacts c = contactName.getSelectionModel().getSelectedItem();
            aptContIDTxt.setText(String.valueOf(c.getContactID()));
        }
    }

    @FXML
    void ExitToMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/Appointment.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    ObservableList<Contacts> contactList = FXCollections.observableArrayList();

    @FXML
    boolean OnActionAddAppointment(ActionEvent event) throws IOException, SQLException {
        try {


            TimeZone est = TimeZone.getTimeZone("America/New_York");
            long offsetToEST = (long) (est.getOffset(new Date().getTime()) / 1000 / 60);
            Integer appointmentID = valueOf(aptIDtxt.getText());
            String title = aptTitleTxt.getText();
            String description = aptDescrTxt.getText();
            String location = aptLocTxt.getText();
            String type = aptTypeTxt.getText();

            LocalDateTime start = LocalDateTime.parse(aptStartTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            LocalDateTime end = LocalDateTime.parse(aptEndTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            LocalDateTime createDate = LocalDateTime.parse(aptCreateDateTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            String createdBy = aptCreateByTxt.getText();
            LocalDateTime lastUpdate = LocalDateTime.parse(aptLastUpdateTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
            String lastUpdatedBy = aptLstUpdByTxt.getText();
            Integer customerID = valueOf(aptCustIDTxt.getText());
            Integer userID = valueOf(aptUIDTxt.getText());
            Integer contactID = valueOf(aptContIDTxt.getText());

            LocalDateTime startTime = LocalDateTime.parse(aptStartTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));

            startTime = startTime.plus(Duration.ofMinutes(offsetToEST));

            LocalDateTime endTime = LocalDateTime.parse(aptEndTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));

            endTime = endTime.plus(Duration.ofMinutes(offsetToEST));



            LocalTime businessHoursStart = LocalTime.of(9, 00);
            LocalTime businessHoursEnd = LocalTime.of(23, 00);


            LocalDateTime startDateTime = LocalDateTime.parse(aptStartTxt.getText(), formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(aptEndTxt.getText(), formatter);



            for (Appointment appointment : AppointmentDB.allAppointments) {
                if((startDateTime.isEqual(appointment.getStart()) || startDateTime.isAfter(appointment.getStart()) && startDateTime.isBefore(appointment.getEnd()))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("CONFLICT");
                    alert.setContentText("Please enter a time for the start and end time of the appointment that is not already taken");
                    alert.showAndWait();
                    return false;
                }
            }


            if (startTime.toLocalTime().isBefore(businessHoursStart) || endTime.toLocalTime().isAfter(businessHoursEnd)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("TOO EARLY!");
                alert.setContentText("Please enter a time after business opening hour of 0800 EST and before business closing hours of 1000 EST");
                alert.showAndWait();

            } else if (!title.equals("") && !type.equals("") && !description.equals("") && !location.equals("")) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/View/Appointment.fxml"));
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


    public AddAppointmentController() throws SQLException {

        try {
            Connection conn = DBConnection.startConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM contacts");
            while (rs.next()) {

                contactList.add(new Contacts(rs.getInt("Contact_ID"),rs.getString("Contact_Name"),rs.getString("Email")));
            }
        } catch (SQLException ce) {
            Logger.getLogger(ce.toString());
        }
    }



    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {

        aptCreateByTxt.setText(String.valueOf(User.getUsername()));
        aptLstUpdByTxt.setText(String.valueOf(User.getUsername()));
        try {

            Connection conn = DBConnection.startConnection();

            ResultSet rs = conn.createStatement().executeQuery("SELECT MAX(Appointment_ID) AS highestID FROM appointments");
            while (rs.next()) {

                int tempID = rs.getInt("highestID");

                aptIDtxt.setText(String.valueOf(tempID + 1));
                System.out.println(rs.getInt(tempID));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        contactName.setItems(contactList);
    }
}

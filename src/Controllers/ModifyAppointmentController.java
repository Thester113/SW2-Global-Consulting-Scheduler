package Controllers;

import DAO.AppointmentDB;
import DAO.DBConnection;
import Model.Appointment;
import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.awt.event.ActionEvent;
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


public class ModifyAppointmentController implements Initializable {
    private Appointment newModifyAppointment;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
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
    private Button EditAppointmentBtn;

    @FXML
    private Button ExitBtn;

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
    ObservableList<Contacts> contactList = FXCollections.observableArrayList();

    public void EditAppointment() throws SQLException {
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
    private void SetContactID (ActionEvent event) throws IOException {

    }


    @FXML
    public void sendAppointment(Appointment modifyAppointment)
    {
        newModifyAppointment = modifyAppointment;
        aptIDtxt.setText(String.valueOf(newModifyAppointment.getAppointmentID()));
        aptTitleTxt.setText(newModifyAppointment.getTitle());
        aptDescrTxt.setText(newModifyAppointment.getDescription());
        aptLocTxt.setText(newModifyAppointment.getLocation());
        aptTypeTxt.setText(newModifyAppointment.getType());
        aptStartTxt.setText(String.valueOf(newModifyAppointment.getStart().format(formatter)));
        aptEndTxt.setText(String.valueOf(newModifyAppointment.getEnd().format(formatter)));
        aptLstUpdByTxt.setText(newModifyAppointment.getLastUpdatedBy());
        aptLastUpdateTxt.setText(String.valueOf(newModifyAppointment.getLastUpdate().format(formatter)));
        aptCreateByTxt.setText(newModifyAppointment.getCreatedBy());
        aptCreateDateTxt.setText(String.valueOf(newModifyAppointment.getCreateDate().format(formatter)));
        aptCustIDTxt.setText(String.valueOf(newModifyAppointment.getCustomerID()));
        aptUIDTxt.setText(String.valueOf(newModifyAppointment.getUserID()));
        aptContIDTxt.setText(String.valueOf(newModifyAppointment.getContactID()));
        int comboBoxPreset = newModifyAppointment.getContactID();
        Contacts c = new Contacts(comboBoxPreset);
        contactName.setValue(c);
    }

    @FXML
    void ExitToMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/Appointment.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


    @FXML
    void OAFillContID(ActionEvent event) {
        if (contactName.getSelectionModel().isEmpty()) {
            return;
        }
        else {
            Contacts c = contactName.getSelectionModel().getSelectedItem();
            aptContIDTxt.setText(String.valueOf(c.getContactID()));
        }
    }


    @FXML
    boolean OnActionEditAppointment(ActionEvent event) throws SQLException, IOException{
        TimeZone est = TimeZone.getTimeZone("America/New_York");
        long offsetToEST = (long) (est.getOffset(new Date().getTime()) / 1000 / 60);
        LocalDateTime startTime = LocalDateTime.parse(aptStartTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));

        startTime = startTime.plus(Duration.ofMinutes(offsetToEST));

        LocalDateTime endTime = LocalDateTime.parse(aptEndTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));

        endTime = endTime.plus(Duration.ofMinutes(offsetToEST));



        LocalTime businessHoursStart = LocalTime.of(9, 00);
        LocalTime businessHoursEnd = LocalTime.of(23, 00);


        LocalDateTime startDateTime = LocalDateTime.parse(aptStartTxt.getText(), formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(aptEndTxt.getText(), formatter);
        try {

            if (aptTitleTxt.getText().isEmpty() || aptDescrTxt.getText().isEmpty() || aptLocTxt.getText().isEmpty() || aptTypeTxt.getText().isEmpty() || aptStartTxt.getText().isEmpty() || aptEndTxt.getText().isEmpty() || aptCreateDateTxt.getText().isEmpty() || aptCreateByTxt.getText().isEmpty() || aptLastUpdateTxt.getText().isEmpty() || aptLstUpdByTxt.getText().isEmpty() || aptCustIDTxt.getText().isEmpty() || aptCustIDTxt.getText().isEmpty() || aptContIDTxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing Entries");
                alert.setContentText("Please ensure all fields are entered");
                alert.showAndWait();
            }


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

            }
            if (!aptTitleTxt.equals("") && !aptTypeTxt.equals("") && !aptDescrTxt.equals("") && !aptLocTxt.equals("")){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/View/Appointment.fxml"));
                Parent parent = loader.load();

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();

                return AppointmentDB.editAppointment(Integer.valueOf(
                        aptIDtxt.getText()),
                        aptTitleTxt.getText(),
                        aptDescrTxt.getText(),
                        aptLocTxt.getText(),
                        aptTypeTxt.getText(),
                        LocalDateTime.parse(aptStartTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC)),
                        LocalDateTime.parse(aptEndTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC)),
                        LocalDateTime.parse(aptCreateDateTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC)),
                        aptCreateByTxt.getText(),
                        LocalDateTime.parse(aptLastUpdateTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC)),
                        aptLstUpdByTxt.getText(),
                        Integer.valueOf(aptCustIDTxt.getText()),
                        Integer.valueOf(aptUIDTxt.getText()),
                        Integer.valueOf(aptContIDTxt.getText()));
            }
        }

        catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing selection");
            alert.setContentText("Please ensure all date and time fields are formatted YYYY-MM-DD HH:MM prior to adding an appointment");
            alert.showAndWait();
            return false;
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactName.setItems(contactList);

    }
}
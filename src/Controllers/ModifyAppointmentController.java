package Controllers;


import DAO.AppointmentDB;
import DAO.DBConnection;
import Model.Appointments;
import Model.Contacts;
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

/**
 * Ability for user to edit an appointment using the AppointmentDB class to modify the DB
 */
public class ModifyAppointmentController implements Initializable {
  private Appointments newModifyAppointments;
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
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


  public ModifyAppointmentController() throws SQLException {
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM contacts");
      while (rs.next()) {
        /**Creates a Country Object using Strings for Country selection
         //columnLabel corresponds to Column! not the attribute of the object*/

        contactList.add(new Contacts(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));

      }
    } catch (SQLException c) {
      Logger.getLogger(c.toString());
    }
  }

  @FXML
  private void SetContactID(MouseEvent event) throws IOException {

  }


  /**
   * @see AppointmentController#sceneEditAppointment(ActionEvent)
   * Sets the fields based on selection from Appointment Main Table View
   */


  @FXML
  public void sendAppointment(Appointments modifyAppointments) {
    newModifyAppointments = modifyAppointments;
    aptIDtxt.setText(String.valueOf(newModifyAppointments.getAppointmentID()));
    aptTitleTxt.setText(newModifyAppointments.getTitle());
    aptDescrTxt.setText(newModifyAppointments.getDescription());
    aptLocTxt.setText(newModifyAppointments.getLocation());
    aptTypeTxt.setText(newModifyAppointments.getType());
    aptStartTxt.setText(newModifyAppointments.getStart().format(formatter));
    aptEndTxt.setText(newModifyAppointments.getEnd().format(formatter));
    aptLstUpdByTxt.setText(newModifyAppointments.getLastUpdatedBy());
    aptLastUpdateTxt.setText(newModifyAppointments.getLastUpdate().format(formatter));
    aptCreateByTxt.setText(newModifyAppointments.getCreatedBy());
    aptCreateDateTxt.setText(newModifyAppointments.getCreateDate().format(formatter));
    aptCustIDTxt.setText(String.valueOf(newModifyAppointments.getCustomerID()));
    aptUIDTxt.setText(String.valueOf(newModifyAppointments.getUserID()));
    aptContIDTxt.setText(String.valueOf(newModifyAppointments.getContactID()));

    int comboBoxPreset = newModifyAppointments.getContactID();

    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM contacts WHERE Contact_ID = " + comboBoxPreset);
      rs.next();
      contactName.setValue(new Contacts(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));


    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @FXML
  public void ExitToMain(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(getClass().getResource("/Views/Appointment.fxml"));
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  /**
   * Uses combobox selection based on name and sets ID
   *
   * @param event
   */
  @FXML
  public void OAFillContID(ActionEvent event) {
    if (contactName.getSelectionModel().isEmpty()) {
      return;
    } else {
      Contacts c = contactName.getSelectionModel().getSelectedItem();
      aptContIDTxt.setText(String.valueOf(c.getContactID()));
    }
  }

  /**
   * @param event
   * @return
   * @throws SQLException
   * @throws IOException
   * @see AppointmentDB#modifyAppointment(
   *Integer,
   * String,
   * String,
   * String,
   * String,
   * LocalDateTime,
   * LocalDateTime,
   * LocalDateTime,
   * String,
   * LocalDateTime,
   * String,
   * Integer,
   * Integer,
   * Integer)
   * edited appointment sent to the DB
   */
  @FXML
  boolean OnActionModifyAppointment(ActionEvent event) throws SQLException, IOException {
    TimeZone tz = TimeZone.getTimeZone("America/New_York");
    Long offsetToEST = Long.valueOf(tz.getOffset(new Date().getTime()) / 1000 / 60);
    LocalDateTime startTime = LocalDateTime.parse(aptStartTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));

    /**
     * Start time is set to EST
     */

    startTime = startTime.plus(Duration.ofMinutes(offsetToEST));

    /**
     *Gets user local time entered and sets to UTC
     */

    LocalDateTime endTime = LocalDateTime.parse(aptEndTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));

    /**
     *Sets end time to EST
     */

    endTime = endTime.plus(Duration.ofMinutes(offsetToEST));

    /**
     * Makes sure startTime and endTime are between business hours of 8-22
     */


    LocalTime businessHoursStart = LocalTime.of(8, 0);
    LocalTime businessHoursEnd = LocalTime.of(22, 0);

    /**
     * Checks if date time falls between other scheduled appointments
     */


    LocalDateTime startDateTime = LocalDateTime.parse(aptStartTxt.getText(), formatter);
    LocalDateTime endDateTime = LocalDateTime.parse(aptEndTxt.getText(), formatter);
    try {
      /**
       * Check that all fields are entered if not gives an alert to enter fields
       */

      if (aptTitleTxt.getText().isEmpty() || aptDescrTxt.getText().isEmpty() || aptLocTxt.getText().isEmpty() || aptTypeTxt.getText().isEmpty() || aptStartTxt.getText().isEmpty() || aptEndTxt.getText().isEmpty() || aptCreateDateTxt.getText().isEmpty() || aptCreateByTxt.getText().isEmpty() || aptLastUpdateTxt.getText().isEmpty() || aptLstUpdByTxt.getText().isEmpty() || aptCustIDTxt.getText().isEmpty() || aptCustIDTxt.getText().isEmpty() || aptContIDTxt.getText().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Entries Missing");
        alert.setContentText("Please ensure all fields are entered");
        alert.showAndWait();
      }

      /**
       * Makes sure no overlapping appointment times
       */


      //Lambda expression
      for (Appointments appointments : AppointmentDB.allAppointments) {
        if ((startDateTime.isEqual(appointments.getStart()) || startDateTime.isAfter(appointments.getStart()) && startDateTime.isBefore(appointments.getEnd()))) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("APPOINTMENT TIME CONFLICT");
          alert.setContentText("Please enter a time for the start and end time of the appointment that is not already taken");
          alert.showAndWait();
          return false;
        }
      }

      /**
       * Checks if start time and end time are within the business hours
       */


      if (startTime.toLocalTime().isBefore(businessHoursStart) || endTime.toLocalTime().isAfter(businessHoursEnd)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("TOO EARLY!");
        alert.setContentText("Please enter a time after business opening hour of 0800 EST and before business closing hours of 1000 EST");
        alert.showAndWait();

      }

      /**
       * If fields are not blank sends Appointment object to DB
       * @see AppointmentDB#modifyAppointment(
       *Integer,
       * String,
       * String,
       * String,
       * String,
       * LocalDateTime,
       * LocalDateTime,
       * LocalDateTime,
       * String,
       * LocalDateTime,
       * String,
       * Integer,
       * Integer,
       * Integer)
       *
       */
      else if (!aptTitleTxt.equals("") && !aptTypeTxt.equals("") && !aptDescrTxt.equals("") && !aptLocTxt.equals("")) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Appointment.fxml"));
        Parent parent = loader.load();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

        return AppointmentDB.modifyAppointment(Integer.valueOf(
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
      /**
       * @exception DateTimeParseException e if date time fields are not formatted correctly this is caught to alert the user to modify them correctly
       */


    } catch (DateTimeParseException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Missing selection");
      alert.setContentText("Please ensure all date and time fields are formatted YYYY-MM-DD HH:MM prior to adding an appointment");
      alert.showAndWait();
      return false;

    } finally {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/Views/Appointment.fxml"));
      Parent parent = loader.load();

      Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      Parent scene = loader.getRoot();
      stage.setScene(new Scene(scene));
      stage.show();

    }
    return false;
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    contactName.setItems(contactList);

  }
}

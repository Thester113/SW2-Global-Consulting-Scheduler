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
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
  Long offsetToUTC = (long) (ZonedDateTime.now().getOffset()).getTotalSeconds();
  ObservableList<Contacts> contactList = FXCollections.observableArrayList();
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

  public ModifyAppointmentController() throws SQLException {
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM contacts");
      while (rs.next()) {
        /**Creates a Country Object using Strings for Country selection
         //columnLabel corresponds to Column! not the attribute of the object*/

        contactList.add(new Contacts(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));

      }
    } catch (SQLException ce) {
      Logger.getLogger(ce.toString());
    }
  }

  @FXML
  void SetContactID(MouseEvent event) throws IOException {
    if (contactName.getSelectionModel().isEmpty()) {
    } else {
      Contacts c = contactName.getSelectionModel().getSelectedItem();
      aptContIDTxt.setText(String.valueOf(c.getContactID()));
    }

  }

  /**
   * @see AppointmentController#sceneEditAppointment(ActionEvent)
   * Sets the fields based on selection from Appointment Main Table View
   */


  @FXML
  public void sendAppointment(Appointments modifyAppointments) {
    aptIDtxt.setText(String.valueOf(modifyAppointments.getAppointmentID()));
    aptTitleTxt.setText(modifyAppointments.getTitle());
    aptDescrTxt.setText(modifyAppointments.getDescription());
    aptLocTxt.setText(modifyAppointments.getLocation());
    aptTypeTxt.setText(modifyAppointments.getType());
    aptStartTxt.setText(modifyAppointments.getStart().format(formatter));
    aptEndTxt.setText(modifyAppointments.getEnd().format(formatter));
    aptLstUpdByTxt.setText(modifyAppointments.getLastUpdatedBy());
    aptLastUpdateTxt.setText(modifyAppointments.getLastUpdate().format(formatter));
    aptCreateByTxt.setText(modifyAppointments.getCreatedBy());
    aptCreateDateTxt.setText(modifyAppointments.getCreateDate().format(formatter));
    aptCustIDTxt.setText(String.valueOf(modifyAppointments.getCustomerID()));
    aptUIDTxt.setText(String.valueOf(modifyAppointments.getUserID()));
    aptContIDTxt.setText(String.valueOf(modifyAppointments.getContactID()));

    int comboBoxPreset = modifyAppointments.getContactID();
    Contacts c = new Contacts(comboBoxPreset);
    contactName.setValue(c);
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
  void OAFillContID(ActionEvent event) {
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
  boolean OnActionEditAppointment(ActionEvent event) throws SQLException, IOException {
    TimeZone tz = TimeZone.getTimeZone("America/New_York");
    long offsetToEST = tz.getOffset(new Date().getTime()) / 1000 / 60;
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


    LocalTime businessHoursStart = LocalTime.of(8, 00);
    LocalTime businessHoursEnd = LocalTime.of(22, 00);

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
        alert.setTitle("Missing Entries");
        alert.setContentText("Please ensure all fields are entered");
        alert.showAndWait();
      }

      /**
       * Makes sure no overlapping appointment times
       */


      ObservableList<Appointments> allAppointments = AppointmentDB.allAppointments;
      //Lambda expression
      for (Appointments appointments : allAppointments) {
        if ((startDateTime.isEqual(appointments.getStart()) || startDateTime.isAfter(appointments.getStart()) && startDateTime.isBefore(appointments.getEnd()))) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("CONFLICT");
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
    }
    /**
     * @exception DateTimeParseException e if date time fields are not formatted correctly this is caught to alert the user to modify them correctly
     */ catch (DateTimeParseException e) {
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

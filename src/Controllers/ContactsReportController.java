package Controllers;

import Model.Appointment;
import Model.Contacts;

import DAO.DBConnection;
import DAO.ReportDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ContactsReportController implements Initializable {

  @FXML
  private ComboBox<Contacts> contactCB;

  @FXML
  private TableView<Appointment> contactAppointmentTbl;

  @FXML
  private TableColumn<Appointment, Integer> appointmentID;

  @FXML
  private TableColumn<Appointment, String> aptTitle;

  @FXML
  private TableColumn<Appointment, String> aptType;

  @FXML
  private TableColumn<Appointment, String> aptDescription;

  @FXML
  private TableColumn<Appointment, LocalDateTime> aptStart;

  @FXML
  private TableColumn<Appointment, LocalDateTime> aptEnd;

  @FXML
  private TableColumn<Appointment, Integer> aptCustID;

  @FXML
  private Button exitBtn;

  @FXML
  private Button mainBtn;

  @FXML
  void backToMain(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/Views/MainScreen.fxml"));
    loader.load();

    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Parent scene = loader.getRoot();
    stage.setScene(new Scene(scene));
    stage.show();
  }

  @FXML
  void displayContactSchedule(ActionEvent event) throws IOException {
    try {
      //selecting the contact object to send to query the DB
      Contacts contactSchedule = contactCB.getSelectionModel().getSelectedItem();
      ReportDB.sendContactSelection(contactSchedule);
      //Getting the appointments from the DB and populating the tableview
      contactAppointmentTbl.setItems(ReportDB.getContactSchedule());
      for (Appointment appointment : ReportDB.contactSchedule) {
        System.out.println(appointment.getStart());
      }
      appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
      aptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
      aptType.setCellValueFactory(new PropertyValueFactory<>("type"));
      aptDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
      aptStart.setCellValueFactory(new PropertyValueFactory<>("start"));
      aptEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
      aptCustID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
  }



  @FXML
  void exitApp(ActionEvent event) {
    Button sourceButton = (Button) event.getSource();
    exitBtn.setText(sourceButton.getText());
    DBConnection.closeConnection();
    System.exit(0);
  }
  ObservableList<Contacts> contactList = FXCollections.observableArrayList();

  @FXML
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM contacts");
      while (rs.next()) {

        contactList.add(new Contacts(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));
      }
      contactCB.setItems(contactList);

    }
    catch (SQLException ce) {
      Logger.getLogger(ce.toString());
    }
  }
}

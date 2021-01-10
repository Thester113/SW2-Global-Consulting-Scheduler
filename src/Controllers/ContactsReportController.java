package Controllers;


import Model.Appointments;
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

/*
  Combo Box is used to select a contact this will set a table view with the contact's appointments.
 */

public class ContactsReportController implements Initializable {

  @FXML
  private ComboBox<Contacts> contactCB;

  @FXML
  private TableView<Appointments> contactAppointmentTbl;

  @FXML
  private TableColumn<Appointments, Integer> appointmentID;

  @FXML
  private TableColumn<Appointments, String> aptTitle;

  @FXML
  private TableColumn<Appointments, String> aptType;

  @FXML
  private TableColumn<Appointments, String> aptDescription;

  @FXML
  private TableColumn<Appointments, LocalDateTime> aptStart;

  @FXML
  private TableColumn<Appointments, LocalDateTime> aptEnd;

  @FXML
  private TableColumn<Appointments, Integer> aptCustID;

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
      //Gets the contact to send as a query to the DB
      Contacts contactSchedule = contactCB.getSelectionModel().getSelectedItem();
      ReportDB.sendContactSelection(contactSchedule);
      //Checks the appointments from the DB and populates the tableview
      contactAppointmentTbl.setItems(ReportDB.getContactSchedule());
      for (Appointments appointments : ReportDB.contactSchedule) {
        System.out.println(appointments.getStart());
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
  void exitToApp(ActionEvent event) {
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
      //Creates Country Objects using Strings for Country selection
      //columnLabel corresponds to Column value and not the attribute of the object
      if (rs.next()) {
        do {

          contactList.add(new Contacts(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));
        } while (rs.next());
      }
      contactCB.setItems(contactList);

    }
    catch (SQLException ce) {
      Logger.getLogger(ce.toString());
    }
  }
}

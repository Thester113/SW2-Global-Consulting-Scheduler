package Controllers;

import DAO.CountriesDB;
import DAO.CustomerDB;
import DAO.DBConnection;
import DAO.FirstLevelDivisionDB;
import Model.Countries;
import Model.FirstLevelDivision;
import Model.Users;
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AddCustomerController implements Initializable {

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  Long offsetToUTC = (long) (ZonedDateTime.now().getOffset()).getTotalSeconds();


  ObservableList<FirstLevelDivision> fldList = FirstLevelDivisionDB.getAllFirstLevelDivisions();
  @FXML
  private Button exit;
  @FXML
  private Button addCustomer;
  @FXML
  private TextField custIDTxt;
  @FXML
  private TextField custNameTxt;
  @FXML
  private TextField custAddressTxt;
  @FXML
  private TextField custPostalTxt;
  @FXML
  private TextField custPhoneTxt;
  @FXML
  private TextField createdByTxt;
  @FXML
  private TextField createDateTxt;
  @FXML
  private TextField lastUpdateTxt;
  @FXML
  private TextField lastUpdatedByTxt;
  @FXML
  private ComboBox<FirstLevelDivision> cbDiv;
  @FXML
  private ComboBox<Countries> cbCountry;

  public AddCustomerController() throws SQLException {
  }


  @FXML
  boolean addCustomer(ActionEvent event) throws IOException {
    try {
      Integer customerID = Integer.valueOf(custIDTxt.getText());
      String customerName = custNameTxt.getText();
      String address = custAddressTxt.getText();
      String postal = custPostalTxt.getText();
      String phone = custPhoneTxt.getText();
      LocalDateTime createDate = LocalDateTime.parse(createDateTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
      String createdBy = createdByTxt.getText();
      LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdateTxt.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC));
      String lastUpdatedBy = lastUpdatedByTxt.getText();
      int divisionID = Integer.valueOf(String.valueOf(cbDiv.getSelectionModel().getSelectedItem().getDivisionID()));


      if (!customerName.equals("") && !address.equals("") && !postal.equals("") && !phone.equals("")) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/Views/Customer.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

        return CustomerDB.addCustomer(customerID, customerName, address, postal, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID);
      } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Missing selection");
        alert.setContentText("Please enter a customer name, address, postal code, and phone number for customer");
        alert.showAndWait();
      }
      return false;

    } catch (DateTimeParseException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Missing selection");
      alert.setContentText("Please ensure all date and time fields are formatted YYYY-MM-DD HH:MM prior to adding an appointment");
      alert.showAndWait();
      return true;
    }
  }

  @FXML
  void exitMainMenu(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(getClass().getResource("/Views/Customer.fxml"));
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }


  @FXML
  void SetDivisionID(MouseEvent event) throws IOException, SQLException {

    if (cbCountry.getSelectionModel().isEmpty()) {
      System.out.println(cbCountry.getSelectionModel().toString());
      return;
    }


    else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("United States")) {
      try {
        cbDiv.setItems(FirstLevelDivisionDB.getusFilteredFirstLevelDivisions());

        ObservableList<FirstLevelDivision> usFilteredFirstLevelDivisions = FirstLevelDivisionDB.usFilteredFirstLevelDivisions;
        for (int i = 0, usFilteredFirstLevelDivisionsSize = usFilteredFirstLevelDivisions.size(); i < usFilteredFirstLevelDivisionsSize; i++) {
          FirstLevelDivision usFLD = usFilteredFirstLevelDivisions.get(i);
          System.out.println(usFLD.getDivision());
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }


    else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("Canada")) {
      try {
        cbDiv.setItems(FirstLevelDivisionDB.getcanadaFilteredFirstLevelDivisions());
        for (FirstLevelDivision canadaFLD : FirstLevelDivisionDB.canadaFilteredFirstLevelDivisions) {
          System.out.println(canadaFLD.getDivision());
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("United Kingdom")) {
      try {
        cbDiv.setItems(FirstLevelDivisionDB.getukFilteredFirstLevelDivisions());

        ObservableList<FirstLevelDivision> ukFilteredFirstLevelDivisions = FirstLevelDivisionDB.ukFilteredFirstLevelDivisions;
        for (int i = 0, ukFilteredFirstLevelDivisionsSize = ukFilteredFirstLevelDivisions.size(); i < ukFilteredFirstLevelDivisionsSize; i++) {
          FirstLevelDivision ukFLD = ukFilteredFirstLevelDivisions.get(i);
          System.out.println(ukFLD.getDivision());
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }


  @FXML
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      cbCountry.setItems(CountriesDB.getAllCountries());
      for (Countries countries : CountriesDB.allCountries) {
        System.out.println(countries.getCountry());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      cbDiv.setItems(FirstLevelDivisionDB.getAllFirstLevelDivisions());
      for (FirstLevelDivision firstLevelDivision : FirstLevelDivisionDB.allFirstLevelDivisions) {
        System.out.println(firstLevelDivision.getDivision());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }



    createdByTxt.setText(String.valueOf(Users.getUsername()));
    lastUpdatedByTxt.setText(String.valueOf(Users.getUsername()));
    try {

      Connection conn = DBConnection.startConnection();

      ResultSet rs = conn.createStatement().executeQuery("SELECT Customer_ID FROM customers ORDER BY Customer_ID DESC LIMIT 1 ");

      if (rs.next()) {
        do {

          int tempID = rs.getInt("Customer_ID");

          custIDTxt.setText(String.valueOf(tempID + 1));
          System.out.println(rs.getInt(tempID));

        } while (rs.next());
      }


    } catch (Exception exc) {
      exc.printStackTrace();
    }
  }
}

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
  /**
   * format the LocalDateTime fields
   */
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  /**
   * Gets the offset of the users system date time from UTC used in converting the time from the DB
   */
  Long offsetToUTC = Long.valueOf((ZonedDateTime.now().getOffset()).getTotalSeconds());
  /**
   * Division ID List for Combo Box
   */

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

  /**
   * Gets the fields and sends to the db
   *
   * @param event
   * @return
   * @throws IOException
   * @see CustomerDB#addCustomer(Integer, String, String, String, String, LocalDateTime, String, LocalDateTime, String, Integer)
   */
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

      /**
       * Ensures fields are not blank and throws an alert if they are, if entered utilizes
       * @see CustomerDB#addCustomer(Integer, String, String, String, String, LocalDateTime, String, LocalDateTime, String, Integer)
       */
      if (!customerName.equals("") && !address.equals("") && !postal.equals("") && !phone.equals("")) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerMain.fxml"));
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
      /**
       * Checks for formatting on the date time fields and catches a date time parse exception
       * Gives an alert if format is incorrect detailing proper formatting
       */
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
    Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerMain.fxml"));
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  /**
   * Filtered list of division IDs that correspond to a country selection from combobox
   */


  @FXML
  void SetDivisionID(MouseEvent event) throws IOException, SQLException {

    if (cbCountry.getSelectionModel().isEmpty()) {
      System.out.println(cbCountry.getSelectionModel().toString());
      return;
    }

    //US Filter
    else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("U.S")) {
      try {
        cbDiv.setItems(FirstLevelDivisionDB.getusFilteredFirstLevelDivisions());
        /**For each lambda to get all appointments, less code then normal for i method*/

        for (FirstLevelDivision usFLD : FirstLevelDivisionDB.usFilteredFirstLevelDivisions) {
          System.out.println(usFLD.getDivision());
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    //Canada Filter
    else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("Canada")) {
      try {
        cbDiv.setItems(FirstLevelDivisionDB.getcanadaFilteredFirstLevelDivisions());
        /**For each lambda to get all appointments, less code then normal for i method*/
        for (FirstLevelDivision canadaFLD : FirstLevelDivisionDB.canadaFilteredFirstLevelDivisions) {
          System.out.println(canadaFLD.getDivision());
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    //UK Filter
    else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("UK")) {
      try {
        cbDiv.setItems(FirstLevelDivisionDB.getukFilteredFirstLevelDivisions());
        /**For each lambda to get all appointments, less code then normal for i method*/
        for (FirstLevelDivision ukFLD : FirstLevelDivisionDB.ukFilteredFirstLevelDivisions) {
          System.out.println(ukFLD.getDivision());
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * Setting the combo boxes, customer ID
   *
   * @param url
   * @param resourceBundle
   */
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

    /**
     * Auto-populate Created By field with the value of a valid user log in that is stored in the User object
     */

    createdByTxt.setText(String.valueOf(Users.getUsername()));
    lastUpdatedByTxt.setText(String.valueOf(Users.getUsername()));
    try {
      // Connection to the database
      Connection conn = DBConnection.startConnection();
      //Select the last row from Customer ID
      ResultSet rs = conn.createStatement().executeQuery("SELECT Customer_ID FROM customers ORDER BY Customer_ID DESC LIMIT 1 ");
      // SQL query

      /**
       * @param custIDTxt is auto generated
       */
      while (rs.next()) {
        //Create a temporary var for customer ID
        int tempID = rs.getInt("Customer_ID");
        //Set the customer ID to auto increment by 1
        custIDTxt.setText(String.valueOf(tempID + 1));
        System.out.println(rs.getInt(tempID));

      }


    } catch (Exception exc) {
      exc.printStackTrace();
    }
  }
}

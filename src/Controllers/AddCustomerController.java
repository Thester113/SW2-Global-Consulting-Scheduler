package Controllers;
/**
 * AddCustomerController adds customers to the DB using the DAO.CustomersDB
 */

import DAO.CountriesDB;
import DAO.CustomerDB;
import DAO.DBConnection;
import DAO.FirstLevelDivisionDB;
import Model.Countries;
import Model.FirstLevelDivisions;
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
   * DateTimeFormatter puts Local Date and Time in format
   */

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  Long offsetToUTC = (long) (ZonedDateTime.now().getOffset()).getTotalSeconds();


  ObservableList<FirstLevelDivisions> fldList = FirstLevelDivisionDB.getAllFirstLevelDivisions();
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
  private ComboBox<FirstLevelDivisions> cbDiv;
  @FXML
  private ComboBox<Countries> cbCountry;

  public AddCustomerController() throws SQLException {
  }

  /**
   * Gets customer fields and passes them to database
   *
   * @param event
   * @return
   * @throws IOException
   */


  @FXML
  boolean addCustomerController(ActionEvent event) throws IOException {
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
      int divisionID = Integer.parseInt(String.valueOf(cbDiv.getSelectionModel().getSelectedItem().getDivisionID()));


      if (!customerName.equals("") && !address.equals("") && !postal.equals("") && !phone.equals("")) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/Views/Customer.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();

        return CustomerDB.addCustomer(customerID, customerName, address, postal, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionID);
      } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Selection is Missing");
        alert.setContentText("A field is missing for customer");
        alert.showAndWait();
      }
      return false;
      /**
       * Checks for formatting errors and provides exception if missing a field
       * Issues alert if missing formatting
       */

    } catch (DateTimeParseException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("FORMAT INCORRECT");
      alert.setContentText("Please check that date and time fields are formatted correctly when adding an appointment");
      alert.showAndWait();
      return true;
    }
  }

  @FXML
  void exitToMainMenu(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(getClass().getResource("/Views/Customer.fxml"));
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }


  /**
   * Countries have own list
   *
   * @param event on selection of one of the countries the filtered lists will be set in the division combo box
   * @throws IOException
   * @throws SQLException
   * @see FirstLevelDivisionDB#usFilteredFirstLevelDivisions
   * @see FirstLevelDivisionDB#ukFilteredFirstLevelDivisions
   * @see FirstLevelDivisionDB#canadaFilteredFirstLevelDivisions
   * A for each lambda will run which helps eliminate time that code will need to run through the objects. This satisfies lambda task.
   */
  @FXML
  void SetDivisionID(MouseEvent event) throws IOException, SQLException {

    /**
     * Combo Box Division ID list
     */

    if (cbCountry.getSelectionModel().isEmpty()) {
      System.out.println(cbCountry.getSelectionModel().toString());
      return;
    } else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("United States")) {
      try {
        cbDiv.setItems(FirstLevelDivisionDB.getUSFilteredFirstLevelDivisions());
        /**
         * Filters list of division IDs that connect with a country
         */

        ObservableList<FirstLevelDivisions> usFilteredFirstLevelDivisions = FirstLevelDivisionDB.usFilteredFirstLevelDivisions;
        for (FirstLevelDivisions usFLD : usFilteredFirstLevelDivisions) {
          System.out.println(usFLD.getDivision());
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("Canada")) {
      try {
        cbDiv.setItems(FirstLevelDivisionDB.getCanadaFilteredFirstLevelDivisions());
        for (FirstLevelDivisions canadaFLD : FirstLevelDivisionDB.canadaFilteredFirstLevelDivisions) {
          System.out.println(canadaFLD.getDivision());
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("United Kingdom")) {
      try {
        cbDiv.setItems(FirstLevelDivisionDB.getUKFilteredFirstLevelDivisions());

        ObservableList<FirstLevelDivisions> ukFilteredFirstLevelDivisions = FirstLevelDivisionDB.ukFilteredFirstLevelDivisions;
        for (FirstLevelDivisions ukFLD : ukFilteredFirstLevelDivisions) {
          System.out.println(ukFLD.getDivision());
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * Sets the combo boxes and customer ID
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
      for (FirstLevelDivisions firstLevelDivisions : FirstLevelDivisionDB.allFirstLevelDivisions) {
        System.out.println(firstLevelDivisions.getDivision());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }



    createdByTxt.setText(String.valueOf(Users.getUserName()));
    lastUpdatedByTxt.setText(String.valueOf(Users.getUserName()));
    try {
      // Connects to the database
      Connection conn = DBConnection.startConnection();
      //Query Selects the last row from Customer ID
      ResultSet rs = conn.createStatement().executeQuery("SELECT Customer_ID FROM customers ORDER BY Customer_ID DESC LIMIT 1 ");
      /**
       * @param custIDTxt is generated automatically
       */
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

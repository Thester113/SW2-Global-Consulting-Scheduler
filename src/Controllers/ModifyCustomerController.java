package Controllers;


import DAO.CountriesDB;
import DAO.CustomerDB;
import DAO.FirstLevelDivisionDB;
import Model.Countries;
import Model.Customers;
import Model.FirstLevelDivisions;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Allows the user to modify a customer using CustomerDB and access to MYSQL DB
 */
public class ModifyCustomerController implements Initializable {
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
  Long offsetToUTC = (long) (ZonedDateTime.now().getOffset()).getTotalSeconds();
  ObservableList<FirstLevelDivisions> firstLevelDivisionsObservableList = FirstLevelDivisionDB.getAllFirstLevelDivisions();
  ObservableList<FirstLevelDivisions> usFirstLevelDivisionsObservableList = FXCollections.observableArrayList();
  ObservableList<FirstLevelDivisions> ukFirstLevelDivisionsObservableList = FXCollections.observableArrayList();
  ObservableList<FirstLevelDivisions> canadaFirstLevelDivisionsObservableList = FXCollections.observableArrayList();
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
  private TextField lastUpdatedByTF;
  @FXML
  private TextField lastUpdateTF;
  @FXML
  private TextField createdByTF;
  @FXML
  private TextField createDateTF;
  @FXML
  private ComboBox<Countries> cbCountry;
  @FXML
  private ComboBox<FirstLevelDivisions> cbDivID;

  public ModifyCustomerController() throws SQLException {
  }

  /**
   * Sends and returns fields that re updated to the database
   *
   * @param event
   * @return
   * @throws SQLException
   * @throws IOException
   * @see CustomerDB#editCustomer(
   *Integer,
   * String,
   * String,
   * String,
   * String,
   * Timestamp,
   * String,
   * Timestamp,
   * String,
   * Integer)
   */
  @FXML
  boolean modifyCustomer(ActionEvent event) throws SQLException, IOException {
    try {
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/Views/Customer.fxml"));
      Parent parent = loader.load();

      Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      Parent scene = loader.getRoot();
      stage.setScene(new Scene(scene));
      stage.show();

      return CustomerDB.editCustomer(
              Integer.valueOf(custIDTxt.getText()),
              custNameTxt.getText(),
              custAddressTxt.getText(),
              custPostalTxt.getText(),
              custPhoneTxt.getText(),
              Timestamp.valueOf(LocalDateTime.parse(createDateTF.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC))),
              createdByTF.getText(),
              Timestamp.valueOf(LocalDateTime.parse(lastUpdateTF.getText(), formatter).minus(Duration.ofSeconds(offsetToUTC))),
              lastUpdatedByTF.getText(),
              Integer.valueOf(String.valueOf(cbDivID.getSelectionModel().getSelectedItem().getDivisionID())));
    }
    catch (DateTimeParseException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Selection Missing");
      alert.setContentText("Please ensure all date and time fields are formatted correctly prior to adding an appointment");
      alert.showAndWait();
      return false;
    }

  }

  @FXML
  private void addCustomer(ActionEvent event) {

  }

  /**
   * Sets the selected object from the Customer tableview
   */

  @FXML
  public void passCustomer(Customers modifyCustomer) {

    custIDTxt.setText(String.valueOf(modifyCustomer.getCustomerID()));
    custNameTxt.setText(modifyCustomer.getCustomerName());
    custAddressTxt.setText(modifyCustomer.getAddress());
    custPostalTxt.setText(modifyCustomer.getPostal());
    custPhoneTxt.setText(String.valueOf(modifyCustomer.getPhone()));
    lastUpdatedByTF.setText(modifyCustomer.getLastUpdatedBy());
    lastUpdateTF.setText(modifyCustomer.getLastUpdate().format(formatter));
    createdByTF.setText(modifyCustomer.getCreatedBy());
    createDateTF.setText(modifyCustomer.getCreateDate().format(formatter));

    int comboBoxPreset = modifyCustomer.getDivisionID();
    FirstLevelDivisions fld = new FirstLevelDivisions(comboBoxPreset);
    cbDivID.setValue(fld);

    /**
     * Combobox is set to the division name based on country selected
     */

    if (fld.getDivisionID() <= 54) {
      String countryName = "United States";
      Countries c = new Countries(countryName);
      cbCountry.setValue(c);
    } else if (fld.getDivisionID() > 54 && fld.getDivisionID() <= 72) {
      String countryName = "United Kingdom";
      Countries c = new Countries(countryName);
      cbCountry.setValue(c);
    } else if (fld.getDivisionID() > 72) {
      String countryName = "Canada";
      Countries c = new Countries(countryName);
      cbCountry.setValue(c);
    }

    try {
      cbCountry.setItems(CountriesDB.getAllCountries());
      ObservableList<Countries> allCountries = CountriesDB.allCountries;
      Iterator<Countries> iterator = allCountries.iterator();
      while (iterator.hasNext()) {
        Countries countries = iterator.next();
        System.out.println(countries.getCountry());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      cbDivID.setItems(FirstLevelDivisionDB.getAllFirstLevelDivisions());
      ObservableList<FirstLevelDivisions> allFirstLevelDivisions = FirstLevelDivisionDB.allFirstLevelDivisions;
      int i = 0, allFirstLevelDivisionsSize = allFirstLevelDivisions.size();
      while (i < allFirstLevelDivisionsSize) {
        FirstLevelDivisions firstLevelDivisions = allFirstLevelDivisions.get(i);
        System.out.println(firstLevelDivisions.getDivision());
        i++;
      }
      cbDivID.setValue(fld);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Lambda filtered lists for each country based on division id and country id utilizing a predicate f, and sorting through the objects
   * Uses switch
   */

  @FXML
  private void SetDivisionID(ActionEvent event) throws IOException, SQLException {
    if (cbCountry.getSelectionModel().isEmpty()) {
      System.out.println(cbCountry.getSelectionModel().toString());
      return;
    }
    switch (cbCountry.getSelectionModel().getSelectedItem().getCountry()) {
      case "U.S":

        var usResult = firstLevelDivisionsObservableList.stream().filter(f -> f.getDivisionID() < 54).collect(Collectors.toList());
        cbDivID.setItems(usFirstLevelDivisionsObservableList = FXCollections.observableList(usResult));
        break;
      case "Canada":
        var canadaResult = firstLevelDivisionsObservableList.stream().filter(f -> (f.getDivisionID() > 54) && (f.getDivisionID() < 101)).collect(Collectors.toList());
        cbDivID.setItems(canadaFirstLevelDivisionsObservableList = FXCollections.observableList(canadaResult));
        break;
      case "UK":
        var ukResult = firstLevelDivisionsObservableList.stream().filter(f -> f.getDivisionID() >= 101).collect(Collectors.toList());
        cbDivID.setItems(ukFirstLevelDivisionsObservableList = FXCollections.observableList(ukResult));
        break;
    }

  }


  @FXML
  void exitToMainMenu(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(getClass().getResource("/Views/Customer.fxml"));
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }
}

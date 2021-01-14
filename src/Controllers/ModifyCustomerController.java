package Controllers;


import DAO.CountriesDB;
import DAO.CustomerDB;
import DAO.DBConnection;
import DAO.FirstLevelDivisionDB;
import Model.*;
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

  private Customers newModifyCustomer;

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

  /**
   * Initializes the Modify Customer Controller
   *
   * @throws SQLException
   */

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

    } catch (DateTimeParseException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Selection Missing");
      alert.setContentText("Please ensure all date and time fields are formatted correctly prior to adding an appointment");
      alert.showAndWait();
      return false;

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/Views/Customer.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
      } catch (NullPointerException ignored) {
      }

    }
    return false;
  }

  @FXML
  private void addCustomer(ActionEvent event) {

  }

  /**
   * Sets the selected object from the Customer tableview
   */
//TODO: Make Work
  @FXML
  public void passCustomer(Customers modifyCustomer) {
    newModifyCustomer = modifyCustomer;
    custIDTxt.setText(String.valueOf(newModifyCustomer.getCustomerID()));
    custNameTxt.setText(newModifyCustomer.getCustomerName());
    custAddressTxt.setText(newModifyCustomer.getAddress());
    custPostalTxt.setText(newModifyCustomer.getPostal());
    custPhoneTxt.setText(String.valueOf(newModifyCustomer.getPhone()));
    lastUpdatedByTF.setText(newModifyCustomer.getLastUpdatedBy());
    lastUpdateTF.setText(newModifyCustomer.getLastUpdate().format(formatter));
    createdByTF.setText(newModifyCustomer.getCreatedBy());
    createDateTF.setText(newModifyCustomer.getCreateDate().format(formatter));

    int comboBoxPreset = newModifyCustomer.getDivisionID();
    FirstLevelDivisions fld = new FirstLevelDivisions(comboBoxPreset);
    cbDivID.setValue(fld);

//TODO: Make work
    if (fld.getDivisionID() <= 54)
    {
      String countryName = "U.S";
      Countries c = new Countries(countryName);
      cbCountry.setValue(c);
    }
    else if (fld.getDivisionID() >54 && fld.getDivisionID() <= 72)
    {
      String countryName = "UK";
      Countries c = new Countries(countryName);
      cbCountry.setValue(c);
    }
    else if (fld.getDivisionID() > 72)
    {
      String countryName = "Canada";
      Countries c = new Countries(countryName);
      cbCountry.setValue(c);
    }


    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rs = conn.createStatement().executeQuery("SELECT fld.Division_ID, fld.Division, countries.Country_ID, countries.Country FROM first_level_divisions AS fld JOIN countries ON (fld.COUNTRY_ID = countries.Country_ID)  WHERE Division_ID = " + comboBoxPreset);
      rs.next();
      cbDivID.setValue(new FirstLevelDivisions(rs.getInt("Division_ID"), rs.getString("Division")));
      cbCountry.setValue(new Countries(rs.getString("Country"), rs.getInt("Country_ID")));

    } catch (SQLException e) {
      e.printStackTrace();
    }


    try {
      cbDivID.setItems(FirstLevelDivisionDB.getAllFirstLevelDivisions());
      for (FirstLevelDivisions firstLevelDivision : FirstLevelDivisionDB.allFirstLevelDivisions) {
        System.out.println(firstLevelDivision.getDivision());
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Lambda filtered lists for each country based on division id and country id utilizing a predicate f, and sorting through the objects
   * Uses switch
   * Lambda improves code performance and makes response quicker
   *
   * @param event
   */

  @FXML
  private void SetDivisionID(MouseEvent event) throws IOException, SQLException {
    if (cbCountry.getSelectionModel().isEmpty()) {
      System.out.println(cbCountry.getSelectionModel().toString());
      return;

    } else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("U.S")) {
      var usResult = firstLevelDivisionsObservableList.stream().filter(f -> f.getDivisionID() < 54).collect(Collectors.toList());
      cbDivID.setItems(usFirstLevelDivisionsObservableList = FXCollections.observableList(usResult));
    } else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("Canada")) {
      var canadaResult = firstLevelDivisionsObservableList.stream().filter(f -> (f.getDivisionID() > 54) && (f.getDivisionID() < 101)).collect(Collectors.toList());
      cbDivID.setItems(canadaFirstLevelDivisionsObservableList = FXCollections.observableList(canadaResult));
    } else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("UK")) {
      var ukResult = firstLevelDivisionsObservableList.stream().filter(f -> f.getDivisionID() >= 101).collect(Collectors.toList());
      cbDivID.setItems(ukFirstLevelDivisionsObservableList = FXCollections.observableList(ukResult));
    }

  }

  /**
   * Exits to main menu
   *
   * @param event
   * @throws IOException
   */
  @FXML
  void exitToMainMenu(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(getClass().getResource("/Views/Customer.fxml"));
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  /**
   * Gets Combo Box data for Country and Division
   *
   * @param url
   * @param resourceBundle
   */
  @Override
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
      cbDivID.setItems(FirstLevelDivisionDB.getAllFirstLevelDivisions());
      FirstLevelDivisionDB.allFirstLevelDivisions.stream().map(FirstLevelDivisions::getDivision).forEach(System.out::println);
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
}

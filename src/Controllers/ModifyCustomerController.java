package Controllers;

import DAO.CountriesDB;
import DAO.CustomerDB;
import DAO.FirstLevelDivisionDB;
import Model.Countries;
import Model.Customers;
import Model.FirstLevelDivision;
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

public class ModifyCustomerController implements Initializable {
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
  Long offsetToUTC = (long) (ZonedDateTime.now().getOffset()).getTotalSeconds();

  private Customers modifyCustomerNew;

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
  private ComboBox<FirstLevelDivision> cbDivID;

  public ModifyCustomerController() throws SQLException {
  }


  @FXML
  boolean editCustomer(ActionEvent event) throws SQLException, IOException {
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
      alert.setTitle("Missing selection");
      alert.setContentText("Please ensure all date and time fields are formatted YYYY-MM-DD HH:MM prior to adding an appointment");
      alert.showAndWait();
      return false;
    }

  }

  @FXML
  private void addCustomer(ActionEvent event)
  {

  }

  @FXML
  public void sendCustomer(Customers modifyCustomer) {
    modifyCustomerNew = modifyCustomer;
    custIDTxt.setText(String.valueOf(modifyCustomerNew.getCustomerID()));
    custNameTxt.setText(modifyCustomerNew.getCustomerName());
    custAddressTxt.setText(modifyCustomerNew.getAddress());
    custPostalTxt.setText(modifyCustomerNew.getPostal());
    custPhoneTxt.setText(String.valueOf(modifyCustomerNew.getPhone()));
    lastUpdatedByTF.setText(modifyCustomerNew.getLastUpdatedBy());
    lastUpdateTF.setText(modifyCustomerNew.getLastUpdate().format(formatter));
    createdByTF.setText(modifyCustomerNew.getCreatedBy());
    createDateTF.setText(modifyCustomerNew.getCreateDate().format(formatter));
    int comboBoxPreset = modifyCustomerNew.getDivisionID();
    FirstLevelDivision fld = new FirstLevelDivision(comboBoxPreset);
    cbDivID.setValue(fld);

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
      for (Iterator<Countries> iterator = allCountries.iterator(); iterator.hasNext(); ) {
        Countries countries = iterator.next();
        System.out.println(countries.getCountry());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      cbDivID.setItems(FirstLevelDivisionDB.getAllFirstLevelDivisions());
      ObservableList<FirstLevelDivision> allFirstLevelDivisions = FirstLevelDivisionDB.allFirstLevelDivisions;
      for (int i = 0, allFirstLevelDivisionsSize = allFirstLevelDivisions.size(); i < allFirstLevelDivisionsSize; i++) {
        FirstLevelDivision firstLevelDivision = allFirstLevelDivisions.get(i);
        System.out.println(firstLevelDivision.getDivision());
      }
      cbDivID.setValue(fld);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  ObservableList<FirstLevelDivision> firstLevelDivisionObservableList = FirstLevelDivisionDB.getAllFirstLevelDivisions();
  ObservableList<FirstLevelDivision> usFirstLevelDivisionObservableList = FXCollections.observableArrayList();
  ObservableList<FirstLevelDivision> canadaFirstLevelDivisionObservableList = FXCollections.observableArrayList();
  ObservableList<FirstLevelDivision> ukFirstLevelDivisionObservableList = FXCollections.observableArrayList();


  @FXML
  private void SetDivisionID(ActionEvent event) throws IOException, SQLException {
    if (cbCountry.getSelectionModel().isEmpty()) {
      System.out.println(cbCountry.getSelectionModel().toString());
      return;
    }
    if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("U.S")) {

      var usResult = firstLevelDivisionObservableList.stream().filter(f -> f.getDivisionID() < 54).collect(Collectors.toList());
      cbDivID.setItems(usFirstLevelDivisionObservableList = FXCollections.observableList(usResult));
    } else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("Canada")) {
      var canadaResult = firstLevelDivisionObservableList.stream().filter(f -> (f.getDivisionID() > 54) && (f.getDivisionID() < 101)).collect(Collectors.toList());
      cbDivID.setItems(canadaFirstLevelDivisionObservableList = FXCollections.observableList(canadaResult));
    } else if (cbCountry.getSelectionModel().getSelectedItem().getCountry().equals("UK")) {
      var ukResult = firstLevelDivisionObservableList.stream().filter(f -> f.getDivisionID() >= 101).collect(Collectors.toList());
      cbDivID.setItems(ukFirstLevelDivisionObservableList = FXCollections.observableList(ukResult));
    }

  }


  @FXML
  void exitMainMenu(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Object scene = FXMLLoader.load(getClass().getResource("/Views/Customer.fxml"));
    stage.setScene(new Scene((Parent) scene));
    stage.show();
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

  }
}

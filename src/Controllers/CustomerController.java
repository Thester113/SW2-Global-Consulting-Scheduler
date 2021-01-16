package Controllers;



import DAO.CustomerDB;
import DAO.DBConnection;
import Model.Customers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Customer class where add, edit, and delete are available. A tableview with all Customers is found here.
 */

public class CustomerController implements Initializable {

  @FXML
  private TableView<Customers> custTable;

  @FXML
  private TableColumn<Customers, Integer> custCustomerId;

  @FXML
  private TableColumn<Customers, String> custName;

  @FXML
  private TableColumn<Customers, String> custAddress;

  @FXML
  private TableColumn<Customers, String> custPostal;

  @FXML
  private TableColumn<Customers, String> custPhone;

  @FXML
  private TableColumn<Customers, LocalDateTime> custCreateDate;

  @FXML
  private TableColumn<Customers, String> custCreatedBy;

  @FXML
  private TableColumn<Customers, Timestamp> custLastUpdate;

  @FXML
  private TableColumn<Customers, String> custLastUpdatedBy;

  @FXML
  private TableColumn<Customers, Integer> custDivisionID;

  @FXML
  private Button addCustomer;

  @FXML
  private Button deleteCustomer;

  @FXML
  private Button editCustomer;

  @FXML
  private Button mainButton;

  @FXML
  private Button exitButton;

  @FXML
  private ComboBox<Customers> cbCustomerTable;

  /**
   * Method adds handling for combo box on Customer
   *
   * @param event
   */
  @FXML
  public void onComboBoxSelect(ActionEvent event) {

  }

  /**
   * Auto increments customers
   */
  public CustomerController() {

    try {
      Statement statement = DBConnection.startConnection().createStatement();
      statement.executeUpdate("ALTER TABLE customers AUTO_INCREMENT");
    } catch (SQLException ce) {
      Logger.getLogger(ce.toString());
    }
  }


  /**
   * Sets the values of the tableview from the MySQL DB
   * Try/Catch block runs a lambda for each to get all objects
   * Lambda Expression improves code efficiency and makes getting customers data quicker
   *
   * @param url
   * @param resourceBundle
   */

  @FXML
  public void initialize(URL url, ResourceBundle resourceBundle) {
    //Customer Table
    custCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerID"));
    custName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
    custAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
    custPostal.setCellValueFactory(new PropertyValueFactory<>("postal"));
    custPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    custCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
    custCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    custLastUpdate.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
    custLastUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
    custDivisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

    try {
      custTable.setItems(CustomerDB.getAllCustomers());

      for (Customers customer : CustomerDB.allCustomers) {
        System.out.println(customer.getCustomerName());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Exits to main screen from Customer
   *
   * @param event
   */
  @FXML
  void exitToApp(ActionEvent event) {
    Button sourceButton = (Button) event.getSource();
    exitButton.setText(sourceButton.getText());
    DBConnection.closeConnection();
    System.exit(0);
  }

  /**
   * Provides access to add a new customer to the DB
   *
   * @param event
   * @throws IOException
   */

  @FXML
  void sceneAddCustomer(ActionEvent event) throws IOException {

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/Views/AddCustomer.fxml"));
    loader.load();

    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Parent scene = loader.getRoot();
    stage.setScene(new Scene(scene));
    stage.show();
  }

  /**
   * Deletes a customer
   *
   * @param event
   * @throws SQLException
   * @throws IOException
   */

  @FXML
  void sceneDeleteCustomer(ActionEvent event) throws SQLException, IOException {
    try {
      Customers selectedItem = custTable.getSelectionModel().getSelectedItem();
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setHeaderText("Warning!");
      alert.setHeaderText("All appointments associated for " + selectedItem.getCustomerName() + " will be deleted");
      alert.setContentText("Are you sure you want to delete the customer?");

      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == ButtonType.OK) {
        CustomerDB.deleteCustomer(selectedItem.getCustomerID());
        System.out.println("Deletion Successful!");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/Customer.fxml"));
        Parent parent = loader.load();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

      }
    } catch (IOException e) {
      String s = "a customer";
    }

  }

  /**
   * Loads main screen
   *
   * @param event
   * @throws IOException
   */
  @FXML
  void sceneBackToMain(ActionEvent event) throws IOException {

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("/Views/MainScreen.fxml"));
    loader.load();

    Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    Parent scene = loader.getRoot();
    stage.setScene(new Scene(scene));
    stage.show();
  }

  /**
   * Creates an edit scene upon user selection of an item from the table view, if no selection is made an alert pops up requesting a selection
   *
   * @param event
   * @throws IOException
   */

  @FXML
  void sceneEditCustomer(ActionEvent event) throws IOException {
    try {
      Customers modifyCustomer = custTable.getSelectionModel().getSelectedItem();
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/Views/ModifyCustomer.fxml"));
      Parent parent = loader.load();

      Scene modifyCustomerScene = new Scene(parent);
      ModifyCustomerController controller = loader.getController();
      controller.passCustomer(modifyCustomer);

      Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
      window.setScene(modifyCustomerScene);
      window.setResizable(false);
      window.show();
    } catch (NullPointerException e) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setHeaderText("Select a Customer");
      alert.setHeaderText("Select a customer to edit");
      alert.setContentText("No customer has been selected!");
    }

  }


}


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
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

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

  public CustomerController() {

    try {
      Statement statement = DBConnection.startConnection().createStatement();
      statement.executeUpdate("ALTER TABLE customers AUTO_INCREMENT");
    } catch (SQLException ce) {
      Logger.getLogger(ce.toString());
    }
  }

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
      ObservableList<Customers> allCustomers = CustomerDB.allCustomers;
      for (Iterator<Customers> iterator = allCustomers.iterator(); iterator.hasNext(); ) {
        Customers customer = iterator.next();
        System.out.println(customer.getCustomerName());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void exitApp(ActionEvent event) {
    Button sourceButton = (Button) event.getSource();
    exitButton.setText(sourceButton.getText());
    DBConnection.closeConnection();
    System.exit(0);
  }

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

  @FXML
  void sceneDeleteCustomer(ActionEvent event) throws SQLException, IOException {
    try {
      Customers selectedItem = custTable.getSelectionModel().getSelectedItem();
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setHeaderText("Warning");
      alert.setHeaderText("All associated appointments for " + selectedItem.getCustomerName() + " will be deleted");
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
      alert.setHeaderText("Please select a customer to edit");
      alert.setContentText("No customer selected!");
    }

  }

  @FXML
  void onComboBoxSelect(ActionEvent event) {

  }
}


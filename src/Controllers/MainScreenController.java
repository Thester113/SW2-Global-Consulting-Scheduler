package Controllers;
import DAO.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    /**
     * Access to customers, appointments, and reports
     */
    @FXML
    private Button customerAppointmentReport;

    @FXML
    private Button contactSchedulesReport;

    @FXML
    private Button customerSchedulesBtn;

    @FXML
    private Button customerBtn;

    @FXML
    private Button aptBtn;

    @FXML
    private Button exitBtn;

    @FXML
    private Button logoutButton;

    @FXML
    private Button exitButton;

    /**
     * Exit the application
     * @param event
     */
    @FXML
    void exitApp(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }

    /**
     * Access to the appointment table
     * @param event
     * @throws IOException
     */
    @FXML
    void sceneAptMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/AppointmentMain.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**
     * Access to the customer table
     * @param event
     * @throws IOException
     */
    @FXML
    void sceneCustMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerMain.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Access to the CustomerSchedules report
     * Select a customer
     * @param event
     * @throws IOException
     */
    @FXML
    void sceneCustomerSchedulesReport(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerSchedulesReport.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }
    /**
     * Access to the Contact Schedules report
     * Select a contact
     * @param event
     * @throws IOException
     */
    @FXML
    void sceneContactSchedulesReport(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/ContactSchedulesReport.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * Access to the Customer AppointmentSchedules report
     * Gives the total by month and by type
     * @param event
     * @throws IOException
     */
    @FXML
    void sceneCustomerAppointmentReport(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/CustomerAppointmentsReportView.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    void sceneLogout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/View/Login.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    public void sceneLogin(ActionEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}


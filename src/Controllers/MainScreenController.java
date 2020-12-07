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


    @FXML
    void sceneAptMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/Views/AppointmentMain.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    void sceneCustMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/Views/CustomerMain.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


    @FXML
    void sceneCustomerSchedulesReport(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/Views/CustomerSchedulesReport.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    void sceneContactSchedulesReport(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/Views/ContactSchedulesReport.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }


    @FXML
    void sceneCustomerAppointmentReport(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/Views/CustomerAppointmentsReportView.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    @FXML
    void sceneLogout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    public void sceneLogin(ActionEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}


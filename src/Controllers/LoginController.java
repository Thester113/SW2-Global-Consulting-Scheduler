package Controllers;

import Model.Appointment;
import DAO.AppointmentDB;
import DAO.DBConnection;
import DAO.UsersDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Alert alert;
    private String loginError;
    private String enterCorrectUorP;
    private String errorHeader;

    @FXML
    private Button exitButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label detectedLoc;

    @FXML
    private Label GCO;

    @FXML
    private Label UserIDLabel;

    @FXML
    private Label EnterPasswordLabel;

    @FXML
    private TextField userIDField;

    @FXML
    private PasswordField passwordField;

    //Cases for login are either a successful login or incorrect username or password, username and password are from DB
    @FXML
    void handleLogin(ActionEvent event) throws IOException, SQLException {
        //Get username and password entered
        String username = userIDField.getText();
        String password = passwordField.getText();

        //verify user against database users, see DBQuery login for logic
        boolean verifiedUser = UsersDB.login(username, password);
        if (verifiedUser) {
            boolean isFound = true;
            AppointmentDB.getAllAppointments();
            //Run a foreach lamda loop through the observable list of all the appointments and return an alert
            for (Appointment appointment : AppointmentDB.allAppointments) {
                LocalDateTime within15Minutes = LocalDateTime.now();
                isFound = true;
                //Compare the system time to the appointment start times and see if start is within 15-1 minute(s) of all start times
                if (within15Minutes.isAfter(appointment.getStart().minusMinutes(15)) && within15Minutes.isBefore(appointment.getStart())){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("UPCOMING APPOINTMENT");
                    alert.setContentText("Appointment: " + appointment.getAppointmentID() + " starts at " + appointment.getStart());
                    alert.showAndWait();
                    isFound = true;
                    break;
                }
                else {
                    isFound = false;
                }
            }
            if (!isFound) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No upcoming appointments!");
                alert.setContentText("You have no upcoming appointments in the next 15 minutes");
                alert.showAndWait();
            }

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/Views/MainScreen.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
        else if (!verifiedUser || userIDField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(loginError);
            alert.setHeaderText(errorHeader);
            alert.setContentText(enterCorrectUorP);
            alert.showAndWait();
        }

    }

    @FXML
    void handleExit(ActionEvent event) throws IOException {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        detectedLoc.setText(Locale.getDefault().getLanguage());
        try {
            rb = ResourceBundle.getBundle("Utilities", Locale.getDefault());

            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                GCO.setText(rb.getString("GCO"));
                UserIDLabel.setText(rb.getString("UserIDLabel"));
                EnterPasswordLabel.setText(rb.getString("EnterPasswordLabel"));
                loginButton.setText(rb.getString("Login"));
                exitButton.setText(rb.getString("Exit"));
                loginError = rb.getString("loginError");
                enterCorrectUorP = rb.getString("enterCorrectUorP");
                errorHeader = rb.getString(errorHeader);

            }
        } catch (Exception e) {
        }
    }
}
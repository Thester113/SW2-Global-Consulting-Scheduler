package Controllers;



import Model.Appointments;
import DAO.AppointmentDB;
import DAO.DBConnection;
import DAO.UsersDB;
import javafx.collections.ObservableList;
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

/**
 * Login Controller verifies the user exists int he DB
 * Pulls login attempts and logs them them in login_activity.txt. SHows both failed and successful logins
 * Username is captured and used to auto fill the created by and last updated by fields on adding an appointment or customer.
 */
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
    private Label GC;

    @FXML
    private Label UserIDLabel;

    @FXML
    private Label EnterPasswordLabel;

    @FXML
    private TextField userIDField;

    @FXML
    private PasswordField passwordField;

    /**
     * Cases for login are either a successful login or incorrect username or password, username and password are from DB
     */
    @FXML
    void handlerLogin(ActionEvent event) throws IOException, SQLException {

        String username = userIDField.getText();
        String password = passwordField.getText();


        boolean foundUser = UsersDB.login(username, password);
        if (foundUser) {
            boolean isFound = true;
            AppointmentDB.getAllAppointments();
            //foreach lambda loop that runs through observable list

            for (Appointments appointments : AppointmentDB.allAppointments) {
                LocalDateTime within15Minutes = LocalDateTime.now();
                isFound = true;
                // 15-1 minute(s) of all start times
                if (within15Minutes.isAfter(appointments.getStart().minusMinutes(15)) && within15Minutes.isBefore(appointments.getStart())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("UPCOMING APPOINTMENT");
                    alert.setContentText("Appointment: " + appointments.getAppointmentID() + " starts at " + appointments.getStart());
                    alert.showAndWait();
                    isFound = true;
                    break;
                } else {
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
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(loginError);
            alert.setHeaderText(errorHeader);
            alert.setContentText(enterCorrectUorP);
            alert.showAndWait();
        }

    }

    /**
     * Exits application from Login Screen
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void handlerExit(ActionEvent event) throws IOException {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }

    /**
     * Sets based on location of user to either French or English Language
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detectedLoc.setText(Locale.getDefault().getLanguage());
        try {
            resourceBundle = ResourceBundle.getBundle("Utilities/lang", Locale.getDefault());

            String language = Locale.getDefault().getLanguage();
            if ("fr".equals(language) || "en".equals(language)) {
                GC.setText(resourceBundle.getString("GC"));
                UserIDLabel.setText(resourceBundle.getString("UserIDLabel"));
                EnterPasswordLabel.setText(resourceBundle.getString("EnterPasswordLabel"));
                loginButton.setText(resourceBundle.getString("Login"));
                exitButton.setText(resourceBundle.getString("Exit"));
                loginError = resourceBundle.getString("loginError");
                enterCorrectUorP = resourceBundle.getString("enterCorrectUorP");
                errorHeader = resourceBundle.getString(errorHeader);
            }
        } catch (Exception e) {
        }
    }
}
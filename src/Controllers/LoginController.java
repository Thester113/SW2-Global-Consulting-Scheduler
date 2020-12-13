package Controllers;

import Model.Appointment;
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


    @FXML
    void handlerLogin(ActionEvent event) throws IOException, SQLException {

        String username = userIDField.getText();
        String password = passwordField.getText();


        boolean foundUser = UsersDB.login(username, password);
        if (foundUser) {
            boolean isFound = true;
            AppointmentDB.getAllAppointments();
            //foreach lambda loop
            ObservableList<Appointment> allAppointments = AppointmentDB.allAppointments;
            for (int i = 0, allAppointmentsSize = allAppointments.size(); i < allAppointmentsSize; i++) {
                Appointment appointment = allAppointments.get(i);
                LocalDateTime within15Minutes = LocalDateTime.now();
                isFound = true;
                // 15-1 minute(s) of all start times
                if (within15Minutes.isAfter(appointment.getStart().minusMinutes(15)) && within15Minutes.isBefore(appointment.getStart())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("UPCOMING APPOINTMENT");
                    alert.setContentText("Appointment: " + appointment.getAppointmentID() + " starts at " + appointment.getStart());
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
            if (foundUser && !userIDField.getText().isEmpty()) {
                passwordField.getText();
            }
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(loginError);
            alert.setHeaderText(errorHeader);
            alert.setContentText(enterCorrectUorP);
            alert.showAndWait();
        }

    }

    @FXML
    void handlerExit(ActionEvent event) throws IOException {
        Button sourceButton = (Button) event.getSource();
        exitButton.setText(sourceButton.getText());
        DBConnection.closeConnection();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        detectedLoc.setText(Locale.getDefault().getLanguage());
        try {
            rb = ResourceBundle.getBundle("Utilities/lang", Locale.getDefault());

            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                GCO.setText(rb.getString("GC"));
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
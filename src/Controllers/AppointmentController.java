package Controllers;
/**
 * Appointment class where add, edit, and delete are available. A tableview with all Appointments can be found here
 */


import DAO.AppointmentDB;
import DAO.DBConnection;
import Model.Appointments;
import javafx.collections.FXCollections;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static DAO.AppointmentDB.deleteAppointment;

public class AppointmentController implements Initializable {

    ObservableList<Appointments> aptList = AppointmentDB.getAllAppointments();
    ObservableList<Appointments> weeklyAppointmentsList = FXCollections.observableArrayList();
    ObservableList<Appointments> monthlyAppointmentsList = FXCollections.observableArrayList();
    /**
     * @param allAptsRb displays all appointments
     */

    @FXML
    private RadioButton allAptsRB;
    @FXML
    private ToggleGroup aptTableTGLGRP;
    /**
     * @param weeklyRB shows appointments by week
     */

    @FXML
    private RadioButton weeklyRB;
    /**
     * @param monthlyRB shows appointments by month
     */


    @FXML
    private RadioButton monthlyRB;
    @FXML
    private TableView<Appointments> aptTable;
    @FXML
    private TableColumn<Appointments, Integer> aptAppointmentID;
    @FXML
    private TableColumn<Appointments, String> aptTitle;
    @FXML
    private TableColumn<Appointments, String> aptDescription;
    @FXML
    private TableColumn<Appointments, String> aptLocation;
    @FXML
    private TableColumn<Appointments, String> aptType;
    @FXML
    private TableColumn<Appointments, LocalDateTime> aptStart;
    @FXML
    private TableColumn<Appointments, LocalDateTime> aptEnd;
    @FXML
    private TableColumn<Appointments, LocalDateTime> aptCreateDate;
    @FXML
    private TableColumn<Appointments, String> aptCreatedBy;
    @FXML
    private TableColumn<Appointments, LocalDateTime> aptLastUpdate;
    @FXML
    private TableColumn<Appointments, String> aptLastUpdatedBy;
    @FXML
    private TableColumn<Appointments, Integer> aptCID;
    @FXML
    private TableColumn<Appointments, Integer> aptUID;
    @FXML
    private TableColumn<Appointments, Integer> aptContID;
    @FXML
    private Button addAppointment;
    @FXML
    private Button editAppointment;
    @FXML
    private Button deleteAppointment;
    @FXML
    private Button menuButton;
    @FXML
    private Button exitButton;

    public AppointmentController() throws SQLException {
    }

    @FXML
    void sceneMainMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/MainScreen.fxml"));
        loader.load();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Utilizing a for each lambda loop to reduce amount of code used
     *
     * @param event
     * @throws SQLException
     */

    @FXML
    void rescBundallAppointmentsOA(ActionEvent event) throws SQLException {

        try {
            aptTable.setItems(AppointmentDB.getAllAppointments());
            /**
             * Lambda expression using a for each
             */

            for (Appointments appointments : AppointmentDB.allAppointments) {
                System.out.println(appointments.getStart());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void rescBundweeklyAppointmentsOA(ActionEvent event) throws SQLException {

        LocalDate today = LocalDate.from(ZonedDateTime.now());
        LocalDate oneWeekFromToday = LocalDate.from(ZonedDateTime.now()).plusWeeks(1);


        if ((this.aptTableTGLGRP.getSelectedToggle().equals(this.weeklyRB))) {

            Predicate<Appointments> weeklyView = appointment -> (appointment.getStart().toLocalDate().equals(today))
                    || appointment.getStart().toLocalDate().isAfter(today)
                    && appointment.getStart().toLocalDate().isBefore((oneWeekFromToday));
            System.out.println(today);

            var weeklyResult = aptList.stream().filter(weeklyView).collect(Collectors.toList());

            aptTable.setItems(weeklyAppointmentsList = FXCollections.observableList(weeklyResult));
        }
    }

    /**
     * Uses monthly radio button selection to show only appointment
     *
     * @param event
     */

    @FXML
    void rescBundmonthlyAppointmentsOA(ActionEvent event) {


        LocalDate today = LocalDate.from(ZonedDateTime.now());
        LocalDate oneMonthFromToday = LocalDate.from(ZonedDateTime.now()).plusMonths(1);
        if ((this.aptTableTGLGRP.getSelectedToggle().equals(this.monthlyRB))) {
            Predicate<Appointments> monthlyView = appointment -> (appointment.getStart().toLocalDate().equals(today))
                    || appointment.getStart().toLocalDate().isAfter((today))
                    && appointment.getStart().toLocalDate().isBefore((oneMonthFromToday));
            System.out.println(today);

            var monthList = aptList.stream().filter(monthlyView).collect(Collectors.toList());

            aptTable.setItems(monthlyAppointmentsList = FXCollections.observableList(monthList));
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
    void sceneAddAppointment(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/AddAppointment.fxml"));
        loader.load();

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void sceneDeleteAppointment(ActionEvent event) {
        try {

            Appointments selectedItem = aptTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Warning");
            alert.setHeaderText("Delete appointment type: " + selectedItem.getType() + " ID Number: " + selectedItem.getAppointmentID() + " ?");
            alert.setContentText("Are you sure you want to delete the appointment?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteAppointment(selectedItem.getAppointmentID());
                System.out.println("Appointment: " + selectedItem.getAppointmentID() + " Successful!");
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Views/Appointment.fxml"));
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
    void sceneEditAppointment(ActionEvent event) throws IOException {
        try {

            Appointments modifyAppointments = aptTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Views/ModifyAppointment.fxml"));
            Parent parent = loader.load();
            Scene modifyCustomerScene = new Scene(parent);

            ModifyAppointmentController controller = loader.getController();
            controller.sendAppointment(modifyAppointments);

            Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
            window.setScene(modifyCustomerScene);
            window.setResizable(false);
            window.show();
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing selection");
            alert.setContentText("Please select an appointment you would like to edit.");
            alert.showAndWait();
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Appointments Table
        aptAppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        aptLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        aptDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        aptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        aptStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        aptEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        aptCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        aptCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        aptLastUpdate.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        aptLastUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        aptCID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        aptUID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        aptContID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        try {
            aptTable.setItems(AppointmentDB.getAllAppointments());

            ObservableList<Appointments> allAppointments = AppointmentDB.allAppointments;
            /**
             *  For each lambda to get appointments
             *  helps reduce code
             */
            for (Appointments appointments : allAppointments) {
                System.out.println(appointments.getStart());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

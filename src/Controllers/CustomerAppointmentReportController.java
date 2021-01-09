package Controllers;
/**
 * Combo Box is used to select a customer then sets a table view with the selected customer's appointments.
 */


import DAO.AppointmentDB;
import DAO.DBConnection;
import Model.Appointments;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Month;
import java.util.Iterator;
import java.util.ResourceBundle;

public class CustomerAppointmentReportController implements Initializable {
  @FXML
  private Button menuButton;

  @FXML
  private Button exitButton;

  @FXML
  private TextField monthJan;

  @FXML
  private TextField monthFeb;

  @FXML
  private TextField monthMar;

  @FXML
  private TextField monthApr;

  @FXML
  private TextField monthMay;

  @FXML
  private TextField monthJun;

  @FXML
  private TextField monthJul;

  @FXML
  private TextField monthAug;

  @FXML
  private TextField monthSep;

  @FXML
  private TextField monthOct;

  @FXML
  private TextField monthNov;

  @FXML
  private TextField monthDec;

  @FXML
  private TextField planningTF;

  @FXML
  private TextField deBriefingTF;


  @FXML
  void exitToApp(ActionEvent event) {
    Button sourceButton = (Button) event.getSource();
    exitButton.setText(sourceButton.getText());
    DBConnection.closeConnection();
    System.exit(0);
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

  @FXML
  public void initialize(URL url, ResourceBundle resourceBundle) {
    try {
      int planningSessionCount = 0;
      int deBrief = 0;
      int janCount = 1;
      int febCount = 1;
      int marCount = 1;
      int aprCount = 1;
      int mayCount = 1;
      int junCount = 1;
      int julCount = 1;
      int augCount = 1;
      int sepCount = 1;
      int octCount = 1;
      int novCount = 1;
      int decCount = 1;
      ObservableList<Appointments> allAppointments = AppointmentDB.allAppointments;
      for (Iterator<Appointments> iterator = allAppointments.iterator(); iterator.hasNext(); ) {
        Appointments appointments = iterator.next();
        String aptType = appointments.getType();
        {
          if (aptType.contains("Planning Session")) {
            planningSessionCount = planningSessionCount + 1;
            planningTF.setText(String.valueOf(planningSessionCount));
          } else if (aptType.contains("De-Briefing")) {
            deBrief = deBrief + 1;
            deBriefingTF.setText(String.valueOf(deBrief));
          }
        }


      }
      ObservableList<Appointments> appointments = AppointmentDB.allAppointments;
      for (Iterator<Appointments> iterator = appointments.iterator(); iterator.hasNext(); ) {
        Appointments appointment = iterator.next();
        Month aptStart = appointment.getStart().getMonth();
        switch (aptStart) {
          case JANUARY:
            janCount = janCount + 1;
            monthJan.setText(String.valueOf(janCount));

            break;
          case FEBRUARY:
            febCount = febCount + 1;
            monthFeb.setText(String.valueOf(febCount));

            break;
          case MARCH:
            marCount = marCount + 1;
            monthMar.setText(String.valueOf(marCount));

            break;
          case APRIL:
            aprCount = aprCount + 1;
            monthApr.setText(String.valueOf(aprCount));
            break;
          case MAY:
            mayCount = mayCount + 1;
            monthMay.setText(String.valueOf(mayCount));
            break;
          case JUNE:
            junCount = junCount + 1;
            monthJun.setText(String.valueOf(junCount));
            break;
          case JULY:
            julCount = julCount + 1;
            monthJul.setText(String.valueOf(julCount));
            break;
          case AUGUST:
            augCount = augCount + 1;
            monthAug.setText(String.valueOf(augCount));
            break;
          case SEPTEMBER:
            sepCount = sepCount + 1;
            monthSep.setText(String.valueOf(sepCount));

            break;
          case OCTOBER:
            octCount++;
            monthOct.setText(String.valueOf(octCount));

            break;
          case NOVEMBER:
            novCount = novCount + 1;
            monthNov.setText(String.valueOf(novCount));

            break;
          case DECEMBER:
            decCount = decCount + 1;
            monthDec.setText(String.valueOf(decCount));

            break;
        }
        return;

      }

      AppointmentDB.getAllAppointments();



    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}


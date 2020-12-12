package Controllers;

import DAO.AppointmentDB;
import DAO.DBConnection;
import Model.Appointment;
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
  private TextField numJan;

  @FXML
  private TextField numFeb;

  @FXML
  private TextField numMar;

  @FXML
  private TextField numApr;

  @FXML
  private TextField numMay;

  @FXML
  private TextField numJun;

  @FXML
  private TextField numJul;

  @FXML
  private TextField numAug;

  @FXML
  private TextField numSep;

  @FXML
  private TextField numOct;

  @FXML
  private TextField numNov;

  @FXML
  private TextField numDec;

  @FXML
  private TextField planningTF;

  @FXML
  private TextField deBriefingTF;


  @FXML
  void exitApp(ActionEvent event) {
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
      ObservableList<Appointment> allAppointments = AppointmentDB.allAppointments;
      for (Iterator<Appointment> iterator = allAppointments.iterator(); iterator.hasNext(); ) {
        Appointment appointment = iterator.next();
        String aptType = appointment.getType();
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
      ObservableList<Appointment> appointments = AppointmentDB.allAppointments;
      for (Iterator<Appointment> iterator = appointments.iterator(); iterator.hasNext(); ) {
        Appointment appointment = iterator.next();
        Month aptStart = appointment.getStart().getMonth();
        switch (aptStart) {
          case JANUARY:
            janCount = janCount + 1;
            numJan.setText(String.valueOf(janCount));

            break;
          case FEBRUARY:
            febCount = febCount + 1;
            numFeb.setText(String.valueOf(febCount));

            break;
          case MARCH:
            marCount = marCount + 1;
            numMar.setText(String.valueOf(marCount));

            break;
          case APRIL:
            aprCount = aprCount + 1;
            numApr.setText(String.valueOf(aprCount));
            break;
          case MAY:
            mayCount = mayCount + 1;
            numMay.setText(String.valueOf(mayCount));
            break;
          case JUNE:
            junCount = junCount + 1;
            numJun.setText(String.valueOf(junCount));
            break;
          case JULY:
            julCount = julCount + 1;
            numJul.setText(String.valueOf(julCount));
            break;
          case AUGUST:
            augCount = augCount + 1;
            numAug.setText(String.valueOf(augCount));
            break;
          case SEPTEMBER:
            sepCount = sepCount + 1;
            numSep.setText(String.valueOf(sepCount));

            break;
          case OCTOBER:
            octCount++;
            numOct.setText(String.valueOf(octCount));

            break;
          case NOVEMBER:
            novCount = novCount + 1;
            numNov.setText(String.valueOf(novCount));

            break;
          case DECEMBER:
            decCount = decCount + 1;
            numDec.setText(String.valueOf(decCount));

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


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
      for (int i = 0, allAppointmentsSize = allAppointments.size(); i < allAppointmentsSize; i++) {
        Appointment appointment = allAppointments.get(i);
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
      for (int i = 0, appointmentsSize = appointments.size(); i < appointmentsSize; i++) {
        Appointment appointment = appointments.get(i);
        Month aptStart = appointment.getStart().getMonth();
        if (aptStart == Month.JANUARY) {
          janCount = janCount + 1;
          numJan.setText(String.valueOf(janCount));

        } else if (aptStart == Month.FEBRUARY) {
          febCount = febCount + 1;
          numFeb.setText(String.valueOf(febCount));

        } else if (aptStart == Month.MARCH) {
          marCount = marCount + 1;
          numMar.setText(String.valueOf(marCount));

        } else if (aptStart == Month.APRIL) {
          aprCount = aprCount + 1;
          numApr.setText(String.valueOf(aprCount));
        } else if (aptStart == Month.MAY) {
          mayCount = mayCount + 1;
          numMay.setText(String.valueOf(mayCount));
        } else if (aptStart == Month.JUNE) {
          junCount = junCount + 1;
          numJun.setText(String.valueOf(junCount));
        } else if (aptStart == Month.JULY) {
          julCount = julCount + 1;
          numJul.setText(String.valueOf(julCount));
        } else if (aptStart == Month.AUGUST) {
          augCount = augCount + 1;
          numAug.setText(String.valueOf(augCount));
        } else if (aptStart == Month.SEPTEMBER) {
          sepCount = sepCount + 1;
          numSep.setText(String.valueOf(sepCount));

        } else if (aptStart == Month.OCTOBER) {
          octCount++;
          numOct.setText(String.valueOf(octCount));

        } else if (aptStart == Month.NOVEMBER) {
          novCount = novCount + 1;
          numNov.setText(String.valueOf(novCount));

        } else if (aptStart == Month.DECEMBER) {
          decCount = decCount + 1;
          numDec.setText(String.valueOf(decCount));

        }
        return;

      }

      AppointmentDB.getAllAppointments();



    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}


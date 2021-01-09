package Model;


import DAO.DBConnection;
import DAO.DBQuery;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {
  public static void main(String[] args) throws SQLException {
    //Sets up the DB connection
    Connection connection = DBConnection.startConnection();
    DBQuery.setStatement(connection);
    Statement statement = DBQuery.getStatement();
    ResultSet resultSet = statement.getResultSet();


    launch(args);

    DBConnection.closeConnection();

  }

  /**
   * Goes to main menu where you can select customers, appointments, and the reports for viewing
   *
   * @param primaryStage
   * @throws IOException
   */

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/Views/Login.fxml"));
    primaryStage.setTitle("Global Scheduler Application");
    primaryStage.setScene(new Scene(root, 600, 600));
    primaryStage.setResizable(false);
    primaryStage.show();
  }
}
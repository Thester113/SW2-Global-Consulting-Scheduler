package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
  //JDBC URL
  private static final String protocol = "jdbc";
  private static final String vendorName = ":MySQL:";
  private static final String ipAddress = "//wgudb.ucertify.com/WJ06JAg";

  //JDBC URL
  private static final String jdbcURL = protocol + vendorName + ipAddress;

  //Driver Interface Reference
  private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
  static Connection connection = null;

  private static final String username = "U06JAg"; //Username
  private static final String password = "53688779415"; //Password

  //Methods
  public static Connection startConnection() throws SQLException {
    try
    {
      Class.forName(MYSQLJDBCDriver);
      connection = (Connection)DriverManager.getConnection(jdbcURL, username, password);
      System.out.println("Connection is successful!");
    }
    catch (ClassNotFoundException e)
    {
      System.out.println(e.getMessage());
    }

    return connection;
  }

  public static void closeConnection()
  {
    try {
      connection.close();
      System.out.println("Connection is closed!");
    }
    catch (SQLException e)
    {
      System.out.println("Error:" + e.getMessage());
    }
  }
}
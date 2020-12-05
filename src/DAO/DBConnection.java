package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Used for the Utility classes to connect to the Database
 */
public class DBConnection
{
  //JDBC URL
  private static final String protocol = "jdbc";
  private static final String vendorName = ":MySQL:";
  private static final String ipAddress = "//wgudb.ucertify.com/<DB Name>";

  //JDBC URL
  private static final String jdbcURL = protocol + vendorName + ipAddress;

  //Driver Interface Reference
  private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
  static Connection conn = null;

  private static final String username = ""; //Username
  private static final String password = ""; //Password

  //Methods
  public static Connection startConnection()
  {
    try
    {
      Class.forName(MYSQLJDBCDriver);
      conn = (Connection)DriverManager.getConnection(jdbcURL, username, password);
      System.out.println("Connection successful!");
    }
    catch (ClassNotFoundException e)
    {
      System.out.println(e.getMessage());
    }

    catch (SQLException e)
    {
      System.out.println(e.getMessage());
    }

    return conn;
  }

  public static void closeConnection()
  {
    try {
      conn.close();
      System.out.println("Connection closed!");
    }
    catch (SQLException e)
    {
      System.out.println("Error:" + e.getMessage());
    }
  }
}
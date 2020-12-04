package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Tom Hester
 */

public class DBConnection
{
  private static Connection dbconnection;
  private static final String DB_NAME = "WJ06JAg";
  private static final String USER_NAME = "U06JAg";
  private static final String DB_PASS = "53688779415";
  private static final String DB_URL = "jdbc:mysql://wgudb.ucertify.com/" + DB_NAME;
  private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

  // Returns database connection
  public static Connection ConnectionObject()
  {
    return dbconnection;
  }

  // Connect to database
  public static void dbConnect()
  {
    try
    {
      Class.forName(DB_DRIVER);
      dbconnection = DriverManager.getConnection(DB_URL, USER_NAME, DB_PASS);
      System.out.println("Connected to DB");
    }
    catch(SQLException exception)
    {
      System.out.println("SQL Exception: " + exception.getMessage() + " and SQL State: " + exception.getSQLState());
      System.out.println("Error Code: " + exception.getErrorCode());
    }
    catch(ClassNotFoundException exception)
    {
      System.out.println(exception.getMessage());
    }
  }

  // Close db connection
  public static void dbDisconnect()
  {
    try
    {
      dbconnection.close();
      System.out.println("Disconnected from DB");
    }
    catch (SQLException exception)
    {
      System.out.println("SQL Exception: " + exception.getMessage());
    }
  }
}
package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Used to query the database
 */
public class DBQuery {
    public static Statement statement;
    //Create Statement Object
    public static void setStatement(Connection connect) throws SQLException {
        statement = connect.createStatement();
    }

    //Return Statement Object
    public static Statement getStatement()
    {
        return statement;
    }




}

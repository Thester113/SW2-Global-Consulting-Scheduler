package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DBQuery {
    public static Statement statement;

    public static void setStatement(Connection connect) throws SQLException {
        statement = connect.createStatement();
    }


    public static Statement getStatement()
    {
        return statement;
    }




}

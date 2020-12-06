package DAO;

import Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDB {

    //Login Verification
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Boolean login(String username, String password) {
        //Looking at attributes username and password from textfield entered and verifying against DB
        //Taking the username, timestamps, dates and success/failure to logger
        try {
            String query = "SELECT * FROM users WHERE User_Name='" + username + "'AND Password='" + password + "'";
            //Connecting to DB and executing query for username = DB Column User_Name and same for Password
            ResultSet rs = DBQuery.statement.executeQuery(query);
            //Creating User object and using setters for object
            if (rs.next()) {
                currentUser = new User();
                currentUser.setUserName(rs.getString("User_Name"));
                currentUser.setPassword(rs.getString("Password"));
                Logger.log(username, true);
                return true;
            } else{
                Logger.log(username, false);
                return false;
            }

        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return false;
        }
    }
}

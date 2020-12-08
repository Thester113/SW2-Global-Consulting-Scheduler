package DAO;

import Model.Users;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UsersDB {


    private static Users currentUsers;

    public static Users getCurrentUser() {
        return currentUsers;
    }

    public static Boolean login(String username, String password) {

        try {
            String query = "SELECT * FROM users WHERE User_Name='" + username + "'AND Password='" + password + "'";

            ResultSet rs = DBQuery.statement.executeQuery(query);

            if (rs.next()) {
                currentUsers = new Users();
                currentUsers.setUserName(rs.getString("User_Name"));
                currentUsers.setPassword(rs.getString("Password"));
                Logger.log(username, true);
                return true;
            } else{
                Logger.log(username, false);
                return false;
            }

        }
        catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
    }
}

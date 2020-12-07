package DAO;

import Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDB {


    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static Boolean login(String username, String password) {

        try {
            String query = "SELECT * FROM users WHERE User_Name='" + username + "'AND Password='" + password + "'";

            ResultSet rs = DBQuery.statement.executeQuery(query);

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

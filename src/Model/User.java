package Model;

import DAO.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;

/**
 * User class is used for log in purposes and log activity
 */
public class User {
    Integer userID;
    static String username;
    String password;
    Date createDate;
    String createdBy;
    Timestamp lastUpdate;
    String lastUpdatedBy;

    public User() {

    }


    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public static String getUsername() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public User(Integer userID, String username, String password, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }
    //Contact Table
    ObservableList<Contacts> contList = FXCollections.observableArrayList();
    public void UserMain() {
        try {
            Connection conn = DBConnection.startConnection();
            ResultSet rc = conn.createStatement().executeQuery("select * FROM contacts");
            while (rc.next()) {
                contList.add(new Contacts(rc.getInt("Contact_ID"), rc.getString("Contact_Name"), rc.getString("Email")));
            }
        }
        catch (
                SQLException e) {
            Logger.getLogger(e.toString());
        }

    }


}

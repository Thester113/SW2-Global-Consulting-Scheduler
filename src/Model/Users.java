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
 * Users class for log in purposes and to log activity
 */
public class Users {
    static String userName;
    Integer userID;
    String password;
    Date createDate;
    String createdBy;
    Timestamp lastUpdate;
    String lastUpdatedBy;



    public Users() {

    }

    public Users(Integer userID, String userName, String password, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.userID = userID;
        Users.userName = userName;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public static String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        Users.userName = userName;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
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

  //Contact Table
  ObservableList<Contacts> contList = FXCollections.observableArrayList();

  public void UsersMain() {
    try {
      Connection conn = DBConnection.startConnection();
      ResultSet rc = conn.createStatement().executeQuery("SELECT * FROM contacts");
      while (rc.next()) {
        contList.add(new Contacts(rc.getInt("Contact_ID"), rc.getString("Contact_Name"), rc.getString("Email")));
      }
    } catch (
            SQLException e) {
      Logger.getLogger(e.toString());
        }

    }


}

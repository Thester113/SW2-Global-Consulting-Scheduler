package Model;


import java.time.LocalDateTime;

/**
 * Customer Class for the addition, editing, and deletion of customers
 * Contains getters/setters
 */
public class Customers {

  public int customerID;
  public String customerName;
  private String address;
  private String postal;
  private String phone;
  private LocalDateTime createDate;
  private String createdBy;
  private LocalDateTime lastUpdate;
  private String lastUpdatedBy;
  private int divisionID;



  public Customers(int customerID, String customerName, String address, String postalCode, String phone, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionID) {
    this.customerID=customerID;
    this.customerName=customerName;
    this.address=address;
    this.postal=postalCode;
    this.phone=phone;
    this.createDate=createDate;
    this.createdBy=createdBy;
    this.lastUpdate=lastUpdate;
    this.lastUpdatedBy=lastUpdatedBy;
    this.divisionID=divisionID;
  }

  //getters/setters
  public int getCustomerID() {
    return customerID;
  }

  public void setCustomerID(int customerID) {
    this.customerID = customerID;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPostal() {
    return postal;
  }

  public void setPostal(String postal) {
    this.postal = postal;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(LocalDateTime lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public int getDivisionID() {
    return divisionID;
  }

  public void setDivisionID(int divisionID) {
    this.divisionID = divisionID;
  }

  @Override

  public String toString() {

    return ((customerName ));
  }
}
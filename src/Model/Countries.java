package Model;




import java.time.LocalDateTime;

/**
 * Country class used for customers and sorting the division ID
 */
public class Countries {
  private Integer countryID;
  private String country;
  private LocalDateTime createDate;
  private String createdBy;
  private LocalDateTime lastUpdate;
  private String lastUpdateBy;

  public Countries(Integer countryID, String country, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy) {
    this.countryID = countryID;
    this.country = country;
    this.createDate = createDate;
    this.createdBy = createdBy;
    this.lastUpdate = lastUpdate;
    this.lastUpdateBy = lastUpdateBy;
  }

  //Populate Single parameter country
  public Countries(String country) {
    this.country=country;
  }

  public Integer getCountryID() {
    return countryID;
  }

  public void setCountryID(Integer countryID) {
    this.countryID = countryID;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
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

  public String getLastUpdateBy() {
    return lastUpdateBy;
  }

  public void setLastUpdateBy(String lastUpdateBy) {
    this.lastUpdateBy = lastUpdateBy;
  }



  @Override
  public String toString() {
    return ((country));
  }
}



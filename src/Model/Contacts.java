package Model;

/**
 * Contact Class mostly used for filling associated attributes and allowing the UI to show a name vice a number through combo box selections
 */
public class Contacts {
    private  int contactID;
    private String contactName;
    private String contactEmail;

    public Contacts(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public Contacts() {

    }

    public Contacts(int contactID) {
        this.contactID = contactID;
    }

    public  int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }



    @Override
    public String toString() {

        return ("Contact Name: " +(contactName )+ " Contact ID: " + contactID);
    }
}

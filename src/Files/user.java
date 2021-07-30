package Files;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Button;

public class User {
    String name, employeeID, accType, accRole, location, password, dateCreated;
    int sn;
    Button button;
    public User(){
    
    }
    
    public User(int sn, String employeeID, String name, String accType, String location, String date_created){
        this.sn = sn;
        this.name = name;
        this.employeeID = employeeID;
        this.accType = accType;
        this.location = location;
        this.dateCreated = date_created;
        setButton(new Button("Edit"));
    }
    
    public User(int sn, String employeeID, String name, String accType, String accRole, String location, String dateCreated){
        this.sn = sn;
        this.name = name;
        this.employeeID = employeeID;
        this.accType = accType;
        this.accRole = accRole;
        this.location = location;
        this.dateCreated = dateCreated;
        setButton(new Button("Edit"));
    }
    
    public User(int sn, String employeeID, String accType, String location) {
        this.sn = sn;
        this.employeeID = employeeID;
        this.accType = accType;
        this.location = location;
    }

    public User(int sn, String username, String accType) {
        this.sn = sn;
        this.employeeID = employeeID;
        this.accType = accType;
    }
    
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setEmployeeID(String employeeID){
        this.employeeID = employeeID;
    }
    public String getEmployeeID(){
        return employeeID;
    }
    public void setType(String accType){
        this.accType = accType;
    }
    public String getType(){
        return accType;
    }
    public void setRole(String accRole){
        this.accRole = accRole;
    }
    public String getRole(){
        return accRole;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public String getLocation(){
        return location;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }
    public void setDate_Created(String dateCreated){
        this.dateCreated = dateCreated;
    }
    public String getDate_Created(){
        return dateCreated;
    }
    public void setSN(int sn){
        this.sn = sn;
    }
    public int getSN(){
        return sn;
    }
    public Button getButton()
    {
        return button;       
    }
    
    private void setButton(Button button)
    {
        this.button = button;       
    }
    public String getDate(){
        LocalDate myDateObj = LocalDate.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }
     
}

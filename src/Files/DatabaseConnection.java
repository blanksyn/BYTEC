package Files;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName = "WarehouseA";
        String databaseUser = "admin";
        String databasePass = "LMworksFYP321!";
        String databaseUrl = "lmworks.cup6j3mwxwgo.us-east-2.rds.amazonaws.com";
        String url = "jdbc:mysql://"+databaseUrl+"/" + databaseName + "?autoReconnect=true&useSSL=false";

        try{
            Class.forName("com.mysql.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePass);
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return  databaseLink;
    }
}

package Files;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main_Controller {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="loginBtn"
    private Button loginBtn; // Value injected by FXMLLoader

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label Errormsg;

    private double xOffset = 0;
    private double yOffset = 0;
    static String user;

    DatabaseConnection con = new DatabaseConnection();
    Connection connectDB = con.getConnection();

    @FXML
    void cancelAccount(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginAccount(ActionEvent event) throws IOException {
        if(username.getText().isBlank()==false && password.getText().isBlank()==false){
            validateLogin(event);
        }else{
            Errormsg.setText("Please enter Username/Password.");
        }
    }

    private void validateLogin(ActionEvent event){

        String UT = "";

        String verifyLogin= "SELECT count(1) as toCheck,type FROM accounts WHERE employeeID = '" + username.getText() + "' AND password = '" + password.getText() + "'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt("toCheck")==1) {
                    UT = queryResult.getString("type");
                    //change static username
                    this.user = username.getText();

                    login(event,UT,this.user);
                }else{
                    Errormsg.setText("Invalid login. Try again.");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


    private void login(ActionEvent event, String userType,String user){

        //change scene
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(userType+".fxml"));
            Parent root = loader.load();
            Navigation nav = new Navigation();
            nav.stageSetup(event,root);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


}

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

    @FXML
    void cancelAccount(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    void loginAccount(ActionEvent event) throws IOException {
        if(username.getText().isBlank()==false && password.getText().isBlank()==false){
            validateLogin(event);
            //prototypeLogin(event);
        }else{
            Errormsg.setText("Please enter Username/Password.");
        }
    }

    private void validateLogin(ActionEvent event){
        //prototypeLogin(event);

        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();
        String UT = "";
        String ac = username.getText();

        String verifyLogin= "SELECT count(1) FROM accounts WHERE employeeID = '" + username.getText() + "' AND password = '" + password.getText() + "'";
        String getUserType = "SELECT type FROM accounts WHERE employeeID = '" + username.getText() + "' AND password = '" + password.getText() + "'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            Statement statement2 = connectDB.createStatement();
            ResultSet queryUserType = statement2.executeQuery(getUserType);

            while(queryResult.next()){
                if(queryResult.getInt(1)==1){
                    while(queryUserType.next()){
                        UT = queryUserType.getString(1);
                    }
                    login(event,UT,ac);
                    
                }else{
                    Errormsg.setText("Invalid login. Try again.");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    private void prototypeLogin(ActionEvent event) {
        String ac = username.getText();
        //System.out.println(ac);
        try{
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource(ac+".fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();
            loginStage.show();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    private void login(ActionEvent event, String userType,String user){

        //pass username to controller
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(userType+".fxml"));
            Parent root = loader.load();

            switch (userType){
                case "WM":
                    WM_Controller controllerwm = loader.getController();
                    break;

                case "SP":
                    SP_Controller controllersp = loader.getController();
                    controllersp.welcomeMsg(user);
                    break;

                case "PP":
                    PP_Controller controllerpp = loader.getController();
                    controllerpp.welcomeMsg(user);
                    break;
                case "RC":
                    RC_Controller controllerrc = loader.getController();
                    controllerrc.welcomeMsg(user);
                    break;
                default: System.out.println("Error! Invalid user type.");
            }

            Navigation nav = new Navigation();
            nav.stageSetup(event,root);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


}

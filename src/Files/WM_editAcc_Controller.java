package Files;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WM_editAcc_Controller extends WM implements Initializable{

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField name;

    @FXML
    private TextField employeeID;

    @FXML
    private PasswordField pass;

    @FXML
    private PasswordField cPass;

    @FXML
    private ComboBox<String> cb_accType;
    
    @FXML
    private ComboBox<String> cb_role;
    
    static String thisOriEid;
    static int thisSN;
    
    static void getme(int sna, String oriEid){
        thisSN = sna;
        thisOriEid = oriEid;
    }
    
    //@Override
    public void initialize(URL url, ResourceBundle rb){welcome(welcomeLabel);
        welcome(welcomeLabel);

        try{
            //DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT DISTINCT role FROM accounts;");
            while (rs.next()) {
                cb_role.getItems().add(rs.getString("role"));
            }

        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        cb_role.setEditable(true);
        editViewAccWM(thisSN, name, employeeID, pass,  cPass, cb_accType, cb_role);
        //System.out.println(thisSN);

    }
    
    
    @FXML
    void closeAdd(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();
            loginStage.show();

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    loginStage.setX(event.getScreenX() - xOffset);
                    loginStage.setY(event.getScreenY() - yOffset);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void closeWindow(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();
            loginStage.show();

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    loginStage.setX(event.getScreenX() - xOffset);
                    loginStage.setY(event.getScreenY() - yOffset);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void deleteAcc(ActionEvent event) {
        deleteAccWM(thisSN,event);
    }

    @FXML
    void editAccount(ActionEvent event) {
        String Type = (String) cb_accType.getValue();
        if(cb_accType.getValue() == null)
            Type = "";
        String accRole = (String) cb_role.getValue();
        if(cb_role.getValue() == null)
            accRole = "";
        String accName = name.getText();
        String accID = employeeID.getText();
        String accPass = pass.getText();
        String accCpass = cPass.getText();
        editAccWM(thisSN, thisOriEid, event, accName,accID,accPass,accCpass,Type,accRole);
    }

}

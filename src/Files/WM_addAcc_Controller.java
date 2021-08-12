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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class WM_addAcc_Controller extends WM implements Initializable{
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
    
    private double xOffset = 0;
    private double yOffset = 0;

    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        welcome(welcomeLabel);
        cb_accType.getItems().add("Supervisor");
        cb_accType.getItems().add("Picker/Packer");
        cb_accType.getItems().add("Receiver");
        cb_role.setEditable(true);
        
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT DISTINCT role FROM accounts;");
            while (rs.next()) {
                cb_role.getItems().add(rs.getString("role"));
            }

        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }

        welcome(welcomeLabel);
    }

    @FXML
    void addAccount(ActionEvent event) {
        String Type = (String) cb_accType.getValue();
        if(cb_accType.getValue() == null)
            Type = "";
        String role = (String) cb_role.getValue();
        if(cb_role.getValue() == null)
            role = "";
        String accName = name.getText();
        String accEmpID = employeeID.getText();
        String accPass = pass.getText();
        String accCpass = cPass.getText();
        addNewAccWM(event, accName, accEmpID, accPass, accCpass, Type, role);
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


}

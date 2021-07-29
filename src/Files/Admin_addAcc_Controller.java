package Files;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Admin_addAcc_Controller {

    @FXML
    private Button closeBtn;


    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField name;

    @FXML
    private TextField username;

    @FXML
    private PasswordField pass;

    @FXML
    private PasswordField cPass;

    @FXML
    private ComboBox<String> cb_accType;

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<String> cb_location;


    @FXML
    void closeWindow(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


    @FXML
    void addAccount(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void closeAdd(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}

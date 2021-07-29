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

public class Admin_addLoc_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField locName;

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField postal;

    @FXML
    void addLocation(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("Admin_locSettings.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 750, 500);
            loginStage.setScene(scene);
            loginStage.show();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void closeAdd(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("Admin_locSettings.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 750, 500);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();
            loginStage.show();


        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void closeWindow(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("Admin_locSettings.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 750, 500);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();
            loginStage.show();


        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}

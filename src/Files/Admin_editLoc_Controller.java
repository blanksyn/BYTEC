package Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Admin_editLoc_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField locName;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField postal;

    @FXML
    private Button deleteLocBtn;

    @FXML
    void closeEdit(ActionEvent event) {
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

    @FXML
    void deleteLoc(ActionEvent event) {
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
    void editLocation(ActionEvent event) {
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

package Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Admin_editAcc_Controller {

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
    private ComboBox<?> cb_accType;

    @FXML
    private Button saveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<?> cb_location;

    @FXML
    private Button deleteBtn;

    @FXML
    void closeAdd(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            loginStage.setScene(scene);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void closeWindow(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            loginStage.setScene(scene);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void deleteAccount(ActionEvent event) {

    }

    @FXML
    void editAccount(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            loginStage.setScene(scene);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


}

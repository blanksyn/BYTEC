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

public class Admin_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private ImageView logoutBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<?> tableAccount;

    @FXML
    private TableColumn<?, ?> col_sn;

    @FXML
    private TableColumn<?, ?> col_Name;

    @FXML
    private TableColumn<?, ?> col_username;

    @FXML
    private TableColumn<?, ?> col_type;

    @FXML
    private TableColumn<?, ?> col_location;

    @FXML
    private TableColumn<?, ?> col_action;

    @FXML
    private Button addAccBtn;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private Button locSettingBtn;

    @FXML
    private ComboBox<?> CB_field;


    @FXML
    void addAccFunction(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Admin_addAcc.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 450, 500);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();


        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void locSettingFunction(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Admin_locSettings.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 750, 500);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void closeWindow(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void logoutAcc(MouseEvent event) throws IOException {
        Navigation nav = new Navigation();
    }

    @FXML
    void searchFunction(ActionEvent event) {

    }

}

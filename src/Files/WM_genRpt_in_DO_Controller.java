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

public class WM_genRpt_in_DO_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private ImageView logoutBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button accMgtBtn;

    @FXML
    private Button WHEnvBtn;

    @FXML
    private Button POINBtn;

    @FXML
    private Button GenRptBtn;

    @FXML
    private Button invRptBtn;

    @FXML
    private Button InbRptBtn;

    @FXML
    private Button OutbRptBtn;

    @FXML
    private Button POBtn;

    @FXML
    private Button DOBtn;

    @FXML
    private TableView<?> tableCourier;

    @FXML
    private TableColumn<?, ?> col_sn;

    @FXML
    private TableColumn<?, ?> col_month;

    @FXML
    private TableColumn<?, ?> col_year;

    @FXML
    private TableColumn<?, ?> col_total;

    @FXML
    private TableColumn<?, ?> col_action;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<?> CB_field;

    @FXML
    private Button genRptBtn;

    @FXML
    void generate_Rpt(ActionEvent event) {

    }

    @FXML
    void Nav_Gen_Rpt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_inv.fxml"));
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
    void Nav_InDO(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_in_DO.fxml"));
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
    void Nav_InPO(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_in_PO.fxml"));
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
    void Nav_InbRpt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_in_PO.fxml"));
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
    void Nav_InvRpt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_inv.fxml"));
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
    void Nav_OutbRpt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_out_PO.fxml"));
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
    void Nav_PO_In(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_POIN.fxml"));
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
    void Nav_WH_Env(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_courierMgt.fxml"));
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
    void Nav_accMgt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM.fxml"));
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
    void closeWindow(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void logoutAcc(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Confirm logout?");

        if(alert.showAndWait().get()== ButtonType.OK){

            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage logoutStage = (Stage)logoutBtn.getScene().getWindow();
            Scene scene = new Scene(root, 700, 650);
            logoutStage.setTitle("Login");
            logoutStage.setScene(scene);
            Image image = new Image("image/logo192.png");
            logoutStage.getIcons().add(image);
            scene.setFill(Color.TRANSPARENT);
            logoutStage.centerOnScreen();
            logoutStage.show();

        }
    }

    @FXML
    void searchFunction(ActionEvent event) {

    }

}

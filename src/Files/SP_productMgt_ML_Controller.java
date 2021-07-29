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

public class SP_productMgt_ML_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private ImageView logoutBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button pickListBtn;

    @FXML
    private Button WHEnvBtn;

    @FXML
    private Button POINBtn;

    @FXML
    private Button POOutBtn;

    @FXML
    private Button productMLBtn;

    @FXML
    private TableView<?> tableProductML;

    @FXML
    private TableColumn<?, ?> col_sn;

    @FXML
    private TableColumn<?, ?> col_upc;

    @FXML
    private TableColumn<?, ?> col_Name;

    @FXML
    private TableColumn<?, ?> col_qty;

    @FXML
    private TableColumn<?, ?> col_unit;

    @FXML
    private TableColumn<?, ?> col_location;

    @FXML
    private TableColumn<?, ?> col_supplier;

    @FXML
    private TableColumn<?, ?> col_category;

    @FXML
    private TableColumn<?, ?> col_minQty;

    @FXML
    private TableColumn<?, ?> col_maxQty;

    @FXML
    private TableColumn<?, ?> col_ARStatus;

    @FXML
    private TableColumn<?, ?> col_dateAdded;

    @FXML
    private TableColumn<?, ?> col_action;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<?> CB_field;

    String Username;

    public void welcomeMsg(String username){
        welcomeLabel.setText("User: "+ username);
        Username = username;
    }

    @FXML
    void Nav_DO_Out(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_deliveryOrderOut(event,Username);
    }

    @FXML
    void Nav_PO_In(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_purchaseOrderIn(event,Username);
    }

    @FXML
    void Nav_pickList(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_purchaseOrderOut(event,Username);
    }

    @FXML
    void Nav_productMgt(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_productManagement(event,Username);
    }

    @FXML
    void Nav_productML(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_productManagement(event,Username);
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

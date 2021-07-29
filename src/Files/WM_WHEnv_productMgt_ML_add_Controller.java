package Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class WM_WHEnv_productMgt_ML_add_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField TF_upc;

    @FXML
    private TextField TF_name;

    @FXML
    private TextField TF_maxQty;

    @FXML
    private TextField TF_weight;

    @FXML
    private TextField TF_length;

    @FXML
    private TextField TF_width;

    @FXML
    private TextField TF_height;

    @FXML
    private TextField TF_minQty;

    @FXML
    private TextArea TA_desc;

    @FXML
    private ComboBox<?> CB_location;

    @FXML
    private ComboBox<?> CB_unit;

    @FXML
    private ComboBox<?> CB_supplier;

    @FXML
    private ComboBox<?> CB_cat;

    @FXML
    private CheckBox checkBox_restock;

    @FXML
    private ImageView IV_product;

    @FXML
    private Button loadBtn;

    @FXML
    private TextField TF_fileLoc;

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private ComboBox<?> CB_specialHand;

    @FXML
    void addProduct(ActionEvent event) {
        //code to add product_indv to db

        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML_add.fxml"));
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
    void cancel(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1500, 700);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void closeWindow(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1500, 700);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void loadImage(ActionEvent event) {
        //code to load image from "TF_fileLoc"
    }

}

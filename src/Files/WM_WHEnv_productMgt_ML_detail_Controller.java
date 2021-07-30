package Files;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class WM_WHEnv_productMgt_ML_detail_Controller extends WM implements Initializable{

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label upc;

    @FXML
    private Label name;

    @FXML
    private TableView<product_indv> tableVProduct;

    @FXML
    private TableColumn<product_indv, Integer> col_sn;

    @FXML
    private TableColumn<product_indv, String> col_sku;

    @FXML
    private TableColumn<product_indv, String> col_location;

    @FXML
    private TableColumn<product_indv, String> col_expDate;

    @FXML
    private TableColumn<product_indv, String> col_dateAdd;

    @FXML
    private TableColumn<product_indv, product_indv> col_action;
    
    static String thisUPC, thisName;
    static int thisSN;
    
    ObservableList<product_indv> ObserveList = FXCollections.observableArrayList();
    
    static void getme(int sna, String upc, String name){
    thisSN = sna;
    thisUPC = upc;
    thisName = name;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        welcome(welcomeLabel);
        viewProdIndvWM(thisUPC, thisName, tableVProduct, ObserveList, col_sn, col_sku, col_location, col_expDate,
            col_dateAdd, col_action, upc, name);
    }
    
    @FXML
    void closeWindow(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}

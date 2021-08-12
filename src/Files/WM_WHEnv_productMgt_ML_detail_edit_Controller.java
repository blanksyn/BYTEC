package Files;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WM_WHEnv_productMgt_ML_detail_edit_Controller extends WM implements Initializable{
    @FXML
    private Label welcomeLabel;

    @FXML
    private ComboBox<String> CB_location;

    @FXML
    private TextField TF_sku;
    
    @FXML
    private TextField TF_expDate;

    static String thisUPC, thisSKU;
    static int thisSN;
    
    static void getme(int sna, String upc, String sku){
    thisSN = sna;
    thisUPC = upc;
    thisSKU = sku;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){welcome(welcomeLabel);
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs4 = connectDB.createStatement().executeQuery("SELECT location FROM upc_location WHERE upc = '" + thisUPC + "';");
            while (rs4.next()) {
                CB_location.getItems().add(rs4.getString("location"));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        editViewProdIndvWM(thisSN, thisSKU, TF_sku, CB_location, TF_expDate);
    }
    
    @FXML
    void editProduct(ActionEvent event) {
        String loc = (String) CB_location.getValue();
        if(CB_location.getValue() == null)
            loc = "";
        String sku = TF_sku.getText(); 
        String expiryDate = TF_expDate.getText();
        editProdIndvWM(thisSN, thisSKU, event, sku, loc, expiryDate);
        
    }

    @FXML
    void cancel(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML_detail.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
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
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML_detail.fxml"));
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

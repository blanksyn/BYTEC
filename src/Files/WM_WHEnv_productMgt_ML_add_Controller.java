package Files;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class WM_WHEnv_productMgt_ML_add_Controller extends WM implements Initializable{
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
    private ComboBox<String> CB_location;

    @FXML
    private ComboBox<String> CB_unit;

    @FXML
    private ComboBox<String> CB_supplier;

    @FXML
    private ComboBox<String> CB_cat;

    @FXML
    private CheckBox checkBox_restock;

    @FXML
    private TextField TF_fileLoc;

    @FXML
    private ComboBox<String> CB_specialHand;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){welcome(welcomeLabel);
        try{
            //DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT DISTINCT unit FROM product_master;");
            ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT DISTINCT category FROM product_master;");
            ResultSet rs3 = connectDB.createStatement().executeQuery("SELECT DISTINCT special_handling FROM product_master;");
            ResultSet rs4 = connectDB.createStatement().executeQuery("SELECT location FROM storage WHERE vol_avail > 0;");
            ResultSet rs5 = connectDB.createStatement().executeQuery("SELECT name FROM supplier;");
            while (rs.next()) {
                CB_unit.getItems().add(rs.getString("unit"));
            }
            while (rs2.next()) {
                CB_cat.getItems().add(rs2.getString("category"));
            }
            while (rs3.next()) {
                CB_specialHand.getItems().add(rs3.getString("special_handling"));
            }
            while (rs4.next()) {
                CB_location.getItems().add(rs4.getString("location"));
            }
            while (rs5.next()) {
                CB_supplier.getItems().add(rs5.getString("name"));
            }

        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        TF_fileLoc.setVisible(false);
    }
    
    @FXML
    void addProduct(ActionEvent event) {
        String unit = (String) CB_unit.getValue();
        if(CB_unit.getValue() == null)
            unit = "";
        String cat = (String) CB_cat.getValue();
        if(CB_cat.getValue() == null)
            cat = "";
        String specialHand = (String) CB_specialHand.getValue();
        if(CB_specialHand.getValue() == null)
            specialHand = "";
        String loc = (String) CB_location.getValue();
        if(CB_location.getValue() == null)
            loc = "";
        String sup = (String) CB_supplier.getValue();
        if(CB_supplier.getValue() == null)
            sup = "";
        String upc = TF_upc.getText(); 
        String name = TF_name.getText(); 
        String maxQ = TF_maxQty.getText(); 
        String weight = TF_weight.getText(); 
        String length = TF_length.getText(); 
        String width = TF_width.getText(); 
        String height = TF_height.getText(); 
        String minQ = TF_minQty.getText(); 
        String desc = TA_desc.getText();
        boolean restock = checkBox_restock.isSelected();
        String imageLoc = TF_fileLoc.getText();
        
        addProdMasterWM(event, upc, name, maxQ, weight, length, width, height, minQ, desc, loc, unit, sup,
             cat, specialHand, restock, imageLoc);
    }

    @FXML
    void cancel(ActionEvent event) {
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

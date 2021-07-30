package Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SP_productMgt_ML_detailView_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField TF_name;

    @FXML
    private TextField TF_maxQty;

    @FXML
    private TextField TF_supplier;

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
    private ComboBox<String> CB_unit;

    @FXML
    private ComboBox<String> CB_cat;

    @FXML
    private CheckBox checkBox_restock;

    @FXML
    private ImageView IV_product;

    @FXML
    private ComboBox<String> CB_specialHand;

    @FXML
    private TextField TF_upc;

    String Username;

    @FXML
    void initialize(String username, String upc) {

        welcomeLabel.setText("User: " + username);
        this.Username = username;

        TF_upc.setText(upc);
        TF_upc.setEditable(false);

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT * FROM product_master WHERE upc = '"+ upc+ "' LIMIT 1;";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                TF_name.setText(queryResult.getString("prod_name"));
                TF_name.setEditable(false);

                CB_unit.setValue(queryResult.getString("unit"));

                TF_maxQty.setText(queryResult.getString("max_qty"));
                TF_maxQty.setEditable(false);

                TF_supplier.setText(queryResult.getString("supplier"));
                TF_supplier.setEditable(false);

                TF_weight.setText(queryResult.getString("weight"));
                TF_weight.setEditable(false);

                TF_length.setText(queryResult.getString("length"));
                TF_length.setEditable(false);

                TF_width.setText(queryResult.getString("width"));
                TF_width.setEditable(false);

                TF_height.setText(queryResult.getString("height"));
                TF_height.setEditable(false);

                TF_minQty.setText(queryResult.getString("min_qty"));
                TF_minQty.setEditable(false);

                TA_desc.setText(queryResult.getString("description"));
                TA_desc.setEditable(false);

                CB_specialHand.setValue(queryResult.getString("special_handling"));
                CB_specialHand.setEditable(false);


                //checkBox_restock;
                //IV_product



            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    @FXML
    void closeWindow(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_productManagement(event,Username);
    }

}

package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SP_productMgt_ML_detail_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label Lab_upc;

    @FXML
    private Label Lab_name;

    @FXML
    private TableView<product_indv> tblViewProd;

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
    private TextField TF_keyword;


    String Username, upc;

    ObservableList<product_indv> upcProd = FXCollections.observableArrayList();

    @FXML
    void initialize(String username, String upc){

        welcomeLabel.setText("User: "+ username);
        this.Username = username;
        Lab_upc.setText(upc);
        this.upc = upc;
        int count =1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT date_added,sku,location,expiry_date FROM product_indv WHERE upc = '"+ upc+ "' AND (status = 'standby' OR status ='Picked' OR status is null) ORDER BY upc ASC";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                upcProd.add(new product_indv(queryResult.getString("date_added"),count,queryResult.getString("sku"),
                        queryResult.getString("location"),queryResult.getString("expiry_date")));
                count++;
            }

            String getName = "SELECT prod_name FROM product_master WHERE upc = '"+ upc + "'; ";
            Statement stName = connectDB.createStatement();
            ResultSet rsName = stName.executeQuery(getName);

            while(rsName.next()){
                Lab_name.setText(rsName.getString("prod_name"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_sku.setCellValueFactory((new PropertyValueFactory<>("sku")));
        col_expDate.setCellValueFactory((new PropertyValueFactory<>("expiry_date")));
        col_dateAdd.setCellValueFactory((new PropertyValueFactory<>("date_added")));
        col_location.setCellValueFactory((new PropertyValueFactory<>("loc")));


        tblViewProd.setItems(upcProd);

        FilteredList<product_indv> filteredData = new FilteredList<>(upcProd, b-> true);

        TF_keyword.textProperty().addListener((observable, oldValue,newValue)->{

            //if no change detected then no change to list
            filteredData.setPredicate(product_indv -> {

                boolean s =false;
                if(newValue.isEmpty() || newValue.isBlank() || newValue ==null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(product_indv.getSku().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(product_indv.getExpiry_date().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(product_indv.getDate_added().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(product_indv.getLoc().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else
                    s = false;//no match found
                return s;
            });
        });

        SortedList<product_indv> sortedData = new SortedList<>(filteredData);
        //bind sorted results with tableview
        sortedData.comparatorProperty().bind(tblViewProd.comparatorProperty());
        //apply filtered and sorted data to table view
        tblViewProd.setItems(sortedData);
    }

    @FXML
    void closeWindow(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_productManagement(event,Username);
    }

}

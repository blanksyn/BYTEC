package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class RC_AppRcvList_view_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<product_indv> tbl_rcvList;

    @FXML
    private TableColumn<product_indv, Integer> col_sn;

    @FXML
    private TableColumn<product_indv, String> col_upc;

    @FXML
    private TableColumn<product_indv, String> col_productName;

    @FXML
    private TableColumn<product_indv, String> col_sku;

    @FXML
    private TableColumn<product_indv, String> col_location;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<String> CB_field;

    @FXML
    private Label Lab_PONum;

    @FXML
    private Label Lab_DONum;

    @FXML
    private Label Lab_dateRcv;

    @FXML
    private Label Lab_sup;

    @FXML
    private Button printLabelBtn;
    String Username,PONum,DONum,supplier,date_rcv;

    ObservableList<product_indv> rcvList = FXCollections.observableArrayList();

    @FXML
    void initialize(String username, String PONum, String DONum,String supplier, Date date_rcv){
        this.Username = username;
        this. PONum = PONum;
        this.DONum = DONum;
        this.date_rcv = String.valueOf(date_rcv);
        this.supplier = supplier;
        welcomeLabel.setText("User: "+ Username);
        Lab_DONum.setText(DONum);
        Lab_dateRcv.setText(this.date_rcv);
        Lab_sup.setText(supplier);
        Lab_PONum.setText(PONum);

        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT upc FROM POin_rcv WHERE DONum = '" +DONum +"' ";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                String getName = "SELECT prod_name FROM product_master WHERE upc = '" +queryResult.getString("upc") +"' ";
                Statement stName  = connectDB.createStatement();
                ResultSet rsName  = stName.executeQuery(getName);

                while(rsName.next()) {
                    String getSKU = "SELECT sku FROM POin_rcv_detail WHERE upc = '" +queryResult.getString("upc") +"' AND DONum = '"+DONum+"'";
                    Statement stSKU  = connectDB.createStatement();
                    ResultSet rsSKU  = stSKU.executeQuery(getSKU);

                    while(rsSKU.next()) {
                        String getLoc = "SELECT location FROM product_indv WHERE sku = '" +rsSKU.getString("sku") +"' ";
                        Statement stLoc  = connectDB.createStatement();
                        ResultSet rsLoc  = stLoc.executeQuery(getLoc);

                        while(rsLoc.next()) {
                            rcvList.add(new product_indv(count, queryResult.getString("upc"), rsName.getString("prod_name"),
                                    rsSKU.getString("sku"), rsLoc.getString("location")));
                            count++;
                        }
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_sku.setCellValueFactory((new PropertyValueFactory<>("sku")));
        col_location.setCellValueFactory((new PropertyValueFactory<>("loc")));

        tbl_rcvList.setItems(rcvList);

    }


    @FXML
    void closeWindow(ActionEvent event) {

        Navigation nav =new Navigation();
        nav.RC_ApproveRcvList(event,Username);
    }

    @FXML
    void printLabel(ActionEvent event) {

    }

    @FXML
    void searchFunction(ActionEvent event) {

    }

}

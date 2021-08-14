package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SP_POIN_POVIEW_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label Lab_PONum;

    @FXML
    private Label Lab_supplier;

    @FXML
    private TableView<POin> tbl_POinDet;

    @FXML
    private TableColumn<POin, Integer> col_sn;

    @FXML
    private TableColumn<POin, String> col_upc;

    @FXML
    private TableColumn<POin, String> col_productName;

    @FXML
    private TableColumn<POin, String> col_qtyOrd;

    @FXML
    private TableColumn<POin, String> col_qtyRcv;

    @FXML
    private TableColumn<POin, String> col_qtyRem;

    @FXML
    private TextField TF_keyword;


    ObservableList<POin> POList = FXCollections.observableArrayList();
    private double xOffset = 0;
    private double yOffset = 0;
    String Username,PONum;

    @FXML
    void initialize(String username,String PONum){
        Lab_PONum.setText(PONum);
        welcomeLabel.setText("User: "+ username);
        this.Username = username;
        this.PONum = PONum;

        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT supplier FROM POin WHERE PONum = "+ PONum;
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                Lab_supplier.setText(queryResult.getString("supplier"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT upc,qty_ordered,qty_rcv,qty_remaining FROM POin_detail WHERE PONum = "+ PONum;
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                String getProdName = "SELECT prod_name FROM product_master WHERE upc = "+ queryResult.getString("upc");
                Statement statementPN = connectDB.createStatement();
                ResultSet queryResultPN = statementPN.executeQuery(getProdName);
                while(queryResultPN.next()) {
                    POList.add(new POin(queryResult.getString("upc"),count, queryResultPN.getString("prod_name"), queryResult.getInt("qty_ordered"),
                            queryResult.getInt("qty_rcv"), queryResult.getInt("qty_remaining")));
                    count++;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_qtyOrd.setCellValueFactory((new PropertyValueFactory<>("qty_ordered")));
        col_qtyRcv.setCellValueFactory((new PropertyValueFactory<>("qty_rcv")));
        col_qtyRem.setCellValueFactory((new PropertyValueFactory<>("qty_remaining")));

        tbl_POinDet.setItems(POList);

        FilteredList<POin> filteredData = new FilteredList<>(POList, b-> true);

        TF_keyword.textProperty().addListener((observable, oldValue,newValue)->{

            //if no change detected then no change to list
            filteredData.setPredicate(POin -> {

                boolean s =false;
                if(newValue.isEmpty() || newValue.isBlank() || newValue ==null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(POin.getUpc().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POin.getProd_name().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(String.valueOf(POin.getQty_ordered()).toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(String.valueOf(POin.getQty_rcv()).indexOf(searchKeyword)>-1){
                    s = true;
                }else if(String.valueOf(POin.getQty_remaining()).indexOf(searchKeyword)>-1){
                    s = true;
                }else
                    s = false;//no match found
                return s;
            });
        });

        SortedList<POin> sortedData = new SortedList<>(filteredData);
        //bind sorted results with tableview
        sortedData.comparatorProperty().bind(tbl_POinDet.comparatorProperty());
        //apply filtered and sorted data to table view
        tbl_POinDet.setItems(sortedData);
    }

    @FXML
    void closeWindow(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_POIN_PO.fxml"));
            Parent root = loader.load();
            SP_POIN_PO_Controller SPPOINCon = loader.getController();
            SPPOINCon.welcomeMsg(Username);
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);

            root.setOnMousePressed((MouseEvent event1) -> {
                xOffset = event1.getSceneX();
                yOffset = event1.getSceneY();
            });

            root.setOnMouseDragged((MouseEvent event1) -> {
                loginStage.setX(event1.getScreenX() - xOffset);
                loginStage.setY(event1.getScreenY() - yOffset);
            });
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


}

package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.sql.*;

public class PP_view_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<POout> tbl_pickingList;

    @FXML
    private TableColumn<POout, Integer> col_sn;

    @FXML
    private TableColumn<POout, String> col_upc;

    @FXML
    private TableColumn<POout, String> col_prodName;

    @FXML
    private TableColumn<POout, String> col_sku;

    @FXML
    private TableColumn<POout, String> col_skuScanned;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<String> CB_field;

    @FXML
    private Label Lab_PONum;

    @FXML
    private Label Lab_Comp;

    @FXML
    private Button confirmBtn;

    String Username ="";
    String PONum;
    ObservableList<POout> scanList = FXCollections.observableArrayList();

    @FXML
    void initialize(String username,String PONum,String comp){
        welcomeLabel.setText("User: "+ username);
        this.Username =username;
        Lab_Comp.setText(comp);
        Lab_PONum.setText(PONum);
        this.PONum =PONum;

        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT upc,prod_name,sku,sku_scanned FROM pickingList_detail WHERE PONum = " +PONum +" AND (sku_scanned is null or sku_scanned = '')";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                scanList.add(new POout(count,queryResult.getString("upc"),queryResult.getString("prod_name"),
                        queryResult.getString("sku"),queryResult.getString("sku_scanned")));
                count++;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_prodName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_sku.setCellValueFactory((new PropertyValueFactory<>("sku")));
        col_skuScanned.setCellValueFactory((new PropertyValueFactory<>("sku_scanned")));

        tbl_pickingList.setItems(scanList);

        tbl_pickingList.setEditable(true);
        col_skuScanned.setCellFactory(TextFieldTableCell.forTableColumn());

        FilteredList<POout> filteredData = new FilteredList<>(scanList, b-> true);

        TF_keyword.textProperty().addListener((observable, oldValue,newValue)->{

            //if no change detected then no change to list
            filteredData.setPredicate(POout -> {

                boolean s =false;
                if(newValue.isEmpty() || newValue.isBlank() || newValue ==null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(POout.getUpc().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getProd_name().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getSku().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getSku_scanned().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else
                    s = false;//no match found
                return s;
            });
        });

        SortedList<POout> sortedData = new SortedList<>(filteredData);
        //bind sorted results with tableview
        sortedData.comparatorProperty().bind(tbl_pickingList.comparatorProperty());
        //apply filtered and sorted data to table view
        tbl_pickingList.setItems(sortedData);

    }

    @FXML
    void closeWindow(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.PP_home(event,Username);
    }

    @FXML
    void searchFunction(ActionEvent event) {

    }

    @FXML
    void updatePO(ActionEvent event) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();

        //update sku status
        String updateDataDet = "UPDATE product_indv SET status = '' WHERE sku = ?";
        PreparedStatement pstDetUP = connectDB.prepareStatement(updateDataDet);
        String updateDataDetsku = "UPDATE product_indv SET status = 'Picked' WHERE sku = ?";
        PreparedStatement pstDetUPsku = connectDB.prepareStatement(updateDataDetsku);
        String updatePL = "UPDATE pickingList_detail SET sku_scanned = ? WHERE sku = ? AND PONum = '"+ PONum+"';"  ;
        PreparedStatement pstupdatePL = connectDB.prepareStatement(updatePL);
        for(POout p:scanList){
            pstDetUP.setString(1, p.sku);
            pstDetUP.execute();
            pstDetUPsku.setString(1,p.sku_scanned);
            pstDetUPsku.execute();
            pstupdatePL.setString(1,p.sku_scanned);
            pstupdatePL.setString(2,p.sku);
            pstupdatePL.execute();
        }

        String updatepickSt = "UPDATE POout SET status = 'Not Approved', ppBy = '"+Username+"' WHERE PONum = '"+ PONum+"';"  ;
        PreparedStatement pstupdatepickSt = connectDB.prepareStatement(updatepickSt);
        pstupdatepickSt.execute();

        System.out.println("Product picked and updated.");
        closeWindow(event);

    }

    public void editSKUScan(TableColumn.CellEditEvent<POout, String> pOoutStringCellEditEvent) {
        POout po = tbl_pickingList.getSelectionModel().getSelectedItem();
        po.setSku_scanned(pOoutStringCellEditEvent.getNewValue());
    }
}

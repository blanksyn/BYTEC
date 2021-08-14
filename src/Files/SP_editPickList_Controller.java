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

import java.sql.*;

public class SP_editPickList_Controller {


    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<product_indv> tbl_EpickList;

    @FXML
    private TableColumn<product_indv, Integer> col_sn;

    @FXML
    private TableColumn<product_indv, String> col_upc;

    @FXML
    private TableColumn<product_indv, String> col_productName;

    @FXML
    private TableColumn<product_indv, String> col_sku;

    @FXML
    private TableColumn<product_indv, String> col_skuScan;

    @FXML
    private TableColumn<product_indv, String> col_location;

    @FXML
    private TableColumn<product_indv, String> col_action;

    @FXML
    private ComboBox<String> CB_UPC;

    @FXML
    private ComboBox<String> CB_productName;

    @FXML
    private TextField TF_qty;

    @FXML
    private TextField TF_compName;

    @FXML
    private TextField TF_PONum;

    @FXML
    private TextArea TA_compAdd;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button orderBtn;

    private double xOffset = 0;
    private double yOffset = 0;
    private String Username, PONum ,SONum;

    ObservableList<product_indv> newList = FXCollections.observableArrayList();

    DatabaseConnection con = new DatabaseConnection();
    Connection connectDB = con.getConnection();

    @FXML
    void initialize(String username, String SONum){
        int count = 1;
        welcomeLabel.setText("User: "+ username);
        Username = username;
        this.SONum = SONum;

        CB_UPC.setEditable(true);
        CB_productName.setEditable(true);

        //get PONum
        try {
            String getValues = "SELECT PONum,status FROM POout WHERE SONum = '"+ SONum +"' LIMIT 1;";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while (queryResult.next()) {

                this.PONum = queryResult.getString("PONum");
                TF_PONum.setText(PONum);

                if(queryResult.getString("status").equals("Picking in Progress")){
                    deleteBtn.setVisible(false);
                    orderBtn.setVisible(false);
                }
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            //fill combobox and text fields
            String getValues = "SELECT upc,prod_name FROM product_master";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while (queryResult.next()) {
                CB_UPC.getItems().add(queryResult.getString("upc"));
                CB_productName.getItems().add((queryResult.getString("prod_name")));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try{
            String getValues = "SELECT company,company_add FROM POout WHERE SONum ="+ SONum;
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                TF_compName.setText(queryResult.getString("company"));
                TA_compAdd.setText(queryResult.getString("company_add"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        //fill table coloumn
        try{

            String getValues = "SELECT sn,upc,prod_name,sku,sku_scanned FROM pickingList_detail WHERE SONum = '"+ SONum+"';";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                String getLoc = "SELECT location FROM product_indv WHERE sku = '"+ queryResult.getString("sku")+"';";
                Statement stLoc = connectDB.createStatement();
                ResultSet queryResultLoc = stLoc.executeQuery(getLoc);

                while(queryResultLoc.next()) {
                    newList.add(new product_indv(count, queryResult.getString("upc"), queryResult.getString("prod_name"),
                            queryResult.getString("sku"),queryResult.getString("sku_scanned"), queryResultLoc.getString("location")));
                    count++;

                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_sku.setCellValueFactory((new PropertyValueFactory<>("sku")));
        col_skuScan.setCellValueFactory((new PropertyValueFactory<>("sku_scanned")));
        col_location.setCellValueFactory((new PropertyValueFactory<>("loc")));


        //delete button
        Callback<TableColumn<product_indv, String>, TableCell<product_indv, String>> cellFactory
                = //
                new Callback<TableColumn<product_indv, String>, TableCell<product_indv, String>>() {
                    @Override
                    public TableCell call(final TableColumn<product_indv, String> param) {
                        final TableCell<product_indv, String> cell = new TableCell<product_indv, String>() {

                            final Button btn = new Button("Delete");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        product_indv entry = getTableView().getItems().get(getIndex());
                                        try {
                                            String updateStat = "UPDATE product_indv SET status = '' WHERE sku = '" + entry.getSku() + "';";
                                            PreparedStatement pstStat = connectDB.prepareStatement(updateStat);
                                            pstStat.execute();
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        newList.remove(entry);

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        col_action.setCellFactory(cellFactory);
        tbl_EpickList.setItems(newList);

        FilteredList<product_indv> filteredData = new FilteredList<>(newList, b-> true);

        TF_keyword.textProperty().addListener((observable, oldValue,newValue)->{

            //if no change detected then no change to list
            filteredData.setPredicate(product_indv -> {

                boolean s =false;
                if(newValue.isEmpty() || newValue.isBlank() || newValue ==null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(product_indv.getUpc().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(product_indv.getProd_name().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(product_indv.getSku().toLowerCase().indexOf(searchKeyword)>-1){
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
        sortedData.comparatorProperty().bind(tbl_EpickList.comparatorProperty());
        //apply filtered and sorted data to table view
        tbl_EpickList.setItems(sortedData);
    }

    @FXML
    void addToList(ActionEvent event) throws SQLException {
        int count = newList.size()+1;
        String upc,qty,prod_name;
        upc = CB_UPC.getValue();
        prod_name = CB_productName.getValue();

        if(prod_name==null){
            prod_name="";
        }
        if(upc==null){
            upc="";
        }

        if(PONum != null) {
            PONum = TF_PONum.getText();
        }

        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();
        boolean noStock = true;

        if(TF_qty.getText() == null || TF_qty.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error has occurred");
            alert.setHeaderText("Quantity field is blank! ");
            alert.setContentText("Please enter quantity.");

            alert.showAndWait();
        }else if((!upc.isEmpty()||!upc.isBlank()) && (!prod_name.isEmpty()|| !prod_name.isBlank())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error has occurred");
            alert.setHeaderText("Both fields are filled! ");
            alert.setContentText("Select only one field.");

            alert.showAndWait();
        }else if((!upc.equals("") || !upc.isBlank()) &&(prod_name==null || prod_name.isBlank())){
            qty = TF_qty.getText();

            if(isStringInt(qty)== true) {
                String getValues = "SELECT sku,location FROM product_indv WHERE upc = '" + upc + "'AND status is null ORDER BY date_added ASC LIMIT " + qty + ";";
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(getValues);

                while (queryResult.next()) {
                    String getPDL = "SELECT prod_name FROM product_master WHERE upc = " + upc;
                    Statement statementPDL = connectDB.createStatement();
                    ResultSet queryResultPDL = statementPDL.executeQuery(getPDL);
                    while (queryResultPDL.next()) {
                        newList.add(new product_indv(count, upc,
                                queryResultPDL.getString("prod_name"), queryResult.getString("sku"), queryResult.getString("location")));
                        count++;
                        System.out.println("UPC: " + upc);
                        System.out.println("Product name: " + queryResultPDL.getString("prod_name"));
                        System.out.println("Sku: " + queryResult.getString("sku"));
                        System.out.println("Location: " + queryResult.getString("location"));
                        noStock = false;
                    }
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error has occurred");
                alert.setHeaderText("Quantity field is not a number! ");
                alert.setContentText("Please enter a valid number.");

                alert.showAndWait();
            }

        }else if((!prod_name.equals("") ||!prod_name.isBlank()) && (upc==null || upc.isBlank())){
            qty=TF_qty.getText();

            if(isStringInt(qty)==true) {
                String getUpc = "SELECT upc FROM product_master WHERE prod_name = '" + prod_name + "' LIMIT 1;";
                Statement statementUpc = connectDB.createStatement();
                ResultSet queryResultUpc = statementUpc.executeQuery(getUpc);
                while (queryResultUpc.next()) {

                    String getValues = "SELECT sku,location FROM product_indv WHERE upc = " + queryResultUpc.getString("upc") + " AND (status = '' or status is null) ORDER BY date_added ASC LIMIT " + TF_qty.getText() + ";";
                    Statement statement = connectDB.createStatement();
                    ResultSet queryResult = statement.executeQuery(getValues);

                    while (queryResult.next()) {
                        newList.add(new product_indv(count,
                                queryResultUpc.getString("upc"), prod_name, queryResult.getString("sku"), queryResult.getString("location")));
                        count++;
                        System.out.println("UPC: " + queryResultUpc.getString("upc"));
                        System.out.println("Product name: " + prod_name);
                        System.out.println("Sku: " + queryResult.getString("sku"));
                        System.out.println("Location: " + queryResult.getString("location"));
                        noStock = false;

                    }

                }

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error has occurred");
                alert.setHeaderText("Quantity field is not a number! ");
                alert.setContentText("Please enter a valid number.");

                alert.showAndWait();
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Empty fields");
            alert.setHeaderText("Both fields empty! ");
            alert.setContentText("Please select one product.");

            alert.showAndWait();
        }

        if(noStock==true){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error has occurred");
            alert.setHeaderText("Not enough stock for selected request! ");
            alert.setContentText("Please select another product.");

            alert.showAndWait();
        }

        CB_UPC.getSelectionModel().getSelectedIndex();
        CB_UPC.setValue(null);
        CB_productName.getSelectionModel().getSelectedIndex();
        CB_productName.setValue(null);
        TF_qty.setText(null);
    }

    public boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }


    @FXML
    void closeWindow(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_purchaseOrderOut(event,Username);
    }

    @FXML
    void deleteList(ActionEvent event) throws SQLException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Ooops did you want to delete?");
        alert.setContentText("Confirm delete");

        if(alert.showAndWait().get()== ButtonType.OK) {

            //delete PO entry
            System.out.println("Deleting POout...");
            String deletePO = "DELETE FROM POout WHERE SONum = '" + SONum+"';";
            PreparedStatement ps = connectDB.prepareStatement(deletePO);
            ps.execute();
            System.out.println("POout entry deleted");

            //change standby status to ''
            System.out.println("Resetting SKU status");
            String getValues = "SELECT sku_scanned FROM pickingList_detail WHERE SONum = '" + SONum +"';";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);
            String sku_scanned ="";
            while(queryResult.next()){
                if (queryResult.getString("sku_scanned")==null ||sku_scanned.equals("")){
                    String getsku = "SELECT sku FROM pickingList_detail WHERE SONum = '" + SONum + "';";
                    Statement stSku = connectDB.createStatement();
                    ResultSet queryResultSku = stSku.executeQuery(getsku);
                    while(queryResultSku.next()) {
                        String deletePLD = "UPDATE product_indv SET status = '' WHERE sku = '" + queryResultSku.getString("sku")+"';";
                        PreparedStatement pstDet = connectDB.prepareStatement(deletePLD);
                        pstDet.execute();
                    }
                }
                else {
                    sku_scanned = queryResult.getString("sku_scanned");
                    String deletePLD = "UPDATE product_indv SET status = '' WHERE sku = '" + queryResult.getString("sku_scanned")+"';";
                    PreparedStatement pstDet = connectDB.prepareStatement(deletePLD);
                    pstDet.execute();

                }
            }
            System.out.println("SKU reset.");

            //delete all entry in picking list detail that has the PO number
            System.out.println("Deleting entries in pickingList_detail...");
            String deletePLD = "DELETE FROM pickingList_detail WHERE SONum = '" + SONum+"';";
            PreparedStatement pstDet = connectDB.prepareStatement(deletePLD);
            pstDet.execute();
            System.out.println("Entries deleted");
        }

        System.out.println("Closing window...");
        closeWindow(event);


    }

    @FXML
    void editLIst(ActionEvent event) throws SQLException {

        String beforeStatus = "";

        //get original creator and original date
        String getdate = "SELECT status FROM POout WHERE SONum = '" + SONum + "' LIMIT 1";
        Statement statementDate = connectDB.createStatement();
        ResultSet queryResultDate = statementDate.executeQuery(getdate);

        while(queryResultDate.next()) {
            beforeStatus = queryResultDate.getString("status");
        }

        //edit all fields in db

        //reset status
        System.out.println("Resetting status...");
        if(beforeStatus.equals("Unpicked")){
            String getsku = "SELECT sku FROM pickingList_detail WHERE SONum = '" + SONum+"';";
            Statement stSku = connectDB.createStatement();
            ResultSet queryResultSku = stSku.executeQuery(getsku);

            while(queryResultSku.next()) {
                String deletePLD = "UPDATE product_indv SET status = '' WHERE sku = '" + queryResultSku.getString("sku")+ "';";
                PreparedStatement pstDet = connectDB.prepareStatement(deletePLD);
                pstDet.execute();
            }

        }else{
            String getValues = "SELECT sku_scanned FROM pickingList_detail WHERE SONum = " + SONum+"';";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()) {
               String deletePLD = "UPDATE product_indv SET status = '' WHERE sku = '" + queryResult.getString("sku_scanned")+"';";
               PreparedStatement pstDet = connectDB.prepareStatement(deletePLD);
               pstDet.execute();
            }
        }
        System.out.println("Status reset.");

        //delete all entry in picking list detail that has the SO number
        System.out.println("Editing Sales Order...");
        String deletePLD = "DELETE FROM pickingList_detail WHERE SONum = '" + SONum+"';";
        PreparedStatement pstDetail = connectDB.prepareStatement(deletePLD);
        pstDetail.execute();

        //recreate a new entry
        //send data to database
        String PO = TF_PONum.getText();
        String comp_name = TF_compName.getText();
        String comp_add = TA_compAdd.getText();

        //update POout
        System.out.println("Updating POout...");
        String upPOout = "UPDATE POout SET date_edited = ?,last_edit = ?  WHERE SONum = '" + SONum + "';";
        PreparedStatement psPOout = connectDB.prepareStatement(upPOout);
        psPOout.setString(1, String.valueOf(java.time.LocalDate.now()));
        psPOout.setString(2,Username);
        psPOout.execute();
        System.out.println("POout updated.");

        //insert new data into pickingList _detail
        System.out.println("Editting pickingList_detail...");
        String insertDataDet = "INSERT INTO pickingList_detail (SONum,company,date_created,upc,prod_name,sku) VALUES (?,?,?,?,?,?);";
        PreparedStatement pstDet = connectDB.prepareStatement(insertDataDet);
        for(product_indv p:newList){
            String upc = p.upc;
            String prod_name = p.prod_name;
            String sku = p.sku;
            pstDet.setString(1, SONum);
            pstDet.setString(2,comp_name);
            pstDet.setString(3, String.valueOf(java.time.LocalDate.now()));
            pstDet.setString(4, upc);
            pstDet.setString(5,prod_name);
            pstDet.setString(6, sku);
            pstDet.execute();
        }
        System.out.println("PickingList updated.");

        //update status
        System.out.println("Updating product status...");
        String updateDataDet = "UPDATE product_indv SET status = 'standby' WHERE sku = ?";
        PreparedStatement pstDetUP = connectDB.prepareStatement(updateDataDet);
        for(product_indv p:newList){
            pstDetUP.setString(1, p.sku);
            pstDetUP.execute();
        }
        System.out.println("Status updated.");

        System.out.println("Closing window...");
        closeWindow(event);
    }

    @FXML
    void resetList(ActionEvent event) {
        newList.clear();
        System.out.println("List cleared.");
    }


}

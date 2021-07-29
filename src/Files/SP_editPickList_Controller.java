package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private Button closeBtn;

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
    private Button addBtn;

    @FXML
    private Button orderBtn;

    @FXML
    private TextField TF_compName;

    @FXML
    private TextField TF_PONum;

    @FXML
    private TextArea TA_compAdd;

    @FXML
    private Button resetbtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Label errorAdd;

    private double xOffset = 0;
    private double yOffset = 0;
    private String Username, PONum;

    ObservableList<product_indv> newList = FXCollections.observableArrayList();

    @FXML
    void initialize(String username, String PONum){
        int count = 1;
        welcomeLabel.setText("User: "+ username);
        Username = username;
        this.PONum = PONum;

        CB_UPC.setEditable(true);
        CB_productName.setEditable(true);
        TF_PONum.setText(PONum);

        try {
            //fill combobox and text fields
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

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
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT company,company_add FROM POout WHERE PONum ="+ PONum;
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
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT sn,upc,prod_name,sku,sku_scanned FROM pickingList_detail WHERE PONum ="+ PONum;
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                String getLoc = "SELECT location FROM product_indv WHERE sku ="+ queryResult.getString("sku");
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
    }

    @FXML
    void addToList(ActionEvent event) throws SQLException {
        int count = newList.size()+1;
        String upc;
        if(CB_UPC.getValue()==null){
            upc = "";
        }else{
            upc = CB_UPC.getValue();
        }
        String prod_name = CB_productName.getValue();

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
        }else if(!upc.equals("") && !prod_name.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error has occurred");
            alert.setHeaderText("Both fields are filled! ");
            alert.setContentText("Select only one field.");

            alert.showAndWait();
        }else if(!upc.equals("")){
            String getValues = "SELECT sku,location FROM product_indv WHERE upc = '"+ upc + "'AND status is null ORDER BY date_added ASC LIMIT "+ TF_qty.getText()+ ";";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                String getPDL = "SELECT prod_name FROM product_master WHERE upc = "+ upc ;
                Statement statementPDL = connectDB.createStatement();
                ResultSet queryResultPDL = statementPDL.executeQuery(getPDL);
                while(queryResultPDL.next()) {
                    newList.add(new product_indv(count, upc,
                            queryResultPDL.getString("prod_name"), queryResult.getString("sku"), queryResult.getString("location")));
                    count++;
                    System.out.println("UPC: " + upc);
                    System.out.println("Product name: " + queryResultPDL.getString("prod_name"));
                    System.out.println("Sku: " + queryResult.getString("sku"));
                    System.out.println("Location: " + queryResult.getString("location"));
                    noStock=false;
                }
            }


        }else if(!prod_name.equals("")){

            String getUpc = "SELECT upc FROM product_master WHERE prod_name = '"+ prod_name + "' LIMIT 1;";
            Statement statementUpc = connectDB.createStatement();
            ResultSet queryResultUpc = statementUpc.executeQuery(getUpc);
            while(queryResultUpc.next()) {

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
                    noStock=false;

                }

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

        refreshTable();
        CB_UPC.getSelectionModel().getSelectedIndex();
        CB_UPC.setValue(null);
        CB_productName.getSelectionModel().getSelectedIndex();
        CB_productName.setValue(null);
        TF_qty.setText(null);
    }

    void refreshTable(){

        //fill table coloumn
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
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            //delete PO entry
            String deletePO = "DELETE FROM POout WHERE PONum = " + PONum;
            PreparedStatement ps = connectDB.prepareStatement(deletePO);
            ps.execute();

            //change standby status to ''
            String getValues = "SELECT sku_scanned FROM pickingList_detail WHERE PONum = " + PONum;
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                String sku_scanned = queryResult.getString("sku_scanned");

                if(sku_scanned.equals("") || sku_scanned ==null) {
                    String getsku = "SELECT sku FROM pickingList_detail WHERE PONum = " + PONum;
                    Statement stSku = connectDB.createStatement();
                    ResultSet queryResultSku = stSku.executeQuery(getsku);

                    String deletePLD = "UPDATE product_indv SET status = '' WHERE sku = " + queryResultSku.getString("sku");
                    PreparedStatement pstDet = connectDB.prepareStatement(deletePLD);
                    pstDet.execute();
                }else{
                    String deletePLD = "UPDATE product_indv SET status = '' WHERE sku = " + queryResult.getString("sku_scanned");
                    PreparedStatement pstDet = connectDB.prepareStatement(deletePLD);
                    pstDet.execute();
                }
            }

            //delete all entry in picking list detail that has the PO number
            String deletePLD = "DELETE FROM pickingList_detail WHERE PONum = " + PONum;
            PreparedStatement pstDet = connectDB.prepareStatement(deletePLD);
            pstDet.execute();
        }

        closeWindow(event);

    }

    @FXML
    void editLIst(ActionEvent event) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();

        String date_created ="";
        String createdBy ="";
        String beforeStatus = "";
        String delivery_date = "";

        //get original creator and original date
        String getdate = "SELECT date_created,createdBy,status,delivery_date FROM POout WHERE PONum = " + PONum + " LIMIT 1";
        Statement statementDate = connectDB.createStatement();
        ResultSet queryResultDate = statementDate.executeQuery(getdate);

        while(queryResultDate.next()) {
            date_created = queryResultDate.getString("date_created");
            createdBy = queryResultDate.getString("createdBy");
            beforeStatus = queryResultDate.getString("status");
            delivery_date = queryResultDate.getString("delivery_date");
        }

        //remove all fields in db

        //delete PO entry
        String deletePO = "DELETE FROM POout WHERE PONum = " + PONum;
        PreparedStatement ps = connectDB.prepareStatement(deletePO);
        ps.execute();

        //change standby status to ''
        if(beforeStatus.equals("Unpicked")){
            String getsku = "SELECT sku FROM pickingList_detail WHERE PONum = " + PONum;
            Statement stSku = connectDB.createStatement();
            ResultSet queryResultSku = stSku.executeQuery(getsku);

            while(queryResultSku.next()) {
                String deletePLD = "UPDATE product_indv SET status = '' WHERE sku = " + queryResultSku.getString("sku");
                PreparedStatement pstDet = connectDB.prepareStatement(deletePLD);
                pstDet.execute();
            }

        }else{
            String getValues = "SELECT sku_scanned FROM pickingList_detail WHERE PONum = " + PONum;
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()) {
               String deletePLD = "UPDATE product_indv SET status = '' WHERE sku = " + queryResult.getString("sku_scanned");
               PreparedStatement pstDet = connectDB.prepareStatement(deletePLD);
               pstDet.execute();
            }
        }

        //delete all entry in picking list detail that has the PO number
        String deletePLD = "DELETE FROM pickingList_detail WHERE PONum = " + PONum;
        PreparedStatement pstDetail = connectDB.prepareStatement(deletePLD);
        pstDetail.execute();


        //recreate a new entry
        //send data to database
        String PO = TF_PONum.getText();
        String comp_name = TF_compName.getText();
        String comp_add = TA_compAdd.getText();

        //insert new PO in POout
        String insertData = "INSERT INTO POout (PONum,company,company_add,date_created,createdBy,status,last_edit,date_edited,delivery_date) VALUES (?,?,?,?,?,?,?,?,?);";
        PreparedStatement pst = connectDB.prepareStatement(insertData);
        pst.setString(1, PO);
        pst.setString(2,comp_name);
        pst.setString(3,comp_add);
        pst.setString(4, date_created);
        pst.setString(5,createdBy);
        pst.setString(6, beforeStatus);
        pst.setString(7,Username);
        pst.setString(8,String.valueOf(java.time.LocalDate.now()));
        pst.setString(9,delivery_date);
        pst.execute();

        //insert new data into pickingList _detail
        String insertDataDet = "INSERT INTO pickingList_detail (PONum,company,date_created,upc,prod_name,sku) VALUES (?,?,?,?,?,?);";
        PreparedStatement pstDet = connectDB.prepareStatement(insertDataDet);
        for(product_indv p:newList){
            String upc = p.upc;
            String prod_name = p.prod_name;
            String sku = p.sku;
            pstDet.setString(1, PO);
            pstDet.setString(2,comp_name);
            pstDet.setString(3, String.valueOf(java.time.LocalDate.now()));
            pstDet.setString(4, upc);
            pstDet.setString(5,prod_name);
            pstDet.setString(6, sku);
            pstDet.execute();
        }

        //update status
        String updateDataDet = "UPDATE product_indv SET status = 'standby' WHERE sku = ?";
        PreparedStatement pstDetUP = connectDB.prepareStatement(updateDataDet);
        for(product_indv p:newList){
            pstDetUP.setString(1, p.sku);
            pstDetUP.execute();
        }

        closeWindow(event);
    }

    @FXML
    void resetList(ActionEvent event) {
        newList.clear();
    }

}

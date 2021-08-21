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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff;

import java.sql.*;

public class SP_newPickList_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<product_indv> tbl_NpickList;

    @FXML
    private TableColumn<product_indv, Integer> col_sn;

    @FXML
    private TableColumn<product_indv, Integer> col_upc;

    @FXML
    private TableColumn<product_indv, String> col_productName;

    @FXML
    private TableColumn<product_indv, String> col_qty;


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
    private Label errorAdd;

    private double xOffset = 0;
    private double yOffset = 0;
    private String Username, PONum;
    int countList = 1;
    int countNum = 1;

    ObservableList<product_indv> newList = FXCollections.observableArrayList();
    ObservableList<product_indv> temp = FXCollections.observableArrayList();

    DatabaseConnection con = new DatabaseConnection();
    Connection connectDB = con.getConnection();

    @FXML
    void initialize(){
        this.PONum = "";

        //initialise username and welcome message
        this.Username = Main_Controller.user;
        welcomeMsg(Username);

        CB_UPC.setEditable(true);
        CB_productName.setEditable(true);
        TF_PONum.setText(PONum);
        CB_UPC.setValue("");
        CB_productName.setValue("");

        try {
            //fill combobox
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

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_qty.setCellValueFactory((new PropertyValueFactory<>("qty")));

        tbl_NpickList.setItems(temp);

        tbl_NpickList.setEditable(true);
        col_qty.setCellFactory(TextFieldTableCell.forTableColumn());
        col_qty.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setQty(e.getNewValue());
        });
    }

    public void welcomeMsg(String username){
        welcomeLabel.setText("User: "+ username);
        Username = username;
    }


    @FXML
    void addToList(ActionEvent event) throws SQLException {
        String upc,qty,prod_name;
        boolean nostock = true;

        upc = CB_UPC.getValue();
        prod_name = CB_productName.getValue();

        if(PONum != null) {
            PONum = TF_PONum.getText();
        }

        if(upc==null){
            upc="";
        }
        if(prod_name==null){
            prod_name="";
        }

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
        }else if((!upc.equals("") || !upc.isBlank()) &&(prod_name==null || prod_name.isBlank()) ){
            qty = TF_qty.getText();

            if(isStringInt(qty)== true) {
                String getValues = "SELECT sku,location FROM product_indv WHERE upc = " + upc + " AND (status = '' or status is null) ORDER BY date_added ASC LIMIT " + qty + ";";
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(getValues);

                while (queryResult.next()) {
                    nostock = false;
                }

                if (nostock == true) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("An error has occurred");
                    alert.setHeaderText("Not enough stock for selected request! ");
                    alert.setContentText("Please select another product.");

                    alert.showAndWait();
                } else {

                    String getPDL = "SELECT prod_name FROM product_master WHERE upc = '" + upc + "' LIMIT 1;";
                    Statement statementPDL = connectDB.createStatement();
                    ResultSet queryResultPDL = statementPDL.executeQuery(getPDL);
                    while (queryResultPDL.next()) {
                        temp.add(new product_indv(countNum, upc,
                                queryResultPDL.getString("prod_name"), qty));
                        System.out.println("UPC: " + upc);
                        System.out.println("Product name: " + queryResultPDL.getString("prod_name"));
                        System.out.println("Qty: " + qty);
                        countNum++;
                    }

                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("An error has occurred");
                alert.setHeaderText("Quantity field is not a number! ");
                alert.setContentText("Please enter a valid number.");

                alert.showAndWait();
            }

        }else if((!prod_name.equals("") ||!prod_name.isBlank()) && (upc==null || upc.isBlank())){
            qty = TF_qty.getText();

            if(isStringInt(qty)== true) {
                String tempUpc = "";
                String getUpc = "SELECT upc FROM product_master WHERE prod_name = '" + prod_name + "' LIMIT 1;";
                Statement statementUpc1 = connectDB.createStatement();
                ResultSet queryResultUpc1 = statementUpc1.executeQuery(getUpc);

                while (queryResultUpc1.next()) {
                    String getValues = "SELECT sku,location FROM product_indv WHERE upc = '" + queryResultUpc1.getString("upc") + "' AND (status = '' or status is null) ORDER BY date_added ASC LIMIT " + qty + ";";
                    Statement statement = connectDB.createStatement();
                    ResultSet queryResult = statement.executeQuery(getValues);

                    while (queryResult.next()) {
                        nostock = false;
                    }
                }

                if (nostock == true) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("An error has occurred");
                    alert.setHeaderText("Not enough stock for selected request! ");
                    alert.setContentText("Please select another product.");

                    alert.showAndWait();
                } else {

                    Statement statementUpc = connectDB.createStatement();
                    ResultSet queryResultUpc = statementUpc.executeQuery(getUpc);

                    while (queryResultUpc.next()) {
                        temp.add(new product_indv(countNum,
                                queryResultUpc.getString("upc"), prod_name, qty));
                        tempUpc = queryResultUpc.getString("upc");
                        System.out.println("UPC: " + queryResultUpc.getString("upc"));
                        System.out.println("Product name: " + prod_name);
                        System.out.println("Qty: " + qty);
                        countNum++;
                    }

                }
            }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error has occurred");
            alert.setHeaderText("Quantity field is not a number! ");
            alert.setContentText("Please enter a valid number.");

            alert.showAndWait();
        }

            //System.out.println("product name: " + prod_name);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Empty fields");
            alert.setHeaderText("Both fields empty! ");
            alert.setContentText("Please select one product.");

            alert.showAndWait();
        }

        refreshTable();
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

    void getProd(String upc, String qty) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();

        String getValues = "SELECT sku,location FROM product_indv WHERE upc = "+ upc + " AND (status = '' or status is null) ORDER BY date_added ASC LIMIT "+ qty+ ";";
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(getValues);

        while(queryResult.next()){

            String getPDL = "SELECT prod_name FROM product_master WHERE upc = " + upc +";";
            Statement statementPDL = connectDB.createStatement();
            ResultSet queryResultPDL = statementPDL.executeQuery(getPDL);
            while(queryResultPDL.next()) {

                newList.add(new product_indv(countList, upc,
                        queryResultPDL.getString("prod_name"), queryResult.getString("sku"), queryResult.getString("location")));
                countList++;
                System.out.println("UPC: " + upc);
                System.out.println("Product name: " + queryResultPDL.getString("prod_name"));
                System.out.println("Sku: " + queryResult.getString("sku"));
                System.out.println("Location: "+ queryResult.getString("location"));
            }
        }

        System.out.println("SKU selected.");
    }

    void refreshTable(){

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_qty.setCellValueFactory((new PropertyValueFactory<>("qty")));

        tbl_NpickList.setItems(temp);
    }

    @FXML
    void resetList(ActionEvent event) {
        newList.clear();
        temp.clear();
        countNum=1;
    }

    @FXML
    void closeWindow(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP.fxml"));
            Parent root = loader.load();

            SP_Controller controllersp = loader.getController();
            controllersp.welcomeMsg(Username);
            Navigation nav = new Navigation();
            nav.stageSetup(event,root);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void orderProducts(ActionEvent event) throws SQLException {

        for(product_indv p:temp){
            getProd(p.upc, p.qty);
        }

        //send data to database
        String PO = "";
        if(TF_PONum.getText() == null || TF_PONum.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error has occurred");
            alert.setHeaderText("PO Number cannot be left blank! ");
            alert.setContentText("Please enter PO number.");

            alert.showAndWait();
        }
        else {
            PO = TF_PONum.getText();

            String comp_name = TF_compName.getText();
            String comp_add = TA_compAdd.getText();
            String SONum = genSONum();

            //insert new PO in POout
            String insertData = "INSERT INTO POout (PONum,company,company_add,date_created,createdBy,status,delivery_date,SONum,ppBy) VALUES (?,?,?,?,?,?,?,?,?);";
            PreparedStatement pst = connectDB.prepareStatement(insertData);
            pst.setString(1, PO);
            pst.setString(2, comp_name);
            pst.setString(3, comp_add);
            pst.setString(4, String.valueOf(java.time.LocalDate.now()));
            pst.setString(5, Username);
            pst.setString(6, "Unpicked");
            pst.setString(7, String.valueOf(java.time.LocalDate.now().plusWeeks(1)));
            pst.setString(8, SONum);
            pst.setString(9, "");
            pst.execute();

            //insert new data into pickingList _detail
            String insertDataDet = "INSERT INTO pickingList_detail (SONum,company,date_created,upc,prod_name,sku) VALUES (?,?,?,?,?,?);";
            PreparedStatement pstDet = connectDB.prepareStatement(insertDataDet);
            for (product_indv p : newList) {
                String upc = p.upc;
                String prod_name = p.prod_name;
                String sku = p.sku;
                pstDet.setString(1, SONum);
                pstDet.setString(2, comp_name);
                pstDet.setString(3, String.valueOf(java.time.LocalDate.now()));
                pstDet.setString(4, upc);
                pstDet.setString(5, prod_name);
                pstDet.setString(6, sku);
                pstDet.execute();
                System.out.println("Data inserted into pickingList_detail for sku: "+ p.sku);
            }
            System.out.println("Data inserted into pickingList_detail.");

            //update status
            System.out.println("Updating product status...");
            String updateDataDet = "UPDATE product_indv SET status = 'standby' WHERE sku = ?";
            PreparedStatement pstDetUP = connectDB.prepareStatement(updateDataDet);
            for (product_indv p : newList) {
                pstDetUP.setString(1, p.sku);
                pstDetUP.execute();
                System.out.println("Status updated for: " + p.sku);
            }
            System.out.println("Status successfully updated.");


            System.out.println("New sales order created.");

            closeWindow(event);
        }
    }

    public String genSONum(){
        String SONum = "";

        try {
            //fill PO Label
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT SONum FROM POout ORDER BY SONum DESC LIMIT 1";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while (queryResult.next()) {
                String check = queryResult.getString("SONum");
                if(check.equals("")){
                    SONum = "200000";
                }else {
                    int so = Integer.parseInt(check);
                    so = so + 1;
                    SONum = String.valueOf(so);
                }
            }

        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(SONum.equals("")){
            SONum = "200000";
        }

        return SONum;
    }

}

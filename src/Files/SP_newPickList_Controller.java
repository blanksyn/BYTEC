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
    private TableColumn<product_indv, String> col_sku;

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
    private Label errorAdd;

    private double xOffset = 0;
    private double yOffset = 0;
    private String Username, PONum;
    int countList = 1;

    ObservableList<product_indv> newList = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        CB_UPC.setEditable(true);
        CB_productName.setEditable(true);
        TF_PONum.setText(PONum);

        try {
            //fill combobox
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

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_sku.setCellValueFactory((new PropertyValueFactory<>("sku")));
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
                                        refreshTable();

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
        tbl_NpickList.setItems(newList);
    }

    public void welcomeMsg(String username, String PONum){
        welcomeLabel.setText("User: "+ username);
        Username = username;
        this.PONum = PONum;
    }


    @FXML
    void addToList(ActionEvent event) throws SQLException {
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
        }else if((!upc.isEmpty()||upc==null) && (!prod_name.isEmpty()||prod_name==null)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error has occurred");
            alert.setHeaderText("Both fields are filled! ");
            alert.setContentText("Select only one field.");

            alert.showAndWait();
        }else if(!upc.equals("")){
            String getValues = "SELECT sku,location FROM product_indv WHERE upc = "+ upc + " AND (status = '' or status is null) ORDER BY date_added ASC LIMIT "+ TF_qty.getText()+ ";";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

                while(queryResult.next()){

                    String getPDL = "SELECT prod_name FROM product_master WHERE upc = " + upc +";";
                    Statement statementPDL = connectDB.createStatement();
                    ResultSet queryResultPDL = statementPDL.executeQuery(getPDL);
                    while(queryResultPDL.next()) {
                            noStock=false;

                        newList.add(new product_indv(countList, upc,
                                queryResultPDL.getString("prod_name"), queryResult.getString("sku"), queryResult.getString("location")));
                        countList++;
                        System.out.println("UPC: " + upc);
                        System.out.println("Product name: " + queryResultPDL.getString("prod_name"));
                        System.out.println("Sku: " + queryResult.getString("sku"));
                        System.out.println("Location: "+ queryResult.getString("location"));
                    }

                }

            //System.out.println("UPC: " + upc);
            //System.out.println("PONUM: " + PONum);
            //System.out.println("LIMIT: "+ TF_qty.getText());

        }else if(!prod_name.equals("")){

            String getUpc = "SELECT upc FROM product_master WHERE prod_name = '"+ prod_name + "' LIMIT 1;";
            Statement statementUpc = connectDB.createStatement();
            ResultSet queryResultUpc = statementUpc.executeQuery(getUpc);
            while(queryResultUpc.next()) {

                String getValues = "SELECT sku,location FROM product_indv WHERE upc = " + queryResultUpc.getString("upc") + " AND (status = '' or status is null) ORDER BY date_added ASC LIMIT " + TF_qty.getText() + ";";
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(getValues);

                while (queryResult.next()) {
                    newList.add(new product_indv(countList,
                            queryResultUpc.getString("upc"), prod_name, queryResult.getString("sku"), queryResult.getString("location")));
                    countList++;
                    System.out.println("UPC: " + queryResultUpc.getString("upc"));
                    System.out.println("Product name: " + prod_name);
                    System.out.println("Sku: " + queryResult.getString("sku"));
                    System.out.println("Location: " + queryResult.getString("location"));
                    noStock=false;
                }

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
                                        refreshTable();

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
        tbl_NpickList.setItems(newList);
    }

    @FXML
    void resetList(ActionEvent event) {
        newList.clear();
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
        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();

        //check for existing PONumber


        //send data to database
        String PO = TF_PONum.getText();
        String comp_name = TF_compName.getText();
        String comp_add = TA_compAdd.getText();

        //insert new PO in POout
        String insertData = "INSERT INTO POout (PONum,company,company_add,date_created,createdBy,status,delivery_date) VALUES (?,?,?,?,?,?,?);";
        PreparedStatement pst = connectDB.prepareStatement(insertData);
        pst.setString(1, PO);
        pst.setString(2,comp_name);
        pst.setString(3,comp_add);
        pst.setString(4, String.valueOf(java.time.LocalDate.now()));
        pst.setString(5,Username);
        pst.setString(6, "Unpicked");
        pst.setString(7,String.valueOf(java.time.LocalDate.now().plusWeeks(1)));
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
        System.out.println("Status successfully updated.");

        //find current quantity in master list and update
        for(product_indv p:newList) {
            String getValues = "SELECT qty FROM product_master WHERE upc = '" + p.upc + "';";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while (queryResult.next()) {
                int num = queryResult.getInt("qty");
                num--;
                String updateQty = "UPDATE product_master SET qty = '"+ num + "' WHERE upc = '"+ p.upc+"';";
                PreparedStatement pstQty = connectDB.prepareStatement(updateQty);
                pstQty.execute();
                System.out.println("Master list quantity successfully updated.");
            }
        }

        System.out.println("New sales order created.");

        closeWindow(event);
    }

}

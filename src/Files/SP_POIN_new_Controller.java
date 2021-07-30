package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.*;

public class SP_POIN_new_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<POin> tbl_newPO;

    @FXML
    private TableColumn<POin, Integer> col_sn;

    @FXML
    private TableColumn<POin, String> col_upc;

    @FXML
    private TableColumn<POin, String> col_productName;

    @FXML
    private TableColumn<POin, Integer> col_qty;

    @FXML
    private TableColumn<POin, String> col_action;

    @FXML
    private ComboBox<String> CB_UPC;

    @FXML
    private ComboBox<String> CB_productName;

    @FXML
    private TextField TF_qty;

    @FXML
    private Button addBtn;

    @FXML
    private Button createPOBtn;

    @FXML
    private ComboBox<String> CB_supName;

    @FXML
    private TextField TF_PONum;

    @FXML
    private Button resetbtn;

    @FXML
    private TextField TF_supEmail;
    String Username;
    int countList =1;

    ObservableList<POin> newList = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        CB_UPC.setEditable(true);
        CB_productName.setEditable(true);

        try {
            //fill supplier combobox
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT name FROM supplier";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while (queryResult.next()) {
                CB_supName.getItems().add(queryResult.getString("name"));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            //fill PO Label
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT PONum FROM POin ORDER BY PONum DESC LIMIT 1";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while (queryResult.next()) {
                int po = Integer.parseInt(queryResult.getString("PONum")) + 1;
                TF_PONum.setText(String.valueOf(po));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_qty.setCellValueFactory((new PropertyValueFactory<>("qty")));


        //delete button
        Callback<TableColumn<POin, String>, TableCell<POin, String>> cellFactory
                = //
                new Callback<TableColumn<POin, String>, TableCell<POin, String>>() {
                    @Override
                    public TableCell call(final TableColumn<POin, String> param) {
                        final TableCell<POin, String> cell = new TableCell<POin, String>() {

                            final Button btn = new Button("Delete");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        POin entry = getTableView().getItems().get(getIndex());
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
        tbl_newPO.setItems(newList);
    }

    public void welcomeMsg(String username){
        welcomeLabel.setText("User: "+ username);
        Username = username;
    }

    @FXML
    void fill_cb(ActionEvent event) {
        String supName = CB_supName.getValue();

        try {
            //fill combobox
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT upc,prod_name FROM product_master WHERE supplier = '"+ supName + "';";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while (queryResult.next()) {
                String getSup = "SELECT email FROM supplier WHERE name = '"+ supName + "';";
                Statement stSup = connectDB.createStatement();
                ResultSet rsSup = stSup.executeQuery(getSup);

                while (rsSup.next()) {
                    CB_UPC.getItems().add(queryResult.getString("upc"));
                    CB_productName.getItems().add((queryResult.getString("prod_name")));
                    TF_supEmail.setText(rsSup.getString("email"));
                }
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void addToList(ActionEvent event) throws SQLException {
        String upc,prod_name;

        if(CB_UPC.getValue()==null){
            upc = "";
        }else{
            upc = CB_UPC.getValue();
        }

        if(CB_productName.getValue()==null){
            prod_name = "";
        }else{
            prod_name = CB_productName.getValue();
        }

        String qtyVal = "";


        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();

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
            qtyVal = TF_qty.getText();
            String getValues = "SELECT prod_name,supplier FROM product_master WHERE upc = "+ upc;
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()) {
                newList.add(new POin(countList, upc,
                        queryResult.getString("prod_name"),Integer.parseInt(qtyVal) ));
                countList++;

                System.out.println("UPC: " + upc);
                System.out.println("Product name: " + queryResult.getString("prod_name"));
                System.out.println("Quantity: " + qtyVal);

            }

        }else if(!prod_name.equals("")){
            qtyVal = TF_qty.getText();
            String getValues = "SELECT upc,supplier FROM product_master WHERE prod_name = '"+ prod_name+"';";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()) {
                newList.add(new POin(countList, queryResult.getString("upc"),
                        prod_name,Integer.parseInt(qtyVal) ));
                countList++;

                System.out.println("UPC: " + queryResult.getString("upc"));
                System.out.println("Product name: " + prod_name);
                System.out.println("Quantity: " + qtyVal);

            }
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

        printOL(newList);
    }

    @FXML
    void closeWindow(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_purchaseOrderIn(event,Username);
    }

    void printOL(ObservableList<POin> print){
        for(int i = 0; i<newList.size();i++) {
            System.out.println("Serial No: " + print.get(i).getSn());
            System.out.println("upc: " + print.get(i).getUpc());
            System.out.println("Product Name: " + print.get(i).getProd_name());
            System.out.println("val: " + print.get(i).qty);
        }

    }

    @FXML
    void createPO(ActionEvent event) throws SQLException {
        String PONum = TF_PONum.getText();
        String supplier = CB_supName.getValue();

        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();


        //update POin
        String newPOin = "INSERT INTO POin (PONum,supplier,orderBy,order_date,status,eta) VALUES (?,?,?,?,?,?);";
        PreparedStatement pst = connectDB.prepareStatement(newPOin);
        pst.setString(1, PONum);
        pst.setString(2,supplier);
        pst.setString(3,Username);
        pst.setString(4, String.valueOf(java.time.LocalDate.now()));
        pst.setString(5,"Not Received");
        pst.setString(6, String.valueOf(java.time.LocalDate.now().plusWeeks(1)));
        pst.execute();

        //update POin
        for(int i = 0; i<newList.size();i++) {
            String newPOinDetail = "INSERT INTO POin_detail (PONum,upc,qty_ordered,qty_rcv,qty_remaining) VALUES (?,?,?,?,?);";
            PreparedStatement pstDet = connectDB.prepareStatement(newPOinDetail);
            pstDet.setString(1, PONum);
            pstDet.setString(2,newList.get(i).getUpc());
            pstDet.setString(3, String.valueOf(newList.get(i).qty));
            pstDet.setString(4, "0");
            pstDet.setString(5, String.valueOf(newList.get(i).qty));
            pstDet.execute();

            System.out.println("SN entered to database: " + newList.get(i).getSn());
        }

        System.out.println("Purchase Order created.");
        System.out.println("Going back to main page...");

        Navigation nav = new Navigation();
        nav.SP_purchaseOrderIn(event,Username);
    }

    @FXML
    void resetList(ActionEvent event) {
        newList.clear();
    }

    void refreshTable(){

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_qty.setCellValueFactory((new PropertyValueFactory<>("qty")));


        //delete button
        Callback<TableColumn<POin, String>, TableCell<POin, String>> cellFactory
                = //
                new Callback<TableColumn<POin, String>, TableCell<POin, String>>() {
                    @Override
                    public TableCell call(final TableColumn<POin, String> param) {
                        final TableCell<POin, String> cell = new TableCell<POin, String>() {

                            final Button btn = new Button("Delete");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        POin entry = getTableView().getItems().get(getIndex());
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
        tbl_newPO.setItems(newList);
    }

}

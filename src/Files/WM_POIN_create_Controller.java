package Files;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class WM_POIN_create_Controller extends WM implements Initializable{

    @FXML
    private ImageView logoutBtn;

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
    private TextField TF_keyword, TF_qty;

    @FXML
    private ComboBox<String> CB_supplier;

    @FXML
    private ComboBox<String> CB_UPC;

    @FXML
    private ComboBox<String> CB_productName;
    
    String Username;
    int countList =1;
    
    ObservableList<POin> ObserveList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        welcome(welcomeLabel);
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
                CB_supplier.getItems().add(queryResult.getString("name"));
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
                                        ObserveList.remove(entry);
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
        tbl_newPO.setItems(ObserveList);
    }
    
    @FXML
    void fill_cb(ActionEvent event) {
        String supName = CB_supplier.getValue();

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
                    //TF_supEmail.setText(rsSup.getString("email"));
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
                ObserveList.add(new POin(countList, upc,
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
                ObserveList.add(new POin(countList, queryResult.getString("upc"),
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

        printOL(ObserveList);
    }
    
    void printOL(ObservableList<POin> print){
        for(int i = 0; i<ObserveList.size();i++) {
            System.out.println("Serial No: " + print.get(i).getSn());
            System.out.println("upc: " + print.get(i).getUpc());
            System.out.println("Product Name: " + print.get(i).getProd_name());
            System.out.println("val: " + print.get(i).qty);
        }

    }
    
    @FXML
    void resetList(ActionEvent event) {
        ObserveList.clear();
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
                                        ObserveList.remove(entry);
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
        tbl_newPO.setItems(ObserveList);
    }

    @FXML
    void Nav_PO_Restock(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_POIN_restock.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_PO_Ordered(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_POIN.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_createPO(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_POIN_create.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_Gen_Rpt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_inv.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_PO_In(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_POIN.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }


    @FXML
    void Nav_WH_Env(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_courierMgt.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_accMgt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    

    @FXML
    void closeWindow(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void logoutAcc(MouseEvent event) throws IOException {
        Navigation nav = new Navigation(); nav.logout(event,logoutBtn);
    }

    @FXML
    void orderProducts(ActionEvent event) throws SQLException{
        String PONum = " ";
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(PONum) as MaxPO FROM POin;");
            while (rs.next()) {
                int maxPO = 0;
                if(rs.getString("MaxPO") == null)
                    maxPO = 9000;
                else
                    maxPO = Integer.parseInt(rs.getString("MaxPO"));
                int po = maxPO + 1;
                PONum = String.valueOf(po);
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        String supplier = CB_supplier.getValue();

        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();


        //update POin
        String newPOin = "INSERT INTO POin (PONum,supplier,orderBy,order_date,status,eta) VALUES (?,?,?,?,?,?);";
        PreparedStatement pst = connectDB.prepareStatement(newPOin);
        pst.setString(1, PONum);
        pst.setString(2,supplier);
        pst.setString(3,"111");
        pst.setString(4, String.valueOf(java.time.LocalDate.now()));
        pst.setString(5,"Not Received");
        pst.setString(6, String.valueOf(java.time.LocalDate.now().plusWeeks(1)));
        pst.execute();

        //update POin
        for(int i = 0; i<ObserveList.size();i++) {
            String newPOinDetail = "INSERT INTO POin_detail (PONum,upc,qty_ordered,qty_rcv,qty_remaining) VALUES (?,?,?,?,?);";
            PreparedStatement pstDet = connectDB.prepareStatement(newPOinDetail);
            pstDet.setString(1, PONum);
            pstDet.setString(2,ObserveList.get(i).getUpc());
            pstDet.setString(3, String.valueOf(ObserveList.get(i).qty));
            pstDet.setString(4, "0");
            pstDet.setString(5, String.valueOf(ObserveList.get(i).qty));
            pstDet.execute();

            System.out.println("SN entered to database: " + ObserveList.get(i).getSn());
        }
        
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_POIN.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        
    }
}

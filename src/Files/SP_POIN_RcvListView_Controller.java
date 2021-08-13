package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.*;

public class SP_POIN_RcvListView_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<POin> tbl_rcvList;

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

    @FXML
    private Label Lab_PONum;

    @FXML
    private Label Lab_DONum;

    @FXML
    private Label Lab_dateRcv;

    @FXML
    private Label Lab_sup;

    @FXML
    private Button approvebtn;

    @FXML
    private Button RejectBtn;

    String Username,DONum,PONum;
    ObservableList<POin> rcvListView = FXCollections.observableArrayList();

    @FXML
    void initialize(String username,String DONum,String PONum){
        welcomeLabel.setText("User: "+ username);
        Lab_DONum.setText(DONum);
        this.DONum = DONum;
        this.Username = username;
        Lab_PONum.setText(PONum);
        this.PONum = PONum;
        int count = 1;

        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();

        //hide approve button
        try{

            String getValues = "SELECT approvedBy FROM POin_rcv WHERE DONum = '"+ DONum+"' LIMIT 1;";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);
            String ap = "";

            while (queryResult.next()){
                ap = "Approved by: "+ queryResult.getString("approvedBy");

            }

            if(ap.equals("Approved by: ")){
                approvebtn.setVisible(false);
                RejectBtn.setVisible(false);
            }else {
                System.out.println(ap);
            }



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //fill labels
        try{

            String getValues = "SELECT date_rcv FROM POin_rcv WHERE DONum = '"+ DONum +"' AND PONum = '"+ PONum +"' LIMIT 1";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                Lab_dateRcv.setText(String.valueOf(queryResult.getDate("date_rcv")));
            }

            String getSup = "SELECT supplier FROM POin WHERE PONum = '" + PONum +"';";
            Statement stSup = connectDB.createStatement();
            ResultSet rsSup = stSup.executeQuery(getSup);
            while(rsSup.next()) {
                Lab_sup.setText(rsSup.getString("supplier"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //fill table
        try{
            String getValues = "SELECT * FROM POin_detail WHERE PONum = '"+ PONum +"' AND qty_rcv != '0'";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                String getName = "SELECT prod_name FROM product_master WHERE upc = "+ queryResult.getString("upc");
                Statement stName = connectDB.createStatement();
                ResultSet rsName = stName.executeQuery(getName);
                while(rsName.next()) {
                    rcvListView.add(new POin(queryResult.getString("upc"), rsName.getString("prod_name"), count, queryResult.getInt("qty_ordered"),
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

        tbl_rcvList.setItems(rcvListView);

        FilteredList<POin> filteredData = new FilteredList<>(rcvListView, b-> true);

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
                }else if(String.valueOf(POin.getQty_rcv()).toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(String.valueOf(POin.getQty_remaining()).toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else
                    s = false;//no match found
                return s;
            });
        });

        SortedList<POin> sortedData = new SortedList<>(filteredData);
        //bind sorted results with tableview
        sortedData.comparatorProperty().bind(tbl_rcvList.comparatorProperty());
        //apply filtered and sorted data to table view
        tbl_rcvList.setItems(sortedData);

    }

    @FXML
    void approve(ActionEvent event) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();

        //add product qty to master list
        // get received quantity
        String getQty = "SELECT upc,qty FROM POin_rcv WHERE DONum = "+ DONum;
        Statement stQty = connectDB.createStatement();
        ResultSet rsQty = stQty.executeQuery(getQty);

        while(rsQty.next()) {
            //get current quantity from ML
            String getQtyML = "SELECT qty FROM product_master WHERE upc = "+ rsQty.getString("upc");
            Statement stQtyML = connectDB.createStatement();
            ResultSet rsQtyML = stQtyML.executeQuery(getQtyML);

            while(rsQtyML.next()) {
                //add both qty
                int sum = rsQtyML.getInt("qty")+ rsQty.getInt("qty");
                String updateQty = "UPDATE product_master SET qty =" + sum +" WHERE upc = " + rsQty.getString("upc");
                PreparedStatement psUpdateQty = connectDB.prepareStatement(updateQty);
                psUpdateQty.execute();
                System.out.println("Product master quantity updated.");
            }

        }

        //check for existing SKU*
        String getSKUstatus = "SELECT upc,qty FROM POin_rcv WHERE DONum = "+ DONum;
        Statement stSKU = connectDB.createStatement();
        ResultSet rsSKU = stSKU.executeQuery(getSKUstatus);

        while(rsSKU.next()){
            String getSKUindv = "SELECT count(upc) AS c FROM product_indv WHERE upc = "+ rsSKU.getString("upc");
            Statement stSKUindv = connectDB.createStatement();
            ResultSet rsSKUindv = stSKUindv.executeQuery(getSKUindv);
            while(rsSKUindv.next()) {
                if (rsSKUindv.getInt("c") > 0) {
                    generateExistingSKU(rsSKU.getString("upc"), rsSKU.getInt("qty"));
                    System.out.println("SKU generated.");
                } else {
                    generateNewSKU(rsSKU.getString("upc"), rsSKU.getInt("qty"));
                    System.out.println("NEW SKU generated.");
                }
            }

        }

        //update and check for partial or complete delivery
        String getQtyIndv = "SELECT qty_remaining,qty_rcv,upc FROM POin_detail WHERE PONum = "+ PONum+ " ;";
        Statement stQtyIndv = connectDB.createStatement();
        ResultSet rsQtyIndv = stQtyIndv.executeQuery(getQtyIndv);

        while(rsQtyIndv.next()) {
            int remaining = Integer.parseInt(rsQtyIndv.getString("qty_remaining")) - Integer.parseInt(rsQtyIndv.getString("qty_rcv"));
            String updateRem = "UPDATE POin_detail SET qty_rcv =? ,qty_remaining =? WHERE PONum = '"+ PONum + "' AND upc = '" + rsQtyIndv.getString("upc") + "';";
            PreparedStatement ps =connectDB.prepareStatement(updateRem);
            ps.setString(1,"0");
            ps.setString(2, String.valueOf(remaining));
            ps.execute();
            System.out.println("POin_detail updated.");
        }
        boolean st = false;
        String getValues = "SELECT * FROM POin_detail WHERE PONum = "+ PONum;
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(getValues);

        while(queryResult.next()) {
            if (Integer.parseInt(queryResult.getString("qty_remaining")) > 0) {
                st = true;
            }
        }

        //update status in POin
        String update = "";
        if(!st){
            update = "Fully received";
        } else {
            update = "Partially received";
        }
        String statusUp = "UPDATE POin SET status = '" + update + "' WHERE PONum = " + PONum;
        PreparedStatement ps = connectDB.prepareStatement(statusUp);
        ps.execute();
        System.out.println("PO status updated.");

        //update approved by
        String appBy = "UPDATE POin_rcv SET approvedBy = '" + Username + "' WHERE DONum = " + DONum;
        PreparedStatement psappBy = connectDB.prepareStatement(appBy);
        psappBy.execute();

        closeWindow(event);
    }

    @FXML
    void closeWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_POIN_RcvList.fxml"));
            Parent root = loader.load();
            SP_POIN_RcvList_Controller controllersp = loader.getController();
            controllersp.welcomeMsg(Username);

            Navigation nav = new Navigation();
            nav.stageSetup(event, root);
            System.out.println("Closing window...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void reject(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    void searchFunction(ActionEvent event) {

    }

    void generateNewSKU(String upc, int qty) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();
        String prod_nameSKU = "";
        String upcSKU = "";

            //get product name from upc
            String getUpcML = "SELECT prod_name FROM product_master WHERE upc = "+ upc + " LIMIT 1";
            Statement stUpcML = connectDB.createStatement();
            ResultSet rsUpcML = stUpcML.executeQuery(getUpcML);

            while(rsUpcML.next()) {
                // Get part of product code for sku
                prod_nameSKU = rsUpcML.getString("prod_name");
                prod_nameSKU = prod_nameSKU.substring(0,2);
                prod_nameSKU = prod_nameSKU.toUpperCase();

                //get part of upc for sku
                String upcString = upc;
                if (upcString.length() > 4)
                {
                    upcSKU = upcString.substring(upcString.length() - 4);
                }
                else
                {
                    upcSKU = upcString;
                }

                for(int i = 0;i<qty;i++) {
                    //sku result
                    String result = prod_nameSKU + upcSKU + i;

                    //check upc location
                    boolean locFilled = false;
                    String loc ="";
                    String getLoc = "SELECT location,max_qty FROM upc_location WHERE upc = "+ upc + " ORDER BY location ASC";
                    Statement stLoc = connectDB.createStatement();
                    ResultSet rsLoc = stLoc.executeQuery(getLoc);

                    while(rsLoc.next()) {
                        String getLocQty = "SELECT count(location) AS val FROM product_indv WHERE upc = '"+ upc + "' AND location = '"+ rsLoc.getString("location")+ "' ;";
                        Statement stLocQty = connectDB.createStatement();
                        ResultSet rsLocQty = stLocQty.executeQuery(getLocQty);

                        while(rsLocQty.next()) {
                            if((Integer.parseInt(rsLocQty.getString("val"))< Integer.parseInt(rsLoc.getString("max_qty"))) && !locFilled){
                                locFilled=true;
                                loc = rsLoc.getString("location");
                            }

                        }
                    }

                    String getExpDate = "SELECT expiry_date FROM POin_rcv WHERE upc = '"+ upc + "' AND DONum = '"+ DONum + "' ;";
                    Statement stExpDate = connectDB.createStatement();
                    ResultSet rsExpDate = stExpDate.executeQuery(getExpDate);

                    while(rsExpDate.next()) {


                        //insert sku in product_indv
                        String prodIndv = "INSERT INTO product_indv(upc,sku,date_added,location,expiry_date) VALUES (?,?,?,?,?)";
                        PreparedStatement pstindv = connectDB.prepareStatement(prodIndv);
                        pstindv.setString(1, upc);
                        pstindv.setString(2, result);
                        pstindv.setString(3, String.valueOf(java.time.LocalDate.now()));
                        pstindv.setString(4, loc);
                        pstindv.setString(5, String.valueOf(rsExpDate.getDate("expiry_date")));
                        pstindv.execute();

                        //insert into POin rcv detail
                        String prodrcvDet = "INSERT INTO POin_rcv_detail(DONum,upc,sku) VALUES (?,?,?)";
                        PreparedStatement pstrcvDet = connectDB.prepareStatement(prodrcvDet);
                        pstrcvDet.setString(1, DONum);
                        pstrcvDet.setString(2, upc);
                        pstrcvDet.setString(3, result);
                        pstrcvDet.execute();
                    }

                }
            }


    }

    void generateExistingSKU(String upc, int qty) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();

            //get latest sku
            String getSKUIndv = "SELECT sku FROM product_indv WHERE upc = '" + upc + "' ORDER BY length(sku) DESC, sku DESC LIMIT 1";
            Statement stSKUIndv = connectDB.createStatement();
            ResultSet rsSKUIndv = stSKUIndv.executeQuery(getSKUIndv);

            while(rsSKUIndv.next()) {
                String latestSKU = rsSKUIndv.getString("sku");
                //get product code
                String PC = latestSKU.substring(0,6);
                int num = Integer.parseInt(latestSKU.substring(6));

                for(int i = 1; i<qty+1;i++){
                    String result = PC.concat(String.valueOf(num+i));
                    System.out.println("Sku generated:" + result);

                    //check upc location
                    boolean locFilled = false;
                    String loc ="";
                    String getLoc = "SELECT location,max_qty FROM upc_location WHERE upc = "+ upc + " ORDER BY location ASC";
                    Statement stLoc = connectDB.createStatement();
                    ResultSet rsLoc = stLoc.executeQuery(getLoc);

                    while(rsLoc.next()) {
                        String getLocQty = "SELECT count(location) AS val FROM product_indv WHERE upc = '"+ upc + "' AND location = '"+ rsLoc.getString("location")+ "' ;";
                        Statement stLocQty = connectDB.createStatement();
                        ResultSet rsLocQty = stLocQty.executeQuery(getLocQty);

                        while(rsLocQty.next()) {
                            loc = rsLoc.getString("location");
                            if((Integer.parseInt(rsLocQty.getString("val"))<= Integer.parseInt(rsLoc.getString("max_qty"))) && !locFilled){
                                locFilled=true;
                                loc = rsLoc.getString("location");
                            }

                        }
                    }

                    String getExpDate = "SELECT expiry_date FROM POin_rcv WHERE upc = '"+ upc + "' AND DONum = '"+ DONum + "' ;";
                    Statement stExpDate = connectDB.createStatement();
                    ResultSet rsExpDate = stExpDate.executeQuery(getExpDate);

                    while(rsExpDate.next()) {

                        //insert sku in product_indv
                        String prodIndv = "INSERT INTO product_indv(upc,sku,date_added,location,expiry_date) VALUES (?,?,?,?,?)";
                        PreparedStatement pstindv = connectDB.prepareStatement(prodIndv);
                        pstindv.setString(1, upc);
                        pstindv.setString(2, result);
                        pstindv.setString(3, String.valueOf(java.time.LocalDate.now()));
                        pstindv.setString(4, loc);
                        pstindv.setString(5,  String.valueOf(rsExpDate.getDate("expiry_date")));
                        pstindv.execute();

                        String prodrcvDet = "INSERT INTO POin_rcv_detail(DONum,upc,sku) VALUES (?,?,?)";
                        PreparedStatement pstrcvDet = connectDB.prepareStatement(prodrcvDet);
                        pstrcvDet.setString(1, DONum);
                        pstrcvDet.setString(2, upc);
                        pstrcvDet.setString(3, result);
                        pstrcvDet.execute();
                    }
                }
            }

    }

}

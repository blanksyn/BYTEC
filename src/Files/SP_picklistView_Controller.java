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

import java.sql.*;

public class SP_picklistView_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<POout> tbl_pickList;

    @FXML
    private TableColumn<POout, Integer> col_sn;

    @FXML
    private TableColumn<POout, String> col_upc;

    @FXML
    private TableColumn<POout, String> col_productName;

    @FXML
    private TableColumn<POout, String> col_sku;

    @FXML
    private TableColumn<POout, String> col_skuScanned;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<?> CB_field;

    @FXML
    private Label Lab_PONum;

    @FXML
    private Label Lab_SONum;

    @FXML
    private Label Lab_comp;

    @FXML
    private Label Lab_dateCreated;

    @FXML
    private Button approveBtn;

    @FXML
    private Button rejectBtn;

    private double xOffset = 0;
    private double yOffset = 0;
    String Username,PONum;
    String SONum ="";
    ObservableList<POout> pickList = FXCollections.observableArrayList();

    DatabaseConnection con = new DatabaseConnection();
    Connection connectDB = con.getConnection();

    @FXML
    void initialize(String username, String SONum, String status){
        int count =1;

        //initialize PONum and Username
        welcomeLabel.setText("User: "+ username);
        Lab_PONum.setText(String.valueOf(PONum));
        this.Username = username;
        this.SONum = SONum;
        Lab_SONum.setText(SONum);

        //System.out.println("PONum: "+ PONum);
        //System.out.println("Username: " + Username);

        approveBtn.setVisible(false);
        rejectBtn.setVisible(false);

        if(status.equals("Not Approved")){
            approveBtn.setVisible(true);
            rejectBtn.setVisible(true);
        }

        try{

            //fill table
            String getSO = "SELECT PONum FROM POout WHERE SONum = "+ SONum ;
            Statement stSO = connectDB.createStatement();
            ResultSet rsSO = stSO.executeQuery(getSO);

            while(rsSO.next()){
                PONum = rsSO.getString("PONum");
            }
            Lab_PONum.setText(PONum);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try{

            //fill table
            String getValues = "SELECT sn,upc,prod_name,sku,sku_scanned FROM pickingList_detail WHERE SONum = "+ SONum ;
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                pickList.add(new POout(count,queryResult.getString("upc"),queryResult.getString("prod_name"),
                        queryResult.getString("sku"),queryResult.getString("sku_scanned")));
                count++;
            }

            String getLabel = "SELECT company,date_created FROM pickingList_detail WHERE SONum = '"+ SONum + "' LIMIT 1";
            Statement statement2 = connectDB.createStatement();
            ResultSet queryResult2 = statement2.executeQuery(getLabel);

            while(queryResult2.next()){
                Lab_comp.setText(queryResult2.getString("company"));
                Lab_dateCreated.setText(queryResult2.getDate("date_created").toString());
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_sku.setCellValueFactory((new PropertyValueFactory<>("sku")));
        col_skuScanned.setCellValueFactory((new PropertyValueFactory<>("sku_scanned")));

        tbl_pickList.setItems(pickList);

        FilteredList<POout> filteredData = new FilteredList<>(pickList, b-> true);

        TF_keyword.textProperty().addListener((observable, oldValue,newValue)->{

            //if no change detected then no change to list
            filteredData.setPredicate(POout -> {

                boolean s =false;
                if(newValue.isEmpty() || newValue.isBlank() || newValue ==null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(POout.getProd_name().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getUpc().toLowerCase().indexOf(searchKeyword)>-1){
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
        sortedData.comparatorProperty().bind(tbl_pickList.comparatorProperty());
        //apply filtered and sorted data to table view
        tbl_pickList.setItems(sortedData);
    }


    @FXML
    void approvePL(ActionEvent event) throws SQLException {

        String DO = generateDONum();
        //update picking list status to approved
        String getLabel = "UPDATE POout SET status = 'Approved',DONum = ? WHERE SONum = '"+ SONum + "'";
        PreparedStatement ps = connectDB.prepareStatement(getLabel);
        ps.setString(1,DO);
        ps.execute();

        //update product_indv status
        String getProd = "SELECT sku,sku_scanned,upc FROM pickingList_detail WHERE SONum = " +SONum +" AND (DONum ='' or DONum is null)";
        Statement stProd = connectDB.createStatement();
        ResultSet rsProd = stProd.executeQuery(getProd);
        while(rsProd.next()) {

            //update picking list status to approved
            String updateDO = "UPDATE pickingList_detail SET DONum = ? WHERE sku_scanned = '"+ rsProd.getString("sku_scanned") + "'";
            PreparedStatement upDO = connectDB.prepareStatement(updateDO);
            ps.setString(1,DO);
            ps.execute();

            //update new scanned sku with packed
            String upProdSKU = "UPDATE product_indv SET status = 'Packed' WHERE sku = '" + rsProd.getString("sku_scanned") + "';";
            PreparedStatement psUpProdSKU = connectDB.prepareStatement(upProdSKU);
            psUpProdSKU.execute();

            //find current quantity in master list and update
            System.out.println("Updating qty in masterlist...");

            String getValues = "SELECT qty FROM product_master WHERE upc = '" + rsProd.getString("upc") + "';";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while (queryResult.next()) {
                int num = queryResult.getInt("qty");
                num--;
                String updateQty = "UPDATE product_master SET qty = '" + num + "' WHERE upc = '" + rsProd.getString("upc") + "';";
                PreparedStatement pstQty = connectDB.prepareStatement(updateQty);
                pstQty.execute();
                System.out.println("Master list quantity successfully updated: "+ rsProd.getString("upc"));
            }

        }

        System.out.println("Purchase Order Approved.");
        closeWindow(event);
    }

    public String generateDONum(){
        String DONum = "";
        int currentDO =0;
        boolean check = false;

        //if there is existing DONum
        try{

            //get latest DO number
            String getDONum = "SELECT DONum FROM POout ORDER BY DONum DESC LIMIT 1";
            Statement stDONum = connectDB.createStatement();
            ResultSet rsDONum = stDONum.executeQuery(getDONum);

                while (rsDONum.next()) {
                    try {
                        String sub = "";
                        sub= rsDONum.getString("DONum");
                        if(sub.equals("")){
                            DONum = "100000";
                        }else {
                            currentDO = Integer.parseInt(sub);
                            DONum = String.valueOf(currentDO + 1);
                            check = true;
                        }

                    }catch (NullPointerException e) {
                        DONum = "100000";
                    }

                    System.out.println("Generated DO number: " + DONum);
                }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return DONum;
    }

    @FXML
    void rejectPL(ActionEvent event) throws SQLException {

        String rejectCouunt ="0";
        String getReject = "SELECT reject FROM POout WHERE SONum = '"+ SONum + "' LIMIT 1";
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(getReject);
        while(queryResult.next()){
            rejectCouunt=queryResult.getString("reject");
        }
        int count = Integer.parseInt(rejectCouunt) + 1;

        String getRejectC = "UPDATE POout SET reject = '" + count + "', status = 'Unpicked' WHERE SONum = '"+ SONum + "' LIMIT 1";
        Statement statement2 = connectDB.createStatement();
        statement2.executeUpdate(getRejectC);

        closeWindow(event);
    }

    @FXML
    void closeWindow(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP.fxml"));
            Parent root = loader.load();

            SP_Controller controllersp = loader.getController();
            controllersp.welcomeMsg(Username);

            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            loginStage.centerOnScreen();
            loginStage.setScene(scene);
            root.setOnMousePressed((MouseEvent event1) -> {
                xOffset = event1.getSceneX();
                yOffset = event1.getSceneY();
            });

            root.setOnMouseDragged((MouseEvent event1) -> {
                loginStage.setX(event1.getScreenX() - xOffset);
                loginStage.setY(event1.getScreenY() - yOffset);
            });

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

}

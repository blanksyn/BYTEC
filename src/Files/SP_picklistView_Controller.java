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
    private Label Lab_comp;

    @FXML
    private Label Lab_dateCreated;

    @FXML
    private Button approveBtn;

    @FXML
    private Button rejectBtn;

    private double xOffset = 0;
    private double yOffset = 0;
    String Username;
    String PONum;
    ObservableList<POout> pickList = FXCollections.observableArrayList();

    @FXML
    void initialize(String username, String PONum, String status){
        int count =1;

        //initialize PONum and Username
        welcomeLabel.setText("User: "+ username);
        Lab_PONum.setText(String.valueOf(PONum));
        this.Username = username;
        this.PONum = PONum;

        //System.out.println("PONum: "+ PONum);
        //System.out.println("Username: " + Username);

        if(status.equals("Unpicked")){
            approveBtn.setVisible(false);
            rejectBtn.setVisible(false);
        }

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            //fill table
            String getValues = "SELECT sn,upc,prod_name,sku,sku_scanned FROM pickingList_detail WHERE PONum = "+ PONum ;
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                pickList.add(new POout(count,queryResult.getString("upc"),queryResult.getString("prod_name"),
                        queryResult.getString("sku"),queryResult.getString("sku_scanned")));
                count++;
            }

            String getLabel = "SELECT company,date_created FROM pickingList_detail WHERE PONum = '"+ PONum + "' LIMIT 1";
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
    }


    @FXML
    void approvePL(ActionEvent event) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();

        String DO = generateDONum();
        //update picking list status to approved
        String getLabel = "UPDATE POout SET status = 'Approved',DONum = ? WHERE PONum = '"+ PONum + "'";
        PreparedStatement ps = connectDB.prepareStatement(getLabel);
        ps.setString(1,DO);
        ps.execute();

        //update product_indv status
        String getProd = "SELECT sku,sku_scanned FROM pickingList_detail WHERE PONum = " +PONum +" AND (DONum ='' or DONum is null)";
        Statement stProd = connectDB.createStatement();
        ResultSet rsProd = stProd.executeQuery(getProd);
        while(rsProd.next()) {
            //update DONum
            String upDO = "UPDATE pickingList_detail SET DONum = ? WHERE sku_scanned = '" +rsProd.getString("sku_scanned") + "' AND PONum = "+ PONum ;
            PreparedStatement psUpDo = connectDB.prepareStatement(upDO);
            psUpDo.setString(1,DO);
            psUpDo.execute();

            //update status
            String upProd = "UPDATE product_indv SET status = '' WHERE sku = '" + rsProd.getString("sku")+"';";
            PreparedStatement psUpProd = connectDB.prepareStatement(upProd);
            psUpProd.execute();

            String upProdSKU = "UPDATE product_indv SET status = 'Picked' WHERE sku = '" + rsProd.getString("sku_scanned")+"';";
            PreparedStatement psUpProdSKU = connectDB.prepareStatement(upProdSKU);
            psUpProdSKU.execute();
        }

        System.out.println("Purchase Order Approved.");
        closeWindow(event);
    }

    public String generateDONum(){
        String DONum = "";
        int currentDO =0;

        //if there is existing DONum
        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            //get latest DO number
            String getDONum = "SELECT DONum FROM POout ORDER BY DONum DESC LIMIT 1";
            Statement stDONum = connectDB.createStatement();
            ResultSet rsDONum = stDONum.executeQuery(getDONum);

            while(rsDONum.next()) {
                currentDO = Integer.parseInt(rsDONum.getString("DONum"));
                DONum = String.valueOf(currentDO +1);
            }

            //generate new DO
            if(currentDO==0){
                currentDO = 92235;
                DONum= String.valueOf(currentDO);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return DONum;
    }

    @FXML
    void rejectPL(ActionEvent event) throws SQLException {
        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();
        String rejectCouunt ="0";
        String getReject = "SELECT reject FROM POout WHERE PONum = '"+ PONum + "' LIMIT 1";
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(getReject);
        while(queryResult.next()){
            rejectCouunt=queryResult.getString("reject");
        }
        int count = Integer.parseInt(rejectCouunt) + 1;

        String getRejectC = "UPDATE POout SET reject = "+count +" WHERE PONum = '"+ PONum + "' LIMIT 1";
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

    @FXML
    void searchFunction(ActionEvent event) {

    }

}

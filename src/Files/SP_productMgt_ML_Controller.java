package Files;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SP_productMgt_ML_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private ImageView logoutBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button pickListBtn;

    @FXML
    private Button WHEnvBtn;

    @FXML
    private Button POINBtn;

    @FXML
    private Button POOutBtn;

    @FXML
    private Button productMLBtn;

    @FXML
    private TableView<prod_master> tableProductML;

    @FXML
    private TableColumn<prod_master, Integer> col_sn;

    @FXML
    private TableColumn<prod_master, String> col_upc;

    @FXML
    private TableColumn<prod_master, String> col_Name;

    @FXML
    private TableColumn<prod_master, Integer> col_qty;

    @FXML
    private TableColumn<prod_master, String> col_unit;

    @FXML
    private TableColumn<prod_master, String> col_location;

    @FXML
    private TableColumn<prod_master, String> col_supplier;

    @FXML
    private TableColumn<prod_master, String> col_category;

    @FXML
    private TableColumn<prod_master, Integer> col_minQty;

    @FXML
    private TableColumn<prod_master, Integer> col_maxQty;

    @FXML
    private TableColumn<prod_master, String> col_ARStatus;

    @FXML
    private TableColumn<prod_master, String> col_dateAdded;

    @FXML
    private TableColumn<prod_master, String> col_action;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<String> CB_field;

    String Username;

    ObservableList<prod_master> allProd = FXCollections.observableArrayList();


    @FXML
    void initialize(){
        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT * FROM product_master ORDER BY upc ASC";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                allProd.add(new prod_master(count,queryResult.getString("upc"),queryResult.getString("prod_name"),queryResult.getInt("qty"),
                        queryResult.getString("unit"),queryResult.getString("location"),queryResult.getString("supplier"),
                        queryResult.getString("category"),queryResult.getInt("min_qty"),queryResult.getInt("max_qty"),queryResult.getString("auto_restock_status"),
                        queryResult.getString("date_added")));
                count++;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        Callback<TableColumn<prod_master, String>, TableCell<prod_master, String>> cellFactory
                = //
                new Callback<TableColumn<prod_master, String>, TableCell<prod_master, String>>() {
                    @Override
                    public TableCell call(final TableColumn<prod_master, String> param) {
                        final TableCell<prod_master, String> cell = new TableCell<prod_master, String>() {

                            final Button btn = new Button("View All SKU");
                            final Button btn2 = new Button("View details");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {

                                        //get PONum
                                        prod_master data = getTableView().getItems().get(getIndex());
                                        //System.out.println("selectedData: " + data.getPONum());

                                        try{
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_productMgt_ML_detail.fxml"));
                                            Parent root = loader.load();

                                            SP_productMgt_ML_detail_Controller controllersp = loader.getController();
                                            controllersp.initialize(Username,data.upc);

                                            Navigation nav = new Navigation();
                                            nav.stageSetup(event,root);

                                        }catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    });

                                    btn2.setOnAction(event -> {

                                        //get PONum
                                        prod_master data = getTableView().getItems().get(getIndex());
                                        //System.out.println("selectedData: " + data.getPONum());

                                        try{
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_productMgt_ML_detailView.fxml"));
                                            Parent root = loader.load();

                                            SP_productMgt_ML_detailView_Controller controllersp = loader.getController();
                                            controllersp.initialize(Username,data.upc);

                                            Navigation nav = new Navigation();
                                            nav.stageSetup(event,root);

                                        }catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    });
                                    HBox pane = new HBox(btn,btn2);
                                    pane.setAlignment(Pos.CENTER);
                                    setGraphic(pane);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        col_action.setCellFactory(cellFactory);

        col_sn.setCellValueFactory((new PropertyValueFactory<>("SN")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("UPC")));
        col_Name.setCellValueFactory((new PropertyValueFactory<>("ProdName")));
        col_qty.setCellValueFactory((new PropertyValueFactory<>("qty")));
        col_unit.setCellValueFactory((new PropertyValueFactory<>("unit")));
        col_location.setCellValueFactory((new PropertyValueFactory<>("location")));
        col_supplier.setCellValueFactory((new PropertyValueFactory<>("supplier")));
        col_category.setCellValueFactory((new PropertyValueFactory<>("category")));
        col_minQty.setCellValueFactory((new PropertyValueFactory<>("min_qty")));
        col_maxQty.setCellValueFactory((new PropertyValueFactory<>("max_qty")));
        col_ARStatus.setCellValueFactory((new PropertyValueFactory<>("auto_status")));
        col_dateAdded.setCellValueFactory((new PropertyValueFactory<>("date_added")));

        tableProductML.setItems(allProd);
    }


    public void welcomeMsg(String username){
        welcomeLabel.setText("User: "+ username);
        Username = username;
    }

    @FXML
    void Nav_DO_Out(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_deliveryOrderOut(event,Username);
    }

    @FXML
    void Nav_PO_In(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_purchaseOrderIn(event,Username);
    }

    @FXML
    void Nav_pickList(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_purchaseOrderOut(event,Username);
    }

    @FXML
    void Nav_productMgt(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_productManagement(event,Username);
    }

    @FXML
    void Nav_productML(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_productManagement(event,Username);
    }

    @FXML
    void closeWindow(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void logoutAcc(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Confirm logout?");

        if(alert.showAndWait().get()== ButtonType.OK){

            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage logoutStage = (Stage)logoutBtn.getScene().getWindow();
            Scene scene = new Scene(root, 700, 650);
            logoutStage.setTitle("Login");
            logoutStage.setScene(scene);
            Image image = new Image("image/logo192.png");
            logoutStage.getIcons().add(image);
            scene.setFill(Color.TRANSPARENT);
            logoutStage.centerOnScreen();
            logoutStage.show();

        }
    }

    @FXML
    void searchFunction(ActionEvent event) {

    }

}

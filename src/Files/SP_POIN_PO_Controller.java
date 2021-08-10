package Files;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

public class SP_POIN_PO_Controller {

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
    private Button POBtn;

    @FXML
    private Button rcvListBtn;

    @FXML
    private TableView<POin> tbl_PO;

    @FXML
    private TableColumn<POin, Integer> col_sn;

    @FXML
    private TableColumn<POin, String> col_PONum;

    @FXML
    private TableColumn<POin, String> col_supplier;

    @FXML
    private TableColumn<POin, String> col_ordBy;

    @FXML
    private TableColumn<POin, String> col_ordDate;

    @FXML
    private TableColumn<POin, String> col_status;

    @FXML
    private TableColumn<POin, String> col_action;

    @FXML
    private TextField TF_keyword;


    @FXML
    private Button createPOINBtn;

    String Username;

    ObservableList<POin> POList = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        createPOINBtn.setVisible(true);
        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT * FROM POin WHERE status != 'Fully Received';";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                POList.add(new POin(count,queryResult.getString("PONum"),queryResult.getString("supplier"),queryResult.getString("orderBy"),
                        queryResult.getDate("order_date"),queryResult.getString("status")));
                count++;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        Callback<TableColumn<POin, String>, TableCell<POin, String>> cellFactory
                = //
                new Callback<TableColumn<POin, String>, TableCell<POin, String>>() {
                    @Override
                    public TableCell call(final TableColumn<POin, String> param) {
                        final TableCell<POin, String> cell = new TableCell<POin, String>() {

                            final Button btn = new Button("View");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {

                                        //get PONum
                                        POin data = getTableView().getItems().get(getIndex());
                                        //System.out.println("selectedData: " + data.getPONum());

                                        try{
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_POIN_POVIEW.fxml"));
                                            Parent root = loader.load();

                                            SP_POIN_POVIEW_Controller controllersp = loader.getController();
                                            controllersp.initialize(Username,data.PONum);

                                            Navigation nav = new Navigation();
                                            nav.stageSetup(event,root);

                                        }catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
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

        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_PONum.setCellValueFactory((new PropertyValueFactory<>("PONum")));
        col_supplier.setCellValueFactory((new PropertyValueFactory<>("supplier")));
        col_ordBy.setCellValueFactory((new PropertyValueFactory<>("orderBy")));
        col_ordDate.setCellValueFactory((new PropertyValueFactory<>("orderDate")));
        col_status.setCellValueFactory((new PropertyValueFactory<>("status")));

        tbl_PO.setItems(POList);

        FilteredList<POin> filteredData = new FilteredList<>(POList, b-> true);

        TF_keyword.textProperty().addListener((observable, oldValue,newValue)->{

            //if no change detected then no change to list
            filteredData.setPredicate(POin -> {

                boolean s =false;
                if(newValue.isEmpty() || newValue.isBlank() || newValue ==null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(POin.getPONum().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POin.getSupplier().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POin.getOrderBy().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POin.getOrderDate().toString().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POin.getStatus().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else
                    s = false;//no match found
                return s;
            });
        });

        SortedList<POin> sortedData = new SortedList<>(filteredData);
        //bind sorted results with tableview
        sortedData.comparatorProperty().bind(tbl_PO.comparatorProperty());
        //apply filtered and sorted data to table view
        tbl_PO.setItems(sortedData);
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
    void Nav_viewPOIN(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_purchaseOrderIn(event,Username);
    }

    @FXML
    void createPO(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_POIN_new.fxml"));
            Parent root = loader.load();
            SP_POIN_new_Controller controllersp = loader.getController();
            controllersp.welcomeMsg(Username);

            Navigation nav = new Navigation();
            nav.stageSetup(event, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Nav_viewRcvList(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_POIN_RcvList.fxml"));
            Parent root = loader.load();
            SP_POIN_RcvList_Controller controllersp = loader.getController();
            controllersp.welcomeMsg(Username);

            Navigation nav = new Navigation();
            nav.stageSetup(event, root);
        } catch (IOException e) {
            e.printStackTrace();
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
    void searchFunction(ActionEvent event) {

    }

}

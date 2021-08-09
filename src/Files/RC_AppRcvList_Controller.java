package Files;

import javafx.application.Platform;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;

public class RC_AppRcvList_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private ImageView logoutBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button POINBtn;

    @FXML
    private Button AppRcvListBtn;

    @FXML
    private TableView<POin> tbl_pickList;

    @FXML
    private TableColumn<POin, Integer> col_sn;

    @FXML
    private TableColumn<POin, String> col_PONum;

    @FXML
    private TableColumn<POin, String> col_DONum;

    @FXML
    private TableColumn<POin, String> col_supplier;

    @FXML
    private TableColumn<POin, Date> col_dateRcv;

    @FXML
    private TableColumn<POin, String> col_rcvBy;

    @FXML
    private TableColumn<POin,String> col_action;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<String> CB_field;

    String Username;

    ObservableList<POin> rcvList = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT PONum,supplier FROM POin WHERE status != 'Not received' ORDER BY order_date ASC";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                String getDO = "SELECT DISTINCT DONum,rcvBy,date_rcv FROM POin_rcv WHERE PONum = " + queryResult.getString("PONum");
                Statement stDO = connectDB.createStatement();
                ResultSet rsDO = stDO.executeQuery(getDO);

                while(rsDO.next()) {
                    rcvList.add(new POin(count, queryResult.getString("PONum"), rsDO.getString("DONum"), queryResult.getString("supplier")
                            ,rsDO.getString("rcvBy"), rsDO.getDate("date_rcv")));
                    count++;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_PONum.setCellValueFactory((new PropertyValueFactory<>("PONum")));
        col_DONum.setCellValueFactory((new PropertyValueFactory<>("DONum")));
        col_supplier.setCellValueFactory((new PropertyValueFactory<>("supplier")));
        col_dateRcv.setCellValueFactory((new PropertyValueFactory<>("date_rcv")));
        col_rcvBy.setCellValueFactory((new PropertyValueFactory<>("rcvBy")));


        //View button
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
                                        POin entry = getTableView().getItems().get(getIndex());
                                        //pass username and ponum
                                        try {
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("RC_AppRcvList_view.fxml"));
                                            Parent root = loader.load();
                                            RC_AppRcvList_view_Controller controllerrc = loader.getController();
                                            controllerrc.initialize(Username,entry.PONum,entry.DONum,entry.supplier,entry.date_rcv);

                                            Navigation nav = new Navigation();
                                            nav.stageSetup(event,root);

                                        } catch (IOException e) {
                                            e.printStackTrace();
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
        tbl_pickList.setItems(rcvList);

        FilteredList<POin> filteredData = new FilteredList<>(rcvList, b-> true);

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
                }else if(POin.getDONum().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POin.getSupplier().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(String.valueOf(POin.getDate_rcv()).toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POin.getRcvBy().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else
                    s = false;//no match found
                return s;
            });
        });

        SortedList<POin> sortedData = new SortedList<>(filteredData);
        //bind sorted results with tableview
        sortedData.comparatorProperty().bind(tbl_pickList.comparatorProperty());
        //apply filtered and sorted data to table view
        tbl_pickList.setItems(sortedData);
    }

    public void welcomeMsg(String username){
        welcomeLabel.setText("User: "+ username);
        this.Username = username;
    }

    @FXML
    void Nav_AppRcvList(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.RC_ApproveRcvList(event,Username);
    }

    @FXML
    void Nav_PO_In(ActionEvent event) {
        Navigation nav =new Navigation();
        nav.RC_purchaseOrderIn(event,Username);
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

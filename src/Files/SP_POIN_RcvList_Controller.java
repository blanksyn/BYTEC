package Files;

import javafx.application.Platform;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;

public class SP_POIN_RcvList_Controller {

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
    private TableColumn<POin, String> col_DONum;

    @FXML
    private TableColumn<POin, String> col_supplier;

    @FXML
    private TableColumn<POin, Date> col_dateRcv;

    @FXML
    private TableColumn<POin, String> col_rcvBy;

    @FXML
    private TableColumn<POin, String> col_appBy;

    @FXML
    private TableColumn<POin, String> col_action;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<?> CB_field;

    String Username;
    ObservableList<POin> rcvList = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getStatus = "SELECT PONum FROM POin WHERE status != 'Fully received';";
            Statement stStatus = connectDB.createStatement();
            ResultSet rsStatus = stStatus.executeQuery(getStatus);

            while(rsStatus.next()) {

                String getValues = "SELECT * FROM POin_rcv WHERE PONum = "+ rsStatus.getString("PONum")+ " GROUP BY DONum ";
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(getValues);

                while (queryResult.next()) {
                    String getSup = "SELECT supplier FROM POin WHERE PONum = " + queryResult.getString("PONum");
                    Statement stSup = connectDB.createStatement();
                    ResultSet rsSup = stSup.executeQuery(getSup);
                    while (rsSup.next()) {
                        rcvList.add(new POin(count, queryResult.getString("PONum"), queryResult.getString("DONum"), rsSup.getString("supplier"),
                                queryResult.getDate("date_rcv"), queryResult.getString("rcvBy"),queryResult.getString("approvedBy")));
                        count++;
                    }
                }
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
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_POIN_RcvListView.fxml"));
                                            Parent root = loader.load();

                                            SP_POIN_RcvListView_Controller controllersp = loader.getController();
                                            controllersp.initialize(Username,data.DONum);

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
        col_DONum.setCellValueFactory((new PropertyValueFactory<>("DONum")));
        col_supplier.setCellValueFactory((new PropertyValueFactory<>("supplier")));
        col_dateRcv.setCellValueFactory((new PropertyValueFactory<>("date_rcv")));
        col_rcvBy.setCellValueFactory((new PropertyValueFactory<>("rcvBy")));
        col_appBy.setCellValueFactory((new PropertyValueFactory<>("appBy")));

        tbl_PO.setItems(rcvList);
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
    void Nav_viewRcvList(ActionEvent event) {

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

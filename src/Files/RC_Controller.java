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

public class RC_Controller {

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
    private TableColumn<POin, String> col_supplier;

    @FXML
    private TableColumn<POin, String> col_ordBy;

    @FXML
    private TableColumn<POin, Date> col_ordDate;

    @FXML
    private TableColumn<POin, String> col_status;

    @FXML
    private TableColumn<POin, Date> col_eta;

    @FXML
    private TableColumn<POin, String> col_action;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<?> CB_field;
    String Username;

    ObservableList<POin> rcvPO = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT * FROM POin WHERE status != 'Fully received' AND status != 'Not Approved' ORDER BY order_date ASC";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                rcvPO.add(new POin(count,queryResult.getString("PONum"),queryResult.getString("supplier"),queryResult.getString("orderBy"),
                        queryResult.getDate("order_date"),queryResult.getString("status"),queryResult.getDate("eta")));
                count++;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_PONum.setCellValueFactory((new PropertyValueFactory<>("PONum")));
        col_supplier.setCellValueFactory((new PropertyValueFactory<>("supplier")));
        col_ordBy.setCellValueFactory((new PropertyValueFactory<>("orderBy")));
        col_ordDate.setCellValueFactory((new PropertyValueFactory<>("orderDate")));
        col_status.setCellValueFactory((new PropertyValueFactory<>("status")));
        col_eta.setCellValueFactory((new PropertyValueFactory<>("eta")));


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
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("RC_POView.fxml"));
                                            Parent root = loader.load();
                                            RC_POView_Controller controllerrc = loader.getController();
                                            controllerrc.initialize(Username,entry.PONum,entry.supplier);

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
        tbl_pickList.setItems(rcvPO);
    }

    public void welcomeMsg(String username){
        welcomeLabel.setText("User: "+ username);
        Username = username;
    }

    @FXML
    void Nav_AppRcvList(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.RC_ApproveRcvList(event,Username);
    }

    @FXML
    void Nav_PO_In(ActionEvent event) {
        Navigation nav = new Navigation();
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

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
import java.util.Locale;

public class SP_Controller {

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
    private Button pickListBtn1;

    @FXML
    private TableView<POout> tbl_pickList;

    @FXML
    private TableColumn<POout, Integer> col_sn;

    @FXML
    private TableColumn<POout, Integer> col_PONum;

    @FXML
    private TableColumn<POout, Integer> col_SONum;

    @FXML
    private TableColumn<POout, String> col_comp;

    @FXML
    private TableColumn<POout, String> col_dateCreate;

    @FXML
    private TableColumn<POout, String> col_pickPack;

    @FXML
    private TableColumn<POout, String> col_status;

    @FXML
    private TableColumn<POout, String> col_action;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<?> CB_field;

    @FXML
    private Button createPickBtn;

    private double xOffset = 0;
    private double yOffset = 0;
    String Username;

    ObservableList<POout> pickList = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT * FROM POout WHERE status != 'Approved' ORDER BY reject DESC, status ASC, date_created ASC";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                pickList.add(new POout(count,queryResult.getString("PONum"),queryResult.getString("SONum"),queryResult.getString("company"),
                        queryResult.getDate("date_created"),queryResult.getString("ppBy"),queryResult.getString("status"),queryResult.getString("reject")));
                count++;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        Callback<TableColumn<POout, String>, TableCell<POout, String>> cellFactory
                = //
                new Callback<TableColumn<POout, String>, TableCell<POout, String>>() {
                    @Override
                    public TableCell call(final TableColumn<POout, String> param) {
                        final TableCell<POout, String> cell = new TableCell<POout, String>() {

                            final Button btn = new Button("View");
                            final Button btn2 = new Button("Edit");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {

                                        //get PONum
                                        POout data = getTableView().getItems().get(getIndex());
                                        //System.out.println("selectedData: " + data.getPONum());

                                        try{
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_picklistView.fxml"));
                                            Parent root = loader.load();

                                            SP_picklistView_Controller controllersp = loader.getController();
                                            controllersp.initialize(Username,data.SONum, data.status);

                                            Navigation nav = new Navigation();
                                            nav.stageSetup(event,root);

                                        }catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    });

                                    btn2.setOnAction(event -> {

                                        //get PONum
                                        POout data = getTableView().getItems().get(getIndex());
                                        //System.out.println("selectedData: " + data.getPONum());

                                        try{
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_editPicklist.fxml"));
                                            Parent root = loader.load();

                                            SP_editPickList_Controller controllersp = loader.getController();
                                            controllersp.initialize(Username,data.SONum);

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

        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_PONum.setCellValueFactory((new PropertyValueFactory<>("PONum")));
        col_SONum.setCellValueFactory((new PropertyValueFactory<>("SONum")));
        col_comp.setCellValueFactory((new PropertyValueFactory<>("company")));
        col_dateCreate.setCellValueFactory((new PropertyValueFactory<>("date_created")));
        col_pickPack.setCellValueFactory((new PropertyValueFactory<>("ppBy")));
        col_status.setCellValueFactory((new PropertyValueFactory<>("status")));

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

                if(POout.getPONum().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getCompany().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getDate_created().toString().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getPpBy().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getStatus().toLowerCase().indexOf(searchKeyword)>-1){
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
    void closeWindow(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void createPickingList(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_newPickList.fxml"));
            Parent root = loader.load();

            SP_newPickList_Controller controllersp = loader.getController();
            String PONum ="";
            controllersp.welcomeMsg(Username, PONum);

            Navigation nav = new Navigation();
            nav.stageSetup(event,root);

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void logoutAcc(MouseEvent event) throws IOException {
        Navigation nav = new Navigation(); nav.logout(event,logoutBtn);
    }

    @FXML
    void searchFunction(ActionEvent event){

    }

}

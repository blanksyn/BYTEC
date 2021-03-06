package Files;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class SP_DOOUT_Controller {

    @FXML
    private ImageView logoutBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<POout> tbl_DO;

    @FXML
    private TableColumn<POout, Integer> col_sn;

    @FXML
    private TableColumn<POout, String> col_PONum;

    @FXML
    private TableColumn<POout, String> col_SONum;

    @FXML
    private TableColumn<POout, String> col_DONum;

    @FXML
    private TableColumn<POout, String> col_comp;

    @FXML
    private TableColumn<POout, Date> col_DateCreated;

    @FXML
    private TableColumn<POout, Date> col_DelDate;

    @FXML
    private TableColumn<POout, String> col_action;

    @FXML
    private TextField TF_keyword;


    String Username,PONum;
    ObservableList<POout> DOList = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getStatus = "SELECT PONum,SONum,DONum,company,date_created,delivery_date FROM POout WHERE status = 'Approved';";
            Statement stStatus = connectDB.createStatement();
            ResultSet rsStatus = stStatus.executeQuery(getStatus);

            while(rsStatus.next()) {
                DOList.add(new POout(count,rsStatus.getString("PONum"),rsStatus.getString("SONum"),rsStatus.getString("DONum"),rsStatus.getString("company"),
                        rsStatus.getDate("date_created"),rsStatus.getDate("delivery_date")));
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
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_DOOUT_view.fxml"));
                                            Parent root = loader.load();

                                            SP_DOOUT_view_Controller controllersp = loader.getController();
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
        col_SONum.setCellValueFactory((new PropertyValueFactory<>("SONum")));
        col_DONum.setCellValueFactory((new PropertyValueFactory<>("DONum")));
        col_comp.setCellValueFactory((new PropertyValueFactory<>("company")));
        col_DateCreated.setCellValueFactory((new PropertyValueFactory<>("date_created")));
        col_DelDate.setCellValueFactory((new PropertyValueFactory<>("delivery_date")));

        tbl_DO.setItems(DOList);

        FilteredList<POout> filteredData = new FilteredList<>(DOList, b-> true);

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
                }else if(POout.getDONum().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getCompany().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getDate_created().toString().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getDelivery_date().toString().toLowerCase().indexOf(searchKeyword)>-1){

                }else
                    s = false;//no match found
                return s;
            });
        });

        SortedList<POout> sortedData = new SortedList<>(filteredData);
        //bind sorted results with tableview
        sortedData.comparatorProperty().bind(tbl_DO.comparatorProperty());
        //apply filtered and sorted data to table view
        tbl_DO.setItems(sortedData);
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
    void Nav_viewDOOUT(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_deliveryOrderOut(event,Username);
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


}

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

public class PP_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private ImageView logoutBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<POout> tbl_pick;

    @FXML
    private TableColumn<POout, Integer> col_sn;

    @FXML
    private TableColumn<POout, String> col_SONum;

    @FXML
    private TableColumn<POout, String> col_comp;

    @FXML
    private TableColumn<POout, Date> col_dateCreated;

    @FXML
    private TableColumn<POout, String> col_action;

    @FXML
    private TextField TF_keyword;


    String Username;

    ObservableList<POout> pickingList = FXCollections.observableArrayList();

    @FXML
    void initialize(){
        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT * FROM POout WHERE status = 'Unpicked' ORDER BY date_created ASC";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                pickingList.add(new POout(count,queryResult.getString("SONum"),queryResult.getString("company"),
                        queryResult.getDate("date_created")));
                count++;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_SONum.setCellValueFactory((new PropertyValueFactory<>("SONum")));
        col_comp.setCellValueFactory((new PropertyValueFactory<>("company")));
        col_dateCreated.setCellValueFactory((new PropertyValueFactory<>("date_created")));


        //delete button
        Callback<TableColumn<POout, String>, TableCell<POout, String>> cellFactory
                = //
                new Callback<TableColumn<POout, String>, TableCell<POout, String>>() {
                    @Override
                    public TableCell call(final TableColumn<POout, String> param) {
                        final TableCell<POout, String> cell = new TableCell<POout, String>() {

                            final Button btn = new Button("Go pick!");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        POout entry = getTableView().getItems().get(getIndex());
                                        //pass username and ponum
                                        Navigation nav = new Navigation();
                                        nav.PP_view(event,Username,entry.SONum,entry.company);

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
        tbl_pick.setItems(pickingList);

        FilteredList<POout> filteredData = new FilteredList<>(pickingList, b-> true);

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
                }else
                    s = false;//no match found
                return s;
            });
        });

        SortedList<POout> sortedData = new SortedList<>(filteredData);
        //bind sorted results with tableview
        sortedData.comparatorProperty().bind(tbl_pick.comparatorProperty());
        //apply filtered and sorted data to table view
        tbl_pick.setItems(sortedData);
    }

    public void welcomeMsg(String username){
        welcomeLabel.setText("User: "+ username);
        Username = username;
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

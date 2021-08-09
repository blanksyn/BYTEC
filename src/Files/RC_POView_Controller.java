package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class RC_POView_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<POin> tbl_PO;

    @FXML
    private TableColumn<POin, Integer> col_sn;

    @FXML
    private TableColumn<POin, String> col_upc;

    @FXML
    private TableColumn<POin, String> col_productName;

    @FXML
    private TableColumn<POin, Integer> col_qtyOrd;

    @FXML
    private TableColumn<POin, Integer> col_qtyRcv;

    @FXML
    private TableColumn<POin, Integer> col_qtyRem;

    @FXML
    private TableColumn<POin, String> col_expDate;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<?> CB_field;

    @FXML
    private Label Lab_PONum;

    @FXML
    private Label Lab_sup;

    @FXML
    private Button updateBtn;

    String Username,PONum;
    Date expDate = Date.valueOf(java.time.LocalDate.now());

    ObservableList<POin> rcvOb = FXCollections.observableArrayList();

    @FXML
    void initialize(String username,String PONum,String supplier){
        welcomeLabel.setText("User: "+ username);
        this.Username =username;
        Lab_sup.setText(supplier);
        Lab_PONum.setText(PONum);
        this.PONum =PONum;

        int count = 1;


        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT upc,qty_ordered,qty_rcv,qty_remaining FROM POin_detail WHERE PONum = '" +PONum +"' ";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                String getName = "SELECT prod_name FROM product_master WHERE upc = '" +queryResult.getString("upc") +"' ";
                Statement stName  = connectDB.createStatement();
                ResultSet rsName  = stName.executeQuery(getName);

                while(rsName.next()) {
                    rcvOb.add(new POin(count, queryResult.getString("upc"), rsName.getString("prod_name"),
                            queryResult.getInt("qty_ordered"), queryResult.getInt("qty_rcv"),queryResult.getInt("qty_remaining"),expDate));
                    count++;
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

                            final DatePicker btn = new DatePicker();

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        POin entry = getTableView().getItems().get(getIndex());
                                        expDate = Date.valueOf(btn.getValue());
                                        entry.expDate = expDate;

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_qtyOrd.setCellValueFactory((new PropertyValueFactory<>("qty_ordered")));
        col_qtyRcv.setCellValueFactory((new PropertyValueFactory<>("qty_rcv")));
        col_qtyRem.setCellValueFactory((new PropertyValueFactory<>("qty_remaining")));
        col_expDate.setCellFactory(cellFactory);

        tbl_PO.setItems(rcvOb);

        tbl_PO.setEditable(true);
        col_qtyRcv.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col_qtyRcv.setOnEditCommit(e->{
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setQty_rcv(e.getNewValue());
        });

        FilteredList<POin> filteredData = new FilteredList<>(rcvOb, b-> true);

        TF_keyword.textProperty().addListener((observable, oldValue,newValue)->{

            //if no change detected then no change to list
            filteredData.setPredicate(POin -> {

                boolean s =false;
                if(newValue.isEmpty() || newValue.isBlank() || newValue ==null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(POin.getUpc().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POin.getProd_name().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(String.valueOf(POin.getQty_ordered()).toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(String.valueOf(POin.getQty_rcv()).toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(String.valueOf(POin.getQty_remaining()).toLowerCase().indexOf(searchKeyword)>-1){
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

    @FXML
    void closeWindow(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.RC_purchaseOrderIn(event,Username);
    }

    @FXML
    void searchFunction(ActionEvent event) {
        System.out.println("Date" + rcvOb.get(0).expDate);
    }

    @FXML
    void update(ActionEvent event) throws SQLException {

        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();
        String DOnum = "";

        TextInputDialog DO = new TextInputDialog();
        DO.setTitle("DONum");
        DO.setHeaderText("Enter Delivery Order Number");
        DO.setContentText("DONum: ");

        Optional<String> result = DO.showAndWait();
        if(result.isPresent()){
            DOnum = result.get();
            //System.out.println(result.get());

            //update qty on POin_detail
            String updateDet = "UPDATE POin_detail SET qty_rcv = ? WHERE PONum = '"+PONum+"' AND upc = ?";
            PreparedStatement pstDet = connectDB.prepareStatement(updateDet);

            //update status on POin
            String updateStat = "UPDATE POin SET status = 'Not Approved' WHERE PONum = '"+PONum+"';";
            PreparedStatement pstStat = connectDB.prepareStatement(updateStat);
            pstStat.execute();

            //insert values to POin_rcv
            String updateVal = "INSERT INTO POin_rcv (PONum,DONum,upc,qty,rcvBy,date_rcv,expiry_date) VALUES (?,?,?,?,?,?,?) ;" ;
            PreparedStatement pstupdateVal = connectDB.prepareStatement(updateVal);

            for(POin p:rcvOb){
                pstupdateVal.setString(1,PONum);
                pstupdateVal.setString(2,DOnum);
                pstupdateVal.setString(3,p.upc);
                pstupdateVal.setString(4, String.valueOf(p.qty_rcv));
                pstupdateVal.setString(5, Username);
                pstupdateVal.setString(6,String.valueOf(java.time.LocalDate.now()));
                pstupdateVal.setString(7, String.valueOf(p.expDate));
                pstupdateVal.execute();

                pstDet.setString(1,String.valueOf(p.qty_rcv));
                pstDet.setString(2,p.upc);
                pstDet.execute();
            }

            closeWindow(event);
            System.out.println("Database updated.");
        }


    }


    @FXML
    void editQtyRcv(ActionEvent event) {

    }


    public void editExpDate(TableColumn.CellEditEvent<POin, String> pOinDateCellEditEvent) {
        POin po = tbl_PO.getSelectionModel().getSelectedItem();
    }


}

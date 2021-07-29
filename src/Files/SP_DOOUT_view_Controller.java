package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SP_DOOUT_view_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<POout> tbl_DO;

    @FXML
    private TableColumn<POout, Integer> col_sn;

    @FXML
    private TableColumn<POout, String> col_upc;

    @FXML
    private TableColumn<POout, String> col_productName;

    @FXML
    private TableColumn<POout, Integer> col_qty;

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
    private Label Lab_deliveryDate;

    @FXML
    private Button printBtn;

    @FXML
    private ComboBox<String> CB_courier;

    @FXML
    private Label Lab_DONum;

    String Username,DONum;
    ObservableList<POout> DOTbl = FXCollections.observableArrayList();

    @FXML
    void initialize(String username,String DONum){
        welcomeLabel.setText("User: " + username);
        Lab_DONum.setText(DONum);
        this.DONum = DONum;
        this.Username=username;
        int count =1;

        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();

        //fill labels
        try {
            String getLabels = "SELECT PONum,company,delivery_date FROM POout WHERE DONum = " + DONum +" LIMIT 1";
            Statement stLabels = connectDB.createStatement();
            ResultSet rsLabels = stLabels.executeQuery(getLabels);

            while(rsLabels.next()){
                Lab_comp.setText(rsLabels.getString("company"));
                Lab_PONum.setText(rsLabels.getString("PONum"));
                Lab_deliveryDate.setText(String.valueOf(rsLabels.getDate("delivery_date")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //fill tables
        try {
            String getTblVal = "SELECT DISTINCT upc,prod_name,count(upc) as qty FROM pickingList_detail WHERE DONum = " + DONum;
            Statement stTblVal = connectDB.createStatement();
            ResultSet rsTblVal = stTblVal.executeQuery(getTblVal);

            while(rsTblVal.next()){
                DOTbl.add(new POout(count,rsTblVal.getString("upc"),rsTblVal.getString("prod_name"),rsTblVal.getInt("qty")));
                count++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_qty.setCellValueFactory((new PropertyValueFactory<>("qty")));

        tbl_DO.setItems(DOTbl);

        //fill combobox
        try {
            String getValues = "SELECT name FROM courier";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while (queryResult.next()) {
                CB_courier.getItems().add(queryResult.getString("name"));

            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @FXML
    void closeWindow(ActionEvent event) {
        Navigation nav = new Navigation();
        nav.SP_deliveryOrderOut(event,Username);
    }

    @FXML
    void print(ActionEvent event) throws IOException {

        if(CB_courier.getValue() == null || CB_courier.getValue().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error has occurred");
            alert.setHeaderText("No courier selected! ");
            alert.setContentText("Please select one.");

            alert.showAndWait();
        }else{
            System.out.println("Courier: "+CB_courier.getValue());

            XSSFWorkbook wb = new XSSFWorkbook();//for earlier version use HSSF
            XSSFSheet sheet = wb.createSheet("Delivery Order (Out)");

            String[] colHeadings ={"SN","UPC","Product Name", "Quantity"};

            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short)12);
            headerFont.setColor(IndexedColors.BLACK.index);

            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);

            XSSFRow header = sheet.createRow(0);
            for(int i =0;i<colHeadings.length;i++){
                XSSFCell cell = header.createCell(i);
                cell.setCellValue(colHeadings[i]);
                cell.setCellStyle(headerStyle);
            }


            int index = 1;
            for(int i=0; i<DOTbl.size();i++){
                XSSFRow row = sheet.createRow(index);

                row.createCell(0).setCellValue(DOTbl.get(i).sn);
                row.createCell(1).setCellValue(DOTbl.get(i).upc);
                row.createCell(2).setCellValue(DOTbl.get(i).prod_name);
                row.createCell(3).setCellValue(DOTbl.get(i).qty);
                index++;
            }

            for(int i=0; i<colHeadings.length;i++){
                sheet.autoSizeColumn(i);
            }

            FileOutputStream fileOut = new FileOutputStream("DeliveryOrder-"+DONum+".xlsx");// before 2007 version xls
            wb.write(fileOut);
            fileOut.close();
            wb.close();
            System.out.println("Delivery Order created.");
            closeWindow(event);
        }


    }

    @FXML
    void searchFunction(ActionEvent event) {

    }

}

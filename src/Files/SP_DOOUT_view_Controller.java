package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.sql.*;

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

    String Username,DONum,PONum,SONum,company,delivery_date;
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
            String getLabels = "SELECT PONum,SONum, company,delivery_date FROM POout WHERE DONum = " + DONum +" AND status != 'Delivered' LIMIT 1";
            Statement stLabels = connectDB.createStatement();
            ResultSet rsLabels = stLabels.executeQuery(getLabels);

            while(rsLabels.next()){
                String POSO = rsLabels.getString("PONum")+"/"+ rsLabels.getString("SONum");
                Lab_comp.setText(rsLabels.getString("company"));
                Lab_deliveryDate.setText(String.valueOf(rsLabels.getDate("delivery_date")));
                Lab_PONum.setText(POSO);
                this.company=rsLabels.getString("company");
                this.PONum=rsLabels.getString("PONum");
                this.SONum=rsLabels.getString("SONum");
                this.delivery_date = String.valueOf(rsLabels.getDate("delivery_date"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //fill tables
        try {
            String getTblVal = "SELECT upc,prod_name,count(upc) as qty,sku_scanned FROM pickingList_detail WHERE DONum = '" + DONum + "' GROUP BY upc;";
            Statement stTblVal = connectDB.createStatement();
            ResultSet rsTblVal = stTblVal.executeQuery(getTblVal);

            while(rsTblVal.next()){
                DOTbl.add(new POout(count,rsTblVal.getString("upc"),rsTblVal.getString("prod_name"),rsTblVal.getInt("qty"),rsTblVal.getString("sku_scanned")));
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

        FilteredList<POout> filteredData = new FilteredList<>(DOTbl, b-> true);

        TF_keyword.textProperty().addListener((observable, oldValue,newValue)->{

            //if no change detected then no change to list
            filteredData.setPredicate(POout -> {

                boolean s =false;
                if(newValue.isEmpty() || newValue.isBlank() || newValue ==null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(POout.getUpc().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(POout.getProd_name().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(String.valueOf(POout.getQty()).indexOf(searchKeyword)>-1){
                    s = true;
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

        //change status to delivered
        DatabaseConnection con = new DatabaseConnection();
        Connection connectDB = con.getConnection();
        String comp_address="";

        //update new scanned sku with delivered and delivered date
        for(POout p:DOTbl) {
            try {
                String upProdSKU = "UPDATE product_indv SET status = 'Delivered',delivery_date = ? WHERE sku = '" + p.sku_scanned + "';";
                PreparedStatement psUpProdSKU = connectDB.prepareStatement(upProdSKU);
                psUpProdSKU.setString(1, String.valueOf(java.time.LocalDate.now()));
                psUpProdSKU.execute();

            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Status not changed");
            }
        }

        if(CB_courier.getValue() == null || CB_courier.getValue().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("An error has occurred");
            alert.setHeaderText("No courier selected! ");
            alert.setContentText("Please select one.");

            alert.showAndWait();
        }else{
            System.out.println("Courier: "+CB_courier.getValue());

            //update actual delivery date
            try{
                String updateDate = "UPDATE POout SET delivery_date = ?, status = 'Delivered',courier = '" + CB_courier.getValue() + "' WHERE SONum = '" + SONum + "';";
                PreparedStatement psUpdateDate = connectDB.prepareStatement(updateDate);
                psUpdateDate.setString(1, String.valueOf(java.time.LocalDate.now()));
                psUpdateDate.execute();
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                String getValues = "SELECT company_add FROM POout WHERE SONum = '" + SONum + "';";
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(getValues);

                while(queryResult.next()){
                    comp_address = queryResult.getString("company_add");
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            //create Delivery order
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

            XSSFRow title = sheet.createRow(0);
            title.createCell(0).setCellValue("PO/SO Number:");
            title.createCell(1).setCellValue(PONum + "/"+ SONum);
            title.createCell(2).setCellValue("DO Number:");
            title.createCell(3).setCellValue(DONum);

            XSSFRow blankSpace = sheet.createRow(1);

            XSSFRow title2 = sheet.createRow(2);
            title2.createCell(0).setCellValue("Delivery Dater:");
            title2.createCell(1).setCellValue(delivery_date);
            title2.createCell(2).setCellValue("Company:");
            title2.createCell(3).setCellValue(company);

            XSSFRow title3 = sheet.createRow(3);
            title3.createCell(0).setCellValue("Courier:");
            title3.createCell(1).setCellValue(CB_courier.getValue());
            title3.createCell(2).setCellValue("Company Address:");
            title3.createCell(3).setCellValue(comp_address);

            XSSFRow blankSpace2 = sheet.createRow(4);

            XSSFRow header = sheet.createRow(5);
            for(int i =0;i<colHeadings.length;i++){
                XSSFCell cell = header.createCell(i);
                cell.setCellValue(colHeadings[i]);
                cell.setCellStyle(headerStyle);
            }


            int index = 6;
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

}

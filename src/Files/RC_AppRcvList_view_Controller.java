package Files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class RC_AppRcvList_view_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<product_indv> tbl_rcvList;

    @FXML
    private TableColumn<product_indv, Integer> col_sn;

    @FXML
    private TableColumn<product_indv, String> col_upc;

    @FXML
    private TableColumn<product_indv, String> col_productName;

    @FXML
    private TableColumn<product_indv, String> col_sku;

    @FXML
    private TableColumn<product_indv, String> col_location;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Label Lab_PONum;

    @FXML
    private Label Lab_DONum;

    @FXML
    private Label Lab_dateRcv;

    @FXML
    private Label Lab_sup;

    @FXML
    private Button printLabelBtn;
    String Username,PONum,DONum,supplier,date_rcv;

    ObservableList<product_indv> rcvList = FXCollections.observableArrayList();

    @FXML
    void initialize(String username, String PONum, String DONum,String supplier, Date date_rcv){
        this.Username = username;
        this. PONum = PONum;
        this.DONum = DONum;
        this.date_rcv = String.valueOf(date_rcv);
        this.supplier = supplier;
        welcomeLabel.setText("User: "+ Username);
        Lab_DONum.setText(DONum);
        Lab_dateRcv.setText(this.date_rcv);
        Lab_sup.setText(supplier);
        Lab_PONum.setText(PONum);

        int count = 1;

        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();

            String getValues = "SELECT upc FROM POin_rcv WHERE DONum = '" +DONum +"' ";
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(getValues);

            while(queryResult.next()){
                String getName = "SELECT prod_name FROM product_master WHERE upc = '" +queryResult.getString("upc") +"' ";
                Statement stName  = connectDB.createStatement();
                ResultSet rsName  = stName.executeQuery(getName);

                while(rsName.next()) {
                    String getSKU = "SELECT sku FROM POin_rcv_detail WHERE upc = '" +queryResult.getString("upc") +"' AND DONum = '"+DONum+"'";
                    Statement stSKU  = connectDB.createStatement();
                    ResultSet rsSKU  = stSKU.executeQuery(getSKU);

                    while(rsSKU.next()) {
                        String getLoc = "SELECT location FROM product_indv WHERE sku = '" +rsSKU.getString("sku") +"' ";
                        Statement stLoc  = connectDB.createStatement();
                        ResultSet rsLoc  = stLoc.executeQuery(getLoc);

                        while(rsLoc.next()) {
                            rcvList.add(new product_indv(count, queryResult.getString("upc"), rsName.getString("prod_name"),
                                    rsSKU.getString("sku"), rsLoc.getString("location")));
                            count++;
                        }
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //fill table coloumn
        col_sn.setCellValueFactory((new PropertyValueFactory<>("sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_sku.setCellValueFactory((new PropertyValueFactory<>("sku")));
        col_location.setCellValueFactory((new PropertyValueFactory<>("loc")));

        tbl_rcvList.setItems(rcvList);

        FilteredList<product_indv> filteredData = new FilteredList<>(rcvList, b-> true);

        TF_keyword.textProperty().addListener((observable, oldValue,newValue)->{

            //if no change detected then no change to list
            filteredData.setPredicate(product_indv -> {

                boolean s =false;
                if(newValue.isEmpty() || newValue.isBlank() || newValue ==null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(product_indv.getUpc().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(product_indv.getProd_name().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(product_indv.getSku().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else if(product_indv.getLoc().toLowerCase().indexOf(searchKeyword)>-1){
                    s = true;
                }else
                    s = false;//no match found
                return s;
            });
        });

        SortedList<product_indv> sortedData = new SortedList<>(filteredData);
        //bind sorted results with tableview
        sortedData.comparatorProperty().bind(tbl_rcvList.comparatorProperty());
        //apply filtered and sorted data to table view
        tbl_rcvList.setItems(sortedData);

    }


    @FXML
    void closeWindow(ActionEvent event) {

        Navigation nav =new Navigation();
        nav.RC_ApproveRcvList(event,Username);
    }

    @FXML
    void printLabel(ActionEvent event) throws IOException {
        System.out.println("Creating labels...");
        XSSFWorkbook wb = new XSSFWorkbook();//for earlier version use HSSF
        XSSFSheet sheet = wb.createSheet("Labels - to print"+ DONum);

        String[] colHeadings ={"SN","UPC","Product Name", "Quantity"};

        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short)12);
        headerFont.setColor(IndexedColors.BLACK.index);

        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);

        String[] colHeadings1 ={"SN","UPC","Product Name"};
        String[] colHeadings2 ={"SKU", "Location"};

        System.out.println("Inserting data in workbook...");
        int index = 0;
        for (int i = 0; i<rcvList.size();i++){

            XSSFRow title = sheet.createRow(index);
            title.createCell(0).setCellStyle(headerStyle);
            title.getCell(0).setCellValue("PO Number:");
            title.createCell(1).setCellStyle(headerStyle);
            title.getCell(1).setCellValue(PONum);
            title.createCell(2).setCellStyle(headerStyle);
            title.getCell(2).setCellValue("DO Number:");
            title.createCell(3).setCellStyle(headerStyle);
            title.getCell(3).setCellValue(DONum);
            index++;

            XSSFRow title2 = sheet.createRow(index);
            title2.createCell(0).setCellStyle(headerStyle);
            title2.getCell(0).setCellValue("Date Received:");
            title2.createCell(1).setCellStyle(headerStyle);
            title2.getCell(1).setCellValue(date_rcv);
            title2.createCell(2).setCellStyle(headerStyle);
            title2.getCell(2).setCellValue("Supplier:");
            title2.createCell(3).setCellStyle(headerStyle);
            title2.getCell(3).setCellValue(supplier);
            index++;

            XSSFRow blankSpace3 = sheet.createRow(index);
            index++;

            XSSFRow header = sheet.createRow(index);
            for(int j =0;j<colHeadings1.length;j++){
                XSSFCell cell = header.createCell(j);
                cell.setCellValue(colHeadings1[j]);
                cell.setCellStyle(headerStyle);
            }
            index++;

            XSSFRow row = sheet.createRow(index);
            row.createCell(0).setCellValue(rcvList.get(i).sn);
            row.createCell(1).setCellValue(rcvList.get(i).upc);
            row.createCell(2).setCellValue(rcvList.get(i).prod_name);
            index++;

            XSSFRow header2 = sheet.createRow(index);
            for(int k =0;k<colHeadings2.length;k++){
                XSSFCell cell = header2.createCell(k);
                cell.setCellValue(colHeadings2[k]);
                cell.setCellStyle(headerStyle);
            }
            index++;

            XSSFRow row2 = sheet.createRow(index);
            row2.createCell(0).setCellValue(rcvList.get(i).sku);
            row2.createCell(1).setCellValue(rcvList.get(i).loc);
            index++;

            XSSFRow blankSpace4 = sheet.createRow(index);
            index++;
            XSSFRow blankSpace5 = sheet.createRow(index);
            index++;

        }
        System.out.println("Data inserted successfully");

        for(int i=0; i<6;i++){
            sheet.autoSizeColumn(i);
        }

        FileOutputStream fileOut = new FileOutputStream("Labels-"+DONum+".xlsx");// before 2007 version xls
        wb.write(fileOut);
        fileOut.close();
        wb.close();
        System.out.println("Labels created.");
        closeWindow(event);
    }


}

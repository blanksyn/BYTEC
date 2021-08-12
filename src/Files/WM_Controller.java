package Files;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javax.swing.JOptionPane;

public class WM_Controller extends WM implements Initializable{

    @FXML
    private ImageView logoutBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<User> tableAccount;

    @FXML
    private TableColumn<User, Integer> col_sn;

    @FXML
    private TableColumn<User, String> col_employeeID;

    @FXML
    private TableColumn<User, String> col_Name;

    @FXML
    private TableColumn<User, String> col_type;
    
    @FXML
    private TableColumn<User, String> col_role;

    @FXML
    private TableColumn<User, String> col_location;
    
    @FXML
    private TableColumn<User, String> col_date;

    @FXML
    private TableColumn<User, User> col_action;
    
    @FXML
    TextField TF_keyword;
    
    ObservableList<User> ObserveList = FXCollections.observableArrayList();
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    void Nav_PO_In(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_POIN.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();
            loginStage.show();
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    loginStage.setX(event.getScreenX() - xOffset);
                    loginStage.setY(event.getScreenY() - yOffset);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void nav_Gen_Rpt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_inv.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();
            loginStage.show();
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    loginStage.setX(event.getScreenX() - xOffset);
                    loginStage.setY(event.getScreenY() - yOffset);
                }
            });;

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void addAccFunction(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_addAcc.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 450, 500);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();
            loginStage.show();
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    loginStage.setX(event.getScreenX() - xOffset);
                    loginStage.setY(event.getScreenY() - yOffset);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void closeWindow(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void logoutAcc(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Confirm logout?");

        if(alert.showAndWait().get()== ButtonType.OK){

            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage logoutStage = (Stage)logoutBtn.getScene().getWindow();
            Scene scene = new Scene(root, 700, 650);
            logoutStage.setTitle("Login");
            logoutStage.setScene(scene);
            Image image = new Image("image/logo192.png");
            logoutStage.getIcons().add(image);
            scene.setFill(Color.TRANSPARENT);
            logoutStage.centerOnScreen();
            logoutStage.show();

        }
    }


    @FXML
    void nav_WH_Env(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_courierMgt.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();
            loginStage.show();
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    loginStage.setX(event.getScreenX() - xOffset);
                    loginStage.setY(event.getScreenY() - yOffset);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void searchFunction(ActionEvent event) {

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        //String keyword = TF_keyword.getText();
        //if(!keyword.equals(null))
        welcome(welcomeLabel);
        viewAccountWM(tableAccount,ObserveList, col_sn, col_employeeID, col_Name, col_type, col_role, col_location, col_date, col_action);
        welcome(welcomeLabel);

        Calendar now = Calendar.getInstance();
        int dayNum = (now.get(Calendar.DAY_OF_MONTH));
        if(dayNum == 1){
            String year = (String.valueOf(now.get(Calendar.YEAR)));
            int monthNum = (now.get(Calendar.MONTH) + 1);
            monthNum = 9;
            String month = " ";
            switch(monthNum){
                case 1:
                    month = "January";
                    break;
                case 2:
                    month = "February";
                    break;
                case 3:
                    month = "March";
                    break;
                case 4:
                    month = "April";
                    break;
                case 5:
                    month = "May";
                    break;
                case 6:
                    month = "June";
                    break;
                case 7:
                    month = "July";
                    break;
                case 8:
                    month = "August";
                    break;
                case 9:
                    month = "September";
                    break;
                case 10:
                    month = "October";
                    break;
                case 11:
                    month = "November";
                    break;
                case 12:
                    month = "December";
                    break;

            }
            String firstDate = year + "-" + String.valueOf(monthNum) + "-" + "01";
            String lastDate = year + "-" + String.valueOf(monthNum + 1) + "-" + "01";
            String check = "no";
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            try{
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT month, year FROM ReportMonth");
                while (rs.next()) {
                    if(rs.getString("month").equals(month) && rs.getString("year").equals(year)){
                        check = "yes";
                    }
                }
            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            if(check.equals("no")){
                try{
                    PreparedStatement pst =connectDB.prepareStatement("insert into ReportMonth(month,year,startDate,endDate) values (?,?,?,?)");
                    pst.setString(1,month);
                    pst.setString(2,year);
                    pst.setString(3,firstDate);
                    pst.setString(4,lastDate);
                    pst.execute();
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog(null, e);
                }
            }
        }
        
        //Auto check stock on Monday 
        int firstDay = now.get(Calendar.DAY_OF_WEEK); //Monday = 2
        if(firstDay == 2){
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            try{
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT DISTINCT supplier FROM product_master;");
                while (rs.next()) {
                    String PONum = " ";
                    try{
                        ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT MAX(PONum) as MaxPO FROM POin;");
                        while (rs2.next()) {
                            int maxPO = 0;
                            if(rs2.getString("MaxPO") == null)
                                maxPO = 9000;
                            else
                                maxPO = Integer.parseInt(rs2.getString("MaxPO"));
                            int po = maxPO + 1;
                            PONum = String.valueOf(po);
                        }
                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
                    String check = "no";
                    ResultSet rs3 = connectDB.createStatement().executeQuery("SELECT qty, min_qty FROM product_master WHERE supplier = '" + rs.getString("supplier") + "';");
                        while (rs3.next()) {
                            if(rs3.getInt("qty") < rs3.getInt("min_qty")){
                                check = "yes";
                            }
                    }
                    if(check.equals("yes")){
                        String newPOin = "INSERT INTO POin (PONum,supplier,orderBy,order_date,status,eta) VALUES (?,?,?,?,?,?);";
                        PreparedStatement pst = connectDB.prepareStatement(newPOin);
                        pst.setString(1,PONum);
                        pst.setString(2,rs.getString("supplier"));
                        pst.setString(3,"111");
                        pst.setString(4,String.valueOf(java.time.LocalDate.now()));
                        pst.setString(5,"Not Approved");
                        pst.setString(6,String.valueOf(java.time.LocalDate.now().plusWeeks(1)));
                        pst.execute();
                        ResultSet rs4 = connectDB.createStatement().executeQuery("SELECT upc,qty, min_qty, max_qty FROM product_master WHERE supplier = '" + rs.getString("supplier") + "';");
                        while (rs4.next()) {
                            if(rs4.getInt("qty") < rs4.getInt("min_qty")){
                                int qtyOrdered = rs4.getInt("max_qty") - rs4.getInt("qty");
                                String newPOinDetail = "INSERT INTO POin_detail (PONum,upc,qty_ordered,qty_rcv,qty_remaining) VALUES (?,?,?,?,?);";
                                PreparedStatement pstDet = connectDB.prepareStatement(newPOinDetail);
                                pstDet.setString(1, PONum);
                                pstDet.setString(2, rs4.getString("upc"));
                                pstDet.setString(3, String.valueOf(qtyOrdered));
                                pstDet.setString(4, "0");
                                pstDet.setString(5, String.valueOf(qtyOrdered));
                                pstDet.execute();
                            }
                        }
                    }
                }
            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            
        }
    }

}

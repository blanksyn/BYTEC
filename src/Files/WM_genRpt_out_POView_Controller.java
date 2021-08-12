package Files;

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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

public class WM_genRpt_out_POView_Controller extends WM implements Initializable{
    @FXML
    private ImageView logoutBtn;

    @FXML
    private Label welcomeLabel, labelMonth, labelYear;

    @FXML
    private TableView<POout> tbl_POinRpt;

    @FXML
    private TableColumn<POout, Integer> col_sn;

    @FXML
    private TableColumn<POout, String> col_PONum, col_SONum, col_company, col_CrtBy, col_dateCrt, col_deliverDate, col_status;

    @FXML
    private TableColumn<POout, POout> col_action;

    @FXML
    private TextField TF_keyword;

    String month;
  
    static String thisYear, thisMonth;
    
    ObservableList<POout> ObserveList = FXCollections.observableArrayList();
    
    static void getme(String year, String month){
        thisYear = year;
        thisMonth = month;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        welcome(welcomeLabel);
        labelYear.setText(thisYear);
        labelMonth.setText(thisMonth);
        viewPOoutReportWM(thisMonth, thisYear, tbl_POinRpt, ObserveList, col_sn, col_PONum, col_SONum, col_company, col_CrtBy,
            col_dateCrt, col_deliverDate, col_status, col_action);
    }

    @FXML
    void Nav_Gen_Rpt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_inv.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_InDO(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_in_DO.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_InPO(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_in_PO.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_InbRpt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_in_PO.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_InvRpt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_inv.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_OutbRpt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_out_PO.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_PO_In(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_POIN.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_WH_Env(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_courierMgt.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void Nav_accMgt(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    void closeWindow(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_genRpt_out_PO.fxml"));
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

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
    void searchFunction(ActionEvent event) {

    }

}

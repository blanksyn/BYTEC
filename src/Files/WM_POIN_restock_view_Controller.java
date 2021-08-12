package Files;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WM_POIN_restock_view_Controller extends WM implements Initializable{

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private TableView<POin> table_restock;

    @FXML
    private TableColumn<POin, Integer> col_sn;

    @FXML
    private TableColumn<POin, String> col_upc;

    @FXML
    private TableColumn<POin, String> col_productName;

    @FXML
    private TableColumn<POin, String> col_qty;
    
    @FXML
    private Label PONumLabel,supLabel,dateLabel;

    @FXML
    private TextField TF_keyword;
    
    ObservableList<POin> ObserveList = FXCollections.observableArrayList();
    
    static String thisoriPOnum, thisoriSup;
    static Date thisoriDate;
    static int thisSN;
    
    static void getme(int sna, String oriPOnum, String oriSup, Date oriDate){
        thisSN = sna;
        thisoriPOnum = oriPOnum;
        thisoriSup = oriSup;
        thisoriDate = oriDate;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        welcome(welcomeLabel);
        PONumLabel.setText(thisoriPOnum);
        supLabel.setText(thisoriSup);
        dateLabel.setText(thisoriDate.toString());
        
        POApprovalViewWM(thisoriPOnum, table_restock, ObserveList, col_sn, col_upc, col_productName, col_qty);
        
    }

    @FXML
    void approveOrder(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.initStyle(StageStyle.UTILITY);;
        alert.setHeaderText(null);
        alert.setContentText("Confirm?");

        Optional<ButtonType> action = alert.showAndWait();

        if(action.get() == ButtonType.OK)
        {
            try{
                DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                PreparedStatement pst =connectDB.prepareStatement("UPDATE POin SET status = ? WHERE PONum = '"+thisoriPOnum+"';");
                pst.setString(1,"Not Received"); 
                pst.execute();

            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            try{
                Parent root = FXMLLoader.load(getClass().getResource("WM_POIN_restock.fxml"));
                Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                loginStage.setScene(scene);
                loginStage.centerOnScreen();

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
    }
    
    @FXML
    void deleteOrder(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.initStyle(StageStyle.UTILITY);;
        alert.setHeaderText(null);
        alert.setContentText("Confirm?");

        Optional<ButtonType> action = alert.showAndWait();

        if(action.get() == ButtonType.OK)
        {
            try{
                DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                PreparedStatement pst =connectDB.prepareStatement("DELETE FROM POin WHERE PONum = '"+thisoriPOnum+"';");
                pst.execute();
                PreparedStatement pst2 =connectDB.prepareStatement("DELETE FROM POin_detail WHERE PONum = '"+thisoriPOnum+"';");
                pst2.execute();

            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            try{
                Parent root = FXMLLoader.load(getClass().getResource("WM_POIN_restock.fxml"));
                Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                loginStage.setScene(scene);
                loginStage.centerOnScreen();

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    @FXML
    void cancelOrder(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_POIN_restock.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("WM_POIN_restock.fxml"));
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
    void searchFunction(ActionEvent event) {

    }

}

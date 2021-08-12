package Files;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WM_POIN_view_Controller extends WM implements Initializable{

    @FXML
    private Label welcomeLabel,PONumLabel,supLabel,dateLabel;
    
    @FXML
    private TableView<POin> table_POView;

    @FXML
    private TableColumn<POin, Integer> col_sn, col_QtyOrder, col_rcv, col_qtyRem;

    @FXML
    private TableColumn<POin, String> col_UPC, col_prodName;

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
        ViewPOinWM(thisSN, thisoriPOnum, thisoriSup, thisoriDate, table_POView, ObserveList, col_sn, col_QtyOrder, col_rcv, col_qtyRem,
            col_UPC, col_prodName);
        
    }

    @FXML
    void closeWindow(ActionEvent event) {
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
    void searchFunction(ActionEvent event) {

    }

}

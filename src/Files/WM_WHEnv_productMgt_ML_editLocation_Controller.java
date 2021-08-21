package Files;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

public class WM_WHEnv_productMgt_ML_editLocation_Controller extends WM implements Initializable{

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private TableView<prod_master> tableLocation;
    
    @FXML
    private TableColumn<prod_master, String> col_loc;
    
    @FXML
    private TableColumn<prod_master, Integer> col_sn;
    
    @FXML
    private TableColumn<prod_master, prod_master> col_newloc;
    
    ComboBox CBlocation;
    
    static String thisOriUPC, thisOriLoc;
    static int thisSN;
    static Double thisOriVol;
    ObservableList<prod_master> ObserveList = FXCollections.observableArrayList();
    
    static void getme(int sna, String oriUPC, String oriLoc, Double oriVol){
        thisSN = sna;
        thisOriUPC = oriUPC;
        thisOriLoc = oriLoc;
        thisOriVol = oriVol;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){welcome(welcomeLabel);
        try{
            //DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, location FROM upc_location WHERE upc = '" + thisOriUPC + "';");
            while (rs.next()) {
                ObserveList.add(new prod_master(rs.getInt("sn"),rs.getString("location")));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_loc.setCellValueFactory((new PropertyValueFactory<>("location")));
        col_sn.setCellValueFactory((new PropertyValueFactory<>("SN")));
        
        try {
            tableLocation.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        col_newloc.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_newloc.setCellFactory(param -> new TableCell<prod_master, prod_master>() {
            private final ComboBox CBlocation = new ComboBox();
            //@Override
            protected void updateItem(prod_master user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    try{
                        //DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                        ResultSet rs4 = connectDB.createStatement().executeQuery("SELECT location FROM storage WHERE vol_avail > 0;");
                        while (rs4.next()) {
                            CBlocation.getItems().add(rs4.getString("location"));
                            
                        }
                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
                    setGraphic(CBlocation);
                    
                    CBlocation.setOnAction(
                            event -> {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle(null);
                                alert.initStyle(StageStyle.UTILITY);;
                                alert.setHeaderText(null);
                                alert.setContentText("Confirm change location to " + CBlocation.getValue().toString() + "?");
                                Optional <ButtonType> action = alert.showAndWait();
                                
                                if(action.get() == ButtonType.OK)
                                {
                                    String newLoc = CBlocation.getValue().toString();
                                    for (int i = 0; i < ObserveList.size(); i++)
                                    {
                                        if (ObserveList.get(i).getSN() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                        {
                                            String oriLoc =(ObserveList.get(i).getLocation());
                                            try{
                                                //DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                                                ResultSet rs7 = connectDB.createStatement().executeQuery("SELECT location, max_qty FROM upc_location WHERE upc = '" + thisOriUPC + "' AND location = '" + oriLoc + "';");
                                                while (rs7.next()) {
                                                    String originalLoc = rs7.getString("location");
                                                    int maxQty = rs7.getInt("max_qty");
                                                    int qtyLoc = 0;
                                                    ResultSet rs8 = connectDB.createStatement().executeQuery("SELECT vol_avail FROM storage WHERE location = '" + originalLoc +"';");
                                                    while (rs8.next()) {
                                                        double volB = rs8.getDouble("vol_avail");
                                                        double volAdd = volB + thisOriVol * maxQty;
                                                        qtyLoc = (int)(volAdd/thisOriVol);
                                                        PreparedStatement pst2 =connectDB.prepareStatement("UPDATE storage SET vol_avail = ? WHERE location = ?;");
                                                        pst2.setString(1,String.valueOf(volAdd));
                                                        pst2.setString(2,originalLoc);
                                                        pst2.execute();
                                                    }
                                                    double volUsed = qtyLoc * thisOriVol;
                                                    ResultSet rs5 = connectDB.createStatement().executeQuery("SELECT vol_avail FROM storage WHERE location = '" + newLoc +"';");
                                                    while (rs5.next()) {
                                                        double volLeft = rs5.getDouble("vol_avail") - volUsed;
                                                        PreparedStatement pst2 =connectDB.prepareStatement("UPDATE storage SET vol_avail = ? WHERE location = ?;");
                                                        pst2.setString(1,String.valueOf(volLeft));
                                                        pst2.setString(2,newLoc);
                                                        pst2.execute();
                                                    }
                                                }
                                                
                                                
                                                PreparedStatement pst =connectDB.prepareStatement("UPDATE upc_location SET location = ? WHERE upc = '" + thisOriUPC + "' AND location = '" + oriLoc + "';");
                                                pst.setString(1,newLoc);
                                                pst.execute();

                                            }catch(SQLException ex){
                                                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                                            }
                                        }
                                    }
                                }
                            }
                    );
                }
            }
        });
        tableLocation.setItems(ObserveList);
    }

    @FXML
    void cancel(ActionEvent event) {
        try{
            //DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            String master_loc = "";
            ResultSet rs4 = connectDB.createStatement().executeQuery("SELECT location FROM upc_location WHERE upc >= '" + thisOriUPC + "';");
            while (rs4.next()) {
                master_loc += rs4.getString("location");
                master_loc += ", ";
            }
            PreparedStatement pst4 =connectDB.prepareStatement("UPDATE product_master SET location = ? WHERE (`upc` = '"+thisOriUPC+"');");
            pst4.setString(1,master_loc);
            pst4.execute();
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
        try{
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML_edit.fxml"));
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
            Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML_edit.fxml"));
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

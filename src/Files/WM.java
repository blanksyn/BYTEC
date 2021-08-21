package Files;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class WM extends User{
    double xOffset = 0;
    double yOffset = 0;


    public void welcome(Label welcomeLabel){
        try{
            DatabaseConnection con = new DatabaseConnection();
            Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT employeeID FROM accounts WHERE type = 'WM';");
            while (rs.next()) {
                String username = "User: " + rs.getString("employeeID");
                welcomeLabel.setText(username);
            }

        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    //Account Table for WM
    public ObservableList viewAccountWM(String search, TableView tableAccount, ObservableList<User> ObserveList, TableColumn col_sn, TableColumn col_employeeID, TableColumn col_Name, TableColumn col_type,
            TableColumn col_role, TableColumn col_location, TableColumn col_date, TableColumn<User,User> col_action){
        tableAccount.getItems().clear();
        
        //col_action.setStyle( "-fx-alignment: CENTER;");
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<User, User>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("Edit");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSN() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSN());
                                        String oriEid =(ObserveList.get(i).getEmployeeID());
                                        WM_editAcc_Controller.getme(sn, oriEid);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_editAcc.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        tableAccount.setItems(ObserveList);
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, employeeID, name, type, role, location, date_created FROM accounts WHERE " +
            "sn LIKE '%" + search + "%' or " +
            "employeeID LIKE '%" + search + "%' or " +
            "name LIKE '%" + search + "%' or " +
            "type LIKE '%" + search + "%' or " +
            "role LIKE '%" + search + "%' or " +
            "location LIKE '%" + search + "%' or " +
            "date_created LIKE '%" + search + "%' ORDER BY sn ASC;");
            while (rs.next()) {
                if(rs.getString("type").equals("WM"))
                    accType = "Warehouse Manager";
                else if(rs.getString("type").equals("SP"))
                    accType = "Supervisor";
                else if(rs.getString("type").equals("PP"))
                    accType = "Picker/Packer";
                else if(rs.getString("type").equals("RC"))
                    accType = "Receiver";
                ObserveList.add(new User(rs.getInt("sn"), rs.getString("employeeID"), rs.getString("name"), accType, rs.getString("role"), rs.getString("location"), rs.getString("date_created")));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        col_sn.setCellValueFactory((new PropertyValueFactory<>("SN")));
        col_employeeID.setCellValueFactory((new PropertyValueFactory<>("employeeID")));
        col_Name.setCellValueFactory((new PropertyValueFactory<>("name")));
        col_type.setCellValueFactory((new PropertyValueFactory<>("type")));
        col_role.setCellValueFactory((new PropertyValueFactory<>("role")));
        col_location.setCellValueFactory((new PropertyValueFactory<>("location")));
        col_date.setCellValueFactory((new PropertyValueFactory<>("Date_Created")));
        try {
            tableAccount.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //Adding new Acc for WM
    public void addNewAccWM(ActionEvent event, String name,String employeeID,String pass,String cPass,String cb_accType,String cb_accRole){
        if(name.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Name field is blank");
        }
        else if (employeeID.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Employee ID field is blank");
        }
        else if (pass.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Password field is blank");
        }
        else if (cPass.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Confirm Password field is blank");
        }
        else if (!cPass.equals(pass))
        {
            JOptionPane.showMessageDialog( null,"Confirm password does not match with password.");
        }
        else if (cb_accType.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select an account type");
        }
        else if (cb_accRole.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select/enter an account role");
        }
        else if (employeeID.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select/enter an account role");
        }
        else
        {
            String check = "no";
            try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT employeeID FROM accounts;");
            while (rs.next()) {
                if(employeeID.equals(rs.getString("employeeID"))){
                    JOptionPane.showMessageDialog( null,"Employee ID '"+employeeID+"' already exist!");
                    check = "yes";
                }
            }
            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            
            if(check.equals("no")){
                if(cb_accType.equals("Warehouse Manager"))
                    cb_accType = "WM";
                else if(cb_accType.equals("Supervisor"))
                    cb_accType = "SP";
                else if(cb_accType.equals("Picker/Packer"))
                    cb_accType = "PP";
                else if(cb_accType.equals("Receiver"))
                    cb_accType = "RC";
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
                        ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM accounts;");
                        while (rs.next()) {
                            int sna = rs.getInt("MaxSN");
                            int nextSN = sna+1;
                            PreparedStatement pst =connectDB.prepareStatement("insert into accounts(sn,employeeID,name,type,role,location,date_created,password) values (?,?,?,?,?,?,?,?)");
                            pst.setString(1,String.valueOf(nextSN));
                            pst.setString(2,employeeID);
                            pst.setString(3,name);
                            pst.setString(4,cb_accType);
                            pst.setString(5,cb_accRole);
                            pst.setString(6,"Warehouse A");
                            pst.setString(7,getDate());
                            pst.setString(8,pass);
                            pst.execute();
                        }
                        System.out.println("Query executed");
                    }
                    catch (Exception e){
                        JOptionPane.showMessageDialog(null, e);
                    }
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }                                       

                }
                else
                {
                    System.out.println("Account add was cancelled");
                }
            }
        }
    }
    
    //View Edit Information for Edit Acc for WM
    public void editViewAccWM(int sn, TextField name, TextField employeeID, PasswordField pass, PasswordField cPass, ComboBox<String> cb_accType, ComboBox<String> cb_role){
        cb_accType.getItems().add("Supervisor");
        cb_accType.getItems().add("Picker/Packer");
        cb_accType.getItems().add("Receiver");
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, employeeID, name, type, role, password FROM accounts WHERE (`SN` = '"+sn+"');");
            while (rs.next()) {
                switch (rs.getString("type")) {
                    case "WM":
                        cb_accType.setValue("Warehouse Manager");
                        break;
                    case "SP":
                        cb_accType.setValue("Supervisor");
                        break;
                    case "PP":
                        cb_accType.setValue("Picker/Packer");
                        break;
                    case "RC":
                        cb_accType.setValue("Receiver");
                        break;
                    default:
                        break;
                }
                employeeID.setText(rs.getString("employeeID"));
                name.setText(rs.getString("name")); 
                cb_role.setValue(rs.getString("role"));
                pass.setText(rs.getString("password"));
                cPass.setText(rs.getString("password")); 
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    //Edit Acc for WM
    public void editAccWM(int sn, String oriEmpID, ActionEvent event, String name,String employeeID,String pass,String cPass,String cb_accType, String cb_role){
        if(name.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Name field is blank");
        }
        else if (employeeID.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Employee ID field is blank");
        }
        else if (pass.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Password field is blank");
        }
        else if (cPass.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Confirm Password field is blank");
        }
        else if (!cPass.equals(pass))
        {
            JOptionPane.showMessageDialog( null,"Confirm password does not match with password.");
        }
        else if (cb_accType.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select an account type");
        }
        else if (cb_role.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select/enter an account role");
        }
        else{
            String check = "no";
            if(!oriEmpID.equals(employeeID)){
                try{
                DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT employeeID FROM accounts;");
                while (rs.next()) {
                    if(employeeID.equals(rs.getString("employeeID"))){
                        JOptionPane.showMessageDialog( null,"Employee ID '"+employeeID+"' already exist!");
                        check = "yes";
                    }
                }
                }catch(SQLException ex){
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                }
            }
            if(check.equals("no")){
                if(cb_accType.equals("Warehouse Manager"))
                    cb_accType = "WM";
                else if(cb_accType.equals("Supervisor"))
                    cb_accType = "SP";
                else if(cb_accType.equals("Picker/Packer"))
                    cb_accType = "PP";
                else if(cb_accType.equals("Receiver"))
                    cb_accType = "RC";
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
                        PreparedStatement pst =connectDB.prepareStatement("UPDATE accounts SET employeeID = ?, name =?, type=?, role=?, password=? WHERE (`SN` = '"+sn+"');");
                        pst.setString(1,employeeID);
                        pst.setString(2,name);
                        pst.setString(3,cb_accType);
                        pst.setString(4,cb_role);
                        pst.setString(5,pass);

                        pst.execute();

                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }
                }
            }
        }
    }
    
    //Delete Acc for WM
    public void deleteAccWM(int sn, ActionEvent event){
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
                PreparedStatement pst =connectDB.prepareStatement("DELETE FROM accounts WHERE SN = '"+sn+"';");                
                pst.execute();
                
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM accounts;");
                while (rs.next()) {
                    int sna = rs.getInt("MaxSN");
                    for(int i = sn; i < sna+1; i++){
                        int newi = i - 1;
                        PreparedStatement pst2 =connectDB.prepareStatement("UPDATE accounts SET SN = ? WHERE (`SN` = '"+i+"');");
                        pst2.setString(1,String.valueOf(newi));
                        pst2.execute();
                    }
                }
                
            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            try{
                Parent root = FXMLLoader.load(getClass().getResource("WM.fxml"));
                Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                loginStage.setScene(scene);
                loginStage.centerOnScreen();
                loginStage.show();

                root.setOnMousePressed((MouseEvent event1) -> {
                                        xOffset = event1.getSceneX();
                                        yOffset = event1.getSceneY();
                                    });

                root.setOnMouseDragged((MouseEvent event1) -> {
                            loginStage.setX(event1.getScreenX() - xOffset);
                            loginStage.setY(event1.getScreenY() - yOffset);
                                    });

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
    }
    
    //Courier table for WM
    public ObservableList viewCourierWM(String search, TableView tableAccount, ObservableList<setup_courier> ObserveList, TableColumn col_sn, TableColumn col_Name, 
            TableColumn<setup_courier,setup_courier> col_action){ 
        tableAccount.getItems().clear();

        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<setup_courier, setup_courier>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(setup_courier setupC, boolean empty) {
                super.updateItem(setupC, empty);
                if (setupC == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    setGraphic(moreButton);
                    moreButton.setText("Delete");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSN() == Integer.parseInt(col_sn.getCellData(setupC).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSN());
                                        // create a text input dialog
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle(null);
                                        alert.initStyle(StageStyle.UTILITY);;
                                        alert.setHeaderText(null);
                                        alert.setContentText("Confirm?");
                                        Optional<ButtonType> action = alert.showAndWait();
                                        if(action.get() == ButtonType.OK){
                                            try{
                                                DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                                                PreparedStatement pst =connectDB.prepareStatement("DELETE FROM courier WHERE SN = '"+sn+"';");                
                                                pst.execute();

                                                ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM courier;");
                                                while (rs.next()) {
                                                    int sna = rs.getInt("MaxSN");
                                                    for(int q = sn; q < sna+1; q++){
                                                        int newi = q - 1;
                                                        PreparedStatement pst2 =connectDB.prepareStatement("UPDATE courier SET SN = ? WHERE (`SN` = '"+q+"');");
                                                        pst2.setString(1,String.valueOf(newi));
                                                        pst2.execute();
                                                    }
                                                }
                                            }catch(SQLException ex){
                                                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                                            }
                                            try{
                                                Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_courierMgt.fxml"));
                                                Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                                                Scene scene = new Scene(root);
                                                loginStage.setScene(scene);
                                                loginStage.centerOnScreen();
                                                loginStage.show();

                                                root.setOnMousePressed((MouseEvent event1) -> {
                                                                        xOffset = event1.getSceneX();
                                                                        yOffset = event1.getSceneY();
                                                                    });

                                                root.setOnMouseDragged((MouseEvent event1) -> {
                                                            loginStage.setX(event1.getScreenX() - xOffset);
                                                            loginStage.setY(event1.getScreenY() - yOffset);
                                                                    });

                                            }catch (Exception e){
                                                e.printStackTrace();
                                                e.getCause();
                                            }
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        tableAccount.setItems(ObserveList);
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, name FROM courier WHERE sn LIKE '%" + search + "%' OR name LIKE '%" + search + "%' ORDER BY sn ASC;");
            while (rs.next()) {
                ObserveList.add(new setup_courier(rs.getInt("sn"), rs.getString("name")));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("SN")));
        col_Name.setCellValueFactory((new PropertyValueFactory<>("name")));
        try {
            tableAccount.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        return ObserveList;  
    }
    
    //Adding new Courier for WM
    public void addNewCouWM(ActionEvent event, String name){
        if(name.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Name field is blank");
        }
        else
        {
            String check = "no";
            try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT name FROM courier;");
            while (rs.next()) {
                if((name.toLowerCase()).equals((rs.getString("name").toLowerCase())) || (name.toUpperCase()).equals((rs.getString("name")).toUpperCase())){
                    JOptionPane.showMessageDialog( null,"Courier name '"+ name + "' already exist!");
                    check = "yes";
                }
            }
            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            
            if(check.equals("no")){
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
                        ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM courier;");
                        while (rs.next()) {
                            int sna = rs.getInt("MaxSN");
                            int nextSN = sna+1;
                            PreparedStatement pst =connectDB.prepareStatement("insert into courier(sn,name) values (?,?)");
                            pst.setString(1,String.valueOf(nextSN));
                            pst.setString(2,name);
                            pst.execute();
                        }
                        System.out.println("Query executed");
                    }
                    catch (Exception e){
                        JOptionPane.showMessageDialog(null, e);
                    }
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_courierMgt.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }                                       

                }
                else
                {
                    System.out.println("Account add was cancelled");
                }
            }
        }
    }
    
    //Supplier table for WM
    public ObservableList viewSupplierWM(String search, TableView tableAccount, ObservableList<setup_supplier> ObserveList, TableColumn col_sn, TableColumn col_Name, 
            TableColumn col_email, TableColumn col_contactNo, TableColumn<setup_supplier,setup_supplier> col_action){
        tableAccount.getItems().clear();

        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<setup_supplier, setup_supplier>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(setup_supplier setupC, boolean empty) {
                super.updateItem(setupC, empty);
                if (setupC == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    setGraphic(moreButton);
                    moreButton.setText("Edit");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSN() == Integer.parseInt(col_sn.getCellData(setupC).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSN());
                                        String oriName =(ObserveList.get(i).getName());
                                        WM_WHEnv_supplierMgt_edit_Controller.getme(sn, oriName);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_WHEnv_supplierMgt_edit.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        tableAccount.setItems(ObserveList);
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, name, email, contact_number FROM supplier "
                    + "WHERE sn LIKE '%"+search+"%' or name LIKE '%"+search+"%' or email LIKE '%"+search+"%' or contact_number LIKE '%"+search+"%' ORDER BY sn ASC;");
            while (rs.next()) {
                ObserveList.add(new setup_supplier(rs.getInt("sn"), rs.getString("name"), rs.getString("email"), rs.getString("contact_number")));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("SN")));
        col_Name.setCellValueFactory((new PropertyValueFactory<>("name")));
        col_email.setCellValueFactory((new PropertyValueFactory<>("email")));
        col_contactNo.setCellValueFactory((new PropertyValueFactory<>("CN")));
        try {
            tableAccount.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        return ObserveList;  
    }
    
    //Adding new Supplier for WM
    public void addNewSupWM(ActionEvent event, String name, String email, String cn){
        if(name.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Name field is blank");
        }
        else if(email.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Email field is blank");
        }
        else if(cn.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Contact Number field is blank");
        }
        else
        {
            String check = "no";
            try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT name FROM supplier;");
            while (rs.next()) {
                if((name.toLowerCase()).equals((rs.getString("name").toLowerCase())) || (name.toUpperCase()).equals((rs.getString("name")).toUpperCase())){
                    JOptionPane.showMessageDialog( null,"Supplier name '" + name + "' already exist!");
                    check = "yes";
                }
            }
            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            
            if(check.equals("no")){
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
                        ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM supplier;");
                        while (rs.next()) {
                            int sna = rs.getInt("MaxSN");
                            int nextSN = sna+1;
                            PreparedStatement pst =connectDB.prepareStatement("insert into supplier(sn,name,email,contact_number) values (?,?,?,?)");
                            pst.setString(1,String.valueOf(nextSN));
                            pst.setString(2,name);
                            pst.setString(3,email);
                            pst.setString(4,cn);
                            pst.execute();
                        }
                        System.out.println("Query executed");
                    }
                    catch (Exception e){
                        JOptionPane.showMessageDialog(null, e);
                    }
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_supplierMgt.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }                                       

                }
                else
                {
                    System.out.println("Account add was cancelled");
                }
            }
        }
    }
    
    //View Edit Information for Supplier for WM
    public void editViewSupWM(int sn, TextField name, TextField email, TextField cn){
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, name, email, contact_number FROM supplier WHERE (`SN` = '"+sn+"');");
            while (rs.next()) {
                name.setText(rs.getString("name"));
                email.setText(rs.getString("email")); 
                cn.setText(rs.getString("contact_number"));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    //Edit Sup for WM
    public void editSupWM(int sn, String oriName, ActionEvent event, String name,String email,String cn){
        if(name.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Name field is blank");
        }
        else if(email.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Email field is blank");
        }
        else if(cn.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Contact Number field is blank");
        }
        else
        {
            String check = "no";
            if(!oriName.equals(name)){
                try{
                DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT name FROM supplier;");
                while (rs.next()) {
                    if((name.toLowerCase()).equals((rs.getString("name").toLowerCase())) || (name.toUpperCase()).equals((rs.getString("name")).toUpperCase())){
                        JOptionPane.showMessageDialog( null,"Supplier name '" + name + "' already exist!");
                        check = "yes";
                    }
                }
                }catch(SQLException ex){
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                }
            }
            if(check.equals("no")){
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
                        PreparedStatement pst =connectDB.prepareStatement("UPDATE supplier SET name = ?, email =?, contact_number=? WHERE (`SN` = '"+sn+"');");
                        pst.setString(1,name);
                        pst.setString(2,email);
                        pst.setString(3,cn);
                     pst.execute();

                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_supplierMgt.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }
                }
            }
        }
    }
    
    //Delete Acc for WM
    public void deleteSupWM(int sn, ActionEvent event){
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
                PreparedStatement pst =connectDB.prepareStatement("DELETE FROM supplier WHERE SN = '"+sn+"';");                
                pst.execute();
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM supplier;");
                while (rs.next()) {
                    int sna = rs.getInt("MaxSN");
                    for(int q = sn; q < sna+1; q++){
                        int newi = q - 1;
                        PreparedStatement pst2 =connectDB.prepareStatement("UPDATE supplier SET SN = ? WHERE (`SN` = '"+q+"');");
                        pst2.setString(1,String.valueOf(newi));
                        pst2.execute();
                    }
                }
            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            try{
                Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_supplierMgt.fxml"));
                Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                loginStage.setScene(scene);
                loginStage.centerOnScreen();
                loginStage.show();

                root.setOnMousePressed((MouseEvent event1) -> {
                                        xOffset = event1.getSceneX();
                                        yOffset = event1.getSceneY();
                                    });

                root.setOnMouseDragged((MouseEvent event1) -> {
                            loginStage.setX(event1.getScreenX() - xOffset);
                            loginStage.setY(event1.getScreenY() - yOffset);
                                    });

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
    }
    
    //Storage table for WM
    public ObservableList viewStorageWM(String search, TableView tableAccount, ObservableList<setup_storage> ObserveList, TableColumn col_sn, TableColumn col_Name, 
            TableColumn col_length, TableColumn col_width, TableColumn col_height, TableColumn col_vol
            , TableColumn col_volA, TableColumn<setup_storage,setup_storage> col_action){ 
        tableAccount.getItems().clear();

        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<setup_storage, setup_storage>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(setup_storage setupC, boolean empty) {
                super.updateItem(setupC, empty);
                if (setupC == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    setGraphic(moreButton);
                    moreButton.setText("Edit");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSN() == Integer.parseInt(col_sn.getCellData(setupC).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSN());
                                        String loc = (ObserveList.get(i).getLocation());
                                        //System.out.println(sn);
                                        WM_WHEnv_whSpaceEditSpecific_Controller.getme(loc);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_WHEnv_whSpaceEditSpecific.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        tableAccount.setItems(ObserveList);
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn,location,length,width,height,vol,vol_avail FROM storage "
                    + "WHERE (sn LIKE '%"+search+"%' or location LIKE '%"+search+"%' or " +
                    "length LIKE '%"+search+"%' or width LIKE '%"+search+"%' or " +
                    "height LIKE '%"+search+"%' or vol LIKE '%"+search+"%' or " +
                    "vol_avail LIKE '%"+search+"%') ORDER BY sn ASC;");
            while (rs.next()) {
                ObserveList.add(new setup_storage(rs.getInt("sn"), rs.getString("location"), rs.getDouble("length"), rs.getDouble("width"),rs.getDouble("height"),rs.getDouble("vol"),rs.getDouble("vol_avail")));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("SN")));
        col_Name.setCellValueFactory((new PropertyValueFactory<>("location")));
        col_length.setCellValueFactory((new PropertyValueFactory<>("length")));
        col_width.setCellValueFactory((new PropertyValueFactory<>("width")));
        col_height.setCellValueFactory((new PropertyValueFactory<>("height")));
        col_vol.setCellValueFactory((new PropertyValueFactory<>("vol")));
        col_volA.setCellValueFactory((new PropertyValueFactory<>("VolA")));
        try {
            tableAccount.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        return ObserveList;  
    }
    
    //Adding new Supplier for WM
    public void addStorageWM(ActionEvent event, String name, String lvl, String col, String length, String width, String height,
            String qty, String cbName_Convention, String cbLevel_Convention, String cbCol_Convention){
        if(name.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Name field is blank");
        }
        else if(lvl.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Level/Layer field is blank");
        }
        else if(col.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Column Number field is blank");
        }
        else if(length.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Length field is blank");
        }
        else if(width.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Width field is blank");
        }
        else if(height.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Height field is blank");
        }
        /*else if(cbName_Convention.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select a naming convention for Name");
        }
        else if(cbLevel_Convention.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select a naming convention for Level/Layer");
        }
        else if(cbCol_Convention.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select a naming convention for Column");
        }*/
        else
        {
            
                String check = "no";
                /*try{
                DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT name,name_conv,column,level FROM storage;");
                while (rs.next()) {
                    if(name.equals(rs.getString("name")) && name.equals(rs.getString("name_conv")) && name.equals(rs.getString("column")) && name.equals(rs.getString("level"))){
                        JOptionPane.showMessageDialog( null,"Location '" + name + "' already exist!");
                        check = "yes";
                    }
                }
                }catch(SQLException ex){
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                }*/

            if(check.equals("no")){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(null);
                alert.initStyle(StageStyle.UTILITY);;
                alert.setHeaderText(null);
                alert.setContentText("Confirm?");
                Optional<ButtonType> action = alert.showAndWait();
                //String nameC, colC, lvlC;
                if(action.get() == ButtonType.OK)
                {
                    for (int p = 1; p <= Integer.valueOf(qty); p++){
                        try{
                                DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                                ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(name_conv) as MaxN FROM storage WHERE name = '"+name+"';");
                                while (rs.next()) {
                                    char nama;
                                    if((rs.getString("MaxN")) == null)
                                        nama = 'A';
                                    else
                                        nama = (char) (1 + (rs.getString("MaxN")).charAt(0));
                                    for (int r = 1; r <= Integer.valueOf(col); r++){
                                        for (int t = 1; t <= Integer.valueOf(lvl); t++){
                                        try{
                                            Connection con2 = DriverManager.getConnection("jdbc:mysql://lmworks.cup6j3mwxwgo.us-east-2.rds.amazonaws.com:3306/WarehouseA?useSSL=false", "admin" , "LMworksFYP321!");
                                            ResultSet rs2 = con2.createStatement().executeQuery("SELECT name, name_conv, col, level, MAX(SN) as MaxSN, MAX(name_conv) as MaxN, MAX(col) as MaxC, MAX(level) as MaxL FROM storage;");
                                            while (rs2.next()) {
                                                Double volume = Double.parseDouble(length) * Double.parseDouble(width) * Double.parseDouble(height);
                                                int sna = rs2.getInt("MaxSN");
                                                int nextSN = sna+1;
                                                //int nameC = rs.getInt("MaxN") + p;
                                                //int colC = rs.getInt("MaxC") + r;
                                                //int lvlC = rs.getInt("MaxL") + t;
                                                String location = "";
                                                if(t<10 && r<10)
                                                    location = name + " " + nama + ".0" + t + ".0" + r;
                                                else if(t<10)
                                                    location = name + " " + nama + ".0" + t + "." + r;
                                                else if(r<10)
                                                    location = name + " " + nama + "." + t + ".0" + r;
                                                else
                                                    location = name + " " + nama + "." + t + "." + r;
                                                PreparedStatement pst =connectDB.prepareStatement("insert into storage(sn,name,name_conv,col,level,location,length,width,height,vol,vol_avail) values (?,?,?,?,?,?,?,?,?,?,?)");
                                                pst.setString(1,String.valueOf(nextSN));
                                                pst.setString(2,name);
                                                pst.setString(3,String.valueOf(nama));
                                                if(r<10)
                                                    pst.setString(4,0+String.valueOf(r));
                                                else
                                                    pst.setString(4,String.valueOf(r));
                                                if(t<10)
                                                    pst.setString(5,0+String.valueOf(t));
                                                else
                                                    pst.setString(5,String.valueOf(t));
                                                pst.setString(6,location);
                                                pst.setString(7,length);
                                                pst.setString(8,width);
                                                pst.setString(9,height);
                                                pst.setString(10,String.valueOf(volume));
                                                pst.setString(11,String.valueOf(volume));
                                                pst.execute();
                                                System.out.println(location + " created");
                                            }

                                        }
                                        catch (Exception e){
                                            JOptionPane.showMessageDialog(null, e);
                                        }
                                        }
                                    }

                                }
                                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                                alert2.setTitle(null);
                                alert2.initStyle(StageStyle.UTILITY);;
                                alert2.setHeaderText(null);
                                alert2.setContentText("Storage added.");
                                
                            }
                            catch (Exception e){
                                    JOptionPane.showMessageDialog(null, e);
                                }
                                    /*if(cbName_Convention.equals("Alphabetical")){
                                        try{
                                            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                                            ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(name_conv) as MaxN FROM storage;");
                                            while (rs.next()) {
                                                if(rs.getString("MaxN").equals("null"))
                                                    nameC = "A";
                                                else
                                                    nameC = rs.getString("MaxN") + 1;
                                            }
                                            System.out.println("Query executed");
                                        }
                                        catch (Exception e){
                                            JOptionPane.showMessageDialog(null, e);
                                        }
                                    }
                                    else{
                                        try{
                                            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                                            ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(name_conv) as MaxN FROM storage;");
                                            while (rs.next()) {
                                                int nameCint = rs.getInt("MaxN") + 1;
                                                nameC = String.valueOf(nameCint);
                                            }
                                            System.out.println("Query executed");
                                        }
                                        catch (Exception e){
                                            JOptionPane.showMessageDialog(null, e);
                                        }
                                    }*/
                        
                    }
                    
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_whSpace.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }                                       

                }
                else
                {
                    System.out.println("Account add was cancelled");
                }
            }
         
        }
    }
    
    //View Edit Information for Edit Acc for WM
    public void editViewStorageWM(String locName, TextField name, TextField length, TextField width, TextField height){
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT length, width, height FROM storage WHERE location like '" + locName + "%';");
            while (rs.next()) {
                name.setText(locName);
                length.setText(rs.getString("length")); 
                width.setText(rs.getString("width")); 
                height.setText(rs.getString("height"));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    //Edit Storage for WM
    public void editStorageWM(String locName, ActionEvent event, String name, String length, String width, String height){
        if(name.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Name field is blank");
        }
        else if (length.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Length field is blank");
        }
        else if (width.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Width field is blank");
        }
        else if (height.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Height field is blank");
        }
        else{
            String check = "no";
            if(!name.equals(locName)){
                try{
                    DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                    String newName = name + " ";
                    String[] parts = newName.split(" ");
                    String newnewName = parts[0];
                    ResultSet rs = connectDB.createStatement().executeQuery("SELECT location FROM storage WHERE location like '" + name + "%' AND name = '" + newnewName + "';");
                    while (rs.next()) {
                        if(!rs.getString("location").equals("null")){
                            JOptionPane.showMessageDialog( null,"Location with the name '"+name+"' already exist!");
                            check = "yes";
                            break;
                        }
                    }

                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
            }
            
            if(check.equals("no")){
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
                        
                        ResultSet rs7 = connectDB.createStatement().executeQuery("SELECT name, name_conv, level, col, location, vol, vol_avail FROM storage WHERE location like '" + locName + "%';");
                        while (rs7.next()) {
                            String newName = locName + " ";
                            String[] parts = newName.split(" ");
                            String newnewName = parts[0];
                            String locB = rs7.getString("location");
                            PreparedStatement pst =connectDB.prepareStatement("UPDATE storage SET name = ?, length = ?, width = ?, height = ?, location = ?, vol = ?, vol_avail = ?, name_conv = ? WHERE location like '" + locB + "' AND name = '" + newnewName + "';");
                            //String newnewName = newName.split(" ")[0];
                            String newNameConv = rs7.getString("name_conv");
                            String newName2 = name + " ";
                            String[] parts2 = newName2.split(" ");
                            String newnewName2 = parts2[0];
                            String locationNew = newnewName2 + " " + rs7.getString("name_conv") + "." + rs7.getString("level") + "." + rs7.getString("col");
                            if(parts.length>1){
                                newNameConv = parts[1];
                                locationNew = newnewName2 + " " + newNameConv + "." + rs7.getString("level") + "." + rs7.getString("col");
                            }
                            pst.setString(1,newnewName2); 
                            pst.setString(2,length); 
                            pst.setString(3,width); 
                            pst.setString(4,height);
                            
                            pst.setString(5,locationNew); 
                            Double volume = Double.parseDouble(length) * Double.parseDouble(width) * Double.parseDouble(height);
                            pst.setString(6,String.valueOf(volume));
                            if(rs7.getString("vol_avail").equals(rs7.getString("vol")))
                                pst.setString(7,String.valueOf(volume));
                            else{
                                double newVol = volume - Double.parseDouble(rs7.getString("vol")) + Double.parseDouble(rs7.getString("vol_avail"));
                                pst.setString(7,String.valueOf(newVol));
                            }
                            if(!rs7.getString("name_conv").equals(newNameConv))
                                pst.setString(8,newNameConv);
                            else
                                pst.setString(8,rs7.getString("name_conv"));
                            pst.execute();
                            PreparedStatement pst2 =connectDB.prepareStatement("UPDATE upc_location SET location = ? WHERE location like '" + locB + "';");
                            pst2.setString(1,locationNew);
                            pst2.execute();
                        }
                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_whSpace.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }
                }
            }
        }
    }
    
    //Delete Storage for WM
    public void deleteStorageWM(String locName, ActionEvent event){
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
                ResultSet rs7 = connectDB.createStatement().executeQuery("SELECT sn, name, location FROM storage WHERE location like '" + locName + "%';");
                while (rs7.next()) {
                    String newName = locName + " ";
                    String[] parts = newName.split(" ");
                    String newnewName = parts[0];
                    String locB = rs7.getString("location");                 
                    int sn = Integer.valueOf(rs7.getString("sn"));
                    ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM storage;");
                    while (rs.next()) {
                        int sna = rs.getInt("MaxSN");
                        for(int q = sn; q < sna+1; q++){
                            int newi = q - 1;
                            PreparedStatement pst3 =connectDB.prepareStatement("UPDATE storage SET SN = ? WHERE (`SN` = '"+q+"');");
                            pst3.setString(1,String.valueOf(newi));
                            pst3.execute();
                        }
                    }
                    PreparedStatement pst =connectDB.prepareStatement("DELETE FROM storage WHERE location like '" + locB + "'AND name = '" + newnewName + "';");
                    pst.execute();
                    PreparedStatement pst2 =connectDB.prepareStatement("DELETE FROM upc_location WHERE location like '" + locB + " %';");
                    pst2.execute();
                    
                    
                }
                
                
            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle(null);
            alert2.initStyle(StageStyle.UTILITY);;
            alert2.setHeaderText(null);
            alert2.setContentText("Storage deleted.");
            try{
                Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_whSpace.fxml"));
                Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                loginStage.setScene(scene);
                loginStage.centerOnScreen();
                loginStage.show();

                root.setOnMousePressed((MouseEvent event1) -> {
                                        xOffset = event1.getSceneX();
                                        yOffset = event1.getSceneY();
                                    });

                root.setOnMouseDragged((MouseEvent event1) -> {
                            loginStage.setX(event1.getScreenX() - xOffset);
                            loginStage.setY(event1.getScreenY() - yOffset);
                                    });

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
    }
    
    //Edit Storage for WM
    public void editStorageSpecificWM(String locName, ActionEvent event, String name, String length, String width, String height){
        if(name.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Name field is blank");
        }
        else if (length.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Length field is blank");
        }
        else if (width.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Width field is blank");
        }
        else if (height.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Height field is blank");
        }
        else{
            String check = "no";
            if(!name.equals(locName)){
                try{
                    DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                    String newName = name + " ";
                    String[] parts = newName.split(" ");
                    String newnewName = parts[0];
                    ResultSet rs = connectDB.createStatement().executeQuery("SELECT location FROM storage WHERE location like '" + name + "%' AND name = '" + newnewName + "';");
                    while (rs.next()) {
                        if(!rs.getString("location").equals("null")){
                            JOptionPane.showMessageDialog( null,"Location with the name '"+name+"' already exist!");
                            check = "yes";
                            break;
                        }
                    }

                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
            }
            
            if(check.equals("no")){
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
                        
                        ResultSet rs7 = connectDB.createStatement().executeQuery("SELECT name, name_conv, level, col, location, vol, vol_avail FROM storage WHERE location like '" + locName + "%';");
                        while (rs7.next()) {
                            String newName = locName + " ";
                            String[] parts = newName.split(" ");
                            String newnewName = parts[0];
                            String locB = rs7.getString("location");
                            PreparedStatement pst =connectDB.prepareStatement("UPDATE storage SET name = ?, length = ?, width = ?, height = ?, location = ?, vol = ?, vol_avail = ?, name_conv = ? WHERE location like '" + locB + "' AND name = '" + newnewName + "';");
                            //String newnewName = newName.split(" ")[0];
                            String newNameConv = rs7.getString("name_conv");
                            String newName2 = name + " ";
                            String[] parts2 = newName2.split(" ");
                            String newnewName2 = parts2[0];
                            String newnewName3 = parts2[1];
                            String[] parts3 = newnewName3.split("\\.");
                            String newNameConv2 = parts3[0];
                            String newLvl = parts3[1];
                            String newCol = parts3[2];
                            String locationNew = newnewName2 + " " + newNameConv2 + "." + newLvl + "." + newCol;

                            pst.setString(1,newnewName2); 
                            pst.setString(2,length); 
                            pst.setString(3,width); 
                            pst.setString(4,height);
                            
                            pst.setString(5,locationNew); 
                            Double volume = Double.parseDouble(length) * Double.parseDouble(width) * Double.parseDouble(height);
                            pst.setString(6,String.valueOf(volume));
                            if(rs7.getString("vol_avail").equals(rs7.getString("vol")))
                                pst.setString(7,String.valueOf(volume));
                            else{
                                double newVol = volume - Double.parseDouble(rs7.getString("vol")) + Double.parseDouble(rs7.getString("vol_avail"));
                                pst.setString(7,String.valueOf(newVol));
                            }
                            if(!rs7.getString("name_conv").equals(newNameConv2))
                                pst.setString(8,newNameConv2);
                            else
                                pst.setString(8,rs7.getString("name_conv"));
                            pst.execute();
                            PreparedStatement pst2 =connectDB.prepareStatement("UPDATE upc_location SET location = ? WHERE location like '" + locB + "';");
                            pst2.setString(1,locationNew);
                            pst2.execute();
                        }
                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_whSpace.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }
                }
            }
        }
    }
    
    //Master Product Table for WM
    public ObservableList viewProdMasterWM(String search, TableView tableAccount, ObservableList<prod_master> ObserveList, TableColumn col_sn, TableColumn col_upc, TableColumn col_Name, TableColumn col_qty,
            TableColumn col_unit, TableColumn col_location, TableColumn col_supplier, TableColumn col_category, TableColumn col_minQty, TableColumn col_maxQty, TableColumn col_ARStatus, 
            TableColumn col_dateAdded, TableColumn<prod_master,prod_master> col_action, TableColumn<prod_master,prod_master> col_action2){
        tableAccount.getItems().clear();
        
        //col_action.setStyle( "-fx-alignment: CENTER;");
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<prod_master, prod_master>() {
            private final Button moreButton = new Button();
            private final Button moreButton2 = new Button();
        

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
                    setGraphic(moreButton);
                    moreButton.setText("Edit");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSN() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSN());
                                        String oriUPC =(ObserveList.get(i).getUPC());
                                        String oriLoc =(ObserveList.get(i).getLocation());
                                        Double oriVol =(ObserveList.get(i).getVolume());
                                        System.out.println(oriVol);
                                        WM_WHEnv_productMgt_ML_edit_Controller.getme(sn, oriUPC, oriLoc, oriVol);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML_edit.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton2);
                    moreButton2.setText("View All");
                    moreButton2.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSN() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSN());
                                        String oriUPC =(ObserveList.get(i).getUPC());
                                        String oriName =(ObserveList.get(i).getProdName());
                                        WM_WHEnv_productMgt_ML_detail_Controller.getme(sn,oriUPC,oriName);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML_detail.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                    HBox pane = new HBox(moreButton,moreButton2);
                    pane.setAlignment(Pos.CENTER);
                    setGraphic(pane);
                    setText(null);
                    
                }
            }
        });
        tableAccount.setItems(ObserveList);
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, upc, prod_name, qty, unit, location, supplier, category, min_qty, max_qty, auto_restock_status, date_added, volume FROM product_master "
                    + "WHERE (sn LIKE '%" + search + "%' or upc LIKE '%" + search + "%' or " +
                    "prod_name LIKE '%" + search + "%' or qty LIKE '%" + search + "%' or " +
                    "unit LIKE '%" + search + "%' or location LIKE '%" + search + "%' or " +
                    "supplier LIKE '%" + search + "%' or category LIKE '%" + search + "%' or " +
                    "min_qty LIKE '%" + search + "%' or max_qty LIKE '%" + search + "%' or " +
                    "auto_restock_status LIKE '%" + search + "%' or date_added LIKE '%" + search + "%' or " +
                    "volume LIKE '%" + search + "%') ORDER BY sn ASC;");
            while (rs.next()) {
                ObserveList.add(new prod_master(rs.getInt("sn"), rs.getString("upc"), rs.getString("prod_name"), rs.getInt("qty"), rs.getString("unit"), 
                        rs.getString("location"), rs.getString("supplier"), rs.getString("category"), rs.getInt("min_qty"), rs.getInt("max_qty"), 
                        rs.getString("auto_restock_status"), rs.getString("date_added"), rs.getDouble("volume")));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        col_sn.setCellValueFactory((new PropertyValueFactory<>("SN")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("UPC")));
        col_Name.setCellValueFactory((new PropertyValueFactory<>("ProdName")));
        col_qty.setCellValueFactory((new PropertyValueFactory<>("qty")));
        col_unit.setCellValueFactory((new PropertyValueFactory<>("unit")));
        col_location.setCellValueFactory((new PropertyValueFactory<>("location")));
        col_supplier.setCellValueFactory((new PropertyValueFactory<>("supplier")));
        col_category.setCellValueFactory((new PropertyValueFactory<>("category")));
        col_minQty.setCellValueFactory((new PropertyValueFactory<>("min_qty")));
        col_maxQty.setCellValueFactory((new PropertyValueFactory<>("max_qty")));
        col_ARStatus.setCellValueFactory((new PropertyValueFactory<>("auto_status")));
        col_dateAdded.setCellValueFactory((new PropertyValueFactory<>("date_added")));
        try {
            tableAccount.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //Adding new Product Master List for WM
    public void addProdMasterWM(ActionEvent event, String TF_upc, String TF_name, String TF_maxQty, String TF_weight, String TF_length, 
            String TF_width, String TF_height, String TF_minQty, String TA_desc, String CB_location, String CB_unit, String CB_supplier, 
            String CB_cat, String CB_specialHand, boolean checkBox_restock, String TF_fileLoc){
        if(TF_upc.equals(""))
        {
            JOptionPane.showMessageDialog( null,"UPC field is blank");
        }
        else if (TF_name.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Name field is blank");
        }
        else if (TF_maxQty.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Maximum Quantity field is blank");
        }
        else if (TF_weight.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Weight field is blank");
        }
        else if (TF_length.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Length field is blank");
        }
        else if (TF_width.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Width field is blank");
        }
        else if (TF_height.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Height field is blank");
        }
        else if (TF_minQty.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Minimum Quantity field is blank");
        }
        else if (CB_location.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select a location for the product.");
        }
        else if (CB_unit.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select/enter a unit for the product.");
        }
        else if (CB_supplier.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select a supplier for the product.");
        }
        else if (CB_cat.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select/enter a category for the product.");
        }
        else if (Integer. valueOf(TF_maxQty) < Integer. valueOf(TF_minQty))
        {
            JOptionPane.showMessageDialog( null,"Maximum quantity has to be more than Minimum quantity");
        }
        
        else
        {
            String check = "no";
            Double volume = (Double.parseDouble(TF_length) * Double.parseDouble(TF_width) * Double.parseDouble(TF_height))/1000000;
            try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT upc, location FROM product_master;");
            ResultSet rs6 = connectDB.createStatement().executeQuery("SELECT vol_avail FROM storage WHERE location = '" + CB_location + "';");
            while (rs.next()) {
                if(TF_upc.equals(rs.getString("upc")) && CB_location.equals(rs.getString("location"))){
                    JOptionPane.showMessageDialog( null,"Product with UPC = '"+TF_upc+"' at location = '" + CB_location + "' already exist!");
                    check = "yes";
                }
            }
            while (rs6.next()) {
                if(volume>rs6.getDouble("vol_avail")){
                    JOptionPane.showMessageDialog( null, CB_location + " does not have enough space! Please select another.");
                    check = "yes";
                }
            }
            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            
            
            
            if(check.equals("no")){
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
                        ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT vol_avail, vol FROM storage WHERE location = '" + CB_location + "';");
                        while (rs2.next()){
                            int locMaxQty = 0;
                            if(rs2.getDouble("vol_avail") == rs2.getDouble("vol"))
                                locMaxQty = (int)(rs2.getDouble("vol_avail") / volume);
                            else
                                locMaxQty = (int)(rs2.getDouble("vol") / volume);
                            int storageAmt = Integer.valueOf(TF_maxQty)/locMaxQty;
                            if(Integer. valueOf(TF_maxQty)%locMaxQty != 0)
                                storageAmt = storageAmt + 1;
                            ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM product_master;");
                            while (rs.next()) {
                                int sna = rs.getInt("MaxSN");
                                int nextSN = sna+1;
                                PreparedStatement pst =connectDB.prepareStatement("insert into product_master(sn, upc, prod_name, max_qty, weight, length, width, height, min_qty, "
                                        + "description, unit, supplier, category, special_handling, auto_restock_status, image_loc, date_added, volume, qty, location) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                                pst.setString(1,String.valueOf(nextSN));
                                pst.setString(2,TF_upc); 
                                pst.setString(3,TF_name); 
                                pst.setString(4,TF_maxQty); 
                                pst.setString(5,TF_weight); 
                                pst.setString(6,TF_length); 
                                pst.setString(7,TF_width); 
                                pst.setString(8,TF_height); 
                                pst.setString(9,TF_minQty); 
                                pst.setString(10,TA_desc);  
                                pst.setString(11,CB_unit); 
                                pst.setString(12,CB_supplier); 
                                pst.setString(13,CB_cat); 
                                pst.setString(14,CB_specialHand); 
                                pst.setString(15,String.valueOf(checkBox_restock)); 
                                pst.setString(16,TF_fileLoc);
                                pst.setString(17,getDate());
                                pst.setString(18,String.valueOf(volume));
                                pst.setString(19,"0");
                                pst.setString(20," ");
                                pst.execute();
                            }
                            ResultSet rs3 = connectDB.createStatement().executeQuery("SELECT location, vol_avail FROM storage WHERE location >= '" + CB_location + "' limit " + storageAmt + ";");
                            while (rs3.next()) {
                                ResultSet rsa = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM upc_location;");
                                while (rsa.next()) {
                                    int sna2 = rsa.getInt("MaxSN");
                                    int nextSN2 = sna2+1;
                                    PreparedStatement pst2 =connectDB.prepareStatement("insert into upc_location(sn, upc, location, max_qty) values (?,?,?,?)");
                                    pst2.setString(1,String.valueOf(nextSN2));
                                    pst2.setString(2,TF_upc); 
                                    pst2.setString(3,rs3.getString("location"));
                                    int qtyLoc = (int)(rs3.getDouble("vol_avail")/volume);
                                    if (qtyLoc > Integer.valueOf(TF_maxQty))
                                        qtyLoc = Integer.valueOf(TF_maxQty);
                                    pst2.setString(4,String.valueOf(qtyLoc));
                                    pst2.execute();
                                    String loca = rs3.getString("location");
                                    double volUsed = qtyLoc*volume;
                                    ResultSet rs5 = connectDB.createStatement().executeQuery("SELECT vol_avail FROM storage WHERE location = '" + loca + "';");
                                    while (rs5.next()) {
                                        double volLeft = rs5.getDouble("vol_avail") - volUsed;
                                        PreparedStatement pst3 =connectDB.prepareStatement("UPDATE storage SET vol_avail = ? WHERE location = ?;");
                                        pst3.setString(1,String.valueOf(volLeft));
                                        pst3.setString(2,loca);
                                        pst3.execute(); 
                                    }
                                }
                            }
                            String master_loc = "";
                            ResultSet rs4 = connectDB.createStatement().executeQuery("SELECT location FROM upc_location WHERE upc >= '" + TF_upc + "';");
                            while (rs4.next()) {
                                master_loc += rs4.getString("location");
                                master_loc += ", ";
                            }
                            PreparedStatement pst4 =connectDB.prepareStatement("UPDATE product_master SET location = ? WHERE (`upc` = '"+TF_upc+"');");
                            pst4.setString(1,master_loc);
                            pst4.execute();
                            System.out.println("Query executed");      
                        }        
                    }
                    catch (Exception e){
                        JOptionPane.showMessageDialog(null, e);
                    }
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }                                       

                }
                else
                {
                    System.out.println("Master List add was cancelled");
                }
            }
        }
    }
    
    //View Edit Information for Edit Acc for WM
    public void editViewProdMasterWM(int sn, TextField TF_upc, TextField TF_name, TextField TF_maxQty, TextField TF_weight, TextField TF_length, TextField TF_width, TextField TF_height, TextField TF_minQty,
             TextArea TA_desc, ComboBox<String> CB_unit, ComboBox<String> CB_supplier, ComboBox<String> CB_cat, CheckBox checkBox_restock, TextField TF_fileLoc, ComboBox<String> CB_specialHand){
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, upc, prod_name, max_qty, weight, length, width, height, min_qty, "
                                            + "description, location, unit, supplier, category, special_handling, auto_restock_status FROM product_master WHERE (`SN` = '"+sn+"');");
            while (rs.next()) {
                TF_upc.setText(rs.getString("upc"));
                TF_name.setText(rs.getString("prod_name")); 
                TF_maxQty.setText(rs.getString("max_qty")); 
                TF_weight.setText(rs.getString("weight"));
                TF_length.setText(rs.getString("length")); 
                TF_width.setText(rs.getString("width")); 
                TF_height.setText(rs.getString("height")); 
                TF_minQty.setText(rs.getString("min_qty"));
                TA_desc.setText(rs.getString("description")); 
                //CB_location.setValue(rs.getString("location")); 
                CB_unit.setValue(rs.getString("unit")); 
                CB_supplier.setValue(rs.getString("supplier")); 
                CB_cat.setValue(rs.getString("category")); 
                checkBox_restock.setSelected(Boolean.parseBoolean(rs.getString("auto_restock_status"))); 
                //TF_fileLoc.setText(rs.getString("imageLoc")); 
                CB_specialHand.setValue(rs.getString("special_handling"));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    //Edit Item in Master List for WM
    public void editProdMasterWM(int sn, String oriUPC , Double oriVol, ActionEvent event, String TF_upc, String TF_name, String TF_maxQty, String TF_weight, String TF_length, 
            String TF_width, String TF_height, String TF_minQty, String TA_desc, String CB_unit, String CB_supplier, 
            String CB_cat, String CB_specialHand, boolean checkBox_restock, String TF_fileLoc){
        if(TF_upc.equals(""))
        {
            JOptionPane.showMessageDialog( null,"UPC field is blank");
        }
        else if (TF_name.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Name field is blank");
        }
        else if (TF_maxQty.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Maximum Quantity field is blank");
        }
        else if (TF_weight.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Weight field is blank");
        }
        else if (TF_length.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Length field is blank");
        }
        else if (TF_width.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Width field is blank");
        }
        else if (TF_height.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Height field is blank");
        }
        else if (TF_minQty.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Minimum Quantity field is blank");
        }
        /*else if (CB_location.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select a location for the product.");
        }*/
        else if (CB_unit.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select/enter a unit for the product.");
        }
        else if (CB_supplier.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select a supplier for the product.");
        }
        else if (CB_cat.equals(""))
        {
            JOptionPane.showMessageDialog( null,"Please select/enter a category for the product.");
        }
        else if (Integer. valueOf(TF_maxQty) < Integer. valueOf(TF_minQty))
        {
            JOptionPane.showMessageDialog( null,"Maximum quantity has to be more than Minimum quantity");
        }
        else{
            String check = "no";
            Double volume = (Double.parseDouble(TF_length) * Double.parseDouble(TF_width) * Double.parseDouble(TF_height))/1000000;
            if(!oriUPC.equals(TF_upc)){
                try{
                    DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                    ResultSet rs = connectDB.createStatement().executeQuery("SELECT upc, location FROM product_master;");
                    while (rs.next()) {
                        if(TF_upc.equals(rs.getString("upc"))){
                            JOptionPane.showMessageDialog( null,"Product with UPC = '"+TF_upc+"' already exist!");
                            check = "yes";
                        }
                    }

                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
            }
            
            if(check.equals("no")){
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
                        PreparedStatement pst =connectDB.prepareStatement("UPDATE product_master SET upc = ?, prod_name = ?, max_qty = ?, weight = ?, length = ?, width = ?, height = ?, min_qty = ?, "
                                            + "description = ?, unit = ?, supplier = ?, category = ?, special_handling = ?, auto_restock_status = ?, image_loc = ?, volume = ? WHERE (`upc` = '"+oriUPC+"');");
                        pst.setString(1,TF_upc); 
                        pst.setString(2,TF_name); 
                        pst.setString(3,TF_maxQty); 
                        pst.setString(4,TF_weight); 
                        pst.setString(5,TF_length); 
                        pst.setString(6,TF_width); 
                        pst.setString(7,TF_height); 
                        pst.setString(8,TF_minQty); 
                        pst.setString(9,TA_desc);  
                        pst.setString(10,CB_unit); 
                        pst.setString(11,CB_supplier); 
                        pst.setString(12,CB_cat); 
                        pst.setString(13,CB_specialHand); 
                        pst.setString(14,String.valueOf(checkBox_restock)); 
                        pst.setString(15,TF_fileLoc);
                        pst.setString(16,String.valueOf(volume));
                        ResultSet rs7 = connectDB.createStatement().executeQuery("SELECT location, max_qty FROM upc_location WHERE upc = '" + oriUPC +"';");
                        while (rs7.next()) {
                            String locB = rs7.getString("location");
                            int maxQty = rs7.getInt("max_qty");
                            int qtyLoc = 0;
                            ResultSet rs8 = connectDB.createStatement().executeQuery("SELECT vol_avail FROM storage WHERE location = '" + locB +"';");
                            while (rs8.next()) {
                                //System.out.println(oriVol);
                                //System.out.println(rs8.getDouble("vol_avail"));
                                double volB = rs8.getDouble("vol_avail");
                                double volAdd = volB + oriVol * maxQty;
                                qtyLoc = (int)(volAdd/volume);
                                if (qtyLoc > maxQty)
                                    qtyLoc = maxQty;
                                System.out.println(volAdd);
                                System.out.println(oriVol);
                                System.out.println(volume);
                                System.out.println(qtyLoc);
                                PreparedStatement pst2 =connectDB.prepareStatement("UPDATE storage SET vol_avail = ? WHERE location = ?;");
                                pst2.setString(1,String.valueOf(volAdd));
                                pst2.setString(2,locB);
                                pst2.execute();
                            }
                            double volUsed = qtyLoc * volume;
                            ResultSet rs5 = connectDB.createStatement().executeQuery("SELECT vol_avail FROM storage WHERE location = '" + locB +"';");
                            while (rs5.next()) {
                                double volLeft = rs5.getDouble("vol_avail") - volUsed;
                                PreparedStatement pst2 =connectDB.prepareStatement("UPDATE storage SET vol_avail = ? WHERE location = ?;");
                                pst2.setString(1,String.valueOf(volLeft));
                                pst2.setString(2,locB);
                                pst2.execute();
                            }
                            PreparedStatement pst5 =connectDB.prepareStatement("UPDATE upc_location SET upc = ?, max_qty = ? WHERE location = '" + locB +"';");
                            pst5.setString(1,TF_upc); 
                            pst5.setString(2,String.valueOf(qtyLoc));
                            pst5.execute();
                        }
                        pst.execute();

                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }
                }
            }
        }
    }
    
    //Delete Item from Master List for WM
    public void deleteProdMasterWM(int sn, String oriUPC, Double oriVol, ActionEvent event){
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
                PreparedStatement pst =connectDB.prepareStatement("DELETE FROM product_master WHERE SN = '"+sn+"';");                
                pst.execute();
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM product_master;");
                while (rs.next()) {
                    int sna = rs.getInt("MaxSN");
                    for(int q = sn; q < sna+1; q++){
                        int newi = q - 1;
                        PreparedStatement pst2 =connectDB.prepareStatement("UPDATE product_master SET SN = ? WHERE (`SN` = '"+q+"');");
                        pst2.setString(1,String.valueOf(newi));
                        pst2.execute();
                    }
                }
                
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT location, sn, max_qty FROM upc_location WHERE upc = '"+oriUPC+"';");
                while (rs2.next()) {
                    String deleteLoc = rs2.getString("location");
                    int newSN = rs2.getInt("sn");
                    int locMaxQty = rs2.getInt("max_qty");
                    PreparedStatement pst3 =connectDB.prepareStatement("DELETE FROM upc_location WHERE upc = '"+oriUPC+"' AND location = '" + deleteLoc + "';");                
                    pst3.execute();
                    ResultSet rs3 = connectDB.createStatement().executeQuery("SELECT MAX(SN) as MaxSN FROM upc_location;");
                    while (rs3.next()) {
                        int sna = rs3.getInt("MaxSN");
                        for(int q = newSN; q < sna+1; q++){
                            int newi = q - 1;
                            PreparedStatement pst2 =connectDB.prepareStatement("UPDATE upc_location SET SN = ? WHERE (`SN` = '"+q+"');");
                            pst2.setString(1,String.valueOf(newi));
                            pst2.execute();
                        }
                    }
                    ResultSet rs8 = connectDB.createStatement().executeQuery("SELECT vol_avail FROM storage WHERE location = '" + deleteLoc +"';");
                    while (rs8.next()) {
                        double volB = rs8.getDouble("vol_avail");
                        double volAdd = volB + oriVol * locMaxQty;
                        int qtyLoc = (int)(volAdd/oriVol);
                        PreparedStatement pst2 =connectDB.prepareStatement("UPDATE storage SET vol_avail = ? WHERE location = ?;");
                        pst2.setString(1,String.valueOf(volAdd));
                        pst2.setString(2,deleteLoc);
                        pst2.execute();
                    }
                }
            }catch(SQLException ex){
                Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
            }
            try{
                Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML.fxml"));
                Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                loginStage.setScene(scene);
                loginStage.centerOnScreen();
                loginStage.show();

                root.setOnMousePressed((MouseEvent event1) -> {
                                        xOffset = event1.getSceneX();
                                        yOffset = event1.getSceneY();
                                    });

                root.setOnMouseDragged((MouseEvent event1) -> {
                            loginStage.setX(event1.getScreenX() - xOffset);
                            loginStage.setY(event1.getScreenY() - yOffset);
                                    });

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }
    }
    
    //Individual Table for WM
    public ObservableList viewProdIndvWM(String UPC, String Name, TableView tableAccount, ObservableList<product_indv> ObserveList, TableColumn col_sn, TableColumn col_sku, TableColumn col_location, TableColumn col_expDate,
            TableColumn col_dateAdd, TableColumn<product_indv,product_indv> col_action, Label upc, Label name){       
        
        //col_action.setStyle( "-fx-alignment: CENTER;");
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<product_indv, product_indv>() {
            private final Button moreButton = new Button();
        

            //@Override
            protected void updateItem(product_indv user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("Edit");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSn());
                                        String oriUPC =(ObserveList.get(i).getUpc());
                                        String getSKU =(ObserveList.get(i).getSku());
                                        WM_WHEnv_productMgt_ML_detail_edit_Controller.getme(sn,UPC,getSKU);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML_detail_edit.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        tableAccount.setItems(ObserveList);
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, sku, date_added, location, expiry_date FROM product_indv WHERE upc = '" + UPC + "' AND status is NULL ORDER BY sn ASC;");
            while (rs.next()) {
                ObserveList.add(new product_indv(rs.getString("date_added"), rs.getInt("sn"), rs.getString("sku"), rs.getString("location"), rs.getString("expiry_date")));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        col_dateAdd.setCellValueFactory((new PropertyValueFactory<>("Date_added")));
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_sku.setCellValueFactory((new PropertyValueFactory<>("Sku")));
        col_location.setCellValueFactory((new PropertyValueFactory<>("Loc")));
        col_expDate.setCellValueFactory((new PropertyValueFactory<>("expiry_date")));
        upc.setText(UPC);
        name.setText(Name);
        try {
            tableAccount.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //View Edit Information for Indv for WM
    public void editViewProdIndvWM(int sn, String sku, TextField TF_sku, ComboBox<String> CB_location, TextField TF_expDate){
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sku, location, expiry_date FROM product_indv WHERE SN = '"+sn+"' AND sku = '" + sku + "';");
            while (rs.next()) {
                TF_sku.setText(rs.getString("Sku"));
                CB_location.setValue(rs.getString("location")); 
                TF_expDate.setText(rs.getString("Expiry_date"));
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
    }
    
    //Edit Item in Indv for WM
    public void editProdIndvWM(int sn, String oriSKU, ActionEvent event, String TF_sku, String CB_location, String TF_expDate){
        if(TF_sku.equals(""))
        {
            JOptionPane.showMessageDialog( null,"SKU field is blank");
        }
        //else if (CB_location.equals(""))
        //{
        //    JOptionPane.showMessageDialog( null,"Please select a location for the product.");
        //}
        else{
            String check = "no";
            if(!oriSKU.equals(TF_sku)){
                try{
                    DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                    ResultSet rs = connectDB.createStatement().executeQuery("SELECT sku FROM product_indv;");
                    while (rs.next()) {
                        if(TF_sku.equals(rs.getString("sku"))){
                            JOptionPane.showMessageDialog( null,"Product with SKU = '"+TF_sku+"' already exist!");
                            check = "yes";
                        }
                    }

                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
            }
            
            if(check.equals("no")){
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
                        PreparedStatement pst =connectDB.prepareStatement("UPDATE product_indv SET sku = ?, location = ?, expiry_date = ? WHERE sku = '"+oriSKU+"' AND sn = '" + sn + "';");
                        pst.setString(1,TF_sku); 
                        pst.setString(2,CB_location); 
                        pst.setString(3,TF_expDate); 
                        pst.execute();

                    }catch(SQLException ex){
                        Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                    }
                    try{
                        Parent root = FXMLLoader.load(getClass().getResource("WM_WHEnv_productMgt_ML_detail.fxml"));
                        Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        loginStage.setScene(scene);
                        loginStage.centerOnScreen();
                        loginStage.show();

                        root.setOnMousePressed((MouseEvent event1) -> {
                                                xOffset = event1.getSceneX();
                                                yOffset = event1.getSceneY();
                                            });

                        root.setOnMouseDragged((MouseEvent event1) -> {
                                    loginStage.setX(event1.getScreenX() - xOffset);
                                    loginStage.setY(event1.getScreenY() - yOffset);
                                            });

                    }catch (Exception e){
                        e.printStackTrace();
                        e.getCause();
                    }
                }
            }
        }
    }
    
    //POin Table for WM
    public ObservableList viewPOWM(String search, TableView table, ObservableList<POin> ObserveList, TableColumn col_sn, TableColumn col_PONumber, TableColumn col_supplierName, TableColumn col_orderedBy,
            TableColumn col_orderedDate, TableColumn col_status, TableColumn<POin,POin> col_action){ 
        table.getItems().clear();
        
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<POin, POin>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(POin user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSn());
                                        String oriPOnum =(ObserveList.get(i).getPONum());
                                        String oriSup =(ObserveList.get(i).getSupplier());
                                        Date oriDate =(ObserveList.get(i).getOrderDate());
                                        WM_POIN_view_Controller.getme(sn,oriPOnum,oriSup,oriDate);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_POIN_view.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, PONum, supplier, orderBy, order_date, status FROM POin WHERE status != 'Not Approved' AND status != 'Fully Received'"
                    + "AND (sn LIKE '%" + search + "%' or " +
                    "PONum LIKE '%" + search + "%' or " +
                    "supplier LIKE '%" + search + "%' or " +
                    "orderBy LIKE '%" + search + "%' or " +
                    "order_date LIKE '%" + search + "%' or " +
                    "status LIKE '%" + search + "%');");
            while (rs.next()) {
                ObserveList.add(new POin(count, rs.getString("PONum"),rs.getString("supplier"),rs.getString("orderBy"), rs.getDate("order_date"),rs.getString("status")));
                count++;
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_PONumber.setCellValueFactory((new PropertyValueFactory<>("PONum")));
        col_supplierName.setCellValueFactory((new PropertyValueFactory<>("Supplier")));
        col_orderedBy.setCellValueFactory((new PropertyValueFactory<>("OrderBy")));
        col_orderedDate.setCellValueFactory((new PropertyValueFactory<>("OrderDate")));
        col_status.setCellValueFactory((new PropertyValueFactory<>("Status")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //View POin for WM
    public ObservableList ViewPOinWM(int sn, String PONum, String Sup, Date oriDate, TableView table, ObservableList<POin> ObserveList, TableColumn col_sn, TableColumn col_QtyOrder, TableColumn col_rcv, TableColumn col_qtyRem,
            TableColumn col_UPC, TableColumn col_prodName){
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT upc,qty_ordered,qty_rcv,qty_remaining FROM POin_detail WHERE PONum = '" + PONum +"';");
            while (rs.next()) {
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT prod_name FROM product_master WHERE upc = '" + rs.getString("upc") +"';");
                while (rs2.next()) {
                    ResultSet rs4 = connectDB.createStatement().executeQuery("SELECT status FROM POin WHERE PONum = '" + PONum +"';");
                    while (rs4.next()) {
                        if(rs4.getString("status").equals("Not Received"))
                            ObserveList.add(new POin(rs.getString("upc"), count ,rs2.getString("prod_name"),rs.getInt("qty_ordered"),rs.getInt("qty_rcv"),rs.getInt("qty_remaining")));
                        else{
                            ResultSet rs3 = connectDB.createStatement().executeQuery("SELECT qty FROM POin_rcv WHERE upc = '" + rs.getString("upc") +"' AND PONum = '" + PONum + "';");
                            while (rs3.next()) {
                                ObserveList.add(new POin(rs.getString("upc"), count ,rs2.getString("prod_name"),rs.getInt("qty_ordered"),rs3.getInt("qty"),rs.getInt("qty_remaining")));
                            }
                        }
                    }
                    
                    
                }
                count++;
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_QtyOrder.setCellValueFactory((new PropertyValueFactory<>("Qty_ordered")));
        col_rcv.setCellValueFactory((new PropertyValueFactory<>("Qty_rcv")));
        col_qtyRem.setCellValueFactory((new PropertyValueFactory<>("Qty_remaining")));
        col_UPC.setCellValueFactory((new PropertyValueFactory<>("Upc")));
        col_prodName.setCellValueFactory((new PropertyValueFactory<>("Prod_name")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;
    }
    
    //POin restock Table for WM
    public ObservableList viewPOApprovalWM(String search, TableView table, ObservableList<POin> ObserveList, TableColumn col_sn, TableColumn col_PONumber, TableColumn col_supplierName, TableColumn col_dateCreated,
            TableColumn<POin,POin> col_action){ 
        table.getItems().clear();
        
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<POin, POin>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(POin user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSn());
                                        String oriPOnum =(ObserveList.get(i).getPONum());
                                        String oriSup =(ObserveList.get(i).getSupplier());
                                        Date oriDate =(ObserveList.get(i).getOrderDate());
                                        WM_POIN_restock_view_Controller.getme(sn,oriPOnum,oriSup,oriDate);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_POIN_restock_view.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, PONum, supplier, orderBy, order_date, status FROM POin WHERE status = 'Not Approved by WM'"
                    + "AND (sn LIKE '%" + search + "%' or " +
                    "PONum LIKE '%" + search + "%' or " +
                    "supplier LIKE '%" + search + "%' or " +
                    "orderBy LIKE '%" + search + "%' or " +
                    "order_date LIKE '%" + search + "%' or " +
                    "status LIKE '%" + search + "%');");
            while (rs.next()) {
                ObserveList.add(new POin(count, rs.getString("PONum"),rs.getString("supplier"), rs.getDate("order_date")));
                count++;
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_PONumber.setCellValueFactory((new PropertyValueFactory<>("PONum")));
        col_supplierName.setCellValueFactory((new PropertyValueFactory<>("Supplier")));
        col_dateCreated.setCellValueFactory((new PropertyValueFactory<>("OrderDate")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //POin restock Table for WM
    public ObservableList POApprovalViewWM(String PONum, TableView table, ObservableList<POin> ObserveList, TableColumn col_sn, TableColumn col_upc, TableColumn col_productName,
            TableColumn col_qty){       

        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT upc,qty_ordered FROM POin_detail WHERE PONum = '" + PONum +"';");
            while (rs.next()) {
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT prod_name FROM product_master WHERE upc = '" + rs.getString("upc") +"';");
                while (rs2.next()) {
                    ObserveList.add(new POin(rs.getString("upc"), count ,rs2.getString("prod_name"), rs.getInt("qty_ordered")));
                    //moreButton.setText(rs.getInt("qty_ordered"));
                }
                count++;
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_productName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_qty.setCellValueFactory((new PropertyValueFactory<>("qty_ordered")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //Inventory Table Report for WM
    public ObservableList viewInvetoryReportMonthWM(String search, TableView table, ObservableList<product_indv> ObserveList, TableColumn col_sn, TableColumn col_month, TableColumn col_year, TableColumn col_total,
            TableColumn<product_indv,product_indv> col_action){       
        table.getItems().clear();
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<product_indv, product_indv>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(product_indv user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        String year = (ObserveList.get(i).getYear());
                                        String month = (ObserveList.get(i).getMonth());
                                        WM_genRpt_invView_Controller.getme(year,month);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_genRpt_invView.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, month, year, startDate, endDate FROM ReportMonth "
                    + "WHERE sn LIKE '%"+search+"%' or month LIKE '%"+search+"%' or " +
                    "year LIKE '%"+search+"%' or startDate LIKE '%"+search+"%' or endDate LIKE '%"+search+"%'");
            while (rs.next()) {
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT Count(sn) as qtyMonth FROM product_indv WHERE date_added <= '" + rs.getDate("endDate") + "' AND "
                        + "(delivery_date >= '" + rs.getDate("endDate") + "' OR delivery_date is NULL);");
                while (rs2.next()) {
                    ObserveList.add(new product_indv(count, rs.getString("month"),rs2.getInt("qtyMonth"),rs.getString("year")));
                    count++;
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_month.setCellValueFactory((new PropertyValueFactory<>("month")));
        col_year.setCellValueFactory((new PropertyValueFactory<>("year")));
        col_total.setCellValueFactory((new PropertyValueFactory<>("qtyMonth")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        return ObserveList;  
    }
    
    //Individual prod Table Current for WM Report
    public ObservableList viewProdIndvReportCurrentWM(String month, String year, TableView tableAccount, ObservableList<product_indv> ObserveList, TableColumn col_sn, 
            TableColumn col_upc, TableColumn col_sku, TableColumn col_prodName, TableColumn col_unit, TableColumn col_loc, TableColumn col_sup,
            TableColumn col_cat, TableColumn col_expDate){
        int count = 1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs3 = connectDB.createStatement().executeQuery("SELECT startDate, endDate FROM ReportMonth WHERE month = '" + month + "' AND year = '" + year + "';");
            while (rs3.next()) {
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT upc, sku, location, expiry_date FROM product_indv WHERE date_added <= '" + rs3.getDate("endDate") + "' AND (delivery_date >= '" + rs3.getDate("endDate") + "' OR delivery_date is NULL);");
                while (rs.next()) {
                    ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT prod_name, unit, supplier, category FROM product_master WHERE upc = '" + rs.getString("upc") + "';");
                    while (rs2.next()) {
                        ObserveList.add(new product_indv(count,rs.getString("upc"), rs.getString("sku"), rs2.getString("prod_name"), rs2.getString("unit"), 
                                rs.getString("location"), rs2.getString("supplier"), rs2.getString("category"), rs.getString("expiry_date")));
                        count++;
                    }
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
       
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_upc.setCellValueFactory((new PropertyValueFactory<>("upc")));
        col_sku.setCellValueFactory((new PropertyValueFactory<>("Sku")));
        col_prodName.setCellValueFactory((new PropertyValueFactory<>("prod_name")));
        col_unit.setCellValueFactory((new PropertyValueFactory<>("unit")));
        col_loc.setCellValueFactory((new PropertyValueFactory<>("Loc")));
        col_sup.setCellValueFactory((new PropertyValueFactory<>("supplier")));
        col_cat.setCellValueFactory((new PropertyValueFactory<>("category")));
        col_expDate.setCellValueFactory((new PropertyValueFactory<>("expiry_date")));

        try {
            tableAccount.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //POin Table Report View for WM
    public ObservableList viewPOinReportMonthWM(String search, TableView table, ObservableList<POin> ObserveList, TableColumn col_sn, TableColumn col_month, TableColumn col_year, TableColumn col_total,
            TableColumn<POin,POin> col_action){       
        table.getItems().clear();
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<POin, POin>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(POin user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        //Date startDate =(ObserveList.get(i).getStartDate());
                                        //Date endDate =(ObserveList.get(i).getEndDate());
                                        String year = (ObserveList.get(i).getYear());
                                        String month = (ObserveList.get(i).getMonth());
                                        WM_genRpt_in_POView_Controller.getme(year,month);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_genRpt_in_POView.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, month, year, startDate, endDate FROM ReportMonth "
                    + "WHERE sn LIKE '%"+search+"%' or month LIKE '%"+search+"%' or " +
                    "year LIKE '%"+search+"%' or startDate LIKE '%"+search+"%' or endDate LIKE '%"+search+"%'");
            while (rs.next()) {
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT Count(DISTINCT PONum) as qtyMonth FROM POin_rcv WHERE approvedBy is Not NULL AND date_rcv >= '" + rs.getDate("startDate") + "' AND date_rcv < '" + rs.getDate("endDate") + "';");
                while (rs2.next()) {
                    ObserveList.add(new POin(count, rs.getString("month"),rs2.getInt("qtyMonth"),rs.getString("year")));
                    count++;
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_month.setCellValueFactory((new PropertyValueFactory<>("month")));
        col_year.setCellValueFactory((new PropertyValueFactory<>("year")));
        col_total.setCellValueFactory((new PropertyValueFactory<>("qtyMonth")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //POin Table Report View Per Month for WM
    public ObservableList viewPOinReportWM(String search, String month, String year, TableView table, ObservableList<POin> ObserveList, TableColumn col_sn, TableColumn col_PONum, TableColumn col_supplier, TableColumn col_OrdBy,
            TableColumn col_dateOrd, TableColumn col_compDate, TableColumn col_status, TableColumn<POin,POin> col_action){       
        table.getItems().clear();
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<POin, POin>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(POin user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSn());
                                        String oriPOnum =(ObserveList.get(i).getPONum());
                                        String oriSup =(ObserveList.get(i).getSupplier());
                                        Date oriDate =(ObserveList.get(i).getOrderDate());
                                        WM_genRpt_in_POViewDet_Controller.getme(sn,oriPOnum,oriSup,oriDate);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_genRpt_in_POViewDet.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs3 = connectDB.createStatement().executeQuery("SELECT startDate, endDate FROM ReportMonth WHERE month = '" + month + "' AND year = '" + year + "';");
            while (rs3.next()) {
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT DISTINCT PONum, date_rcv FROM POin_rcv WHERE date_rcv >= '" + rs3.getDate("startDate") + "' AND date_rcv < '" + rs3.getDate("endDate") + "';");
                while (rs2.next()) {
                    ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, PONum, supplier, orderBy, order_date, status FROM POin WHERE (sn LIKE '%"+search+"%' or supplier LIKE '%"+search+"%'"
                            + "or orderBy LIKE '%"+search+"%' or order_date LIKE '%"+search+"%' or status LIKE '%"+search+"%') AND status = 'Fully received' AND PONum = '" + rs2.getString("PONum") + "';");
                    while (rs.next()) {
                        ObserveList.add(new POin(count, rs2.getString("PONum"),rs.getString("supplier"),rs.getString("orderBy"), rs.getDate("order_date"),rs2.getDate("date_rcv"),rs.getString("status")));
                        count++;
                    }
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_PONum.setCellValueFactory((new PropertyValueFactory<>("PONum")));
        col_supplier.setCellValueFactory((new PropertyValueFactory<>("Supplier")));
        col_OrdBy.setCellValueFactory((new PropertyValueFactory<>("OrderBy")));
        col_dateOrd.setCellValueFactory((new PropertyValueFactory<>("OrderDate")));
        col_compDate.setCellValueFactory((new PropertyValueFactory<>("date_rcv")));
        col_status.setCellValueFactory((new PropertyValueFactory<>("Status")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //View Detailed POin Report for WM
    public ObservableList ViewDetPOinReportWM(int sn, String PONum, String Sup, Date oriDate, TableView table, ObservableList<POin> ObserveList, TableColumn col_sn, TableColumn col_QtyOrder,
            TableColumn col_UPC, TableColumn col_prodName){
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT upc, qty_ordered FROM POin_detail WHERE PONum = '" + PONum +"';");
            while (rs.next()) {
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT prod_name FROM product_master WHERE upc = '" + rs.getString("upc") +"';");
                while (rs2.next()) {
                    ObserveList.add(new POin(rs.getString("upc"), count,rs2.getString("prod_name"),rs.getInt("qty_ordered")));
                }
                count++;
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_QtyOrder.setCellValueFactory((new PropertyValueFactory<>("Qty_ordered")));
        col_UPC.setCellValueFactory((new PropertyValueFactory<>("Upc")));
        col_prodName.setCellValueFactory((new PropertyValueFactory<>("Prod_name")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;
    }
    
    //DOin Table Report View for WM
    public ObservableList viewDOinReportMonthWM(String search, TableView table, ObservableList<POin> ObserveList, TableColumn col_sn, TableColumn col_month, TableColumn col_year, TableColumn col_total,
            TableColumn<POin,POin> col_action){  
        table.getItems().clear();
        
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<POin, POin>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(POin user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        //Date startDate =(ObserveList.get(i).getStartDate());
                                        //Date endDate =(ObserveList.get(i).getEndDate());
                                        String year = (ObserveList.get(i).getYear());
                                        String month = (ObserveList.get(i).getMonth());
                                        WM_genRpt_in_DOView_Controller.getme(year,month);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_genRpt_in_DOView.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, month, year, startDate, endDate FROM ReportMonth "
                    + "WHERE (sn LIKE '%"+search+"%' or month LIKE '%"+search+"%' or " +
                    "year LIKE '%"+search+"%' or startDate LIKE '%"+search+"%' or " +
                    "endDate LIKE '%"+search+"%');");
            while (rs.next()) {
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT Count(Distinct PONum) as qtyMonth,PONum FROM POin_rcv WHERE "
                        + "approvedBy is Not NULL AND date_rcv >= '" + rs.getDate("startDate") + "' "
                        + "AND date_rcv < '" + rs.getDate("endDate") + "';");
                while (rs2.next()) {
                    ObserveList.add(new POin(count, rs.getString("month"),rs2.getInt("qtyMonth"),rs.getString("year")));
                    count++;
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_month.setCellValueFactory((new PropertyValueFactory<>("month")));
        col_year.setCellValueFactory((new PropertyValueFactory<>("year")));
        col_total.setCellValueFactory((new PropertyValueFactory<>("qtyMonth")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //DOin Table Report View Per Month for WM
    public ObservableList viewDOinReportWM(String search, String month, String year, TableView table, ObservableList<POin> ObserveList, TableColumn col_sn, TableColumn col_DONum, TableColumn col_supplier, TableColumn col_OrdBy,
            TableColumn col_dateOrd, TableColumn col_compDate, TableColumn col_status, TableColumn<POin,POin> col_action){ 
        table.getItems().clear();
        
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<POin, POin>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(POin user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSn());
                                        String oriPOnum =(ObserveList.get(i).getPONum());
                                        String oriDOnum =(ObserveList.get(i).getDONum());
                                        String oriSup =(ObserveList.get(i).getSupplier());
                                        Date oriDate =(ObserveList.get(i).getOrderDate());
                                        try{
                                            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
                                            ResultSet rs = connectDB.createStatement().executeQuery("SELECT DISTINCT PONum FROM POin_rcv WHERE DONum = '" + oriDOnum + "';");
                                            while (rs.next()) {
                                                oriPOnum = rs.getString("PONum");
                                            }
                                        }catch(SQLException ex){
                                            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
                                        }
                                        WM_genRpt_in_DOViewDet_Controller.getme(sn,oriPOnum,oriSup,oriDate,oriDOnum);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_genRpt_in_DOViewDet.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs3 = connectDB.createStatement().executeQuery("SELECT startDate, endDate FROM ReportMonth WHERE month = '" + month + "' AND year = '" + year + "';");
            while (rs3.next()) {
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT DISTINCT DONum, PONum, date_rcv FROM POin_rcv WHERE date_rcv >= '" + rs3.getDate("startDate") + "' AND date_rcv < '" + rs3.getDate("endDate") + "';");
                while (rs.next()) {
                    ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT sn, supplier, orderBy, order_date, status FROM POin WHERE (sn LIKE '%"+search+"%' or supplier LIKE '%"+search+"%'"
                            + "or orderBy LIKE '%"+search+"%' or order_date LIKE '%"+search+"%' or status LIKE '%"+search+"%') AND status = 'Fully received' AND PONum = '" + rs.getString("PONum") +"';");
                    while (rs2.next()) {
                        ObserveList.add(new POin(rs.getString("DONum"), count, rs2.getString("supplier"),rs2.getString("orderBy"), rs2.getDate("order_date"),rs.getDate("date_rcv"),rs2.getString("status")));
                        count++;
                    }
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
       
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_DONum.setCellValueFactory((new PropertyValueFactory<>("DONum")));
        col_supplier.setCellValueFactory((new PropertyValueFactory<>("Supplier")));
        col_OrdBy.setCellValueFactory((new PropertyValueFactory<>("OrderBy")));
        col_dateOrd.setCellValueFactory((new PropertyValueFactory<>("OrderDate")));
        col_compDate.setCellValueFactory((new PropertyValueFactory<>("date_rcv")));
        col_status.setCellValueFactory((new PropertyValueFactory<>("Status")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //View Detailed DOin Report for WM
    public ObservableList ViewDetDOinReportWM(int sn, String PONum, String DONum, String Sup, Date oriDate, TableView table, ObservableList<POin> ObserveList, TableColumn col_sn, TableColumn col_QtyOrder,
            TableColumn col_UPC, TableColumn col_prodName){
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT upc, qty FROM POin_detail WHERE DONum = '" + DONum +"';");
            while (rs.next()) {
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT prod_name FROM product_master WHERE upc = '" + rs.getString("upc") +"';");
                while (rs2.next()) {
                    ObserveList.add(new POin(rs.getString("upc"), count,rs2.getString("prod_name"),rs.getInt("qty")));
                }
                count++;
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_QtyOrder.setCellValueFactory((new PropertyValueFactory<>("qty")));
        col_UPC.setCellValueFactory((new PropertyValueFactory<>("Upc")));
        col_prodName.setCellValueFactory((new PropertyValueFactory<>("Prod_name")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;
    }
    
    //POout Table Report View for WM
    public ObservableList viewPOoutReportMonthWM(String search, TableView table, ObservableList<POout> ObserveList, TableColumn col_sn, TableColumn col_month, TableColumn col_year, TableColumn col_total,
            TableColumn<POout,POout> col_action){       
        table.getItems().clear();
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<POout, POout>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(POout user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        String year = (ObserveList.get(i).getYear());
                                        String month = (ObserveList.get(i).getMonth());
                                        WM_genRpt_out_POView_Controller.getme(year,month);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_genRpt_out_POView.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, month, year, startDate, endDate FROM ReportMonth "
                    + "WHERE sn LIKE '%"+search+"%' or month LIKE '%"+search+"%' or " +
                    "year LIKE '%"+search+"%' or startDate LIKE '%"+search+"%' or endDate LIKE '%"+search+"%'");
            while (rs.next()) {
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT Count(DISTINCT PONum) as qtyMonth FROM POout WHERE status = 'Delivered' AND delivery_date >= '" + rs.getDate("startDate") + "' AND delivery_date < '" + rs.getDate("endDate") + "';");
                while (rs2.next()) {
                    ObserveList.add(new POout(count, rs.getString("month"),rs2.getInt("qtyMonth"),rs.getString("year")));
                    count++;
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_month.setCellValueFactory((new PropertyValueFactory<>("month")));
        col_year.setCellValueFactory((new PropertyValueFactory<>("year")));
        col_total.setCellValueFactory((new PropertyValueFactory<>("qtyMonth")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        return ObserveList;  
    }
    
    //POout Table Report View Per Month for WM
    public ObservableList viewPOoutReportWM(String search, String month, String year, TableView table, ObservableList<POout> ObserveList, TableColumn col_sn, TableColumn col_PONum, TableColumn col_SONum, TableColumn col_company, TableColumn col_CrtBy,
            TableColumn col_dateCrt, TableColumn col_deliverDate, TableColumn col_status, TableColumn<POout,POout> col_action){       
        table.getItems().clear();
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<POout, POout>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(POout user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSn());
                                        String oriPOnum =(ObserveList.get(i).getPONum());
                                        String oriSOnum =(ObserveList.get(i).getSONum());
                                        String oriComp =(ObserveList.get(i).getCompany());
                                        Date oriDate =(ObserveList.get(i).getDelivery_date());
                                        System.out.print(oriSOnum);
                                        WM_genRpt_out_POViewDet_Controller.getme(sn,oriPOnum,oriSOnum,oriComp,oriDate);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_genRpt_out_POViewDet.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs3 = connectDB.createStatement().executeQuery("SELECT startDate, endDate FROM ReportMonth WHERE month = '" + month + "' AND year = '" + year + "';");
            while (rs3.next()) {
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, PONum, SONum, company, createdBy, date_created, delivery_date, status FROM POout "
                        + "WHERE (sn LIKE '%"+search+"%' or PONum LIKE '%"+search+"%'"
                        + "or SONum LIKE '%"+search+"%' or company LIKE '%"+search+"%' or createdBy LIKE '%"+search+"%'"
                        + "or date_created LIKE '%"+search+"%' or delivery_date LIKE '%"+search+"%' or status LIKE '%"+search+"%') "
                        + "AND status = 'Delivered'AND delivery_date >= '" + rs3.getDate("startDate") + "' AND delivery_date < '" + rs3.getDate("endDate") + "';");
                while (rs.next()) {
                    ObserveList.add(new POout(count, rs.getString("PONum"),rs.getString("SONum"),rs.getString("company"),rs.getString("createdBy"), rs.getDate("date_created"),rs.getDate("delivery_date"),rs.getString("status")));
                    count++;
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_PONum.setCellValueFactory((new PropertyValueFactory<>("PONum")));
        col_SONum.setCellValueFactory((new PropertyValueFactory<>("SONum")));
        col_company.setCellValueFactory((new PropertyValueFactory<>("company")));
        col_CrtBy.setCellValueFactory((new PropertyValueFactory<>("createdBy")));
        col_dateCrt.setCellValueFactory((new PropertyValueFactory<>("date_created")));
        col_deliverDate.setCellValueFactory((new PropertyValueFactory<>("delivery_date")));
        col_status.setCellValueFactory((new PropertyValueFactory<>("Status")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //View Detailed POout Report for WM
    public ObservableList ViewDetPOoutReportWM(int sn, String SONum, String Comp, Date oriDate, TableView table, ObservableList<POout> ObserveList, TableColumn col_sn, TableColumn col_SKU,
            TableColumn col_UPC, TableColumn col_prodName){
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT upc, prod_name, sku_scanned FROM pickingList_detail WHERE SONum = '" + SONum + "';");
            while (rs.next()) {
                ObserveList.add(new POout(count, rs.getString("upc"), rs.getString("prod_name"), rs.getString("sku_scanned")));
                System.out.println(rs.getString("upc"));
                System.out.println(rs.getString("prod_name"));
                System.out.println(rs.getString("sku_scanned"));
                count++;
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_UPC.setCellValueFactory((new PropertyValueFactory<>("Upc")));
        col_prodName.setCellValueFactory((new PropertyValueFactory<>("Prod_name")));
        col_SKU.setCellValueFactory((new PropertyValueFactory<>("Sku_scanned")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;
    }
    
    //DOout Table Report View for WM
    public ObservableList viewDOoutReportMonthWM(String search, TableView table, ObservableList<POout> ObserveList, TableColumn col_sn, TableColumn col_month, TableColumn col_year, TableColumn col_total,
            TableColumn<POout,POout> col_action){       
        table.getItems().clear();
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<POout, POout>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(POout user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        String year = (ObserveList.get(i).getYear());
                                        String month = (ObserveList.get(i).getMonth());
                                        WM_genRpt_out_DOView_Controller.getme(year,month);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_genRpt_out_DOView.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, month, year, startDate, endDate FROM ReportMonth "
                    + "WHERE sn LIKE '%"+search+"%' or month LIKE '%"+search+"%' or " +
                    "year LIKE '%"+search+"%' or startDate LIKE '%"+search+"%' or endDate LIKE '%"+search+"%'");
            while (rs.next()) {
                ResultSet rs2 = connectDB.createStatement().executeQuery("SELECT Count(DISTINCT DONum) as qtyMonth FROM POout WHERE status = 'Delivered' AND delivery_date >= '" + rs.getDate("startDate") + "' AND delivery_date < '" + rs.getDate("endDate") + "';");
                while (rs2.next()) {
                    ObserveList.add(new POout(count, rs.getString("month"),rs2.getInt("qtyMonth"),rs.getString("year")));
                    count++;
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_month.setCellValueFactory((new PropertyValueFactory<>("month")));
        col_year.setCellValueFactory((new PropertyValueFactory<>("year")));
        col_total.setCellValueFactory((new PropertyValueFactory<>("qtyMonth")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        return ObserveList;  
    }
    
    //DOout Table Report View Per Month for WM
    public ObservableList viewDOoutReportWM(String search, String month, String year, TableView table, ObservableList<POout> ObserveList, TableColumn col_sn, TableColumn col_DONum, TableColumn col_SONum, TableColumn col_company, TableColumn col_CrtBy,
            TableColumn col_dateCrt, TableColumn col_deliverDate, TableColumn col_status, TableColumn<POout,POout> col_action){       
        table.getItems().clear();
        col_action.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        col_action.setCellFactory(param -> new TableCell<POout, POout>() {
            private final Button moreButton = new Button();

            //@Override
            protected void updateItem(POout user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null) {
                    setGraphic(null);
                    return;
                }
                else
                {
                    //moreButton.getStyleClass().add("moreButton");
                    setGraphic(moreButton);
                    moreButton.setText("View");
                    moreButton.setOnAction(
                            event -> {
                                for (int i = 0; i < ObserveList.size(); i++)
                                {
                                    if (ObserveList.get(i).getSn() == Integer.parseInt(col_sn.getCellData(user).toString()))
                                    {
                                        int sn =(ObserveList.get(i).getSn());
                                        String oriDOnum =(ObserveList.get(i).getDONum());
                                        String oriSOnum =(ObserveList.get(i).getSONum());
                                        String oriComp =(ObserveList.get(i).getCompany());
                                        Date oriDate =(ObserveList.get(i).getDelivery_date());
                                        WM_genRpt_out_DOViewDet_Controller.getme(sn,oriDOnum,oriSOnum,oriComp,oriDate);
                                        try {
                                            Parent fxml2 = FXMLLoader.load(getClass().getResource("WM_genRpt_out_DOViewDet.fxml"));
                                            Scene window3 = new Scene(fxml2);
                                            Stage parentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                                            parentStage.setScene(window3);
                                            parentStage.centerOnScreen();
                                            parentStage.show();

                                            fxml2.setOnMousePressed(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    xOffset = event.getSceneX();
                                                    yOffset = event.getSceneY();
                                                }
                                            });
                                            fxml2.setOnMouseDragged(new EventHandler<MouseEvent>() {
                                                @Override
                                                public void handle(MouseEvent event) {
                                                    parentStage.setX(event.getScreenX() - xOffset);
                                                    parentStage.setY(event.getScreenY() - yOffset);
                                                }
                                            });
                                        } catch (Exception e){
                                            e.printStackTrace();
                                            e.getCause();
                                        }
                                    }

                                }
                            }
                    );
                }
            }
        });
        
        table.setItems(ObserveList);
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs3 = connectDB.createStatement().executeQuery("SELECT startDate, endDate FROM ReportMonth WHERE month = '" + month + "' AND year = '" + year + "';");
            while (rs3.next()) {
                ResultSet rs = connectDB.createStatement().executeQuery("SELECT sn, DONum, SONum, company, createdBy, date_created, delivery_date, status FROM POout "
                        + "WHERE (sn LIKE '%"+search+"%' or DONum LIKE '%"+search+"%'"
                        + "or SONum LIKE '%"+search+"%' or company LIKE '%"+search+"%' or createdBy LIKE '%"+search+"%'"
                        + "or date_created LIKE '%"+search+"%' or delivery_date LIKE '%"+search+"%' or status LIKE '%"+search+"%') "
                        + "AND status = 'Delivered'AND delivery_date >= '" + rs3.getDate("startDate") + "' AND delivery_date < '" + rs3.getDate("endDate") + "';");
                while (rs.next()) {
                    ObserveList.add(new POout(rs.getString("DONum"), count, rs.getString("SONum"),rs.getString("company"),rs.getString("createdBy"), rs.getDate("date_created"),rs.getDate("delivery_date"),rs.getString("status")));
                    count++;
                }
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_DONum.setCellValueFactory((new PropertyValueFactory<>("DONum")));
        col_SONum.setCellValueFactory((new PropertyValueFactory<>("SONum")));
        col_company.setCellValueFactory((new PropertyValueFactory<>("company")));
        col_CrtBy.setCellValueFactory((new PropertyValueFactory<>("createdBy")));
        col_dateCrt.setCellValueFactory((new PropertyValueFactory<>("date_created")));
        col_deliverDate.setCellValueFactory((new PropertyValueFactory<>("delivery_date")));
        col_status.setCellValueFactory((new PropertyValueFactory<>("Status")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;  
    }
    
    //View Detailed DOout Report for WM
    public ObservableList ViewDetDOoutReportWM(int sn, String DONum, String Comp, Date oriDate, TableView table, ObservableList<POout> ObserveList, TableColumn col_sn, TableColumn col_SKU,
            TableColumn col_UPC, TableColumn col_prodName){
        int count =1;
        try{
            DatabaseConnection con = new DatabaseConnection();Connection connectDB = con.getConnection();
            ResultSet rs = connectDB.createStatement().executeQuery("SELECT upc, prod_name, sku_scanned FROM pickingList_detail WHERE DONum = '" + DONum + "';");
            while (rs.next()) {
                ObserveList.add(new POout(count, rs.getString("upc"), rs.getString("prod_name"), rs.getString("sku_scanned")));
                System.out.println(rs.getString("upc"));
                System.out.println(rs.getString("prod_name"));
                System.out.println(rs.getString("sku_scanned"));
                count++;
            }
        }catch(SQLException ex){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        col_sn.setCellValueFactory((new PropertyValueFactory<>("Sn")));
        col_UPC.setCellValueFactory((new PropertyValueFactory<>("Upc")));
        col_prodName.setCellValueFactory((new PropertyValueFactory<>("Prod_name")));
        col_SKU.setCellValueFactory((new PropertyValueFactory<>("Sku_scanned")));
        try {
            table.setItems(ObserveList);
        }
        catch (NullPointerException ex ){
            Logger.getLogger(User.class.getName()).log(Level.SEVERE,null,ex);
        }
        
        return ObserveList;
    }
}

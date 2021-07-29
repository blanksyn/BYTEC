package Files;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Navigation {

    private double xOffset = 0;
    private double yOffset = 0;

    void stageSetup (ActionEvent event,Parent root){
        try{
            Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1200, 700);
            root.setOnMousePressed((MouseEvent event1) -> {
                xOffset = event1.getSceneX();
                yOffset = event1.getSceneY();
            });

            root.setOnMouseDragged((MouseEvent event1) -> {
                loginStage.setX(event1.getScreenX() - xOffset);
                loginStage.setY(event1.getScreenY() - yOffset);
            });
            loginStage.setScene(scene);
            loginStage.centerOnScreen();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    void SP_purchaseOrderOut(ActionEvent event,String Username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP.fxml"));
            Parent root = loader.load();
            SP_Controller controllersp = loader.getController();
            controllersp.welcomeMsg(Username);

            stageSetup(event,root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void SP_productManagement(ActionEvent event,String Username){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_productMgt_ML.fxml"));
            Parent root = loader.load();
            SP_productMgt_ML_Controller controllersp = loader.getController();
            controllersp.welcomeMsg(Username);

            try{
                Stage loginStage = (Stage)((Node)event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1500, 700);
                root.setOnMousePressed((MouseEvent event1) -> {
                    xOffset = event1.getSceneX();
                    yOffset = event1.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event1) -> {
                    loginStage.setX(event1.getScreenX() - xOffset);
                    loginStage.setY(event1.getScreenY() - yOffset);
                });
                loginStage.setScene(scene);
                loginStage.centerOnScreen();

            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void SP_purchaseOrderIn(ActionEvent event,String Username){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_POIN_PO.fxml"));
            Parent root = loader.load();
            SP_POIN_PO_Controller controllersp = loader.getController();
            controllersp.welcomeMsg(Username);

            stageSetup(event,root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void SP_deliveryOrderOut(ActionEvent event,String Username){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SP_DOOUT.fxml"));
            Parent root = loader.load();
            SP_DOOUT_Controller controllersp = loader.getController();
            controllersp.welcomeMsg(Username);

            stageSetup(event,root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void PP_home(ActionEvent event,String Username){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PP.fxml"));
            Parent root = loader.load();
            PP_Controller controllerpp = loader.getController();
            controllerpp.welcomeMsg(Username);

            stageSetup(event,root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void PP_view(ActionEvent event,String Username,String PONum,String comp){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PP_View.fxml"));
            Parent root = loader.load();
            PP_view_Controller controllerpp = loader.getController();
            controllerpp.initialize(Username,PONum,comp);

            stageSetup(event,root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void RC_purchaseOrderIn(ActionEvent event,String Username){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RC.fxml"));
            Parent root = loader.load();
            RC_Controller controllerrc = loader.getController();
            controllerrc.welcomeMsg(Username);

            stageSetup(event,root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void RC_ApproveRcvList(ActionEvent event,String Username){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RC_AppRcvList.fxml"));
            Parent root = loader.load();
            RC_AppRcvList_Controller controllerrc = loader.getController();
            controllerrc.welcomeMsg(Username);

            stageSetup(event,root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

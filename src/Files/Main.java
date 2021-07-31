package Files;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Login");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(scene);
        Image image = new Image("image/bytec_bg_cropped.png");
        primaryStage.getIcons().add(image);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.centerOnScreen();
        root.setOnMousePressed((MouseEvent event1) -> {
            xOffset = event1.getSceneX();
            yOffset = event1.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event1) -> {
            primaryStage.setX(event1.getScreenX() - xOffset);
            primaryStage.setY(event1.getScreenY() - yOffset);
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

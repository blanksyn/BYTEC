package Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class WM_WHEnv_productMgt_ML_detail_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomLabel;

    @FXML
    private Label upc;

    @FXML
    private Label name;

    @FXML
    private TableView<?> tableVProduct;

    @FXML
    private TableColumn<?, ?> col_sn;

    @FXML
    private TableColumn<?, ?> col_sku;

    @FXML
    private TableColumn<?, ?> col_location;

    @FXML
    private TableColumn<?, ?> col_expDate;

    @FXML
    private TableColumn<?, ?> col_dateAdd;

    @FXML
    private TableColumn<?, ?> col_action;

    @FXML
    void closeWindow(ActionEvent event) {

    }

}

package Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;

public class WM_POIN_restock_view_Controller {

    @FXML
    private Button closeBtn;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableColumn<?, ?> col_sn;

    @FXML
    private TableColumn<?, ?> col_upc;

    @FXML
    private TableColumn<?, ?> col_productName;

    @FXML
    private TableColumn<?, ?> col_qty;

    @FXML
    private TextField TF_keyword;

    @FXML
    private Button searchBtn;

    @FXML
    private ComboBox<?> CB_field;

    @FXML
    private Button approveBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    void approveOrder(ActionEvent event) {

    }

    @FXML
    void cancelOrder(ActionEvent event) {

    }

    @FXML
    void closeWindow(ActionEvent event) {

    }

    @FXML
    void searchFunction(ActionEvent event) {

    }

}

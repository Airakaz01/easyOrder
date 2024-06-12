package gdc.easyorder;

import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{


    @FXML
    private BorderPane mainPane;


    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private MenuItem bonsLivMenuI1;

    @FXML
    private MenuItem bonsLivMenuI2;

    @FXML
    private JFXButton clientBtn1;

    @FXML
    private JFXButton clientBtn2;

    @FXML
    private JFXButton cmdBtn1;

    @FXML
    private JFXButton cmdBtn2;

    @FXML
    private JFXButton factureBtn1;

    @FXML
    private JFXButton factureBtn2;

    @FXML
    private JFXButton homeBtn1;

    @FXML
    private JFXButton homeBtn2;

    @FXML
    private MenuItem listCmdMenuI1;

    @FXML
    private MenuItem listCmdMenuI2;

    @FXML
    private MenuItem listFacMenuI1;

    @FXML
    private MenuItem listFacMenuI2;

    @FXML
    private MenuItem listProdMenuI1;

    @FXML
    private MenuItem listProdMenuI2;

    @FXML
    private JFXButton livraisonBtn1;

    @FXML
    private JFXButton livraisonBtn2;

    @FXML
    private JFXButton produitBtn1;

    @FXML
    private JFXButton produitBtn2;

    @FXML
    private JFXButton settingsBtn1;

    @FXML
    private JFXButton settingsBtn2;

    @FXML
    private AnchorPane slider;
    @FXML
    private AnchorPane center;
    @FXML
    private ImageView Exit;
    @FXML
    private ImageView Reduce;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main obj = new Main();
        Pane view  = obj.getPage("dashboard");
        mainPane.setCenter(view);
        Menu.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), slider);
            TranslateTransition milieu = new TranslateTransition(Duration.seconds(0.4), center);

            slide.setToX(0);
            milieu.setToX(0);
            slide.play();
            milieu.play();

            slide.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuBack.setVisible(true);
            });
        });

        MenuBack.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.4), slider);
            TranslateTransition milieu = new TranslateTransition(Duration.seconds(0.4), center);

            slide.setToX(-200);
            milieu.setToX(-200);
            slide.play();
            milieu.play();

            slide.setOnFinished((ActionEvent e) -> {
                MenuBack.setVisible(false);
                Menu.setVisible(true);
            });
        });
        setupButtonEvents();

    }
    private void setupButtonEvents() {
        Exit.setOnMouseClicked(event -> System.exit(0));
        Reduce.setOnMouseClicked(event -> reduceWindow());
    }
    private void reduceWindow() {
        Stage stage = (Stage) Reduce.getScene().getWindow();
        stage.setIconified(true);
    }
    @FXML
    void showBonsLIv(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("getBonsLiv");
        mainPane.setCenter(view);
    }

    @FXML
    void showClient(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("client");
        mainPane.setCenter(view);
    }

    @FXML
    void showCommande(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("commande");
        mainPane.setCenter(view);

    }

    @FXML
    void showFacture(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("facture");
        mainPane.setCenter(view);
    }

    @FXML
    void showHome(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("dashboard");
        mainPane.setCenter(view);
    }

    @FXML
    void showListFac(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("getListFac");
        mainPane.setCenter(view);
    }

    @FXML
    void showListProd(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("getListProd");
        mainPane.setCenter(view);
    }

    @FXML
    void showLivraison(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("livraison");
        mainPane.setCenter(view);
    }


    @FXML
    void addAdmin(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("adminAdd");
        mainPane.setCenter(view);

    }

    @FXML
    void editAdmin(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("adminEdit");
        mainPane.setCenter(view);
    }

    @FXML
    void showProduit(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("Produit");
        mainPane.setCenter(view);
    }

    @FXML
    void showlistCmd(ActionEvent event) {
        Main obj = new Main();
        Pane view  = obj.getPage("getListCmd");
        mainPane.setCenter(view);
    }

}


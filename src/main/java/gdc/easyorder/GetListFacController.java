package gdc.easyorder;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class GetListFacController implements Initializable {
    Connection con =null;
    PreparedStatement st=null;
    ResultSet rs = null;
    int statut=0;

    @FXML
    private TableColumn<Facture, Date> DateFacC;

    @FXML
    private Button clearFieldsBtn;

    @FXML
    private TableColumn<Facture, Integer> idFacC;

    @FXML
    private TableColumn<Facture, Double> montantFacC;

    @FXML
    private ComboBox<Integer> numClientCombo;

    @FXML
    private ComboBox<Integer> numCmdCombo;

    @FXML
    private TableColumn<Facture, Integer> numCmdFacC;

    @FXML
    private ComboBox<Integer> numFacCombo;

    @FXML
    private Button searchBtn;

    @FXML
    private TableView<Facture> tableFac;

    @FXML
    private ComboBox<String> typeDeRecherche;
    private ObservableList<Integer> items = FXCollections.observableArrayList();
    private ObservableList<String> searchType = FXCollections.observableArrayList();
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String query = "SELECT id_client FROM client";
        con = ConnectionToDB.getConnexion();
        try{
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                items.add(rs.getInt("id_client"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        numClientCombo.setItems(items);
        MyGetObj obj = new MyGetObj();
        List<Integer> idCommandes = obj.getAllIdCommandes();
        List<Integer> idFactures = obj.getAllIdFactures();
        Collections.sort(idCommandes);
        Collections.sort(idFactures);
        numCmdCombo.setItems(FXCollections.observableArrayList(idCommandes));
        numFacCombo.setItems(FXCollections.observableArrayList(idFactures));
        searchType.add("Rechercher par numéro de facture");
        searchType.add("Rechercher par numéro de commande");
        searchType.add("Rechercher par numéro de client");
        typeDeRecherche.setItems(searchType);
        typeDeRecherche.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Rechercher par numéro de facture".equals(newValue)) {
                numFacCombo.setDisable(false);
                numCmdCombo.setDisable(true);
                numClientCombo.setDisable(true);
                statut =1;
            } else if ("Rechercher par numéro de commande".equals(newValue)) {
                numFacCombo.setDisable(true);
                numCmdCombo.setDisable(false);
                numClientCombo.setDisable(true);
                statut = 2;
            }else if ("Rechercher par numéro de client".equals(newValue)) {
            numFacCombo.setDisable(true);
            numCmdCombo.setDisable(true);
            numClientCombo.setDisable(false);
            statut = 3;
        }
        });
    }
    public void clear() {
        numClientCombo.getSelectionModel().select(-1);
        numCmdCombo.getSelectionModel().select(-1);
        numFacCombo.getSelectionModel().select(-1);
    }
    @FXML
    void clearFields(ActionEvent event) {clear();}
    public void showFactures(ObservableList<Facture> list){
        tableFac.setItems(list);
        idFacC.setCellValueFactory(new PropertyValueFactory<Facture,Integer>("numero"));
        DateFacC.setCellValueFactory(new PropertyValueFactory<Facture,Date>("datefac"));
        montantFacC.setCellValueFactory(new PropertyValueFactory<Facture,Double>("montant"));
        numCmdFacC.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCommande().getNumero()).asObject());
    }

    @FXML
    void searchListFac(ActionEvent event) {
        MyGetObj obj = new MyGetObj();
        if (statut==1) {
            int idFac = numFacCombo.getValue();
            ObservableList<Facture> list = obj.getFactureById(idFac);
            showFactures(list);
        } else if (statut==2) {
            int idCmd = numCmdCombo.getValue();
            ObservableList<Facture> list = obj.getFactureByIdCmd(idCmd);
            showFactures(list);
        } else if(statut==3){
            int idClient = numClientCombo.getValue();
            ObservableList<Facture> list=obj.getFactureByIdClient(idClient);
            showFactures(list);
    }
    }

}

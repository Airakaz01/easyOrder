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

public class GetBonsLivController implements Initializable {
    Connection con =null;
    PreparedStatement st=null;
    ResultSet rs = null;
    int statut=0;
    @FXML
    private TableColumn<Livraison, Date> DateLivC;

    @FXML
    private Button clearFieldsBtn;

    @FXML
    private TableColumn<Livraison, Integer> idLivC;

    @FXML
    private ComboBox<Integer> numClientCombo;

    @FXML
    private ComboBox<Integer> numCmdCombo;

    @FXML
    private TableColumn<Livraison, Integer> numCmdLivC;

    @FXML
    private ComboBox<Integer> numLivCombo;

    @FXML
    private Button searchBtn;

    @FXML
    private TableView<Livraison> tableLiv;

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
        List<Integer> idLivraisons = obj.getAllIdFactures();
        Collections.sort(idCommandes);
        Collections.sort(idLivraisons);
        numCmdCombo.setItems(FXCollections.observableArrayList(idCommandes));
        numLivCombo.setItems(FXCollections.observableArrayList(idLivraisons));
        searchType.add("Rechercher par numéro de livraison");
        searchType.add("Rechercher par numéro de commande");
        searchType.add("Rechercher par numéro de client");
        typeDeRecherche.setItems(searchType);
        typeDeRecherche.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Rechercher par numéro de livraison".equals(newValue)) {
                numLivCombo.setDisable(false);
                numCmdCombo.setDisable(true);
                numClientCombo.setDisable(true);
                statut =1;
            } else if ("Rechercher par numéro de commande".equals(newValue)) {
                numLivCombo.setDisable(true);
                numCmdCombo.setDisable(false);
                numClientCombo.setDisable(true);
                statut = 2;
            }else if ("Rechercher par numéro de client".equals(newValue)) {
                numLivCombo.setDisable(true);
                numCmdCombo.setDisable(true);
                numClientCombo.setDisable(false);
                statut = 3;
            }
        });
    }
    public void clear() {
        numClientCombo.getSelectionModel().select(-1);
        numCmdCombo.getSelectionModel().select(-1);
        numLivCombo.getSelectionModel().select(-1);
    }
    public void showLivraisons(ObservableList<Livraison> list){
        tableLiv.setItems(list);
        idLivC.setCellValueFactory(new PropertyValueFactory<Livraison,Integer>("numero"));
        DateLivC.setCellValueFactory(new PropertyValueFactory<Livraison,Date>("dateliv"));
        numCmdLivC.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCommande().getNumero()).asObject());
    }
    @FXML
    void clearFields(ActionEvent event) {clear();}
    @FXML
    void searchBonsLiv(ActionEvent event) {
        MyGetObj obj = new MyGetObj();
        if (statut==1) {
            int idLiv = numLivCombo.getValue();
            ObservableList<Livraison> list = obj.getLivraisonById(idLiv);
            showLivraisons(list);
        } else if (statut==2) {
            int idCmd = numCmdCombo.getValue();
            ObservableList<Livraison> list = obj.getLivraisonById(idCmd);
            showLivraisons(list);
        } else if(statut==3){
            int idClient = numClientCombo.getValue();
            ObservableList<Livraison> list= obj.getLivraisonByIdClient(idClient);
            showLivraisons(list);
        }
    }

    }


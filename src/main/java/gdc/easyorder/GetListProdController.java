package gdc.easyorder;

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
import java.util.List;
import java.util.ResourceBundle;

public class GetListProdController implements Initializable {
    Connection con =null;
    PreparedStatement st=null;
    ResultSet rs = null;
    int statut=0;
    @FXML
    private TableColumn<Produit, String> NomProdC;
    @FXML
    private TableColumn<Produit, Double> PrixProdC;
    @FXML
    private Button clearFieldsBtn;
    @FXML
    private TableColumn<Produit, Integer> idProdC;
    @FXML
    private ComboBox<Integer> numClientCombo;
    @FXML
    private ComboBox<Integer> numCmdCombo;
    @FXML
    private TableColumn<Produit, Integer> quantiteProdC;
    @FXML
    private Button searchBtn;
    @FXML
    private TableView<Produit> tableProd;
    @FXML
    private ComboBox<String> typeDeRecherche;
    private ObservableList<Integer> items = FXCollections.observableArrayList();
    private ObservableList<String> searchType = FXCollections.observableArrayList();
    @Override
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
        Collections.sort(idCommandes);
        numCmdCombo.setItems(FXCollections.observableArrayList(idCommandes));
        searchType.add("Rechercher par numéro de commande");
        searchType.add("Rechercher par numéro de client");
        typeDeRecherche.setItems(searchType);
        typeDeRecherche.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Rechercher par numéro de commande".equals(newValue)) {
                numClientCombo.setDisable(true);
                numCmdCombo.setDisable(false);
                statut =1;
            } else if ("Rechercher par numéro de client".equals(newValue)) {
                numCmdCombo.setDisable(true);
                numClientCombo.setDisable(false);
                statut = 2;
            }
        });
    }
    public void clear() {
        numClientCombo.getSelectionModel().select(-1);
        numCmdCombo.getSelectionModel().select(-1);
    }
    @FXML
    void clearFields(ActionEvent event) {clear();}
    public void showProduis(ObservableList<Produit> list){
        tableProd.setItems(list);
        idProdC.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("numero"));
        NomProdC.setCellValueFactory(new PropertyValueFactory<Produit,String>("nom"));
        quantiteProdC.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("quantite"));
        PrixProdC.setCellValueFactory(new PropertyValueFactory<Produit,Double>("prix"));
    }
    public ObservableList<Produit> getProduitsByClientId(int id){
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        GetCommandeController cmdCont = new GetCommandeController();
        ObservableList<Commande> commandes = cmdCont.getCommandesByIdClient(id);
        for(Commande cmd:commandes){
            produits.addAll(cmd.getProduits());
        }
        return produits;
    }
    public ObservableList<Produit> getProduitsByCommandesId(int id){
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        MyGetObj obj = new MyGetObj();
        List<Produit> listidProd = obj.getListProduits(id);
        produits.addAll(listidProd);
        return produits;
    }
    @FXML
    void searchListProd(ActionEvent event) {
        if (statut==1) {
            int idCmd = numCmdCombo.getValue();
            ObservableList<Produit> list = getProduitsByCommandesId(idCmd);
            showProduis(list);

        } else if (statut==2) {
            int idClient = numClientCombo.getValue();
            ObservableList<Produit> list = getProduitsByClientId(idClient);
            showProduis(list);
        }
    }
}

package gdc.easyorder;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class GetCommandeController implements Initializable {
    Connection con =null;
    PreparedStatement st=null;
    ResultSet rs = null;
    int statut=0;

    @FXML
    private TableColumn<Commande, String> ListeProdCmdC;

    @FXML
    private Button clearFieldsBtn;

    @FXML
    private TableColumn<Commande, Date> dateCmdC;

    @FXML
    private TableColumn<Commande, Integer> idCmdC;

    @FXML
    private TextField nomClientTF;

    @FXML
    private ComboBox<Integer> numClientCmd;

    @FXML
    private TableColumn<Commande, Integer> numClientCmdC;

    @FXML
    private TextField prenomClientTF;

    @FXML
    private Button searchBtn;

    @FXML
    private TableView<Commande> tableCmd;

    @FXML
    private ComboBox<String> typeDeRecherche;
    private ObservableList<Integer> items = FXCollections.observableArrayList();
    private ObservableList<String> searchType = FXCollections.observableArrayList();
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        showCommandes();
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
        numClientCmd.setItems(items);
        searchType.add("Numéro");
        searchType.add("Nom");
        searchType.add("Prénom");
        typeDeRecherche.setItems(searchType);
        typeDeRecherche.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Numéro".equals(newValue)) {
                numClientCmd.setDisable(false);
                nomClientTF.setDisable(true);
                prenomClientTF.setDisable(true);
                statut =1;
            } else if ("Nom".equals(newValue)) {
                nomClientTF.setDisable(false);
                prenomClientTF.setDisable(true);
                numClientCmd.setDisable(true);
                statut=2;
            } else if ("Prénom".equals(newValue)) {
                nomClientTF.setDisable(true);
                prenomClientTF.setDisable(false);
                numClientCmd.setDisable(true);
                statut=3;
            }
        });
    }
    @FXML
    public void clear() {
        nomClientTF.setText("");
        prenomClientTF.setText("");
        numClientCmd.getSelectionModel().select(-1);
    }
    @FXML
    void clearFields(ActionEvent event) {clear();}
    public ObservableList<Commande> getCommandesByIdClient(int idclient){
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        String query = "SELECT * FROM commande WHERE id_client=?";
        MyGetObj obj = new MyGetObj();
        con  = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,idclient);
            rs = st.executeQuery();
            while (rs.next()){
                Commande cmd = new Commande();
                int idCmd = rs.getInt("id_commande");
                cmd.setNumero(idCmd);
                cmd.setDateCmd(rs.getDate("date_commande"));
                int idClient = rs.getInt("id_client");
                cmd.setClient(obj.getClient(idClient));
                cmd.addProduits(obj.getListProduits(idCmd));
                commandes.add(cmd);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return commandes;
    }
    public ObservableList<Commande> getCommandesByClientName(String nameClient){
        MyGetObj obj = new MyGetObj();
        List<Integer> idliste = obj.getClientByName(nameClient);
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        for(int idclient :idliste){
            List<Commande> cmdList = getCommandesByIdClient(idclient);
            commandes.addAll(cmdList);
        }
        return commandes;
    }
    public ObservableList<Commande> getCommandesByClentFname(String Fname){
        MyGetObj obj = new MyGetObj();
        List<Integer> idliste = obj.getClientByFname(Fname);
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        for(int idclient :idliste){
            List<Commande> cmdList = getCommandesByIdClient(idclient);
            commandes.addAll(cmdList);
        }
        return commandes;
    }
    public void showCommandes(ObservableList<Commande> list){
        tableCmd.setItems(list);
        idCmdC.setCellValueFactory(new PropertyValueFactory<Commande,Integer>("numero"));
        dateCmdC.setCellValueFactory(new PropertyValueFactory<Commande,Date>("dateCmd"));
        numClientCmdC.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getClient().getNumero()).asObject());
        ListeProdCmdC.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduitsString()));
    }
    @FXML
    void searchCmdOfClient(ActionEvent event) {
            if (statut==1) {
                int idClient = numClientCmd.getValue();
                ObservableList<Commande> list = getCommandesByIdClient(idClient);
                showCommandes(list);

            } else if (statut==2) {
                String name = nomClientTF.getText();
                ObservableList<Commande> list = getCommandesByClientName(name);
                showCommandes(list);

            } else if (statut==3) {
                String Fname = prenomClientTF.getText();
                ObservableList<Commande> list = getCommandesByClientName(Fname);
                showCommandes(list);
            }
    }
}

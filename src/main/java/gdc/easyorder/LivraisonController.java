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
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class LivraisonController implements Initializable {
    Connection con =null;
    PreparedStatement st=null;
    ResultSet rs = null;
    int id=0;
    int idProdChoisi = 0;
    int idCmdG = 0;
    @FXML
    private Button addLivBtn;

    @FXML
    private Button clearFieldsBtn;

    @FXML
    private DatePicker dateLiv;

    @FXML
    private TableColumn<Livraison, Date> dateLivC;

    @FXML
    private Button deleteLivBtn;

    @FXML
    private Button editLivBtn;
    @FXML
    private Button updateBtn;

    @FXML
    private TableColumn<Livraison, Integer> idLivC;

    @FXML
    private TableColumn<Livraison, Integer> numCmdLivC;

    @FXML
    private ComboBox<Integer> numComLiv;

    @FXML
    private TableView<Livraison> tableLiv;
    @FXML
    private Button LivrerProdBtn;

    @FXML
    private TableColumn<ProduitChoisi, Integer> QttProdC;



    @FXML
    private TableColumn<ProduitChoisi, String> libelleProdC;



    @FXML
    private TableColumn<ProduitChoisi, Integer> numProdC;



    @FXML
    private TableView<ProduitChoisi> tableProdQtt;
    private ObservableList<Integer> items = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showLivraisons();
        String query = "SELECT id_commande FROM commande";
        con = ConnectionToDB.getConnexion();

        try{
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                items.add(rs.getInt("id_commande"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        numComLiv.setItems(items);
    }
    public  ObservableList<ProduitChoisi> getProduitChoisi(){
        ObservableList<ProduitChoisi> produitChoisis = FXCollections.observableArrayList();
        MyGetObj obj = new MyGetObj();
        produitChoisis = obj.getProduitNonLivByIdCmd(idCmdG);
        return produitChoisis;
    }
    public void  showProduitsChoisis(){
        ObservableList<ProduitChoisi> list = getProduitChoisi();
        tableProdQtt.setItems(list);
        numProdC.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getProduit().getNumero()).asObject());
        libelleProdC.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduit().getNom()));
        QttProdC.setCellValueFactory(new PropertyValueFactory<ProduitChoisi,Integer>("quantiteCommande"));
    }
    @FXML
    public void showProdNonLiv(ActionEvent event){
        showProduitsChoisis();
        String query = "INSERT INTO livraison (date_livraison,id_commande) values(?,?)";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setDate(1, java.sql.Date.valueOf(dateLiv.getValue()));
            st.setInt(2,numComLiv.getValue());
            st.executeUpdate();
            showLivraisons();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ObservableList<Livraison> getLivraisons(){
        ObservableList<Livraison> livraisons = FXCollections.observableArrayList();
        String query = "SELECT * FROM livraison";
        MyGetObj obj = new MyGetObj();
        con  = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()){
                Livraison l = new Livraison();
                l.setNumero(rs.getInt("id_livraison"));
                l.setDateliv(rs.getDate("date_livraison"));
                int idCmd = rs.getInt("id_commande");
                l.setCommande(obj.getCommande(idCmd));
                livraisons.add(l);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return livraisons;
    }
    public void showLivraisons(){
        ObservableList<Livraison> list = getLivraisons();
        tableLiv.setItems(list);
        idLivC.setCellValueFactory(new PropertyValueFactory<Livraison,Integer>("numero"));
        numCmdLivC.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCommande().getNumero()).asObject());
        dateLivC.setCellValueFactory(new PropertyValueFactory<Livraison,Date>("dateliv"));
    }
    @FXML
    void getData(MouseEvent event) {
        Livraison livraison = tableLiv.getSelectionModel().getSelectedItem();
        if (livraison != null) {
            id = livraison.getNumero();
            idCmdG  =livraison.getCommande().getNumero();
            showProduitsChoisis();

            addLivBtn.setDisable(true);
        }
    }
    @FXML
    void getProduitData(MouseEvent event) {
        ProduitChoisi produitChoisi = tableProdQtt.getSelectionModel().getSelectedItem();
        if (produitChoisi != null){
            idProdChoisi = produitChoisi.getProduit().getNumero();
            System.out.println("from getProduitData  :"+idProdChoisi );
//            setDisableCmd();

        }
    }
    void clear(){
        addLivBtn.setDisable(false);
        numComLiv.getSelectionModel().select(-1);
        dateLiv.setValue(null);

    }
    @FXML
    public void FinalUpdate(ActionEvent event){

    }
    @FXML
    void addLiv(ActionEvent event) {
        String query = "INSERT INTO livraison (date_livraison,id_commande) values(?,?)";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setDate(1, java.sql.Date.valueOf(dateLiv.getValue()));
            st.setInt(2,numComLiv.getValue());
            st.executeUpdate();
            showLivraisons();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {clear();}
    @FXML
    void deleteLiv(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Attention !!");
        alert.setHeaderText(null);
        alert.setContentText("Vous voulez supprimer cette livraison ?");

        ButtonType yesButton = new ButtonType("Oui");
        ButtonType noButton = new ButtonType("Annuler");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            String query = "DELETE FROM livraison WHERE id_livraison=?";
            con = ConnectionToDB.getConnexion();
            try {
                st=con.prepareStatement(query);
                st.setInt(1,id);
                st.executeUpdate();
                showLivraisons();
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    void editLiv(ActionEvent event) {
        String query = "UPDATE livraison SET date_livraison = ? , id_commande=? WHERE id_livraison=?";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setDate(1, java.sql.Date.valueOf(dateLiv.getValue()));
            st.setInt(2,numComLiv.getValue());
            st.setInt(3,id);
            st.executeUpdate();
            showLivraisons();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void LivrerProd(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Attention !!");
        alert.setHeaderText(null);
        alert.setContentText("Vous voulez livrer ce produit [num Liv : "+id+" , num Cmd : "+idCmdG+" ] ?");

        ButtonType yesButton = new ButtonType("Oui");
        ButtonType noButton = new ButtonType("Annuler");
        alert.getButtonTypes().setAll(yesButton, noButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            String query = "UPDATE commande_produit SET livre='oui', id_livraison=? WHERE id_commande=? AND id_produit=?";
            con = ConnectionToDB.getConnexion();
            try {
                st = con.prepareStatement(query);
                st.setInt(1,id);
                st.setInt(2,idCmdG);
                st.setInt(3,idProdChoisi);
                st.executeUpdate();
                showProduitsChoisis();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }




}






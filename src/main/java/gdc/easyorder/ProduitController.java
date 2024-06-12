package gdc.easyorder;

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
import java.util.Optional;
import java.util.ResourceBundle;

public class ProduitController implements Initializable {

    Connection con =null;
    PreparedStatement st=null;
    ResultSet rs = null;
    int id=0;
    @FXML
    private Button addProdBtn;

    @FXML
    private Button clearFieldsBtn;

    @FXML
    private Button deleteProdBtn;

    @FXML
    private Button editProdBtn;

    @FXML
    private TableColumn<Produit, Integer> idProdC;

    @FXML
    private TextField nomProd;

    @FXML
    private TableColumn<Produit, String> nomProdC;

    @FXML
    private TextField prixProd;

    @FXML
    private TableColumn<Produit, Double> prixProdC;

    @FXML
    private TextField quantiteProd;

    @FXML
    private TableColumn<Produit, Integer> quantiteProdC;

    @FXML
    private TableView<Produit> tableProd;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showProduits();
    }
    public ObservableList<Produit> getProduits(){
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        String query = "SELECT * FROM produit";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()){
                Produit p = new Produit();
                p.setNumero(rs.getInt("id_produit"));
                p.setNom(rs.getString("nom_produit"));
                p.setQuatite(rs.getInt("quantite"));
                p.setPrix(rs.getDouble("prix"));
                produits.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produits;
    }
    public void showProduits(){
        ObservableList<Produit> list = getProduits();
        tableProd.setItems(list);
        idProdC.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("numero"));
        nomProdC.setCellValueFactory(new PropertyValueFactory<Produit,String>("nom"));
        quantiteProdC.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("quantite"));
        prixProdC.setCellValueFactory(new PropertyValueFactory<Produit,Double>("prix"));
    }
    @FXML
    void getData(MouseEvent event) {
        Produit produit =  tableProd.getSelectionModel().getSelectedItem();
        id = produit.getNumero();
        nomProd.setText(produit.getNom());
        quantiteProd.setText(String.valueOf(produit.getQuantite()));
        prixProd.setText(String.valueOf(produit.getPrix()));
        addProdBtn.setDisable(true);
    }

    void clear(){
        nomProd.setText(null);
        quantiteProd.setText(null);
        prixProd.setText(null);
        addProdBtn.setDisable(false);

    }
    @FXML
    void addProd(ActionEvent event) {
        String query = "INSERT INTO produit (nom_produit,quantite,prix) values(?,?,?)";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setString(1,nomProd.getText());
            st.setString(2,quantiteProd.getText());
            st.setString(3,prixProd.getText());
            st.executeUpdate();
            showProduits();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {clear();}

    @FXML
    void deleteProd(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Attention !!");
        alert.setHeaderText(null);
        alert.setContentText("Vous voulez supprimer ce produit ?");

        ButtonType yesButton = new ButtonType("Oui");
        ButtonType noButton = new ButtonType("Annuler");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            String query = "DELETE FROM produit WHERE id_produit=?";
            con = ConnectionToDB.getConnexion();
            try {
                st = con.prepareStatement(query);
                st.setInt(1,id);
                st.executeUpdate();
                showProduits();
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    void editProd(ActionEvent event) {
        String query = "UPDATE produit SET nom_produit = ?, quantite = ?, prix = ? WHERE id_produit=?";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setString(1,nomProd.getText());
            st.setString(2,quantiteProd.getText());
            st.setString(3,prixProd.getText());
            st.setInt(4,id);
            st.executeUpdate();
            showProduits();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }




}
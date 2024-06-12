package gdc.easyorder;

import javafx.beans.property.SimpleIntegerProperty;
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

public class FactureController implements Initializable {
    Connection con =null;
    PreparedStatement st=null;
    ResultSet rs = null;
    int id=0;
    @FXML
    private Button addFacBtn;

    @FXML
    private Button clearFieldsBtn;

    @FXML
    private DatePicker dateFac;

    @FXML
    private TableColumn<Facture, Date> dateFacC;

    @FXML
    private Button deleteFacBtn;

    @FXML
    private Button editFacBtn;

    @FXML
    private TableColumn<Facture, Integer> idFacC;
    @FXML
    private TableColumn<Facture, Double> montantFacC;
    @FXML
    private TableColumn<Facture, Integer> numCmdFactC;
    @FXML
    private ComboBox<Integer> numComFac;
    @FXML
    private TableView<Facture> tableFac;
    private ObservableList<Integer> items = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showFactures();
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
        numComFac.setItems(items);
    }
    public ObservableList<Facture> getFactures(){
        ObservableList<Facture> factures = FXCollections.observableArrayList();
        String query = "SELECT * FROM facture";
        MyGetObj obj = new MyGetObj();
        con  = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()){
                Facture f = new Facture();
                f.setNumero(rs.getInt("id_facture"));
                f.setDatefac(rs.getDate("date_facture"));
                f.setMontant(rs.getDouble("montant"));
                int idCmd = rs.getInt("id_commande");
                f.setCommande(obj.getCommande(idCmd));
                factures.add(f);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return factures;
    }
    public void showFactures(){
        ObservableList<Facture> list = getFactures();
        tableFac.setItems(list);
        idFacC.setCellValueFactory(new PropertyValueFactory<Facture,Integer>("numero"));
        numCmdFactC.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getCommande().getNumero()).asObject());
        montantFacC.setCellValueFactory(new PropertyValueFactory<Facture,Double>("montant"));
        dateFacC.setCellValueFactory(new PropertyValueFactory<Facture, Date>("datefac"));
    }
    @FXML
    void getData(MouseEvent event) {
        Facture facture = tableFac.getSelectionModel().getSelectedItem();
        if (facture != null) {
            id = facture.getNumero();
            addFacBtn.setDisable(true);
        }
    }
    void clear(){ addFacBtn.setDisable(false);}
    @FXML
    void addFac(ActionEvent event) {
        String query = "INSERT INTO facture (date_facture,montant,id_commande) values(?,?,?)";
        con = ConnectionToDB.getConnexion();
        MyGetObj obj = new MyGetObj();
        try {
            st = con.prepareStatement(query);
            int idCmd = numComFac.getValue();
            st.setDate(1, java.sql.Date.valueOf(dateFac.getValue()));
            st.setDouble(2,obj.calMontant(idCmd));
            st.setInt(3,idCmd);
            st.executeUpdate();
            showFactures();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void clearFields(ActionEvent event) {clear();}
    @FXML
    void deleteFac(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Attention !!");
        alert.setHeaderText(null);
        alert.setContentText("Vous voulez supprimer cette facture ?");

        ButtonType yesButton = new ButtonType("Oui");
        ButtonType noButton = new ButtonType("Annuler");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            String query = "DELETE FROM facture WHERE id_facture=?";
            con = ConnectionToDB.getConnexion();
            try {
                st=con.prepareStatement(query);
                st.setInt(1,id);
                st.executeUpdate();
                showFactures();
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @FXML
    void editFac(ActionEvent event) {
        String query = "UPDATE facture SET date_facture = ? ,montant=? , id_commande=? WHERE id_facture=?";
        con = ConnectionToDB.getConnexion();
        MyGetObj obj = new MyGetObj();
        try {
            st = con.prepareStatement(query);
            int idCmd = numComFac.getValue();
            st.setDate(1, java.sql.Date.valueOf(dateFac.getValue()));
            st.setDouble(2,obj.calMontant(idCmd));
            st.setInt(3,idCmd);
            st.setInt(4,id);
            st.executeUpdate();
            showFactures();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

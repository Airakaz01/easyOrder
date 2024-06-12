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

public class ClientController implements Initializable {
    Connection con =null;
    PreparedStatement st=null;
    ResultSet rs = null;

    @FXML
    private Button addClientBtn;

    @FXML
    private TextField adresseClient;

    @FXML
    private TableColumn<Client, String> adresseClientC;

    @FXML
    private Button clearFieldsBtn;

    @FXML
    private Button deleteClientBtn;

    @FXML
    private Button editClientBtn;

    @FXML
    private TableColumn<Client, Integer> idClientC;

    @FXML
    private TextField nomClient;

    @FXML
    private TableColumn<Client, String> nomClientC;

    @FXML
    private TableColumn<Client, String> prenomClientC;

    @FXML
    private TextField prenomClient;

    @FXML
    private TableView<Client> tableClient;

    @FXML
    private TextField telClient;

    @FXML
    private TableColumn<Client, Integer> telClientC;
    int id=0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showClients();
    }
    public ObservableList<Client> getClients(){
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String query = "SELECT * FROM client";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()){
                Client c = new Client();
                c.setNumero(rs.getInt("id_client"));
                c.setNom(rs.getString("nom_client"));
                c.setPrenom(rs.getString("prenom_client"));
                c.setAdresse(rs.getString("adresse_client"));
                c.setTele(rs.getInt("tele_client"));
                clients.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clients;
    }
    public void showClients(){
        ObservableList<Client> list = getClients();
        tableClient.setItems(list);
        idClientC.setCellValueFactory(new PropertyValueFactory<Client,Integer>("numero"));
        nomClientC.setCellValueFactory(new PropertyValueFactory<Client,String>("nom"));
        prenomClientC.setCellValueFactory(new PropertyValueFactory<Client,String>("prenom"));
        adresseClientC.setCellValueFactory(new PropertyValueFactory<Client,String>("adresse"));
        telClientC.setCellValueFactory(new PropertyValueFactory<Client,Integer>("tele"));
    }
    @FXML
    void getData(MouseEvent event) {
        Client client = tableClient.getSelectionModel().getSelectedItem();
        id= client.getNumero();
        nomClient.setText(client.getNom());
        prenomClient.setText(client.getPrenom());
        adresseClient.setText(client.getAdresse());
        telClient.setText(String.valueOf(client.getTele()));
        addClientBtn.setDisable(true);

    }
    void clear(){
        nomClient.setText(null);
        prenomClient.setText(null);
        adresseClient.setText(null);
        telClient.setText(null);
        addClientBtn.setDisable(false);
    }
    @FXML
    void addClient(ActionEvent event) {
        String query = "INSERT INTO client (nom_client,prenom_client,adresse_client,tele_client) values(?,?,?,?)";
        con=ConnectionToDB.getConnexion();
        try {
            st= con.prepareStatement(query);
            st.setString(1,nomClient.getText());
            st.setString(2,prenomClient.getText());
            st.setString(3,adresseClient.getText());
            st.setString(4,telClient.getText());
            st.executeUpdate();
            showClients();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        clear();
    }

    @FXML
    void deleteClient(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Attention !!");
        alert.setHeaderText(null);
        alert.setContentText("Vous voulez supprimer ce client ?");

        ButtonType yesButton = new ButtonType("Oui");
        ButtonType noButton = new ButtonType("Annuler");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            String query = "DELETE FROM client WHERE id_client=?";
            con = ConnectionToDB.getConnexion();
            try {
                st=con.prepareStatement(query);
                st.setInt(1,id);
                st.executeUpdate();
                showClients();
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    void editClient(ActionEvent event) {
        String query = "UPDATE client SET nom_client = ? , prenom_client=?, adresse_client=? , tele_client=? WHERE id_client=?";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setString(1,nomClient.getText());
            st.setString(2,prenomClient.getText());
            st.setString(3,adresseClient.getText());
            st.setString(4,telClient.getText());
            st.setInt(5,id);
            st.executeUpdate();
            showClients();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}


package gdc.easyorder;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
public class CommandeContoller implements Initializable {
    Connection con =null;
    PreparedStatement st=null;
    ResultSet rs = null;
    int id=0;
    int idProdChoisi=0;
    ObservableList<ProduitChoisi> myListChoisi = null;
    @FXML
    private Button initialiserNvCmdBtn;

    @FXML
    private TableColumn<ProduitChoisi, String> libelleProdC;
    @FXML
    private TableColumn<ProduitChoisi, Integer> QttProdC;
    @FXML
    private TableColumn<ProduitChoisi, Integer> numProdC;
    @FXML
    private TableView<ProduitChoisi> tableProdQtt;
    @FXML
    private TableColumn<Commande, String> ListeProdCmdC;

    @FXML
    private Button addCmdBtn;
    @FXML
    private Button addQttBtn;
    @FXML
    private TextField QttTextF;

    @FXML
    private Button clearFieldsBtn;

    @FXML
    private DatePicker dateCmd;

    @FXML
    private TableColumn<Commande, Date> dateCmdC;

    @FXML
    private Button deleteCmdBtn;

    @FXML
    private Button editCmdBtn;

    @FXML
    private TableColumn<Commande, Integer> idCmdC;

    @FXML
    private ComboBox<Integer> numClientCmd;

    @FXML
    private TableColumn<Commande, Integer> numClientCmdC;

    @FXML
    private TableView<Commande> tableCmd;
    private ObservableList<Integer> items = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCommandes();
        showProduitsChoisis();
        setDisableProd();
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
    }

    public ObservableList<Commande> getCommandes(){
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        String query = "SELECT * FROM commande";
        MyGetObj obj = new MyGetObj();
        con  = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
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
    public  ObservableList<ProduitChoisi> getProduitChoisi(){
        ObservableList<ProduitChoisi> produitChoisis = FXCollections.observableArrayList();
        MyGetObj obj = new MyGetObj();
        String query = "SELECT * FROM produit WHERE quantite > 0";
        con  = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()){
                ProduitChoisi prodch = new ProduitChoisi();
                Produit prod = new Produit();
                int idProd = rs.getInt("id_produit");
                prod.setNumero(idProd);
                prod.setNom(rs.getString("nom_produit"));
                prod.setQuatite(rs.getInt("quantite"));
                prod.setPrix(rs.getDouble("prix"));
                prodch.setProduit(prod);
                int qtt = obj.getQttCommandeeByIDs(this.id, idProd);
                prodch.setQuantiteCommande(qtt);
                produitChoisis.add(prodch);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return  produitChoisis;
    }
    public void showCommandes(){
        ObservableList<Commande> list = getCommandes();
        tableCmd.setItems(list);
        idCmdC.setCellValueFactory(new PropertyValueFactory<Commande,Integer>("numero"));
        dateCmdC.setCellValueFactory(new PropertyValueFactory<Commande,Date>("dateCmd"));
        numClientCmdC.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getClient().getNumero()).asObject());
        ListeProdCmdC.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduitsString()));
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
    void getData(MouseEvent event) {
        Commande commande = tableCmd.getSelectionModel().getSelectedItem();
        setDisableProd();
        if(commande != null){
            id = commande.getNumero();
            System.out.println(id);
            initialiserNvCmdBtn.setDisable(true);
            showProduitsChoisis();
        }
    }
    @FXML
    void getProduitData(MouseEvent event){
        ProduitChoisi produitChoisi = tableProdQtt.getSelectionModel().getSelectedItem();
        if (produitChoisi != null){
            idProdChoisi = produitChoisi.getProduit().getNumero();
            System.out.println("from getProduitData  :"+idProdChoisi );
            setDisableCmd();

            }
    }
    public void setDisableProd(){
        if(id==0){
            boolean b =true;
            addCmdBtn.setDisable(b);
//            editCmdBtn.setDisable(b);
            tableProdQtt.setDisable(b);
            deleteCmdBtn.setDisable(b);
            addQttBtn.setDisable(b);
            QttTextF.setDisable(b);
        }
        else{
            boolean b =false;
            addCmdBtn.setDisable(b);
            editCmdBtn.setDisable(b);
            tableProdQtt.setDisable(b);
            deleteCmdBtn.setDisable(b);
            addQttBtn.setDisable(b);
            QttTextF.setDisable(b);
        }


    }
    public void setDisableCmd(){
        if(id==0){
            boolean b =true;
            dateCmd.setDisable(b);
            numClientCmd.setDisable(b);
            initialiserNvCmdBtn.setDisable(b);
        }
        else {
            boolean b=false;
            dateCmd.setDisable(b);
            numClientCmd.setDisable(b);
            initialiserNvCmdBtn.setDisable(b);
        }

    }
    public void clear() {
        dateCmd.setDisable(false);
        numClientCmd.setDisable(false);
        dateCmd.setValue(null);
        QttTextF.setText("");
        numClientCmd.getSelectionModel().select(-1);
        addCmdBtn.setDisable(false);
        editCmdBtn.setDisable(false);
        deleteCmdBtn.setDisable(false);
        initialiserNvCmdBtn.setDisable(false);
        id=0;
    }
    @FXML
    void addQttProd(ActionEvent event){
        if(QttTextF.getText()!=null && QttTextF.getText()!=""){
            int qtt = Integer.parseInt(QttTextF.getText());
            System.out.println(idProdChoisi+"   "+qtt);
            MyGetObj obj =new MyGetObj();
            if(id!=0){
                obj.setQttCommandeeByIDs(id, idProdChoisi,qtt);
                System.out.println("ds addQtt Btn  : "+id+"________"+idProdChoisi);
                showProduitsChoisis();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Veuillez séléctionner votre commande");
                alert.show();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Veuillez remplire le chapms d'abord");
            alert.show();
            showProduitsChoisis();
        }
    }
    @FXML
    void addCmd(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation !!");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez verifier l'etat de votre commande");

            ButtonType yesButton = new ButtonType("Confirmer");
            ButtonType noButton = new ButtonType("Annuler");
            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                ObservableList<ProduitChoisi> listProdChoisi = getProduitChoisi();
                for (ProduitChoisi PC :listProdChoisi){
                    MyGetObj obj = new MyGetObj();
                    int idProd = PC.getProduit().getNumero();
                    int qttChoisi = PC.getQuantiteCommande();
                    int qttDsStock = obj.getQttDsStock(idProd);
                    int nvQtt = qttDsStock-qttChoisi;
                    if(nvQtt>=0){
                        obj.UpdateProdStoreQtt(idProd,nvQtt);
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.setContentText("Votre commande est correctemment enregistré");
                        alert2.show();
                    }
                    else {
                        Alert alert2 = new Alert(Alert.AlertType.WARNING);
                        alert2.setContentText("Attention!! Il y a repture dans le stock");
                        alert2.show();
                    }

                }
                showCommandes();
            }
    }
    @FXML
    void clearFields(ActionEvent event) {clear();}
    @FXML
    void deleteCmd(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Attention !!");
        alert.setHeaderText(null);
        alert.setContentText("Vous voulez supprimer cette commande ?");

        ButtonType yesButton = new ButtonType("Oui");
        ButtonType noButton = new ButtonType("Annuler");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == yesButton) {
            String query = "DELETE FROM commande WHERE id_commande=?";
            con = ConnectionToDB.getConnexion();
            try {
                st=con.prepareStatement(query);
                st.setInt(1,id);
                st.executeUpdate();
                showCommandes();
                clear();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @FXML
    void editCmd(ActionEvent event) {
        String query = "UPDATE commande SET date_commande = ?, id_client=? WHERE id_commande=?";
        con = ConnectionToDB.getConnexion();
        MyGetObj obj = new MyGetObj();
        System.out.println(id);
        try {
            st = con.prepareStatement(query);
            int idClient = numClientCmd.getValue();
            st.setDate(1,java.sql.Date.valueOf(dateCmd.getValue()));
            st.setInt(2,idClient);
            st.setInt(3,id);
            st.executeUpdate();
            System.out.println(id);
            showCommandes();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialiserNvCmd(ActionEvent event) {
        String query = "INSERT INTO commande (date_commande,id_client) values(?,?)";
        con = ConnectionToDB.getConnexion();
        MyGetObj obj = new MyGetObj();
        try {
            st = con.prepareStatement(query);
            int idClient = numClientCmd.getValue();
            st.setDate(1,java.sql.Date.valueOf(dateCmd.getValue()));
            st.setInt(2,idClient);
            st.executeUpdate();
            showCommandes();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setDisableProd();
        setDisableCmd();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Veuillez choisir les produits souhaités et leurs quantités demandées");
        alert.show();
        showProduitsChoisis();
    }
}

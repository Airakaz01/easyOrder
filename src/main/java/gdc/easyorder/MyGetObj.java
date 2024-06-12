package gdc.easyorder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class MyGetObj {
    Connection con =null;
    Connection con2 =null;
    PreparedStatement st=null;
    PreparedStatement st2=null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    public Client getClient(int id_C){
        String query ="SELECT * FROM client WHERE id_client=?";
        con = ConnectionToDB.getConnexion();
        Client client = new Client();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,id_C);
            rs = st.executeQuery();
            while (rs.next()){
                client.setNumero(rs.getInt("id_client"));
                client.setNom(rs.getString("nom_client"));
                client.setPrenom(rs.getString("prenom_client"));
                client.setAdresse(rs.getString("adresse_client"));
                client.setTele(rs.getInt("tele_client"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return client;
    }
    public Commande getCommande(int id_C){
        String query ="SELECT * FROM commande WHERE id_commande=?";
        con = ConnectionToDB.getConnexion();
        Commande commande = new Commande();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,id_C);
            rs = st.executeQuery();
            while (rs.next()){
                commande.setNumero(rs.getInt("id_commande"));
                commande.setDateCmd(rs.getDate("date_commande"));
                int idClient= rs.getInt("id_client");
                commande.setClient(getClient(idClient));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return commande;
    }
    public ObservableList<Integer> getAllIdCommandes(){
        String query= "SELECT id_commande FROM commande";
        ObservableList<Integer> idlist = FXCollections.observableArrayList();
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            rs =st.executeQuery();
            while (rs.next()){
                idlist.add(rs.getInt("id_commande"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return idlist;
    }
    public ObservableList<Integer> getAllIdFactures(){
        String query= "SELECT id_facture FROM facture";
        ObservableList<Integer> idlist = FXCollections.observableArrayList();
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            rs =st.executeQuery();
            while (rs.next()){
                idlist.add(rs.getInt("id_facture"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return idlist;
    }
    public ObservableList<Integer> getAllIdLivraison(){
        String query= "SELECT id_livraison FROM livraison";
        ObservableList<Integer> idlist = FXCollections.observableArrayList();
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            rs =st.executeQuery();
            while (rs.next()){
                idlist.add(rs.getInt("id_livraison"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return idlist;
    }
    public List<Integer> getClientByName(String name){
    String query= "SELECT id_client FROM client WHERE nom_client Like ?";
    List<Integer> idlist = new ArrayList<>();
    con = ConnectionToDB.getConnexion();
    try {
        st = con.prepareStatement(query);
        st.setString(1, "%" + name + "%");
        rs =st.executeQuery();
        while (rs.next()){
            idlist.add(rs.getInt("id_client"));
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return idlist;
}
    public List<Integer> getClientByFname(String Fname){
        String query= "SELECT id_client FROM client WHERE prenom_client Like ?";
        List<Integer> idlist = new ArrayList<>();
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setString(1, "%" + Fname + "%");
            rs =st.executeQuery();
            while (rs.next()){
                idlist.add(rs.getInt("id_client"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return idlist;
    }

    public Double calMontant(int idCmd) {
    String query = "SELECT id_produit FROM commande_produit WHERE id_commande=?";
    Connection connection = ConnectionToDB.getConnexion();
    Double total = 0.0;
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, idCmd);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int idProd = resultSet.getInt("id_produit");
                Produit prod = getProduit(idProd);
                ProduitChoisi prodch = new ProduitChoisi();
                int qtt = getQttCommandeeByIDs(idCmd, idProd);
                prodch.setQuantiteCommande(qtt);
                total += prod.getPrix() * prodch.getQuantiteCommande();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return total;
}
    public Produit getProduit(int idProd){
        String query ="SELECT * FROM produit WHERE id_produit=?";
        con2 = ConnectionToDB.getConnexion();
        Produit produit= new Produit();
        try {
            st2 = con2.prepareStatement(query);
            st2.setInt(1,idProd);
            rs2 = st2.executeQuery();
            while (rs2.next()){
                produit.setNumero(rs2.getInt("id_produit"));
                produit.setNom(rs2.getString("nom_produit"));
                produit.setQuatite(rs2.getInt("quantite"));
                produit.setPrix(rs2.getDouble("prix"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produit;
    }
    public List<Produit> getListProduits(int idCmd){
        List<Produit> listProd = new ArrayList<>();
        String query = "SELECT id_produit FROM commande_produit WHERE id_commande=?";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,idCmd);
            rs = st.executeQuery();
            while (rs.next()){
                listProd.add(getProduit(rs.getInt("id_produit")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return listProd;
    }
    public int getMaxidCmd(){
        String query = "SELECT MAX(id_commande) FROM commande";
        con = ConnectionToDB.getConnexion();
        int maxIdCmd=0;
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            rs = st.executeQuery();
            while (rs.next()) {
                maxIdCmd = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return maxIdCmd;
    }
    public void setCmdProdTable(ObservableList<Integer> selectedItems){
        String query = "INSERT INTO commande_produit (id_commande,id_produit) VALUES (?,?)";
        con2 = ConnectionToDB.getConnexion();
        try {
            st2 = con.prepareStatement(query);
            for (Integer idProd : selectedItems){
                st2.setInt(1,getMaxidCmd());
                st2.setInt(2,idProd);
                st2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setCmdProdTable(int id,ObservableList<Integer> selectedItems){
        String query = "INSERT INTO commande_produit (id_commande,id_produit) VALUES (?,?)";
        con2 = ConnectionToDB.getConnexion();
        try {
            st2 = con.prepareStatement(query);
            for (Integer idProd : selectedItems){
                st2.setInt(1,id);
                st2.setInt(2,idProd);
                st2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deletFromCmdProdTable(int idCmd){
        String query = "DELETE FROM commande_produit WHERE id_commande=?";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,idCmd);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void UpdateCmdProdTable(int idCmd,ObservableList<Integer> selectedItems){
            deletFromCmdProdTable(idCmd);
            setCmdProdTable(idCmd,selectedItems);
    }
    public ObservableList<Facture> getFactureById(int id){
        String query = "SELECT * FROM facture WHERE id_facture=?";
        con = ConnectionToDB.getConnexion();
        ObservableList<Facture> factures = FXCollections.observableArrayList();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,id);
            rs = st.executeQuery();
            while (rs.next()){
                Facture f =new Facture();
                f.setNumero(rs.getInt("id_facture"));
                f.setDatefac(rs.getDate("date_facture"));
                f.setMontant(rs.getDouble("montant"));
                f.setCommande(getCommande(rs.getInt("id_commande")));
                factures.add(f);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return factures;
    }
    public ObservableList<Facture> getFactureByIdCmd(int id){
        ObservableList<Facture> factures = FXCollections.observableArrayList();
        String query = "SELECT * FROM facture WHERE id_commande=?";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,id);
            rs = st.executeQuery();
            while (rs.next()){
                Facture f = new Facture();
                f.setNumero(rs.getInt("id_facture"));
                f.setDatefac(rs.getDate("date_facture"));
                f.setMontant(rs.getDouble("montant"));
                f.setCommande(getCommande(rs.getInt("id_commande")));
                factures.add(f);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return factures;
    }
    public ObservableList<Facture> getFactureByIdClient(int id){
        GetCommandeController cmdCtrl = new GetCommandeController();
        ObservableList<Facture> factures = FXCollections.observableArrayList();
        ObservableList<Commande> commandes =cmdCtrl.getCommandesByIdClient(id);
        for(Commande cmd :commandes){
            factures.addAll(getFactureByIdCmd(cmd.getNumero()));
        }
        return  factures;

    }
    public ObservableList<Livraison> getLivraisonById(int id){
        String query = "SELECT * FROM livraison WHERE id_livraison=?";
        con = ConnectionToDB.getConnexion();
        ObservableList<Livraison> livraisons = FXCollections.observableArrayList();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,id);
            rs = st.executeQuery();
            while (rs.next()){
                Livraison l =new Livraison();
                l.setNumero(rs.getInt("id_livraison"));
                l.setDateliv(rs.getDate("date_livraison"));
                l.setCommande(getCommande(rs.getInt("id_commande")));
                livraisons.add(l);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return livraisons;
    }
    public ObservableList<Livraison> getLivraisonByIdCmd(int id){
        ObservableList<Livraison> livraisons = FXCollections.observableArrayList();
        String query = "SELECT * FROM livraison WHERE id_commande=?";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,id);
            rs = st.executeQuery();
            while (rs.next()){
                Livraison l = new Livraison();
                l.setNumero(rs.getInt("id_livraison"));
                l.setDateliv(rs.getDate("date_livraison"));
                l.setCommande(getCommande(rs.getInt("id_commande")));
                livraisons.add(l);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return livraisons;
    }
    public ObservableList<Livraison> getLivraisonByIdClient(int id){
        GetCommandeController cmdCtrl = new GetCommandeController();
        ObservableList<Livraison> livraisons = FXCollections.observableArrayList();
        ObservableList<Commande> commandes =cmdCtrl.getCommandesByIdClient(id);
        for(Commande cmd :commandes){
            livraisons.addAll(getLivraisonByIdCmd(cmd.getNumero()));
        }
        return  livraisons;
    }
    public Produit getProduitById(int id){
        String query = "SELECT * FROM produit WHERE id_produit=?";
        con = ConnectionToDB.getConnexion();
        Produit produit = new Produit();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,id);
            rs = st.executeQuery();
            while (rs.next()){
                produit.setNumero(rs.getInt("id_produit"));
                produit.setNom(rs.getString("nom_produit"));
                produit.setQuatite(rs.getInt("quantite"));
                produit.setPrix(rs.getDouble("prix"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return produit;
    }
    public int getQttCommandeeByIDs(int idCmd,int idProd){
        String query = "SELECT qtt_commandee FROM commande_produit WHERE id_commande=? AND id_produit=?";
        con = ConnectionToDB.getConnexion();
        int qtt = 0;
        try {
            st =con.prepareStatement(query);
            st.setInt(1,idCmd);
            st.setInt(2,idProd);
            rs = st.executeQuery();
            while (rs.next()){
                qtt = rs.getInt("qtt_commandee");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return qtt;
    }
    public void deletCmdProdRow(int idCmd,int idProd){
        String query = "DELETE FROM commande_produit WHERE id_commande=? AND id_produit=?";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,idCmd);
            st.setInt(2,idProd);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public  int getQttDsStock(int idProd){
        String query = "SELECT quantite FROM produit WHERE id_produit=?";
        int qtt = 0;
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,idProd);
            rs = st.executeQuery();
            while (rs.next()){
                qtt = rs.getInt("quantite");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }return  qtt;

    }
    public void setQttCommandeeByIDs(int idCmd,int idProd,int qtt){
        int test = this.getQttDsStock(idProd)-qtt;
        System.out.println(test);
        if(test>=0){
            String q1 = "SELECT * FROM commande_produit WHERE id_commande=? AND id_produit=?";
            con = ConnectionToDB.getConnexion();
            try {
                st = con.prepareStatement(q1);
                st.setInt(1,idCmd);
                st.setInt(2,idProd);
                rs = st.executeQuery();
                if(rs.next()){

                    System.out.println("rs.next : "+rs.next());
                    rs = st.executeQuery();
                    while (rs.next()){
                        String query = "UPDATE commande_produit SET qtt_commandee = ? WHERE id_commande=? AND id_produit=?";
                        st = con.prepareStatement(query);
                        st.setInt(1,qtt);
                        st.setInt(2,idCmd);
                        st.setInt(3,idProd);
                        st.executeUpdate();
                    }
                }
                else {
                    String q2 ="INSERT INTO commande_produit (id_commande,id_produit,qtt_commandee,livre) values (?,?,?,'non')";
                    st = con.prepareStatement(q2);
                    st.setInt(1,idCmd);
                    st.setInt(2,idProd);
                    st.setInt(3,qtt);
                    st.executeUpdate();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Repture de stock");
            alert.show();
        }
    }
    public void UpdateProdStoreQtt(int idProd,int nvQtt){
        String query = "UPDATE produit SET quantite = ? WHERE id_produit=? ";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,nvQtt);
            st.setInt(2,idProd);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Integer> getListIdProdNonLivByIdCmd(int idCmd){
        List<Integer> idlist = new ArrayList<>();

        String query = "SELECT id_produit FROM commande_produit WHERE id_commande=? AND livre LIKE 'non'";
        con = ConnectionToDB.getConnexion();
        try {
            st = con.prepareStatement(query);
            st.setInt(1,idCmd);
            rs = st.executeQuery();
            while (rs.next()){
                idlist.add(rs.getInt("id_produit"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return idlist;
    }
    public  ObservableList<ProduitChoisi> getProduitNonLivByIdCmd(int idCmd){
        ObservableList<ProduitChoisi> produitChoisis = FXCollections.observableArrayList();
        List<Integer> idProdList = getListIdProdNonLivByIdCmd(idCmd);
        for(int idProd :idProdList){
            ProduitChoisi produitChoisi = new ProduitChoisi();
            Produit produit = getProduit(idProd);
            produitChoisi.setProduit(produit);
            produitChoisi.setQuantiteCommande(getQttCommandeeByIDs(idCmd,idProd));
            produitChoisis.add(produitChoisi);
        }
        return produitChoisis;
    }
}
package gdc.easyorder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
public class DashboardController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;
    @FXML
    private AreaChart<?, ?> DataRevenu;

    @FXML
    private AreaChart<?, ?> DataTotalRevenu;

    @FXML
    private Label Revenu;

    @FXML
    private Label bestclient;

    @FXML
    private Label jrComande;

    @FXML
    private Label jrLivraison;

    @FXML
    private Label nbrClient;

    @FXML
    private Label nbrCommande;

    @FXML
    private Label nbrProduit;

    @FXML
    private Label nbrProduitvendue;

    @FXML
    private Label npbestclient;

    @FXML
    private Label totalRevenu;


    @FXML
    private Button tbpourcentage;

    @FXML
    private StackPane stackpane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NbrClient();
        NbrCommande();
        NbrProduit();
        TRevenu();
        Revenu();
        CommandeJour();
        LivraisonJour();
        nombredproduitVendue();
        BestClient();
        Dashboard_Revenu();
        Dashboard_TRevenu();
        nbrpourcentage();
    }

    public void NbrClient() {
        String query = "select count(id_client) from client";
        con = ConnectionToDB.getConnexion();
        int countE = 0;
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                countE = rs.getInt("count(id_client)");
            }
            nbrClient.setText(String.valueOf(countE));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void NbrCommande() {
        String query = "select count(id_commande) from commande";
        con = ConnectionToDB.getConnexion();
        int countE = 0;
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                countE = rs.getInt("count(id_commande)");
            }
            nbrCommande.setText(String.valueOf(countE));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void NbrProduit() {
        String query = "select count(id_produit) from produit";
        Connection con = ConnectionToDB.getConnexion();
        int countE = 0;
        try {
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                countE = rs.getInt("count(id_produit)");
            }
            nbrProduit.setText(String.valueOf(countE));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void TRevenu() {
        String query = "select SUM(montant) from facture ";
        Connection con = ConnectionToDB.getConnexion();
        double sumR = 0;
        try {
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                sumR = rs.getInt("Sum(montant)");
            }
            totalRevenu.setText(String.valueOf(sumR) + "$");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void Revenu() {
        Date date = new Date();
        java.sql.Date sqldate = new java.sql.Date(date.getTime());
        String query = "select SUM(montant) from facture where date_facture='" + sqldate + "'";
        Connection con = ConnectionToDB.getConnexion();
        double sum = 0;
        try {
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                sum = rs.getInt("SUM(montant)");
            }
            Revenu.setText(String.valueOf(sum) + "$");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void CommandeJour() {
        Date date = new Date();
        java.sql.Date sqldate = new java.sql.Date(date.getTime());
        String query = "select Count(id_commande) from commande where date_commande='" + sqldate + "'";
        Connection con = ConnectionToDB.getConnexion();
        double countC = 0;
        try {
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                countC = rs.getInt("COUNT(id_commande)");
            }
            jrComande.setText(String.valueOf(Integer.valueOf((int) countC)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void LivraisonJour() {
        Date date = new Date();
        java.sql.Date sqldate = new java.sql.Date(date.getTime());
        String query = "select Count(id_livraison) from livraison where date_livraison='" + sqldate + "'";
        Connection con = ConnectionToDB.getConnexion();
        double countl = 0;
        try {
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                countl = rs.getInt("COUNT(id_livraison)");
            }
            jrLivraison.setText(String.valueOf(Integer.valueOf((int) countl)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void nombredproduitVendue() {
        Date date = new Date();
        java.sql.Date sqldate = new java.sql.Date(date.getTime());
        String query = "select count(id_produit) as total_produits from commande_produit where id_commande In(select id_commande from commande where date_commande=?)";
        Connection con = ConnectionToDB.getConnexion();
        double countP = 0;
        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setDate(1, sqldate);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                countP = rs.getInt("total_produits");
            }
            nbrProduitvendue.setText(String.valueOf((int) countP));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void BestClient() {
        String query = "SELECT c.nom_client, c.prenom_client, COUNT(cm.id_commande) AS nombre_commandes " +
                "FROM client c " +
                "JOIN commande cm ON c.id_client = cm.id_client " +
                "GROUP BY c.nom_client, c.prenom_client " +
                "ORDER BY nombre_commandes DESC " +
                "LIMIT 1";
        Connection con = ConnectionToDB.getConnexion();
        String bestClientName = "";
        String bestClientPrenom = "";
        int numberOfOrders = 0;
        try {
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                bestClientName = rs.getString("nom_client");
                bestClientPrenom = rs.getString("prenom_client");
                numberOfOrders = rs.getInt("nombre_commandes");
            }
            bestclient.setText("NbrCommande:" + numberOfOrders);
            npbestclient.setText(bestClientName + " " + bestClientPrenom);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void Dashboard_Revenu() {
        DataRevenu.getData().clear();
        Date date = new Date();
        java.sql.Date sqldate = new java.sql.Date(date.getTime());

        // Calcul de la date il y a une semaine
        Calendar cal = Calendar.getInstance();
        cal.setTime(sqldate);
        cal.add(Calendar.DATE, -7); // 7 jours avant la date actuelle
        java.sql.Date weekAgo = new java.sql.Date(cal.getTimeInMillis());

        String query = "SELECT date_facture, SUM(montant) FROM facture WHERE date_facture BETWEEN '" + weekAgo + "' AND '" + sqldate + "' GROUP BY date_facture ORDER BY date_facture ASC";
        Connection con = ConnectionToDB.getConnexion();

        try {
            XYChart.Series chart = new XYChart.Series();
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                chart.getData().add(new XYChart.Data(rs.getString(1), rs.getInt(2)));
            }
            DataRevenu.getData().add(chart);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void Dashboard_TRevenu() {
        DataTotalRevenu.getData().clear();
        String query = "SELECT date_facture, SUM(montant) FROM facture GROUP BY date_facture ORDER BY date_facture ASC";
        Connection con = ConnectionToDB.getConnexion();
        try {
            XYChart.Series chart = new XYChart.Series();
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            int total = 0;
            while (rs.next()) {
                total += rs.getInt(2);
                chart.getData().add(new XYChart.Data(rs.getString(1), total));
            }
            DataTotalRevenu.getData().add(chart);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void nbrpourcentage() {
        MontantlimitetController.loadMontantLimit();
        Date date = new Date();
        java.sql.Date sqldate = new java.sql.Date(date.getTime());
        String query = "SELECT SUM(p.prix) AS total " +
                "FROM produit p " +
                "JOIN commande_produit cp ON p.id_produit = cp.id_produit " +
                "JOIN commande c ON cp.id_commande = c.id_commande " +
                "WHERE c.date_commande = ?";
        Connection con = ConnectionToDB.getConnexion();
        double montantTotal = 0;
        double montantLimite = MontantlimitetController.getMontantLimit();
        try {
            PreparedStatement st = con.prepareStatement(query);
            st.setDate(1, sqldate);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                montantTotal = rs.getDouble("total");
            }
            double pourcentageRestant = 0;
            if (montantTotal <= montantLimite) {
                pourcentageRestant = ((montantLimite - montantTotal) / montantLimite) * 100;
            } else {
                pourcentageRestant = 0;
            }
            tbpourcentage.setText(String.format("%.2f", pourcentageRestant) + "%");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private Popup popup = null;

    @FXML
    void DetailMontant(MouseEvent event) {
        if (popup == null) {
            popup = new Popup();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Montantlimitet.fxml"));
                Parent root = fxmlLoader.load();
                popup.getContent().add(root);
                popup.setAutoHide(true);
                popup.setOnHidden(e -> popup = null);

                // Positionnement de la fenêtre à droite du bouton
                double buttonX = event.getSceneX(); // Coordonnée X du bouton
                double buttonY = event.getSceneY(); // Coordonnée Y du bouton
                popup.show(stackpane.getScene().getWindow(), buttonX, buttonY);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
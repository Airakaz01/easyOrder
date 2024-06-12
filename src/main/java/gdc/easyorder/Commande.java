package gdc.easyorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commande {
    private int numero;
    private Date dateCmd;
    private Client client;
    private List<Produit> produits = new ArrayList<>();
    public void addProduit(Produit produit) {
        this.produits.add(produit);
    }
    public void addProduits(List<Produit> produits) {
        this.produits.addAll(produits);
    }
    public void removeProduit(Produit produit) {
        this.produits.remove(produit);
    }
    public List<Produit> getProduits() {
        return this.produits;
    }
    public String getProduitsString() {
        StringBuilder sb = new StringBuilder();
        MyGetObj obj = new MyGetObj();
        for (Produit produit : produits) {
            ProduitChoisi produitChoisi = new ProduitChoisi();
            produitChoisi.setProduit(produit);
            produitChoisi.setQuantiteCommande(obj.getQttCommandeeByIDs(numero,produit.getNumero()));
            sb.append(produit.getNom()).append("[").append(produitChoisi.getQuantiteCommande()).append("]").append(" / ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 3); // remove the last "/"
        }
        return sb.toString();
    }
    public int getNumero() {
        return numero;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }
    public Date getDateCmd() {
        return dateCmd;
    }
    public void setDateCmd(Date dateCmd) {
        this.dateCmd = dateCmd;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}

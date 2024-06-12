package gdc.easyorder;

import java.util.Date;

public class Facture {
    private int numero;
    private Date datefac;
    private double montant;
    private Commande commande;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getDatefac() {
        return datefac;
    }

    public void setDatefac(Date datefac) {
        this.datefac = datefac;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }


}

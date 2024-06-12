package gdc.easyorder;

import java.util.Date;

public class Livraison {
    private int numero;
    private Date dateliv;
    private Commande commande;
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }


    public Date getDateliv() {
        return dateliv;
    }

    public void setDateliv(Date dateliv) {
        this.dateliv = dateliv;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }


}

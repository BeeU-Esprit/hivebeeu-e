package tn.esprit.models;

import javafx.beans.property.*;

public class CommandeProduits {
    private final IntegerProperty idCommande = new SimpleIntegerProperty();
    private final IntegerProperty idProduit = new SimpleIntegerProperty();
    private final IntegerProperty quantite = new SimpleIntegerProperty();
    private final DoubleProperty prixUnitaire = new SimpleDoubleProperty();

    public CommandeProduits(int idCommande, int idProduit, int quantite, double prixUnitaire) {
        this.idCommande.set(idCommande);
        this.idProduit.set(idProduit);
        this.quantite.set(quantite);
        this.prixUnitaire.set(prixUnitaire);
    }

    public int getIdCommande() { return idCommande.get(); }
    public IntegerProperty idCommandeProperty() { return idCommande; }

    public int getIdProduit() { return idProduit.get(); }
    public IntegerProperty idProduitProperty() { return idProduit; }

    public int getQuantite() { return quantite.get(); }
    public IntegerProperty quantiteProperty() { return quantite; }
    public void setQuantite(int quantite) { this.quantite.set(quantite); }

    public double getPrixUnitaire() { return prixUnitaire.get(); }
    public DoubleProperty prixUnitaireProperty() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire.set(prixUnitaire); }
}

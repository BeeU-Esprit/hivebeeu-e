package tn.esprit.models;

import javafx.beans.property.*;

public class Produit {
    private final IntegerProperty idProduit = new SimpleIntegerProperty();
    private final StringProperty nomProduit = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty prixUnitaire = new SimpleDoubleProperty();

    // Constructor
    public Produit(int idProduit, String nomProduit, String description, double prixUnitaire) {
        this.idProduit.set(idProduit);
        this.nomProduit.set(nomProduit);
        this.description.set(description);
        this.prixUnitaire.set(prixUnitaire);
    }

    public Produit(String nomProduit, String description, double prixUnitaire) {
        this.nomProduit.set(nomProduit);
        this.description.set(description);
        this.prixUnitaire.set(prixUnitaire);
    }

    // JavaFX Property Methods
    public IntegerProperty idProduitProperty() { return idProduit; }
    public StringProperty nomProduitProperty() { return nomProduit; }
    public StringProperty descriptionProperty() { return description; }
    public DoubleProperty prixProperty() { return prixUnitaire; }

    // Standard Getters and Setters
    public int getIdProduit() { return idProduit.get(); }
    public void setIdProduit(int idProduit) { this.idProduit.set(idProduit); }

    public String getNomProduit() { return nomProduit.get(); }
    public void setNomProduit(String nomProduit) { this.nomProduit.set(nomProduit); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public double getPrixUnitaire() { return prixUnitaire.get(); }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire.set(prixUnitaire); }
}
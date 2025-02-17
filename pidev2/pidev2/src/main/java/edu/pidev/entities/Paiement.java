package edu.pidev.entities;

import java.time.LocalDate;

public class Paiement {
    private int idPaiement; // Ceci est auto-incrémenté
    private float montant;
    private LocalDate dateP;
    private String mode_de_P;
    private String statutP;
    private int idCommande;
    private String statut;

    // Constructeur sans idPaiement, car il est généré automatiquement
    public Paiement(float montant, LocalDate dateP, String mode_de_P, String statutP, int idCommande, String statut) {
        this.montant = montant;
        this.dateP = dateP;
        this.mode_de_P = mode_de_P;
        this.statutP = statutP;
        this.idCommande = idCommande;
        this.statut = statut;
    }

    // Constructeur avec idPaiement pour la récupération
    public Paiement(int idPaiement, float montant, LocalDate dateP, String mode_de_P, String statutP, int idCommande, String statut) {
        this.idPaiement = idPaiement;
        this.montant = montant;
        this.dateP = dateP;
        this.mode_de_P = mode_de_P;
        this.statutP = statutP;
        this.idCommande = idCommande;
        this.statut = statut;
    }

    // Getters et setters
    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public LocalDate getDateP() {
        return dateP;
    }

    public void setDateP(LocalDate dateP) {
        this.dateP = dateP;
    }

    public String getMode_de_P() {
        return mode_de_P;
    }

    public void setMode_de_P(String mode_de_P) {
        this.mode_de_P = mode_de_P;
    }

    public String getStatutP() {
        return statutP;
    }

    public void setStatutP(String statutP) {
        this.statutP = statutP;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Paiement{" +
                "idPaiement=" + idPaiement +
                ", montant=" + montant +
                ", dateP=" + dateP +
                ", mode_de_P='" + mode_de_P + '\'' +
                ", statutP='" + statutP + '\'' +
                ", idCommande=" + idCommande +
                ", statut='" + statut + '\'' +
                '}';
    }
}

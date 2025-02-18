package tn.esprit.models;

import java.time.LocalDate;

public class Paiement {
    private int idPaiement;  // ✅ Add this
    private int idCommande;
    private int idUtilisateur;
    private double montant;
    private String modeDePaiement;
    private LocalDate dateDePaiement;
    private String status;

    // ✅ Constructor with idPaiement
    public Paiement(int idPaiement, int idCommande, int idUtilisateur, double montant, String modeDePaiement, LocalDate dateDePaiement, String status) {
        this.idPaiement = idPaiement;
        this.idCommande = idCommande;
        this.idUtilisateur = idUtilisateur;
        this.montant = montant;
        this.modeDePaiement = modeDePaiement;
        this.dateDePaiement = dateDePaiement;
        this.status = status;
    }

    // ✅ Constructor without idPaiement (for new payments)
    public Paiement(int idCommande, int idUtilisateur, double montant, String modeDePaiement, LocalDate dateDePaiement, String status) {
        this.idCommande = idCommande;
        this.idUtilisateur = idUtilisateur;
        this.montant = montant;
        this.modeDePaiement = modeDePaiement;
        this.dateDePaiement = dateDePaiement;
        this.status = status;
    }

    // ✅ Getters
    public int getIdPaiement() { return idPaiement; }
    public int getIdCommande() { return idCommande; }
    public int getIdUtilisateur() { return idUtilisateur; }
    public double getMontant() { return montant; }
    public String getModeDePaiement() { return modeDePaiement; }
    public LocalDate getDateDePaiement() { return dateDePaiement; }
    public String getStatus() { return status; }

    // ✅ Setters
    public void setIdPaiement(int idPaiement) { this.idPaiement = idPaiement; }
    public void setStatus(String status) { this.status = status; }
    public void setDateDePaiement(LocalDate dateDePaiement) { this.dateDePaiement = dateDePaiement; }
}

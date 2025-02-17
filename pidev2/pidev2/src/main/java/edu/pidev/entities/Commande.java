package edu.pidev.entities;


import java.time.LocalDate;


public class Commande {
    private int idC;
    private LocalDate dateC;
    private int quanitite;
    private double prix;
    private double total;
    private String statut;
    private int idClient;

    // Constructeur
    public Commande(int idC, LocalDate dateC, int quanitite, double prix, double total, String statut, int idClient) {
        this.idC = idC;
        this.dateC = dateC;
        this.quanitite = quanitite;
        this.prix = prix;
        this.total = total;
        this.statut = statut;
        this.idClient = idClient;
    }

    // Getters et Setters
    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public LocalDate getDateC() {
        return dateC;
    }

    public void setDateC(LocalDate dateC) {
        this.dateC = dateC;
    }

    public int getQuanitite() {
        return quanitite;
    }

    public void setQuanitite(int quanitite) {
        this.quanitite = quanitite;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    // Méthode pour afficher les détails de la commande
    @Override
    public String toString() {
        return "Commande{" +
                "idC=" + idC +
                ", dateC=" + dateC +
                ", quanitite=" + quanitite +
                ", prix=" + prix +
                ", total=" + total +
                ", statut='" + statut + '\'' +
                ", idClient=" + idClient +
                '}';
    }
}

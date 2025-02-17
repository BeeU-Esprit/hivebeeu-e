package edu.emmapi.entities;

import java.util.Date;
import java.util.List;

public class Abonnement {
    private int idAbonnement;
    private String nomService;
    private String typeAbonnement;
    private Date date;
    private String statusAbonnement;
    private boolean estDisponible;
    private List<String> listeDesClients;
    private List<String> listeDesCoach;

    public Abonnement() {}

    public Abonnement(int idAbonnement, String nomService, String typeAbonnement, Date date, String statusAbonnement, boolean estDisponible) {
        this.idAbonnement = idAbonnement;
        this.nomService = nomService;
        this.typeAbonnement = typeAbonnement;
        this.date = date;
        this.statusAbonnement = statusAbonnement;
        this.estDisponible = estDisponible;
    }

    // Getters and Setters
    public int getIdAbonnement() {
        return idAbonnement;
    }

    public void setIdAbonnement(int idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getTypeAbonnement() {
        return typeAbonnement;
    }

    public void setTypeAbonnement(String typeAbonnement) {
        this.typeAbonnement = typeAbonnement;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatusAbonnement() {
        return statusAbonnement;
    }

    public void setStatusAbonnement(String statusAbonnement) {
        this.statusAbonnement = statusAbonnement;
    }

    public boolean isEstDisponible() {
        return estDisponible;
    }

    public void setEstDisponible(boolean estDisponible) {
        this.estDisponible = estDisponible;
    }

    public List<String> getListeDesClients() {
        return listeDesClients;
    }

    public void setListeDesClients(List<String> listeDesClients) {
        this.listeDesClients = listeDesClients;
    }

    public List<String> getListeDesCoach() {
        return listeDesCoach;
    }

    public void setListeDesCoach(List<String> listeDesCoach) {
        this.listeDesCoach = listeDesCoach;
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "idAbonnement=" + idAbonnement +
                ", nomService='" + nomService + '\'' +
                ", typeAbonnement='" + typeAbonnement + '\'' +
                ", date=" + date +
                ", statusAbonnement='" + statusAbonnement + '\'' +
                ", estDisponible=" + estDisponible +
                '}';
    }
}
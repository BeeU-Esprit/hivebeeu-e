package edu.emmapi.entities;

public class Service {
    private int idService;
    private String nomService;
    private String typeService;
    private String description;
    private boolean estDisponible;

    public Service() {}

    public Service(int idService, String nomService, String typeService, String description, boolean estDisponible) {
        this.idService = idService;
        this.nomService = nomService;
        this.typeService = typeService;
        this.description = description;
        this.estDisponible = estDisponible;
    }

    // Getters and Setters
    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public String getTypeService() {
        return typeService;
    }

    public void setTypeService(String typeService) {
        this.typeService = typeService;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEstDisponible() {
        return estDisponible;
    }

    public void setEstDisponible(boolean estDisponible) {
        this.estDisponible = estDisponible;
    }

    @Override
    public String toString() {
        return "Service{" +
                "idService=" + idService +
                ", nomService='" + nomService + '\'' +
                ", typeService='" + typeService + '\'' +
                ", description='" + description + '\'' +
                ", estDisponible=" + estDisponible +
                '}';
    }
}
package edu.emmapi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InfoService {

    @FXML
    private TextField nomService_profile;

    @FXML
    private TextField typeService_profile;

    @FXML
    private TextField description_profile;

    @FXML
    private TextField estDisponible_profile;

    public void setNomService_profile(String nomService) {
        this.nomService_profile.setText(nomService);
    }

    public void setTypeService_profile(String typeService) {
        this.typeService_profile.setText(typeService);
    }

    public void setDescription_profile(String description) {
        this.description_profile.setText(description);
    }

    public void setEstDisponible_profile(String estDisponible) {
        this.estDisponible_profile.setText(estDisponible);
    }
}

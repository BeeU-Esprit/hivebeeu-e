package edu.emmapi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InfoAbonnement {

    @FXML
    private TextField nomService_profile;

    @FXML
    private TextField typeAbonnement_profile;

    @FXML
    private TextField date_profile;

    @FXML
    private TextField statusAbonnement_profile;

    public void setNomService_profile(String nomService) {
        this.nomService_profile.setText(nomService);
    }

    public void setTypeAbonnement_profile(String typeAbonnement) {
        this.typeAbonnement_profile.setText(typeAbonnement);
    }

    public void setDate_profile(String date) {
        this.date_profile.setText(date);
    }

    public void setStatusAbonnement_profile(String statusAbonnement) {
        this.statusAbonnement_profile.setText(statusAbonnement);
    }
}
package edu.emmapi.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class InfoTypeService {

    @FXML
    private TextField typeService_profile;

    public void setTypeService_profile(String typeService) {
        this.typeService_profile.setText(typeService);
    }
}
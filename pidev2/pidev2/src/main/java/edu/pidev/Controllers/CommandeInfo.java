package edu.pidev.Controllers;

import edu.pidev.entities.Commande;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;

/*
public class CommandeInfo {

    @FXML
    private DatePicker dateCTextField;

    @FXML
    private TextField prixTextField;

    @FXML
    private TextField produitCTextField;

    @FXML
    private TextField quantiteTextField;

    @FXML
    private TextField statutTextField;

    @FXML
    private TextField totalTextField;

    public void setCommandeInfo(Commande commande) {
        if (commande != null) {
            produitCTextField.setText(commande.getProduitC()); // Remplir le champ produit
            quantiteTextField.setText(String.valueOf(commande.getQuantite())); // Remplir le champ quantité
            prixTextField.setText(String.valueOf(commande.getPrix())); // Remplir le champ prix
            totalTextField.setText(String.valueOf(commande.getTotal())); // Remplir le champ total
            statutTextField.setText(commande.getStatut()); // Remplir le champ statut

            // Si vous avez un DatePicker pour la date
            //dateCTextField.setValue(commande.getDateC()); // Remplir le champ date
        } else {
            System.out.println("La commande est null !");
        }
    }
}

/*
 */
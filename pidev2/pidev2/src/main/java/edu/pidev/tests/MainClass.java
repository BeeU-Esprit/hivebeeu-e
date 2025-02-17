package edu.pidev.tests;

import edu.pidev.entities.Commande;
import edu.pidev.entities.Paiement;
import edu.pidev.services.CommandeService;
import edu.pidev.services.PaiementService;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        // Initialisation des services
        CommandeService commandeService = new CommandeService();
        PaiementService paiementService = new PaiementService();

        try {
            // Création et ajout d'une commande
            Commande commande = new Commande(0, LocalDate.now(), 2, 750.50f, 2 * 750.50f, "En cours", 1);
            commandeService.addEntity(commande);

            // Récupération et affichage des commandes
            System.out.println("\n=== Liste des commandes après ajout ===");
            List<Commande> commandes = commandeService.getAllData();
            commandes.forEach(System.out::println);

            // Récupérer la dernière commande ajoutée pour ajouter un paiement
            if (!commandes.isEmpty()) {
                Commande lastCommande = commandes.get(commandes.size() - 1);

                // Création et ajout d'un paiement pour la commande
                Paiement paiement = new Paiement(0, 1500.00f, LocalDate.now(), "Carte bancaire", "Complété", lastCommande.getIdC(), "Réussi");
                paiementService.addEntity(paiement);
            }

            // Récupération et affichage des paiements
            System.out.println("\n=== Liste des paiements après ajout ===");
            List<Paiement> paiements = paiementService.getAllData();
            paiements.forEach(System.out::println);

            // Modification de la dernière commande ajoutée
            if (!commandes.isEmpty()) {
                Commande lastCommande = commandes.get(commandes.size() - 1);
                lastCommande.setDateC(LocalDate.now().plusDays(2)); // Modification de la date
                lastCommande.setQuanitite(1);
                lastCommande.setPrix(1200.00f);
                lastCommande.setTotal(lastCommande.getQuanitite() * lastCommande.getPrix());
                lastCommande.setStatut("Validée");

                commandeService.updateEntity(lastCommande);
            }

            // Affichage après modification
            System.out.println("\n=== Liste des commandes après modification ===");
            commandeService.getAllData().forEach(System.out::println);
/*
            // Suppression de la dernière commande ajoutée
            if (!commandes.isEmpty()) {
                Commande lastCommande = commandes.get(commandes.size() - 1);
                System.out.println("\n>>> Suppression de la commande ID " + lastCommande.getIdC());
                commandeService.deleteEntity(lastCommande);
            }
*/
            // Vérification après suppression
            System.out.println("\n=== Liste des commandes après suppression ===");
            commandeService.getAllData().forEach(System.out::println);

          /*  // Suppression du dernier paiement ajouté
            if (!paiements.isEmpty()) {
                Paiement lastPaiement = paiements.get(paiements.size() - 1);
                System.out.println("\n>>> Suppression du paiement ID " + lastPaiement.getIdPaiement());
                paiementService.deleteEntity(lastPaiement);
            }
*/
            // Vérification après suppression des paiements
            System.out.println("\n=== Liste des paiements après suppression ===");
            paiementService.getAllData().forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}

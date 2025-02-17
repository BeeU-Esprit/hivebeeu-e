package edu.emmapi.tests;

import edu.emmapi.tools.MyConnection;

import java.sql.*;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = MyConnection.getInstance().getCnx(); // Obtenir la connexion via le singleton

        if (conn == null) {
            System.out.println("Échec de la connexion à la base de données !");
            return;
        }

        while (true) {
            // Affichage du menu principal
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gérer les Services");
            System.out.println("2. Gérer les Abonnements");
            System.out.println("3. Gérer les Types de Services");
            System.out.println("4. Associer un Service à un Abonnement");
            System.out.println("5. Afficher tous les Abonnements avec leurs Services");
            System.out.println("6. Quitter");
            System.out.print("Votre choix : ");
            int choix = lireEntier(scanner, 1, 6, "ID du service à supprimer : "); // Contrôle de saisie pour le choix du menu

            switch (choix) {
                case 1:
                    gererServices(scanner, conn);
                    break;
                case 2:
                    gererAbonnements(scanner, conn);
                    break;
                case 3:
                    gererTypesService(scanner, conn);
                    break;
                case 4:
                    associerServiceAbonnement(scanner, conn);
                    break;
                case 5:
                    afficherAbonnementsAvecServices(conn);
                    break;
                case 6:
                    System.out.println("Au revoir !");
                    try {
                        if (conn != null && !conn.isClosed()) {
                            conn.close();  // Fermer la connexion à la fin
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    scanner.close();
                    return;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    // === Méthodes utilitaires pour les contrôles de saisie ===

    /**
     * Lit un entier entre min et max inclus.
     */
    private static int lireEntier(Scanner scanner, int min, int max, String s) {
        while (true) {
            if (scanner.hasNextInt()) {
                int choix = scanner.nextInt();
                scanner.nextLine(); // Consommer le retour à la ligne
                if (choix >= min && choix <= max) {
                    return choix;
                } else {
                    System.out.println("Erreur : Veuillez saisir un nombre entre " + min + " et " + max + ".");
                }
            } else {
                System.out.println("Erreur : Veuillez saisir un nombre entier valide.");
                scanner.next(); // Consommer l'entrée invalide
            }
        }
    }

    /**
     * Lit une chaîne de caractères non vide.
     */
    private static String lireString(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Erreur : La saisie ne peut pas être vide.");
            }
        }
    }

    /**
     * Lit une date au format YYYY-MM-DD.
     */
    private static Date lireDate(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String dateStr = scanner.nextLine().trim();
            try {
                return Date.valueOf(dateStr); // Convertir en java.sql.Date
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : Format de date invalide. Utilisez le format YYYY-MM-DD.");
            }
        }
    }

    // === Gestion des Services ===

    private static void gererServices(Scanner scanner, Connection conn) {
        while (true) {
            System.out.println("\n=== GESTION DES SERVICES ===");
            System.out.println("1. Ajouter un Service");
            System.out.println("2. Supprimer un Service");
            System.out.println("3. Modifier un Service");
            System.out.println("4. Afficher tous les Services");
            System.out.println("5. Retour au menu principal");
            System.out.print("Votre choix : ");
            int choix = lireEntier(scanner, 1, 5, "ID du service à supprimer : ");

            switch (choix) {
                case 1:
                    ajouterService(scanner, conn);
                    break;
                case 2:
                    supprimerService(scanner, conn);
                    break;
                case 3:
                    modifierService(scanner, conn);
                    break;
                case 4:
                    afficherServices(conn);
                    break;
                case 5:
                    return; // Retour au menu principal
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    private static void ajouterService(Scanner scanner, Connection conn) {
        System.out.println("\n=== Ajouter un Service ===");
        String nomService = lireString(scanner, "Nom du service : ");
        String description = lireString(scanner, "Description du service : ");
        String typeService = lireString(scanner, "Type de service (Activité, Bien-être, Complémentaire) : ");

        String query = "INSERT INTO Service (nomService, description, typeService, estDisponible) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nomService);
            stmt.setString(2, description);
            stmt.setString(3, typeService);
            stmt.setBoolean(4, true); // Par défaut, le service est disponible
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Service ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout du service.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    private static void supprimerService(Scanner scanner, Connection conn) {
        System.out.println("\n=== Supprimer un Service ===");
        int idService = lireEntier(scanner, 1, Integer.MAX_VALUE, "ID du service à supprimer : ");

        String query = "DELETE FROM Service WHERE idService = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idService);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Service supprimé avec succès !");
            } else {
                System.out.println("Aucun service trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    private static void modifierService(Scanner scanner, Connection conn) {
        System.out.println("\n=== Modifier un Service ===");
        int idService = lireEntier(scanner, 1, Integer.MAX_VALUE, "ID du service à modifier : ");
        String nomService = lireString(scanner, "Nouveau nom du service : ");
        String description = lireString(scanner, "Nouvelle description du service : ");

        String query = "UPDATE Service SET nomService = ?, description = ? WHERE idService = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nomService);
            stmt.setString(2, description);
            stmt.setInt(3, idService);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Service modifié avec succès !");
            } else {
                System.out.println("Aucun service trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    private static void afficherServices(Connection conn) {
        String query = "SELECT * FROM Service";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n=== Liste des Services ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("idService") +
                        " | Nom: " + rs.getString("nomService") +
                        " | Description: " + rs.getString("description") +
                        " | Type: " + rs.getString("typeService") +
                        " | Disponible: " + rs.getBoolean("estDisponible"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    // === Gestion des Abonnements ===

    private static void gererAbonnements(Scanner scanner, Connection conn) {
        while (true) {
            System.out.println("\n=== GESTION DES ABONNEMENTS ===");
            System.out.println("1. Ajouter un Abonnement");
            System.out.println("2. Supprimer un Abonnement");
            System.out.println("3. Modifier un Abonnement");
            System.out.println("4. Afficher tous les Abonnements");
            System.out.println("5. Retour au menu principal");
            System.out.print("Votre choix : ");
            int choix = lireEntier(scanner, 1, 5, "ID du service à supprimer : ");

            switch (choix) {
                case 1:
                    ajouterAbonnement(scanner, conn);
                    break;
                case 2:
                    supprimerAbonnement(scanner, conn);
                    break;
                case 3:
                    modifierAbonnement(scanner, conn);
                    break;
                case 4:
                    afficherAbonnements(conn);
                    break;
                case 5:
                    return; // Retour au menu principal
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    private static void ajouterAbonnement(Scanner scanner, Connection conn) {
        System.out.println("\n=== Ajouter un Abonnement ===");
        String nomAbonnement = lireString(scanner, "Nom de l'abonnement : ");
        String typeAbonnement = lireString(scanner, "Type d'abonnement : ");
        Date dateDebut = lireDate(scanner, "Date de début (YYYY-MM-DD) : ");
        String statutAbonnement = lireString(scanner, "Statut de l'abonnement : ");

        String query = "INSERT INTO Abonnement (nomService, typeAbonnement, date, statusAbonnement, estDisponible) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nomAbonnement);
            stmt.setString(2, typeAbonnement);
            stmt.setDate(3, dateDebut);
            stmt.setString(4, statutAbonnement);
            stmt.setBoolean(5, true); // Par défaut, l'abonnement est disponible
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Abonnement ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout de l'abonnement.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    private static void supprimerAbonnement(Scanner scanner, Connection conn) {
        System.out.println("\n=== Supprimer un Abonnement ===");
        int idAbonnement = lireEntier(scanner, 1, Integer.MAX_VALUE, "ID de l'abonnement à supprimer : ");

        String query = "DELETE FROM Abonnement WHERE idAbonnement = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idAbonnement);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Abonnement supprimé avec succès !");
            } else {
                System.out.println("Aucun abonnement trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    private static void modifierAbonnement(Scanner scanner, Connection conn) {
        System.out.println("\n=== Modifier un Abonnement ===");
        int idAbonnement = lireEntier(scanner, 1, Integer.MAX_VALUE, "ID de l'abonnement à modifier : ");
        String nomAbonnement = lireString(scanner, "Nouveau nom de l'abonnement : ");
        String typeAbonnement = lireString(scanner, "Nouveau type d'abonnement : ");
        Date dateDebut = lireDate(scanner, "Nouvelle date de début (YYYY-MM-DD) : ");
        String statutAbonnement = lireString(scanner, "Nouveau statut de l'abonnement : ");

        String query = "UPDATE Abonnement SET nomService = ?, typeAbonnement = ?, date = ?, statusAbonnement = ? WHERE idAbonnement = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nomAbonnement);
            stmt.setString(2, typeAbonnement);
            stmt.setDate(3, dateDebut);
            stmt.setString(4, statutAbonnement);
            stmt.setInt(5, idAbonnement);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Abonnement modifié avec succès !");
            } else {
                System.out.println("Aucun abonnement trouvé avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    private static void afficherAbonnements(Connection conn) {
        String query = "SELECT * FROM Abonnement";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n=== Liste des Abonnements ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("idAbonnement") +
                        " | Nom: " + rs.getString("nomService") +
                        " | Type: " + rs.getString("typeAbonnement") +
                        " | Date: " + rs.getDate("date") +
                        " | Statut: " + rs.getString("statusAbonnement") +
                        " | Disponible: " + rs.getBoolean("estDisponible"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    // === Gestion des Types de Services ===

    private static void gererTypesService(Scanner scanner, Connection conn) {
        while (true) {
            System.out.println("\n=== GESTION DES TYPES DE SERVICES ===");
            System.out.println("1. Ajouter un Type de Service");
            System.out.println("2. Afficher tous les Types de Services");
            System.out.println("3. Retour au menu principal");
            System.out.print("Votre choix : ");
            int choix = lireEntier(scanner, 1, 3, "ID du service à supprimer : ");

            switch (choix) {
                case 1:
                    ajouterTypeService(scanner, conn);
                    break;
                case 2:
                    afficherTypesService(conn);
                    break;
                case 3:
                    return; // Retour au menu principal
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    private static void ajouterTypeService(Scanner scanner, Connection conn) {
        System.out.println("\n=== Ajouter un Type de Service ===");
        String typeService = lireString(scanner, "Type de service (Activité, Bien-être, Complémentaire) : ");

        String query = "INSERT INTO TypeService (typeService) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, typeService);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Type de service ajouté avec succès !");
            } else {
                System.out.println("Échec de l'ajout du type de service.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    private static void afficherTypesService(Connection conn) {
        String query = "SELECT * FROM TypeService";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n=== Liste des Types de Services ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("idTypeService") +
                        " | Type: " + rs.getString("typeService"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    // === Méthodes pour associer un service à un abonnement et afficher les abonnements avec services ===

    private static void associerServiceAbonnement(Scanner scanner, Connection conn) {
        System.out.println("\n=== Associer un Service à un Abonnement ===");
        int idService = lireEntier(scanner, 1, Integer.MAX_VALUE, "ID du service : ");
        int idAbonnement = lireEntier(scanner, 1, Integer.MAX_VALUE, "ID de l'abonnement : ");

        String query = "INSERT INTO AbonnementService (idAbonnement, idService) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idAbonnement);
            stmt.setInt(2, idService);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Service associé à l'abonnement avec succès !");
            } else {
                System.out.println("Échec de l'association du service à l'abonnement.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }

    private static void afficherAbonnementsAvecServices(Connection conn) {
        String query = "SELECT a.idAbonnement, a.nomService AS nomAbonnement, s.nomService AS nomService " +
                "FROM Abonnement a " +
                "JOIN AbonnementService abs ON a.idAbonnement = abs.idAbonnement " + // Remplacez 'as' par 'abs'
                "JOIN Service s ON abs.idService = s.idService"; // Utilisez 'abs' ici aussi

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n=== Liste des Abonnements avec leurs Services ===");
            while (rs.next()) {
                System.out.println("ID Abonnement: " + rs.getInt("idAbonnement") +
                        " | Nom Abonnement: " + rs.getString("nomAbonnement") +
                        " | Service Associé: " + rs.getString("nomService"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }}
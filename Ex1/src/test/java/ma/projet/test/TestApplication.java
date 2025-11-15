package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestApplication {
    
    private static CategorieService categorieService = new CategorieService();
    private static ProduitService produitService = new ProduitService();
    private static CommandeService commandeService = new CommandeService();
    private static LigneCommandeService ligneCommandeService = new LigneCommandeService();
    
    public static void main(String[] args) {
        try {
            // Test 1: Créer des catégories
            System.out.println("=== TEST 1: Création des catégories ===");
            testCreateCategories();
            
            // Test 2: Créer des produits
            System.out.println("\n=== TEST 2: Création des produits ===");
            testCreateProduits();
            
            // Test 3: Créer des commandes
            System.out.println("\n=== TEST 3: Création des commandes ===");
            testCreateCommandes();
            
            // Test 4: Créer des lignes de commande
            System.out.println("\n=== TEST 4: Création des lignes de commande ===");
            testCreateLignesCommande();
            
            // Test 5: Afficher les produits par catégorie
            System.out.println("\n=== TEST 5: Produits par catégorie ===");
            testProduitsParCategorie();
            
            // Test 6: Produits commandés entre deux dates
            System.out.println("\n=== TEST 6: Produits commandés entre deux dates ===");
            testProduitsCommandesEntreDates();
            
            // Test 7: Produits d'une commande donnée
            System.out.println("\n=== TEST 7: Produits d'une commande ===");
            testProduitsParCommande();
            
            // Test 8: Produits avec prix > 100 DH (requête nommée)
            System.out.println("\n=== TEST 8: Produits avec prix > 100 DH ===");
            testProduitsPrixSuperieur100();
            
            // Test 9: Afficher toutes les données
            System.out.println("\n=== TEST 9: Affichage de toutes les données ===");
            testAfficherToutesLesDonnees();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
    
    private static void testCreateCategories() {
        // Vérifier si les catégories existent déjà
        List<Categorie> existing = categorieService.findAll();
        if (existing.size() >= 3) {
            System.out.println("Les catégories existent déjà.");
            return;
        }
        
        Categorie cat1 = new Categorie("CAT001", "Ordinateurs");
        Categorie cat2 = new Categorie("CAT002", "Accessoires");
        Categorie cat3 = new Categorie("CAT003", "Périphériques");
        
        if (categorieService.create(cat1)) {
            System.out.println("Catégorie créée: " + cat1);
        }
        if (categorieService.create(cat2)) {
            System.out.println("Catégorie créée: " + cat2);
        }
        if (categorieService.create(cat3)) {
            System.out.println("Catégorie créée: " + cat3);
        }
    }
    
    private static void testCreateProduits() {
        List<Categorie> categories = categorieService.findAll();
        if (categories.isEmpty()) {
            System.out.println("Aucune catégorie trouvée. Veuillez d'abord créer des catégories.");
            return;
        }
        
        // Vérifier si les produits existent déjà
        List<Produit> existing = produitService.findAll();
        if (existing.size() >= 5) {
            System.out.println("Les produits existent déjà.");
            return;
        }
        
        Categorie cat1 = categories.get(0);
        Categorie cat2 = categories.size() > 1 ? categories.get(1) : cat1;
        
        Produit p1 = new Produit("ES12", 120.0f, cat1);
        Produit p2 = new Produit("ZR85", 100.0f, cat1);
        Produit p3 = new Produit("EE85", 200.0f, cat2);
        Produit p4 = new Produit("AB99", 80.0f, cat2);
        Produit p5 = new Produit("CD77", 150.0f, cat1);
        
        if (produitService.create(p1)) {
            System.out.println("Produit créé: " + p1);
        }
        if (produitService.create(p2)) {
            System.out.println("Produit créé: " + p2);
        }
        if (produitService.create(p3)) {
            System.out.println("Produit créé: " + p3);
        }
        if (produitService.create(p4)) {
            System.out.println("Produit créé: " + p4);
        }
        if (produitService.create(p5)) {
            System.out.println("Produit créé: " + p5);
        }
    }
    
    private static void testCreateCommandes() {
        Calendar cal = Calendar.getInstance();
        
        // Commande 1 - 14 Mars 2013
        cal.set(2013, Calendar.MARCH, 14);
        Commande cmd1 = new Commande(cal.getTime());
        if (commandeService.create(cmd1)) {
            System.out.println("Commande créée: " + cmd1);
        }
        
        // Commande 2 - 20 Mars 2013
        cal.set(2013, Calendar.MARCH, 20);
        Commande cmd2 = new Commande(cal.getTime());
        if (commandeService.create(cmd2)) {
            System.out.println("Commande créée: " + cmd2);
        }
        
        // Commande 3 - 1 Avril 2013
        cal.set(2013, Calendar.APRIL, 1);
        Commande cmd3 = new Commande(cal.getTime());
        if (commandeService.create(cmd3)) {
            System.out.println("Commande créée: " + cmd3);
        }
    }
    
    private static void testCreateLignesCommande() {
        List<Produit> produits = produitService.findAll();
        List<Commande> commandes = commandeService.findAll();
        
        if (produits.isEmpty() || commandes.isEmpty()) {
            System.out.println("Produits ou commandes manquants.");
            return;
        }
        
        // Vérifier si les lignes de commande existent déjà
        List<LigneCommandeProduit> existing = ligneCommandeService.findAll();
        if (existing.size() >= 3) {
            System.out.println("Les lignes de commande existent déjà.");
            return;
        }
        
        // Utiliser la commande ID 4 comme dans l'exemple, ou créer une nouvelle commande
        Commande cmd4 = commandeService.findById(4);
        if (cmd4 == null) {
            // Créer une commande avec date du 14 mars 2013
            Calendar cal = Calendar.getInstance();
            cal.set(2013, Calendar.MARCH, 14);
            cmd4 = new Commande(cal.getTime());
            commandeService.create(cmd4);
            System.out.println("Commande 4 créée pour l'exemple.");
        }
        
        if (produits.size() >= 3) {
            // Trouver les produits par référence
            Produit p1 = null, p2 = null, p3 = null;
            for (Produit p : produits) {
                if ("ES12".equals(p.getReference())) p1 = p;
                if ("ZR85".equals(p.getReference())) p2 = p;
                if ("EE85".equals(p.getReference())) p3 = p;
            }
            
            if (p1 != null && p2 != null && p3 != null) {
                LigneCommandeProduit lcp1 = new LigneCommandeProduit(7, p1, cmd4); // ES12
                LigneCommandeProduit lcp2 = new LigneCommandeProduit(14, p2, cmd4); // ZR85
                LigneCommandeProduit lcp3 = new LigneCommandeProduit(5, p3, cmd4); // EE85
                
                if (ligneCommandeService.create(lcp1)) {
                    System.out.println("Ligne de commande créée: " + lcp1);
                }
                if (ligneCommandeService.create(lcp2)) {
                    System.out.println("Ligne de commande créée: " + lcp2);
                }
                if (ligneCommandeService.create(lcp3)) {
                    System.out.println("Ligne de commande créée: " + lcp3);
                }
            }
        }
        
        // Ajouter quelques lignes pour les autres commandes si nécessaire
        if (commandes.size() > 1 && produits.size() >= 4 && existing.size() < 5) {
            Commande cmd2 = commandes.size() > 1 ? commandes.get(1) : commandes.get(0);
            if (cmd2.getId() != cmd4.getId()) {
                Produit p1 = produits.get(0);
                Produit p4 = produits.size() >= 4 ? produits.get(3) : produits.get(0);
                LigneCommandeProduit lcp4 = new LigneCommandeProduit(3, p1, cmd2);
                LigneCommandeProduit lcp5 = new LigneCommandeProduit(10, p4, cmd2);
                
                ligneCommandeService.create(lcp4);
                ligneCommandeService.create(lcp5);
            }
        }
    }
    
    private static void testProduitsParCategorie() {
        List<Categorie> categories = categorieService.findAll();
        if (categories.isEmpty()) {
            System.out.println("Aucune catégorie trouvée.");
            return;
        }
        
        for (Categorie cat : categories) {
            System.out.println("\nCatégorie: " + cat.getLibelle() + " (" + cat.getCode() + ")");
            List<Produit> produits = produitService.findByCategorie(cat);
            if (produits.isEmpty()) {
                System.out.println("  Aucun produit dans cette catégorie.");
            } else {
                System.out.println("  Produits:");
                for (Produit p : produits) {
                    System.out.println("    - " + p.getReference() + " : " + p.getPrix() + " DH");
                }
            }
        }
    }
    
    private static void testProduitsCommandesEntreDates() {
        Calendar cal = Calendar.getInstance();
        cal.set(2013, Calendar.MARCH, 1);
        Date dateDebut = cal.getTime();
        
        cal.set(2013, Calendar.MARCH, 31);
        Date dateFin = cal.getTime();
        
        System.out.println("Période: " + new SimpleDateFormat("dd/MM/yyyy").format(dateDebut) + 
                          " - " + new SimpleDateFormat("dd/MM/yyyy").format(dateFin));
        
        List<Produit> produits = produitService.findProduitsCommandesEntreDates(dateDebut, dateFin);
        if (produits.isEmpty()) {
            System.out.println("Aucun produit commandé dans cette période.");
        } else {
            System.out.println("Produits commandés:");
            for (Produit p : produits) {
                System.out.println("  - " + p.getReference() + " : " + p.getPrix() + " DH");
            }
        }
    }
    
    private static void testProduitsParCommande() {
        // Utiliser la commande ID 4 comme dans l'exemple
        Commande cmd4 = commandeService.findById(4);
        if (cmd4 == null) {
            // Si la commande 4 n'existe pas, utiliser la première commande disponible
            List<Commande> commandes = commandeService.findAll();
            if (commandes.isEmpty()) {
                System.out.println("Aucune commande trouvée.");
                return;
            }
            cmd4 = commandes.get(0);
        }
        produitService.afficherProduitsParCommande(cmd4.getId());
    }
    
    private static void testProduitsPrixSuperieur100() {
        List<Produit> produits = produitService.findProduitsPrixSuperieur100();
        if (produits.isEmpty()) {
            System.out.println("Aucun produit avec un prix supérieur à 100 DH.");
        } else {
            System.out.println("Produits avec prix > 100 DH (requête nommée):");
            System.out.println("Référence   Prix    Catégorie");
            System.out.println("-----------------------------------");
            for (Produit p : produits) {
                System.out.printf("%-11s %-7.0f DH %s%n", 
                    p.getReference(), p.getPrix(), p.getCategorie().getLibelle());
            }
        }
    }
    
    private static void testAfficherToutesLesDonnees() {
        System.out.println("\n--- Toutes les catégories ---");
        List<Categorie> categories = categorieService.findAll();
        for (Categorie cat : categories) {
            System.out.println(cat);
        }
        
        System.out.println("\n--- Tous les produits ---");
        List<Produit> produits = produitService.findAll();
        for (Produit p : produits) {
            System.out.println(p);
        }
        
        System.out.println("\n--- Toutes les commandes ---");
        List<Commande> commandes = commandeService.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Commande cmd : commandes) {
            System.out.println("ID: " + cmd.getId() + ", Date: " + sdf.format(cmd.getDate()));
        }
        
        System.out.println("\n--- Toutes les lignes de commande ---");
        List<LigneCommandeProduit> lignes = ligneCommandeService.findAll();
        for (LigneCommandeProduit lcp : lignes) {
            System.out.println(lcp);
        }
    }
}


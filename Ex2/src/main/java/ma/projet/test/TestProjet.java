package ma.projet.test;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestProjet {
    public static void main(String[] args) {
        try {
            // Vider la base de données
            HibernateUtil.clearDatabase();
            
            // Création des services
            EmployeService employeService = new EmployeService();
            ProjetService projetService = new ProjetService();
            TacheService tacheService = new TacheService();
            EmployeTacheService employeTacheService = new EmployeTacheService();
            
            // Création d'employés
            Employe emp1 = new Employe("Alami", "Ahmed", "0612345678");
            Employe emp2 = new Employe("Bennani", "Fatima", "0623456789");
            employeService.create(emp1);
            employeService.create(emp2);
            
            // Création de projets
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateDebutProjet = sdf.parse("2013-01-14");
            Date dateFinProjet = sdf.parse("2013-05-30");
            Projet projet1 = new Projet("Gestion de stock", dateDebutProjet, dateFinProjet, emp1);
            projetService.create(projet1);
            
            // Création de tâches
            Date dateDebutTache1 = sdf.parse("2013-02-01");
            Date dateFinTache1 = sdf.parse("2013-02-28");
            Tache tache1 = new Tache("Analyse", dateDebutTache1, dateFinTache1, 5000, projet1);
            tacheService.create(tache1);
            
            Date dateDebutTache2 = sdf.parse("2013-03-01");
            Date dateFinTache2 = sdf.parse("2013-03-31");
            Tache tache2 = new Tache("Conception", dateDebutTache2, dateFinTache2, 8000, projet1);
            tacheService.create(tache2);
            
            Date dateDebutTache3 = sdf.parse("2013-04-01");
            Date dateFinTache3 = sdf.parse("2013-04-30");
            Tache tache3 = new Tache("Développement", dateDebutTache3, dateFinTache3, 12000, projet1);
            tacheService.create(tache3);
            
            Date dateDebutTache4 = sdf.parse("2013-05-01");
            Date dateFinTache4 = sdf.parse("2013-05-15");
            Tache tache4 = new Tache("Test", dateDebutTache4, dateFinTache4, 500, projet1);
            tacheService.create(tache4);
            
            // Création des associations EmployeTache
            Date dateDebutReelle1 = sdf.parse("2013-02-10");
            Date dateFinReelle1 = sdf.parse("2013-02-20");
            EmployeTache et1 = new EmployeTache(dateDebutReelle1, dateFinReelle1, emp1, tache1);
            employeTacheService.create(et1);
            
            Date dateDebutReelle2 = sdf.parse("2013-03-10");
            Date dateFinReelle2 = sdf.parse("2013-03-15");
            EmployeTache et2 = new EmployeTache(dateDebutReelle2, dateFinReelle2, emp1, tache2);
            employeTacheService.create(et2);
            
            Date dateDebutReelle3 = sdf.parse("2013-04-10");
            Date dateFinReelle3 = sdf.parse("2013-04-25");
            EmployeTache et3 = new EmployeTache(dateDebutReelle3, dateFinReelle3, emp2, tache3);
            employeTacheService.create(et3);
            
            // Test des méthodes spécifiques
            System.out.println("=== Test 1: Afficher les tâches réalisées par un employé ===");
            employeService.afficherTachesRealiseesParEmploye(emp1.getId());
            
            System.out.println("\n=== Test 2: Afficher les projets gérés par un employé ===");
            employeService.afficherProjetsGeresParEmploye(emp1.getId());
            
            System.out.println("\n=== Test 3: Afficher les tâches planifiées pour un projet ===");
            projetService.afficherTachesPlanifieesPourProjet(projet1.getId());
            
            System.out.println("\n=== Test 4: Afficher les tâches réalisées avec dates réelles ===");
            projetService.afficherTachesRealiseesAvecDatesReelles(projet1.getId());
            
            System.out.println("\n=== Test 5: Afficher les tâches dont le prix > 1000 DH ===");
            tacheService.afficherTachesPrixSuperieurA1000();
            
            System.out.println("\n=== Test 6: Afficher les tâches réalisées entre deux dates ===");
            Date dateDebut = sdf.parse("2013-02-01");
            Date dateFin = sdf.parse("2013-03-31");
            tacheService.afficherTachesRealiseesEntreDeuxDates(dateDebut, dateFin);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


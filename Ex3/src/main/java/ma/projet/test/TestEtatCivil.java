package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestEtatCivil {
    
    private static HommeService hommeService = new HommeService();
    private static FemmeService femmeService = new FemmeService();
    private static MariageService mariageService = new MariageService();
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        try {
            // Initialiser Hibernate
            HibernateUtil.getSessionFactory();
            
            System.out.println("=== Création des données de test ===\n");
            creerDonneesTest();
            
            System.out.println("\n=== 1. Afficher la liste des femmes ===\n");
            afficherListeFemmes();
            
            System.out.println("\n=== 2. Afficher la femme la plus âgée ===\n");
            afficherFemmeLaPlusAgee();
            
            System.out.println("\n=== 3. Afficher les épouses d'un homme donné ===\n");
            afficherEpousesHomme();
            
            System.out.println("\n=== 4. Afficher le nombre d'enfants d'une femme entre deux dates ===\n");
            afficherNombreEnfantsFemme();
            
            System.out.println("\n=== 5. Afficher les femmes mariées deux fois ou plus ===\n");
            afficherFemmesMarieesDeuxFois();
            
            System.out.println("\n=== 6. Afficher les hommes mariés à quatre femmes entre deux dates ===\n");
            afficherHommesMariesAQuatreFemmes();
            
            System.out.println("\n=== 7. Afficher les mariages d'un homme avec tous les détails ===\n");
            afficherMariagesHommeDetails();
            
            // Fermer Hibernate
            HibernateUtil.shutdown();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void creerDonneesTest() throws ParseException {
        // Créer 10 femmes
        Femme f1 = new Femme("SAFI", "SALIMA", "0612345678", "Casablanca", sdf.parse("15/05/1970"));
        Femme f2 = new Femme("ALAMI", "KARIMA", "0612345679", "Rabat", sdf.parse("20/06/1975"));
        Femme f3 = new Femme("RAMI", "SALIMA", "0612345680", "Fès", sdf.parse("10/03/1968"));
        Femme f4 = new Femme("ALI", "AMAL", "0612345681", "Marrakech", sdf.parse("25/07/1972"));
        Femme f5 = new Femme("ALAOUI", "WAFA", "0612345682", "Tanger", sdf.parse("12/04/1978"));
        Femme f6 = new Femme("BENALI", "FATIMA", "0612345683", "Agadir", sdf.parse("08/09/1975"));
        Femme f7 = new Femme("CHAKIR", "NADIA", "0612345684", "Oujda", sdf.parse("18/11/1980"));
        Femme f8 = new Femme("DOUIRI", "HIND", "0612345685", "Meknes", sdf.parse("22/02/1973"));
        Femme f9 = new Femme("ELMALKI", "SOUAD", "0612345686", "Tétouan", sdf.parse("30/08/1976"));
        Femme f10 = new Femme("FADLI", "LATIFA", "0612345687", "Salé", sdf.parse("05/12/1974"));
        
        femmeService.create(f1);
        femmeService.create(f2);
        femmeService.create(f3);
        femmeService.create(f4);
        femmeService.create(f5);
        femmeService.create(f6);
        femmeService.create(f7);
        femmeService.create(f8);
        femmeService.create(f9);
        femmeService.create(f10);
        
        System.out.println("10 femmes créées.");
        
        // Créer 5 hommes
        Homme h1 = new Homme("SAFI", "SAID", "0623456789", "Casablanca", sdf.parse("10/01/1965"));
        Homme h2 = new Homme("ALAOUI", "MOHAMED", "0623456790", "Rabat", sdf.parse("15/03/1968"));
        Homme h3 = new Homme("BENALI", "AHMED", "0623456791", "Fès", sdf.parse("20/05/1970"));
        Homme h4 = new Homme("CHAKIR", "YOUSSEF", "0623456792", "Marrakech", sdf.parse("25/07/1972"));
        Homme h5 = new Homme("DOUIRI", "KARIM", "0623456793", "Tanger", sdf.parse("30/09/1967"));
        
        hommeService.create(h1);
        hommeService.create(h2);
        hommeService.create(h3);
        hommeService.create(h4);
        hommeService.create(h5);
        
        System.out.println("5 hommes créés.");
        
        // Créer des mariages pour l'exemple demandé
        // Homme h1 (SAFI SAID) avec plusieurs mariages
        Mariage m1 = new Mariage(sdf.parse("03/09/1989"), sdf.parse("03/09/1990"), 0, h1, f2); // KARIMA ALAMI - échoué
        Mariage m2 = new Mariage(sdf.parse("03/09/1990"), null, 4, h1, f3); // SALIMA RAMI - en cours
        Mariage m3 = new Mariage(sdf.parse("03/09/1995"), null, 2, h1, f4); // AMAL ALI - en cours
        Mariage m4 = new Mariage(sdf.parse("04/11/2000"), null, 3, h1, f5); // WAFA ALAOUI - en cours
        
        mariageService.create(m1);
        mariageService.create(m2);
        mariageService.create(m3);
        mariageService.create(m4);
        
        // Autres mariages pour tester les requêtes
        Mariage m5 = new Mariage(sdf.parse("01/01/1995"), null, 2, h2, f6);
        Mariage m6 = new Mariage(sdf.parse("01/01/2000"), null, 1, h2, f7);
        Mariage m7 = new Mariage(sdf.parse("01/01/2005"), null, 3, h2, f8);
        Mariage m8 = new Mariage(sdf.parse("01/01/2010"), null, 2, h2, f9);
        
        mariageService.create(m5);
        mariageService.create(m6);
        mariageService.create(m7);
        mariageService.create(m8);
        
        // Femme f1 mariée deux fois
        Mariage m9 = new Mariage(sdf.parse("01/06/1992"), sdf.parse("01/06/1998"), 2, h3, f1);
        Mariage m10 = new Mariage(sdf.parse("01/06/2000"), null, 1, h4, f1);
        
        mariageService.create(m9);
        mariageService.create(m10);
        
        // Femme f6 mariée deux fois aussi
        Mariage m11 = new Mariage(sdf.parse("01/01/1990"), sdf.parse("01/01/1994"), 1, h3, f6);
        mariageService.create(m11);
        
        System.out.println("Mariages créés.");
    }

    private static void afficherListeFemmes() {
        List<Femme> femmes = femmeService.findAll();
        System.out.println("Liste des femmes (" + femmes.size() + ") :");
        int i = 1;
        for (Femme f : femmes) {
            System.out.println(i + ". " + f.getNom() + " " + f.getPrenom() + 
                             " - Né(e) le " + sdf.format(f.getDateNaissance()) +
                             " - Tél: " + f.getTelephone());
            i++;
        }
    }

    private static void afficherFemmeLaPlusAgee() {
        Femme femme = femmeService.getFemmeLaPlusAgee();
        if (femme != null) {
            System.out.println("Femme la plus âgée : " + femme.getNom() + " " + femme.getPrenom() +
                             " - Né(e) le " + sdf.format(femme.getDateNaissance()));
        } else {
            System.out.println("Aucune femme trouvée.");
        }
    }

    private static void afficherEpousesHomme() {
        // Récupérer le premier homme
        List<Homme> hommes = hommeService.findAll();
        if (!hommes.isEmpty()) {
            Homme homme = hommes.get(0);
            System.out.println("Épouses de " + homme.getNom() + " " + homme.getPrenom() + 
                             " entre le 01/01/1990 et le 31/12/2005 :");
            try {
                List<Mariage> mariages = hommeService.getEpousesEntreDeuxDates(
                    homme.getId(), 
                    sdf.parse("01/01/1990"), 
                    sdf.parse("31/12/2005")
                );
                int i = 1;
                for (Mariage m : mariages) {
                    System.out.println(i + ". " + m.getFemme().getNom() + " " + 
                                     m.getFemme().getPrenom() + 
                                     " - Mariage le " + sdf.format(m.getDateDebut()) +
                                     " - Enfants: " + m.getNbrEnfant());
                    i++;
                }
                if (mariages.isEmpty()) {
                    System.out.println("Aucune épouse trouvée dans cette période.");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private static void afficherNombreEnfantsFemme() {
        List<Femme> femmes = femmeService.findAll();
        if (!femmes.isEmpty()) {
            Femme femme = femmes.get(0);
            try {
                int nbrEnfants = femmeService.getNombreEnfantsEntreDeuxDates(
                    femme.getId(),
                    sdf.parse("01/01/1990"),
                    sdf.parse("31/12/2010")
                );
                System.out.println("Nombre d'enfants de " + femme.getNom() + " " + 
                                 femme.getPrenom() + 
                                 " entre le 01/01/1990 et le 31/12/2010 : " + nbrEnfants);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private static void afficherFemmesMarieesDeuxFois() {
        List<Femme> femmes = femmeService.getFemmesMarieesAuMoinsDeuxFois();
        System.out.println("Femmes mariées au moins deux fois (" + femmes.size() + ") :");
        int i = 1;
        for (Femme f : femmes) {
            System.out.println(i + ". " + f.getNom() + " " + f.getPrenom());
            i++;
        }
        if (femmes.isEmpty()) {
            System.out.println("Aucune femme trouvée.");
        }
    }

    private static void afficherHommesMariesAQuatreFemmes() {
        try {
            Date dateDebut = sdf.parse("01/01/1990");
            Date dateFin = sdf.parse("31/12/2010");
            
            long nombre = mariageService.getNombreHommesMariesAQuatreFemmes(dateDebut, dateFin);
            System.out.println("Nombre d'hommes mariés à quatre femmes entre le 01/01/1990 et le 31/12/2010 : " + nombre);
            
            List<Homme> hommes = mariageService.getHommesMariesAQuatreFemmes(dateDebut, dateFin);
            System.out.println("Détails des hommes :");
            int i = 1;
            for (Homme h : hommes) {
                System.out.println(i + ". " + h.getNom() + " " + h.getPrenom());
                i++;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void afficherMariagesHommeDetails() {
        // Récupérer l'homme h1 (SAFI SAID) qui a plusieurs mariages
        List<Homme> hommes = hommeService.findAll();
        for (Homme h : hommes) {
            if (h.getNom().equals("SAFI") && h.getPrenom().equals("SAID")) {
                hommeService.afficherMariagesHomme(h.getId());
                return;
            }
        }
        System.out.println("Homme SAFI SAID non trouvé.");
    }
}


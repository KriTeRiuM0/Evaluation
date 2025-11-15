package ma.projet.service;

import ma.projet.classes.*;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ma.projet.util.HibernateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProduitService implements IDao<Produit> {
    
    @Override
    public boolean create(Produit o) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Produit o) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Produit o) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    @Override
    public Produit findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Produit.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Produit> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("from Produit", Produit.class).list();
        } finally {
            session.close();
        }
    }
    
    // Afficher la liste des produits par catégorie
    public List<Produit> findByCategorie(Categorie categorie) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Produit> query = session.createQuery(
                "from Produit p where p.categorie = :categorie", Produit.class);
            query.setParameter("categorie", categorie);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    // Afficher les produits commandés entre deux dates
    public List<Produit> findProduitsCommandesEntreDates(Date dateDebut, Date dateFin) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query<Produit> query = session.createQuery(
                "SELECT DISTINCT p FROM Produit p " +
                "JOIN p.ligneCommandes lcp " +
                "JOIN lcp.commande c " +
                "WHERE c.date BETWEEN :dateDebut AND :dateFin", Produit.class);
            query.setParameter("dateDebut", dateDebut);
            query.setParameter("dateFin", dateFin);
            return query.list();
        } finally {
            session.close();
        }
    }
    
    // Afficher les produits commandés dans une commande donnée
    public void afficherProduitsParCommande(int commandeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Commande commande = session.get(Commande.class, commandeId);
            if (commande == null) {
                System.out.println("Commande non trouvée avec l'ID: " + commandeId);
                return;
            }
            
            // Format de date en français avec première lettre majuscule
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
            String dateFormatee = sdf.format(commande.getDate());
            // Capitaliser la première lettre du mois (après le jour et l'espace)
            String[] parts = dateFormatee.split(" ", 3);
            if (parts.length >= 2 && parts[1].length() > 0) {
                parts[1] = parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1);
                dateFormatee = parts[0] + " " + parts[1] + (parts.length > 2 ? " " + parts[2] : "");
            }
            
            System.out.println("\nCommande : " + commande.getId() + "     Date : " + dateFormatee);
            System.out.println();
            System.out.println();
            System.out.println("Liste des produits :");
            System.out.println();
            System.out.println();
            System.out.println("Référence   Prix    Quantité");
            System.out.println();
            System.out.println();
            
            Query<LigneCommandeProduit> query = session.createQuery(
                "SELECT DISTINCT lcp FROM LigneCommandeProduit lcp WHERE lcp.commande.id = :commandeId", 
                LigneCommandeProduit.class);
            query.setParameter("commandeId", commandeId);
            
            List<LigneCommandeProduit> lignes = query.list();
            
            for (LigneCommandeProduit ligne : lignes) {
                Produit p = ligne.getProduit();
                System.out.printf("%-11s %-6.0f DH  %d%n", 
                    p.getReference(), p.getPrix(), ligne.getQuantite());
                System.out.println();
                System.out.println();
            }
            
        } finally {
            session.close();
        }
    }
    
    // Afficher la liste des produits dont le prix est supérieur à 100 DH (requête nommée)
    public List<Produit> findProduitsPrixSuperieur100() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            @SuppressWarnings("unchecked")
            Query<Produit> query = session.getNamedQuery("Produit.findByPrixGreaterThan");
            query.setParameter("prix", 100.0f);
            return query.list();
        } finally {
            session.close();
        }
    }
}


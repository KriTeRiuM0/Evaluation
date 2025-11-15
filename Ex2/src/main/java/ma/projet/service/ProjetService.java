package ma.projet.service;

import ma.projet.dao.IDao;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.classes.EmployeTache;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjetService implements IDao<Projet> {
    
    @Override
    public boolean create(Projet o) {
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
    public boolean update(Projet o) {
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
    public boolean delete(Projet o) {
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
    public Projet findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Projet.class, id);
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Projet> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Projet", Projet.class).list();
        } finally {
            session.close();
        }
    }
    
    public void afficherTachesPlanifieesPourProjet(int projetId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Projet projet = session.get(Projet.class, projetId);
            if (projet != null) {
                System.out.println("Projet : " + projet.getId() + "\tNom : " + projet.getNom());
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
                System.out.println("Date début : " + sdf.format(projet.getDateDebut()));
                System.out.println("Liste des tâches planifiées:");
                System.out.println("Num\tNom\t\tDate Début\tDate Fin\tPrix");
                List<Tache> taches = projet.getTaches();
                if (taches != null) {
                    for (Tache tache : taches) {
                        System.out.println(tache.getId() + "\t" + tache.getNom() + "\t\t" + 
                                         tache.getDateDebut() + "\t" + tache.getDateFin() + 
                                         "\t" + tache.getPrix());
                    }
                }
            }
        } finally {
            session.close();
        }
    }
    
    public void afficherTachesRealiseesAvecDatesReelles(int projetId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Projet projet = session.get(Projet.class, projetId);
            if (projet != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
                SimpleDateFormat sdfShort = new SimpleDateFormat("dd/MM/yyyy");
                String dateDebut = sdf.format(projet.getDateDebut());
                // Mettre la première lettre du mois en majuscule (après "dd ")
                if (dateDebut.length() > 3) {
                    String jour = dateDebut.substring(0, 3);
                    String mois = dateDebut.substring(3);
                    if (mois.length() > 0) {
                        mois = Character.toUpperCase(mois.charAt(0)) + mois.substring(1);
                    }
                    dateDebut = jour + mois;
                }
                System.out.println("Projet : " + projet.getId() + "      Nom : " + projet.getNom() + 
                                 "     Date début : " + dateDebut);
                System.out.println();
                System.out.println("Liste des tâches:");
                System.out.println();
                System.out.println("Num Nom            Date Début Réelle   Date Fin Réelle");
                List<Tache> taches = projet.getTaches();
                if (taches != null) {
                    for (Tache tache : taches) {
                        List<EmployeTache> employeTaches = tache.getEmployeTaches();
                        if (employeTaches != null && !employeTaches.isEmpty()) {
                            for (EmployeTache et : employeTaches) {
                                System.out.println(String.format("%-2d  %-15s%-18s%s", 
                                    tache.getId(), 
                                    tache.getNom(), 
                                    sdfShort.format(et.getDateDebutReelle()), 
                                    sdfShort.format(et.getDateFinReelle())));
                            }
                        }
                    }
                }
            }
        } finally {
            session.close();
        }
    }
}


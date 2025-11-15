package ma.projet.service;

import ma.projet.dao.IDao;
import ma.projet.classes.Tache;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;
import java.util.List;

public class TacheService implements IDao<Tache> {
    
    @Override
    public boolean create(Tache o) {
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
    public boolean update(Tache o) {
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
    public boolean delete(Tache o) {
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
    public Tache findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Tache.class, id);
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Tache> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Tache", Tache.class).list();
        } finally {
            session.close();
        }
    }
    
    public void afficherTachesPrixSuperieurA1000() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Tache> taches = session.createNamedQuery("Tache.findByPrixSuperieur", Tache.class)
                                        .setParameter("prix", 1000.0)
                                        .list();
            System.out.println("Tâches dont le prix est supérieur à 1000 DH:");
            System.out.println("Num\tNom\t\tDate Début\tDate Fin\tPrix");
            for (Tache tache : taches) {
                System.out.println(tache.getId() + "\t" + tache.getNom() + "\t\t" + 
                                 tache.getDateDebut() + "\t" + tache.getDateFin() + 
                                 "\t" + tache.getPrix());
            }
        } finally {
            session.close();
        }
    }
    
    public void afficherTachesRealiseesEntreDeuxDates(Date dateDebut, Date dateFin) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Tache> taches = session.createQuery(
                "SELECT DISTINCT t FROM Tache t JOIN t.employeTaches et " +
                "WHERE et.dateDebutReelle >= :dateDebut AND et.dateFinReelle <= :dateFin", 
                Tache.class)
                .setParameter("dateDebut", dateDebut)
                .setParameter("dateFin", dateFin)
                .list();
            System.out.println("Tâches réalisées entre " + dateDebut + " et " + dateFin + ":");
            System.out.println("Num\tNom\t\tDate Début\tDate Fin\tPrix");
            for (Tache tache : taches) {
                System.out.println(tache.getId() + "\t" + tache.getNom() + "\t\t" + 
                                 tache.getDateDebut() + "\t" + tache.getDateFin() + 
                                 "\t" + tache.getPrix());
            }
        } finally {
            session.close();
        }
    }
}


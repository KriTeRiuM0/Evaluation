package ma.projet.service;

import ma.projet.dao.IDao;
import ma.projet.classes.Employe;
import ma.projet.classes.Tache;
import ma.projet.classes.Projet;
import ma.projet.classes.EmployeTache;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class EmployeService implements IDao<Employe> {
    
    @Override
    public boolean create(Employe o) {
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
    public boolean update(Employe o) {
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
    public boolean delete(Employe o) {
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
    public Employe findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.get(Employe.class, id);
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Employe> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Employe", Employe.class).list();
        } finally {
            session.close();
        }
    }
    
    public void afficherTachesRealiseesParEmploye(int employeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Employe employe = session.get(Employe.class, employeId);
            if (employe != null) {
                System.out.println("Tâches réalisées par " + employe.getPrenom() + " " + employe.getNom() + " :");
                System.out.println("Num\tNom\t\tDate Début Réelle\tDate Fin Réelle");
                List<EmployeTache> employeTaches = employe.getEmployeTaches();
                if (employeTaches != null) {
                    for (EmployeTache et : employeTaches) {
                        Tache tache = et.getTache();
                        System.out.println(tache.getId() + "\t" + tache.getNom() + "\t\t" + 
                                         et.getDateDebutReelle() + "\t" + et.getDateFinReelle());
                    }
                }
            }
        } finally {
            session.close();
        }
    }
    
    public void afficherProjetsGeresParEmploye(int employeId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Employe employe = session.get(Employe.class, employeId);
            if (employe != null) {
                System.out.println("Projets gérés par " + employe.getPrenom() + " " + employe.getNom() + " :");
                List<Projet> projets = employe.getProjets();
                if (projets != null) {
                    for (Projet projet : projets) {
                        System.out.println("Projet : " + projet.getId() + "\tNom : " + projet.getNom() + 
                                         "\tDate début : " + projet.getDateDebut() + 
                                         "\tDate fin : " + projet.getDateFin());
                    }
                }
            }
        } finally {
            session.close();
        }
    }
}


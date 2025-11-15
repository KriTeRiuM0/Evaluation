package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class FemmeService implements IDao<Femme> {
    
    private SessionFactory sessionFactory;

    public FemmeService() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public boolean create(Femme o) {
        Session session = sessionFactory.openSession();
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
    public boolean update(Femme o) {
        Session session = sessionFactory.openSession();
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
    public boolean delete(Femme o) {
        Session session = sessionFactory.openSession();
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
    public Femme findById(int id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Femme.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Femme> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Femme", Femme.class).list();
        } finally {
            session.close();
        }
    }

    /**
     * Retourne la femme la plus âgée
     */
    public Femme getFemmeLaPlusAgee() {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Femme ORDER BY dateNaissance ASC";
            List<Femme> femmes = session.createQuery(hql, Femme.class)
                    .setMaxResults(1)
                    .list();
            return femmes.isEmpty() ? null : femmes.get(0);
        } finally {
            session.close();
        }
    }

    /**
     * Requête native nommée retournant le nombre d'enfants d'une femme entre deux dates
     */
    public int getNombreEnfantsEntreDeuxDates(int femmeId, Date dateDebut, Date dateFin) {
        Session session = sessionFactory.openSession();
        try {
            String sql = "SELECT COALESCE(SUM(m.nbrEnfant), 0) " +
                        "FROM mariage m " +
                        "WHERE m.femme_id = :femmeId " +
                        "AND m.dateDebut >= :dateDebut AND m.dateDebut <= :dateFin";
            
            Object result = session.createNativeQuery(sql)
                    .setParameter("femmeId", femmeId)
                    .setParameter("dateDebut", dateDebut)
                    .setParameter("dateFin", dateFin)
                    .uniqueResult();
            
            return result != null ? ((Number) result).intValue() : 0;
        } finally {
            session.close();
        }
    }

    /**
     * Requête nommée retournant les femmes mariées au moins deux fois
     */
    public List<Femme> getFemmesMarieesAuMoinsDeuxFois() {
        Session session = sessionFactory.openSession();
        try {
            String hql = "SELECT f FROM Femme f " +
                        "WHERE (SELECT COUNT(m) FROM Mariage m WHERE m.femme.id = f.id) >= 2";
            
            return session.createQuery(hql, Femme.class).list();
        } finally {
            session.close();
        }
    }
}


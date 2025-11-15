package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class MariageService implements IDao<Mariage> {
    
    private SessionFactory sessionFactory;

    public MariageService() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public boolean create(Mariage o) {
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
    public boolean update(Mariage o) {
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
    public boolean delete(Mariage o) {
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
    public Mariage findById(int id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Mariage.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Mariage> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Mariage", Mariage.class).list();
        } finally {
            session.close();
        }
    }

    /**
     * Utiliser l'API Criteria pour afficher le nombre d'hommes mariés à quatre femmes entre deux dates
     */
    public long getNombreHommesMariesAQuatreFemmes(Date dateDebut, Date dateFin) {
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
            
            Root<Mariage> mariage = query.from(Mariage.class);
            Join<Mariage, Homme> homme = mariage.join("homme");
            
            Predicate dateCondition = cb.and(
                cb.greaterThanOrEqualTo(mariage.get("dateDebut"), dateDebut),
                cb.lessThanOrEqualTo(mariage.get("dateDebut"), dateFin)
            );
            
            // Sélectionner les IDs des hommes et grouper par homme
            query.select(homme.get("id"))
                 .where(dateCondition)
                 .groupBy(homme.get("id"))
                 .having(cb.ge(cb.count(mariage), 4L));
            
            // Compter le nombre d'hommes distincts
            List<Integer> hommeIds = session.createQuery(query).getResultList();
            return (long) hommeIds.size();
        } finally {
            session.close();
        }
    }

    /**
     * Alternative avec Criteria Query pour obtenir la liste des hommes
     */
    public List<Homme> getHommesMariesAQuatreFemmes(Date dateDebut, Date dateFin) {
        Session session = sessionFactory.openSession();
        try {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Homme> query = cb.createQuery(Homme.class);
            
            Root<Mariage> mariage = query.from(Mariage.class);
            Join<Mariage, Homme> homme = mariage.join("homme");
            
            Predicate dateCondition = cb.and(
                cb.greaterThanOrEqualTo(mariage.get("dateDebut"), dateDebut),
                cb.lessThanOrEqualTo(mariage.get("dateDebut"), dateFin)
            );
            
            query.select(homme)
                 .where(dateCondition)
                 .groupBy(homme.get("id"))
                 .having(cb.ge(cb.count(mariage), 4L));
            
            return session.createQuery(query).getResultList();
        } finally {
            session.close();
        }
    }
}


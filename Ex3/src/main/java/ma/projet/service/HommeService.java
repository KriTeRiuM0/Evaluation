package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

public class HommeService implements IDao<Homme> {
    
    private SessionFactory sessionFactory;

    public HommeService() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public boolean create(Homme o) {
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
    public boolean update(Homme o) {
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
    public boolean delete(Homme o) {
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
    public Homme findById(int id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Homme.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Homme> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("FROM Homme", Homme.class).list();
        } finally {
            session.close();
        }
    }

    /**
     * Afficher les épouses d'un homme entre deux dates
     */
    public List<Mariage> getEpousesEntreDeuxDates(int hommeId, Date dateDebut, Date dateFin) {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Mariage m WHERE m.homme.id = :hommeId " +
                        "AND m.dateDebut >= :dateDebut AND m.dateDebut <= :dateFin";
            return session.createQuery(hql, Mariage.class)
                    .setParameter("hommeId", hommeId)
                    .setParameter("dateDebut", dateDebut)
                    .setParameter("dateFin", dateFin)
                    .list();
        } finally {
            session.close();
        }
    }

    /**
     * Afficher les mariages d'un homme avec tous les détails
     */
    public void afficherMariagesHomme(int hommeId) {
        Session session = sessionFactory.openSession();
        try {
            Homme homme = session.get(Homme.class, hommeId);
            if (homme == null) {
                System.out.println("Homme introuvable !");
                return;
            }

            System.out.println("\nNom : " + homme.getNom() + " " + homme.getPrenom());
            
            String hql = "FROM Mariage m WHERE m.homme.id = :hommeId ORDER BY m.dateDebut";
            List<Mariage> mariages = session.createQuery(hql, Mariage.class)
                    .setParameter("hommeId", hommeId)
                    .list();

            // Mariages en cours
            System.out.println("\nMariages En Cours :");
            int count = 1;
            for (Mariage m : mariages) {
                if (m.estEnCours()) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    System.out.println(count + ". Femme : " + m.getFemme().getNom() + " " + 
                                     m.getFemme().getPrenom() + 
                                     "   Date Début : " + sdf.format(m.getDateDebut()) + 
                                     "    Nbr Enfants : " + m.getNbrEnfant());
                    count++;
                }
            }
            if (count == 1) {
                System.out.println("Aucun mariage en cours.");
            }

            // Mariages échoués
            System.out.println("\nMariages échoués :");
            count = 1;
            for (Mariage m : mariages) {
                if (!m.estEnCours()) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                    System.out.println(count + ". Femme : " + m.getFemme().getNom() + " " + 
                                     m.getFemme().getPrenom() + 
                                     "   Date Début : " + sdf.format(m.getDateDebut()) + 
                                     "    Date Fin : " + sdf.format(m.getDateFin()) + 
                                     "    Nbr Enfants : " + m.getNbrEnfant());
                    count++;
                }
            }
            if (count == 1) {
                System.out.println("Aucun mariage échoué.");
            }
        } finally {
            session.close();
        }
    }
}


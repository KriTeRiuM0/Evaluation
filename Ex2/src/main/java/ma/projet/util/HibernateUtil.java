package ma.projet.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.classes.EmployeTache;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    
    static {
        try {
            // Créer la base de données si elle n'existe pas
            createDatabaseIfNotExists();
            
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(Employe.class);
            configuration.addAnnotatedClass(Projet.class);
            configuration.addAnnotatedClass(Tache.class);
            configuration.addAnnotatedClass(EmployeTache.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
    }
    
    private static void createDatabaseIfNotExists() {
        try {
            // Se connecter sans spécifier la base de données
            String url = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
            Connection conn = DriverManager.getConnection(url, "root", "123456789");
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS projet_db");
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println("Error creating database: " + e.getMessage());
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        getSessionFactory().close();
    }
    
    public static void clearDatabase() {
        Session session = getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            
            // Supprimer dans l'ordre pour respecter les contraintes de clés étrangères
            session.createQuery("DELETE FROM EmployeTache").executeUpdate();
            session.createQuery("DELETE FROM Tache").executeUpdate();
            session.createQuery("DELETE FROM Projet").executeUpdate();
            session.createQuery("DELETE FROM Employe").executeUpdate();
            
            tx.commit();
            System.out.println("Database cleared successfully!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("Error clearing database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}


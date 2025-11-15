package ma.projet.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ma.projet.classes.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    
    static {
        try {
            // Load properties from application.properties file
            Properties properties = new Properties();
            InputStream inputStream = HibernateUtil.class
                .getClassLoader()
                .getResourceAsStream("application.properties");
            
            if (inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            } else {
                // Fallback to default values if file not found
                properties.setProperty("hibernate.connection.driver_class", 
                    "com.mysql.cj.jdbc.Driver");
                properties.setProperty("hibernate.connection.url", 
                    "jdbc:mysql://localhost:3306/gestion_stock?useSSL=false&serverTimezone=UTC");
                properties.setProperty("hibernate.connection.username", "root");
                properties.setProperty("hibernate.connection.password", "");
                properties.setProperty("hibernate.dialect", 
                    "org.hibernate.dialect.MySQL8Dialect");
                properties.setProperty("hibernate.hbm2ddl.auto", "update");
                properties.setProperty("hibernate.show_sql", "true");
                properties.setProperty("hibernate.format_sql", "true");
            }
            
            // Create database if it doesn't exist
            createDatabaseIfNotExists(properties);
            
            // Build service registry
            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(properties)
                .build();
            
            // Build metadata sources with annotated classes
            MetadataSources metadataSources = new MetadataSources(serviceRegistry);
            metadataSources.addAnnotatedClass(Categorie.class);
            metadataSources.addAnnotatedClass(Produit.class);
            metadataSources.addAnnotatedClass(Commande.class);
            metadataSources.addAnnotatedClass(LigneCommandeProduit.class);
            
            // Build metadata and session factory
            Metadata metadata = metadataSources.getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
            
        } catch (Exception e) {
            System.err.println("Initial SessionFactory creation failed." + e);
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    private static void createDatabaseIfNotExists(Properties properties) {
        Connection conn = null;
        Statement stmt = null;
        try {
            String url = properties.getProperty("hibernate.connection.url");
            String username = properties.getProperty("hibernate.connection.username");
            String password = properties.getProperty("hibernate.connection.password");
            String driverClass = properties.getProperty("hibernate.connection.driver_class");
            
            if (url == null || username == null || password == null || driverClass == null) {
                System.err.println("Missing database configuration properties");
                return;
            }
            
            // Load driver
            Class.forName(driverClass);
            
            // Extract database name from URL
            // URL format: jdbc:mysql://localhost:3306/dbname?...
            String dbName = "gestion_stock";
            int lastSlash = url.lastIndexOf('/');
            if (lastSlash > 0 && lastSlash < url.length() - 1) {
                String dbPart = url.substring(lastSlash + 1);
                int questionMark = dbPart.indexOf('?');
                if (questionMark > 0) {
                    dbName = dbPart.substring(0, questionMark);
                } else if (!dbPart.isEmpty()) {
                    dbName = dbPart;
                }
            }
            
            // Create connection URL without database name
            String baseUrl = url.substring(0, lastSlash) + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            
            System.out.println("Attempting to create database: " + dbName);
            System.out.println("Connecting to: " + baseUrl.replace(password, "****"));
            
            // Connect to MySQL server (without database)
            conn = DriverManager.getConnection(baseUrl, username, password);
            stmt = conn.createStatement();
            
            // Create database if it doesn't exist
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            System.out.println("Database '" + dbName + "' created or already exists.");
            
        } catch (Exception e) {
            System.err.println("ERROR: Could not create database automatically: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create database", e);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}


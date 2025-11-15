package ma.projet.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class HibernateUtil {
    
    private static SessionFactory sessionFactory;
    
    static {
        try {
            // Charger les propriétés depuis application.properties
            Properties properties = new Properties();
            FileInputStream inputStream = new FileInputStream(
                "src/main/resources/application.properties"
            );
            properties.load(inputStream);
            inputStream.close();
            
            // Créer le StandardServiceRegistry avec les propriétés
            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
            registryBuilder.applySetting("hibernate.connection.driver_class", 
                properties.getProperty("hibernate.connection.driver_class"));
            registryBuilder.applySetting("hibernate.connection.url", 
                properties.getProperty("hibernate.connection.url"));
            registryBuilder.applySetting("hibernate.connection.username", 
                properties.getProperty("hibernate.connection.username"));
            registryBuilder.applySetting("hibernate.connection.password", 
                properties.getProperty("hibernate.connection.password"));
            registryBuilder.applySetting("hibernate.dialect", 
                properties.getProperty("hibernate.dialect"));
            registryBuilder.applySetting("hibernate.show_sql", 
                properties.getProperty("hibernate.show_sql"));
            registryBuilder.applySetting("hibernate.format_sql", 
                properties.getProperty("hibernate.format_sql"));
            registryBuilder.applySetting("hibernate.hbm2ddl.auto", 
                properties.getProperty("hibernate.hbm2ddl.auto"));
            
            StandardServiceRegistry registry = registryBuilder.build();
            
            // Créer MetadataSources et ajouter les classes annotées
            MetadataSources sources = new MetadataSources(registry);
            sources.addAnnotatedClass(ma.projet.beans.Personne.class);
            sources.addAnnotatedClass(ma.projet.beans.Homme.class);
            sources.addAnnotatedClass(ma.projet.beans.Femme.class);
            sources.addAnnotatedClass(ma.projet.beans.Mariage.class);
            
            // Créer Metadata et SessionFactory
            Metadata metadata = sources.getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}


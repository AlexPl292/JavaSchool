package com.tsystems.javaschool.util;


import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by alex on 07.09.16.
 */
@WebListener
public class EMU implements ServletContextListener {
    private static EntityManagerFactory emf;
    private static ThreadLocal<EntityManager> threadLocal;
    private final static Logger logger = Logger.getLogger(EMU.class);

    public static EntityManager getEntityManager() {
        EntityManager em = threadLocal.get();

        if (em == null) {
            em = emf.createEntityManager();
            threadLocal.set(em);
        }
        return em;
    }

    public static void closeEntityManager() {
        EntityManager em = threadLocal.get();
        if (em != null) {
            em.close();
        }
        threadLocal.remove();
    }

    public static void closeEntityManagerFactory() {
        emf.close();
    }

    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }

    public static void rollback() {
        getEntityManager().getTransaction().rollback();
    }

    public static void commit() {
        getEntityManager().getTransaction().commit();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        emf = Persistence.createEntityManagerFactory("JavaSchool");
        threadLocal = new ThreadLocal<>();
        logger.info("Context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        // ... First close any background tasks which may be using the DB ...
        // ... Then close any DB connection pools ...

        // Now deregister JDBC drivers in this context's ClassLoader:
        // Get the webapp's ClassLoader
        EMU.closeEntityManagerFactory();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        // Loop through all drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                // This driver was registered by the webapp's ClassLoader, so deregister it:
                try {
                    logger.info("Deregistering JDBC driver");
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ex) {
                    logger.error("Error deregistering JDBC driver", ex);
                }
            } else {
                // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
                logger.trace("Not deregistering JDBC driver as it does not belong to this webapp's ClassLoader");
            }
        }
    }
}

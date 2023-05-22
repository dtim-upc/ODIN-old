package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class JpaOrmImplementation implements ORMStoreInterface {

    private static final Logger logger = LoggerFactory.getLogger(JpaOrmImplementation.class);
    private final EntityManagerFactory emf;

    public JpaOrmImplementation() {
        emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
    }

    @Override
    public <T> T save(T object) {
        EntityManager em = emf.createEntityManager();
        T savedObject = null;
        try {
            em.getTransaction().begin();
            savedObject = em.merge(object);
            em.getTransaction().commit();
            logger.info("Object {} saved successfully", object.getClass());
        } catch (Exception e) {
            logger.error("Error saving object {}: {}", object.getClass(), e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return savedObject;
    }

    @Override
    public <T> T findById(Class<T> entityClass, String id) {
        EntityManager em = emf.createEntityManager();
        T object = null;
        try {
            object = em.find(entityClass, id);
        } catch (Exception e) {
            logger.error("Error finding object {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return object;
    }

    @Override
    public <T> List<T> getAll(Class<T> entityClass) {
        EntityManager em = emf.createEntityManager();
        List<T> objects = null;
        try {
            Query query = em.createQuery("SELECT d FROM " + entityClass.getSimpleName() + " d");
            objects = query.getResultList();
        } catch (Exception e) {
            logger.error("Error retrieving all objects {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return objects;
    }

    @Override
    public <T> boolean deleteOne(Class<T> entityClass, String id) {
        EntityManager em = emf.createEntityManager();
        boolean success = false;
        try {
            logger.info("-------------> STARTING DELETE PROCESS");
            em.getTransaction().begin();

            T objectToRemove = em.find(entityClass, id);
            if (objectToRemove != null) {
                logger.info("{} DELETED", entityClass.getSimpleName());
                em.remove(objectToRemove);
                em.getTransaction().commit();
                success = true;
            } else {
                logger.warn("Error deleting {}: Object not found", entityClass.getSimpleName());
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            logger.error("Error deleting {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return success;
    }

    @Override
    public boolean deleteAll(Class<?> entityClass) {
        EntityManager em = emf.createEntityManager();
        boolean success = false;
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM " + entityClass.getSimpleName());
            int deletedCount = query.executeUpdate();
            em.getTransaction().commit();
            success = deletedCount > 0;
        } catch (Exception e) {
            logger.error("Error deleting all {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return success;
    }
}

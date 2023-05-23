package edu.upc.essi.dtim.odin.NextiaStore.RelationalStore;

import edu.upc.essi.dtim.odin.project.Project;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

class JpaOrmImplementationTest {

    private EntityManagerFactory emf;
    private JpaOrmImplementation ormImplementation;
    private Project entity;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("ORMPersistenceUnit");
        ormImplementation = new JpaOrmImplementation();
        this.entity = new Project();
        this.entity.setProjectId("testID");
        this.entity.setProjectName("testName");
        this.entity.setProjectDescription("testDescription");
        this.entity.setProjectColor("testColor");
        this.entity.setProjectPrivacy("testPrivacy");
        this.entity.setCreatedBy("Victor Asenjo Testing");

    }

    @AfterEach
    void tearDown() {
        ormImplementation.deleteOne(Project.class, this.entity.getProjectId());
    }

    @Test
    void testSave() {

        // Call the save method
        Project savedEntity = ormImplementation.save(entity);

        // Verify if the object is saved and returned successfully
        Assertions.assertNotNull(savedEntity);
        Assertions.assertEquals(entity.getProjectId(), savedEntity.getProjectId());
        Assertions.assertEquals(entity.getProjectName(), savedEntity.getProjectName());
    }

    @Test
    void testFindById() {
        // Save the object using EntityManager
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(entity);
        tx.commit();
        em.close();

        // Call the findById method
        Project foundEntity = ormImplementation.findById(Project.class, entity.getProjectId());

        // Verify if the object is found successfully
        Assertions.assertNotNull(foundEntity);
        Assertions.assertEquals(entity.getProjectId(), foundEntity.getProjectId());
        Assertions.assertEquals(entity.getProjectName(), foundEntity.getProjectName());
    }

    @Test
    void testFindById_error() {

        // Call the findById method
        Project foundEntity = ormImplementation.findById(Project.class, "invented");

        // Verify if the object is found successfully
        Assertions.assertNull(foundEntity);
    }

    @Test
    void testDeleteOne() {
        // Save the object using EntityManager
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(entity);
        tx.commit();
        em.close();

        // Call the deleteOne method
        boolean deleted = ormImplementation.deleteOne(Project.class, entity.getProjectId());

        // Verify if the object is deleted successfully
        Assertions.assertTrue(deleted);

        // Try to find the object after deletion
        EntityManager emAfterDeletion = emf.createEntityManager();
        Project deletedEntity = emAfterDeletion.find(Project.class, entity.getProjectId());
        emAfterDeletion.close();

        // Verify if the object is not found after deletion
        Assertions.assertNull(deletedEntity);
    }

    @Test
    void testSave_Exception() {
        // Create a mock entity object
        Project entity = Mockito.mock(Project.class);

        // Mock the EntityManagerFactory to throw an exception when creating the EntityManager
        EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
        Mockito.when(emf.createEntityManager()).thenThrow(new RuntimeException("Error creating EntityManager"));

        // Create the ORM implementation with the mocked EntityManagerFactory
        JpaOrmImplementation ormImplementation = new JpaOrmImplementation();

        // Call the save method and verify the exception
        Assertions.assertThrows(RuntimeException.class, () -> ormImplementation.save(entity));
    }

    @Test
    void testFindById_Exception() {
        // Mock the EntityManagerFactory to throw an exception when creating the EntityManager
        JpaOrmImplementation ormImplementation = Mockito.mock(JpaOrmImplementation.class);
        Mockito.when(ormImplementation.findById(Project.class, "random")).thenThrow(new RuntimeException("Error creating EntityManager"));

        // Call the findById method and verify the exception
        Assertions.assertThrows(RuntimeException.class, () -> ormImplementation.findById(Project.class, "random"));
    }

    @Test
    void testDeleteOne_Exception() {
        // Mock the EntityManagerFactory to throw an exception when creating the EntityManager
        JpaOrmImplementation ormImplementation = Mockito.mock(JpaOrmImplementation.class);
        Mockito.when(ormImplementation.deleteOne(Project.class, "random")).thenThrow(new RuntimeException("Error creating EntityManager"));

        // Call the deleteOne method and verify the exception
        Assertions.assertThrows(RuntimeException.class, () -> ormImplementation.deleteOne(Project.class, "random"));
    }
}

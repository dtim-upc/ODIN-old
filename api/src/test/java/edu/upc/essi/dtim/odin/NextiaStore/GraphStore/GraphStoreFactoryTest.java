package edu.upc.essi.dtim.odin.NextiaStore.GraphStore;

import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreFactory;
import edu.upc.essi.dtim.odin.NextiaStore.GraphStore.GraphStoreInterface;
import edu.upc.essi.dtim.odin.config.AppConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class GraphStoreFactoryTest {

    @Test
    public void testGetInstance_Jena() throws Exception {
        // Create a mock AppConfig
        AppConfig appConfig = Mockito.mock(AppConfig.class);
        Mockito.when(appConfig.getDBTypeProperty()).thenReturn("JENA");

        // Call the getInstance method
        GraphStoreInterface graphStore = GraphStoreFactory.getInstance(appConfig);

        // Assertions
        Assertions.assertNotNull(graphStore);
        Assertions.assertTrue(graphStore instanceof GraphStoreJenaImpl);
    }

    @Test
    public void testGetInstance_Dummy() throws Exception {
        // Create a mock AppConfig
        AppConfig appConfig = Mockito.mock(AppConfig.class);
        Mockito.when(appConfig.getDBTypeProperty()).thenReturn("DUMMY");

        // Call the getInstance method
        GraphStoreInterface graphStore = GraphStoreFactory.getInstance(appConfig);

        // Assertions
        Assertions.assertNull(graphStore);
        // Add assertions specific to the Dummy implementation if available
    }

    @Test
    public void testGetInstance_UnknownDBType() {
        // Create a mock AppConfig
        AppConfig appConfig = Mockito.mock(AppConfig.class);
        Mockito.when(appConfig.getDBTypeProperty()).thenReturn("UNKNOWN");

        // Call the getInstance method and assert that it throws an exception
        Assertions.assertThrows(Exception.class, () -> {
            GraphStoreInterface graphStore = GraphStoreFactory.getInstance(appConfig);
        });
    }

    @Test
    public void testGetInstance_NullAppConfig() {
        // Call the getInstance method with a null AppConfig and assert that it throws an exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            GraphStoreInterface graphStore = GraphStoreFactory.getInstance(null);
        });
    }
}

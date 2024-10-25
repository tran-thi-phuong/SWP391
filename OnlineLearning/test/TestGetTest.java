/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Test;

import dal.TestDAO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author 84336
 */
public class TestGetTest {
    private TestDAO testDAO;
    public TestGetTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testDAO = new TestDAO();
    }
    
    @After
    public void tearDown() {
    }
      @Test
    public void testGetTestById_ValidId() {
        // Arrange
        int testID = 1; // Assume this ID exists in the test database

        // Act
        model.Test result = testDAO.getTestById(testID);

        // Assert
        assertNotNull("The returned test should not be null", result);
        assertEquals(1, result.getTestID());
        assertEquals("Algebra Basics", result.getTitle());
        assertEquals("Introduction to algebraic expressions.", result.getDescription());
        // Add additional assertions for other fields as necessary
    }

    @Test
    public void testGetTestById_InvalidId() {
        // Arrange
        int testID = 0; // Assume this ID does not exist in the test database

        // Act
        model.Test result = testDAO.getTestById(testID);

        // Assert
        assertNull("The returned test should be null for an invalid ID", result);
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package Test;

import dal.TestDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.sql.SQLException;
public class TestCountTest {

    private TestDAO testDAO; // Your TestDAO instance
    @Before
    public void setUp() {
        testDAO = new TestDAO(); // Initialize TestDAO with necessary context
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testCountTests_WithResults() {
        // Act
        int count = testDAO.countTests("Ab");

        // Assert
        assertEquals(10, count); // Adjust based on your sample data setup
    }

    @Test
    public void testCountTests_NoResults() {
        // Act
        int count = testDAO.countTests("Nonexistent");

        // Assert
        assertEquals(0, count); // No matches should return 0
    }

    @Test
    public void testCountTests_EmptyQuery() {
        // Act
        int count = testDAO.countTests("");
        // Assert
        assertEquals(152, count);
    }


}

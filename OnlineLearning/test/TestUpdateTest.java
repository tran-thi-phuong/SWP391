/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import dal.TestDAO;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.*;

/**
 *
 * @author 84336
 */
public class TestUpdateTest {

    private TestDAO testDAO;

    public TestUpdateTest() {
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
    public void testUpdateTest() {
        // Arrange
        model.Test testToUpdate = testDAO.getTestById(153);
        testToUpdate.setSubjectID(1);
        testToUpdate.setTitle("Updated Title");
        testToUpdate.setDescription("Updated Description");
        testToUpdate.setMediaType("video");
        testToUpdate.setMediaURL("videos/updated.mp4");
        testToUpdate.setMediaDescription("Updated media description.");
        testToUpdate.setType("Quiz");
        testToUpdate.setDuration(45); // Updated duration
        testToUpdate.setPassCondition(80.00); // Updated pass condition
        testToUpdate.setLevel("Intermediate");
        testToUpdate.setQuantity(25);

        // Redirect the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Act
        testDAO.updateTest(testToUpdate); // Call the method to update the test

        // Restore the original output
        System.setOut(originalOut);

        // Assert
        String output = outputStream.toString().trim();
        model.Test updatedTest = testDAO.getTestById(153); // Retrieve the updated test

        // Verify the test properties
        assertEquals("Updated Title", updatedTest.getTitle());
        assertEquals("Updated Description", updatedTest.getDescription());
        assertEquals("video", updatedTest.getMediaType());
        assertEquals("videos/updated.mp4", updatedTest.getMediaURL());
        assertEquals("Updated media description.", updatedTest.getMediaDescription());
        assertEquals(45, updatedTest.getDuration());
        assertEquals(80.00, updatedTest.getPassCondition(), 0.01);
        assertEquals("Intermediate", updatedTest.getLevel());
        assertEquals(25, updatedTest.getQuantity());

        assertEquals("Test updated successfully.", output); // Check if the message was logged correctly

    }

    @Test
    public void testUpdateTest_invalid() {
        // Arrange
        model.Test testToUpdate = new model.Test();
        testToUpdate.setTestID(0); // Set an invalid ID to trigger the log message
        testToUpdate.setSubjectID(1);
        testToUpdate.setTitle("Updated Title");
        testToUpdate.setDescription("Updated Description");
        testToUpdate.setMediaType("video");
        testToUpdate.setMediaURL("videos/updated.mp4");
        testToUpdate.setMediaDescription("Updated media description.");
        testToUpdate.setType("Quiz");
        testToUpdate.setDuration(45);
        testToUpdate.setPassCondition(80.00);
        testToUpdate.setLevel("Intermediate");
        testToUpdate.setQuantity(25);

        // Redirect the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Act
        testDAO.updateTest(testToUpdate);

        // Restore the original output
        System.setOut(originalOut);

        // Assert
        String output = outputStream.toString().trim();
        assertEquals("No test found with the provided TestID.", output); // Check the expected message
    }

    @Test(expected = SQLException.class)
    public void testUpdateTest_Exception() throws SQLException {
        model.Test testToUpdate = testDAO.getTestById(153);
        testToUpdate.setSubjectID(-1); // Use an invalid ID to trigger the exception

        // Act & Assert
        assertThrows(SQLException.class, () -> {
            testDAO.updateTest(testToUpdate); // Call the method to update the test
        });
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}

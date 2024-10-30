package Test;

import dal.TestDAO;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.mockito.Mockito;
import java.sql.SQLException;

public class TestAddTest {

    private TestDAO testDAO;

    @Before
    public void setUp() {
        testDAO = new TestDAO(); // Initialize with necessary context (like DB connection)
    }

    @After
    public void tearDown() {
        // Close connections if needed
    }

    @Test
    public void testAddTest() {
        System.out.println("addTest");
        model.Test test = new model.Test();
        test.setSubjectID(1);
        test.setTitle("Algebra Basics");
        test.setDescription("Introduction to algebraic expressions.");
        test.setMediaType("video");
        test.setMediaURL("videos/0805 (11).mp4");
        test.setMediaDescription("A video explaining basic algebra.");
        test.setType("Quiz");
        test.setDuration(30);
        test.setPassCondition(75.00);
        test.setLevel("Beginner");
        test.setQuantity(19);
        TestDAO instance = new TestDAO();
        int result = instance.addTest(test);

        assertTrue("The generated ID should be greater than -1", result > -1);
        System.out.println("Actual Generated ID: " + result);
    }

    @Test
    public void testAddTest_ReturnsMinusOneOnFailure() throws SQLException {
        System.out.println("addTest_ReturnsMinusOneOnFailure");

        model.Test test = new model.Test();

        int result = testDAO.addTest(test);

        assertEquals(-1, result); 
    }

}

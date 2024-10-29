/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package dal;

import java.util.List;
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
public class TestDAOTest {
    
    public TestDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addTest method, of class TestDAO.
     */
    @Test
    public void testAddTest() {
        System.out.println("addTest");
        model.Test test = null;
        TestDAO instance = new TestDAO();
        int expResult = 0;
        int result = instance.addTest(test);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateTest method, of class TestDAO.
     */
    @Test
    public void testUpdateTest() {
        System.out.println("updateTest");
        model.Test test = null;
        TestDAO instance = new TestDAO();
        instance.updateTest(test);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllTests method, of class TestDAO.
     */
    @Test
    public void testGetAllTests_int_int() {
        System.out.println("getAllTests");
        int pageNumber = 0;
        int pageSize = 0;
        TestDAO instance = new TestDAO();
        List<model.Test> expResult = null;
        List<model.Test> result = instance.getAllTests(pageNumber, pageSize);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllTests method, of class TestDAO.
     */
    @Test
    public void testGetAllTests_5args() {
        System.out.println("getAllTests");
        int pageNumber = 0;
        int pageSize = 0;
        String search = "";
        String subjectId = "";
        String type = "";
        TestDAO instance = new TestDAO();
        List<model.Test> expResult = null;
        List<model.Test> result = instance.getAllTests(pageNumber, pageSize, search, subjectId, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTotalTestCount method, of class TestDAO.
     */
    @Test
    public void testGetTotalTestCount() {
        System.out.println("getTotalTestCount");
        String search = "";
        String subjectId = "";
        String type = "";
        TestDAO instance = new TestDAO();
        int expResult = 0;
        int result = instance.getTotalTestCount(search, subjectId, type);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchTests method, of class TestDAO.
     */
    @Test
    public void testSearchTests() {
        System.out.println("searchTests");
        String query = "";
        int offset = 0;
        int limit = 0;
        TestDAO instance = new TestDAO();
        List<model.Test> expResult = null;
        List<model.Test> result = instance.searchTests(query, offset, limit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of countTests method, of class TestDAO.
     */
    @Test
    public void testCountTests() {
        System.out.println("countTests");
        String query = "";
        TestDAO instance = new TestDAO();
        int expResult = 0;
        int result = instance.countTests(query);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllQuizTypes method, of class TestDAO.
     */
    @Test
    public void testGetAllQuizTypes() {
        System.out.println("getAllQuizTypes");
        TestDAO instance = new TestDAO();
        List<String> expResult = null;
        List<String> result = instance.getAllQuizTypes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of countAttemptsByTestID method, of class TestDAO.
     */
    @Test
    public void testCountAttemptsByTestID() {
        System.out.println("countAttemptsByTestID");
        int testID = 0;
        TestDAO instance = new TestDAO();
        int expResult = 0;
        int result = instance.countAttemptsByTestID(testID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculatePassRate method, of class TestDAO.
     */
    @Test
    public void testCalculatePassRate() {
        System.out.println("calculatePassRate");
        int testID = 0;
        TestDAO instance = new TestDAO();
        double expResult = 0.0;
        double result = instance.calculatePassRate(testID);
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTestById method, of class TestDAO.
     */
    @Test
    public void testGetTestById() {
        System.out.println("getTestById");
        int testID = 0;
        TestDAO instance = new TestDAO();
        model.Test expResult = null;
        model.Test result = instance.getTestById(testID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteTest method, of class TestDAO.
     */
    @Test
    public void testDeleteTest() {
        System.out.println("deleteTest");
        int testID = 0;
        TestDAO instance = new TestDAO();
        instance.deleteTest(testID);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}

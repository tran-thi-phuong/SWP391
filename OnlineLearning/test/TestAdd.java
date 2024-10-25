import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import dal.RegistrationsDAO;
import model.Registrations;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class TestAdd {

    private RegistrationsDAO registrationsDAO;

    public TestAdd() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        // Initialize the DAO before each test
        registrationsDAO = new RegistrationsDAO();
    }

    @After
    public void tearDown() {
        // Clean up resources if needed
    }

    @Test
    public void testAddNewRegistrationSuccess() {
    try {
        // Test data
        int userId = 13;
        int subjectId = 2;
        int packageId = 1;
        double totalCost = 100.0;
        Date registrationTime = new Date();
        Date validFrom = new Date();
        Date validTo = new Date(validFrom.getTime() + 30L * 24L * 60L * 60L * 1000L);
        int staffId = 1;
        String note = "Test registration";

        // Execute the method
        registrationsDAO.addNewRegistration(userId, subjectId, packageId, totalCost,
                registrationTime, validFrom, validTo, staffId, note);

        // Log the insertion step
        System.out.println("Registration added successfully.");

    } catch (SQLException e) {
        e.printStackTrace();
        fail("Should not throw SQLException: " + e.getMessage());
    }
}


    @Test(expected = SQLException.class)
    public void testAddNewRegistrationWithInvalidUserId() throws SQLException {
        // Test data with invalid user ID
        int invalidUserId = -1;
        int subjectId = 1;
        int packageId = 1;
        double totalCost = 100.0;
        Date registrationTime = new Date();
        Date validFrom = new Date();
        Date validTo = new Date(validFrom.getTime() + 30L * 24L * 60L * 60L * 1000L);
        int staffId = 1;
        String note = "Test registration";

        // This should throw SQLException due to foreign key constraint
        registrationsDAO.addNewRegistration(invalidUserId, subjectId, packageId, totalCost,
                registrationTime, validFrom, validTo, staffId, note);
        System.out.println("Registration add failed with invalid user ID.");
    }

    @Test(expected = SQLException.class)
    public void testAddNewRegistrationWithNullDates() throws SQLException {
        // Test data with null dates
        Date validFrom = new Date();
        Date validTo = new Date(validFrom.getTime() + 90L * 24L * 60L * 60L * 1000L); // 90 days later
        registrationsDAO.addNewRegistration(1, 1, 1, 100.0,
                null, validFrom, validTo, 1, "Test registration");
        System.out.println("Registration add failed with null dates.");
    }


    @Test(expected = SQLException.class)
    public void testAddNewRegistrationWithInvalidDateRange() throws SQLException {
        // Test data with invalid date range (validTo before validFrom)
        Date registrationTime = new Date();
        Date validFrom = new Date();
        Date validTo = new Date(validFrom.getTime() - 24L * 60L * 60L * 1000L); // 1 day before validFrom

        registrationsDAO.addNewRegistration(1, 1, 1, 100.0,
                registrationTime, validFrom, validTo, 1, "Test registration");
        System.out.println("Registration add failed with invalid date range.");
    }

    @Test(expected = SQLException.class)
    public void testAddNewRegistrationWithInvalidPackageId() throws SQLException {
        // Test data with invalid package ID
        registrationsDAO.addNewRegistration(1, 1, -1, 100.0,
                new Date(), new Date(), new Date(), 1, "Test registration");
        System.out.println("Registration add failed with invalid package ID.");
    }
    
 @Test(expected = SQLException.class)
public void testAddNewRegistrationWithInvalidSubjectId() throws SQLException {
    // Test data with an invalid subject ID
    int userId = 1;
    int invalidSubjectId = 999; // Assuming 999 is guaranteed to be invalid
    int packageId = 1;
    double totalCost = 100.0;
    Date registrationTime = new Date();
    Date validFrom = new Date();
    Date validTo = new Date(validFrom.getTime() + 30L * 24L * 60L * 60L * 1000L);
    int staffId = 1;
    String note = "Test registration";

    // Log the attempt to add a registration


    // This should throw SQLException due to foreign key constraint
    registrationsDAO.addNewRegistration(userId, invalidSubjectId, packageId, totalCost,
            registrationTime, validFrom, validTo, staffId, note);

    // This line should not be reached if the exception is thrown
    System.out.println("Registration add should have failed with invalid subject ID.");
}

}
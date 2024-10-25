import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import dal.RegistrationsDAO;
import model.Registrations;

import java.util.Date;

public class TestUpdate {

    private RegistrationsDAO registrationsDAO;

    public TestUpdate() {
    }

    @BeforeClass
    public static void setUpClass() {
        // Code to run once before any tests in this class
    }

    @AfterClass
    public static void tearDownClass() {
        // Code to run once after all tests in this class
    }

    @Before
    public void setUp() {
        // Initialize the DAO before each test
        registrationsDAO = new RegistrationsDAO();
    }

    @After
    public void tearDown() {
        // Clean up resources if needed (e.g., resetting test data)
    }

    @Test
    public void testUpdateRegistrationSuccess() {
        // Prepare test data for a successful update
        Registrations registration = new Registrations();
        registration.setRegistrationId(1); // Assuming this ID exists
        registration.setValidFrom(new Date());
        registration.setValidTo(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)); // 30 days later
        registration.setStatus("Active");
        boolean result = registrationsDAO.updateRegistration(registration);
        // Check that the update was successful
        assertTrue("Registration should be updated successfully", result);
    }

    @Test
    public void testUpdateNonExistentRegistration() {
        // Prepare test data for a non-existent registration
        Registrations registration = new Registrations();
        registration.setRegistrationId(999); // Assuming this ID does not exist
        registration.setValidFrom(new Date());
        registration.setValidTo(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)); // 30 days later
        registration.setStatus("Processing");
        boolean result = registrationsDAO.updateRegistration(registration);
        // Check that the update was not successful
        assertFalse("Updating a non-existent registration should return false", result);
    }
@Test
public void testUpdateInvalidSubjectID() {
    // Prepare test data for a registration with an invalid SubjectID
    Registrations registration = new Registrations();
    registration.setRegistrationId(1); // Assuming this ID exists
    registration.setSubjectId(7); // Invalid subject ID
    registration.setValidFrom(new Date());
    registration.setValidTo(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)); // 30 days later
    registration.setStatus("Processing");

    boolean result = registrationsDAO.updateRegistration(registration);
    
    // Check that the update was not successful
    assertFalse("Updating a registration with an invalid subject ID should return false", result);
}

    @Test
    public void testUpdateRegistrationWithInvalidDates() {
        // Prepare test data with invalid date range
        Registrations registration = new Registrations();
        registration.setRegistrationId(1); // Assuming this ID exists
        registration.setValidFrom(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)); // 30 days later
        registration.setValidTo(new Date(System.currentTimeMillis() + 15L * 24 * 60 * 60 * 1000)); // 15 days later (invalid)
        boolean result = registrationsDAO.updateRegistration(registration);
        // Check that the update was not successful (this assumes your DAO handles invalid dates)
        assertFalse("Updating with invalid date range should return false", result);
    }
}

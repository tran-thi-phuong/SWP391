import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import dal.RegistrationsDAO;
import java.util.Calendar;
import java.util.Date;

public class TestGetTime {

    private RegistrationsDAO registrationsDAO;

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
    public void testGetNewRegistrationByTimeSuccess() {
        // Prepare test data
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.OCTOBER, 1);
        Date startDate = calendar.getTime();
        calendar.set(2024, Calendar.DECEMBER, 31);
        Date endDate = calendar.getTime();

        // Assuming there are some registrations in this time range
        int count = registrationsDAO.getNewRegistrationByTime(startDate, endDate);
        
        // Check that the count is greater than or equal to 0
        assertTrue("Count of registrations should be non-negative", count >= 0);
    }

    @Test
    public void testGetNewRegistrationByTimeNoRegistrations() {
        // Prepare test data with a date range that has no registrations
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 1);
        Date startDate = calendar.getTime();
        calendar.set(2023, Calendar.JANUARY, 2);
        Date endDate = calendar.getTime();

        // Expecting 0 registrations in this date range
        int count = registrationsDAO.getNewRegistrationByTime(startDate, endDate);
        
        // Check that the count is 0
        assertEquals("Count of registrations should be 0", 0, count);
    }

    @Test
    public void testGetNewRegistrationByTimeInvalidDates() {
        // Prepare test data with an invalid date range (end date before start date)
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.JANUARY, 2);
        Date startDate = calendar.getTime();
        calendar.set(2024, Calendar.JANUARY, 1);
        Date endDate = calendar.getTime();

        // Expecting 0 registrations in this invalid date range
        int count = registrationsDAO.getNewRegistrationByTime(startDate, endDate);
        
        // Check that the count is 0
        assertEquals("Count of registrations should be 0", 0, count);
    }
}

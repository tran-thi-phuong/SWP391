import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import dal.CampaignsDAO;

public class TestCampaignList {
    
    private CampaignsDAO campaignDAO;

    @Before
    public void setUp() throws SQLException {
        campaignDAO = new CampaignsDAO();
        createTestData();
    }
    
    @After
    public void tearDown() throws SQLException {
        cleanUpTestData();
    }

    @Test
    public void testStartCampaign() throws SQLException {
        int CampaignId = 1; 
        boolean result = campaignDAO.startCampaign(CampaignId);
        assertTrue("Campaign should be started successfully", result);

    }
    
    @Test
    public void testStartCampaignwithinvalidId() throws SQLException {
        int CampaignId = 999; 
        boolean result = campaignDAO.startCampaign(CampaignId);
        assertFalse("Campaign should not be started with an invalid ID", result);
    }



    private void createTestData() throws SQLException {
      
    }

    private void cleanUpTestData() throws SQLException {
       
    }
}

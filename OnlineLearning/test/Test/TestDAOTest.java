package Test;

import dal.TestDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/*@RunWith(PowerMockRunner.class)*/
@PrepareForTest(TestDAO.class) // Prepare the TestDAO class for PowerMock
public class TestDAOTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @InjectMocks
    private TestDAO testDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateTest_Success() throws Exception {
        // Arrange
        model.Test test = testDAO.getTestById(0);
        test.setSubjectID(2);
        test.setTitle("Updated Title");
        // Set other fields...

        // Mock the connection and prepared statement behavior
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1); // Simulate a successful update

        // Act
        testDAO.updateTest(test);

        // Assert
        verify(preparedStatement).executeUpdate();
    }

    @Test(expected = SQLException.class)
    public void testUpdateTest_SQLException() throws Exception {
        // Arrange
        model.Test test = testDAO.getTestById(1);
        // Set fields...

        // Mock the connection to throw SQLException
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act
        testDAO.updateTest(test); // This should throw SQLException
    }
}

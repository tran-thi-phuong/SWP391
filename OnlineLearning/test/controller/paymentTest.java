package controller;

import dal.PackagePriceDAO;
import dal.RegistrationsDAO;
import dal.UserDAO;
import model.PackagePrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
public class paymentTest { // Ensure the class name ends with "Test"

    private payment servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @BeforeEach
    public void setUp() throws Exception {
        servlet = new payment();
        
        // Mock the request and response
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = new PrintWriter(new StringWriter());
        
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testDoPostSuccessfulSubmission() throws Exception {
        // Mocking request parameters
        when(request.getParameter("package")).thenReturn("1");
        when(request.getParameter("full-name")).thenReturn("John Doe");
        when(request.getParameter("email")).thenReturn("john.doe@example.com");
        when(request.getParameter("mobile")).thenReturn("1234567890");
        when(request.getParameter("gender")).thenReturn("Male");
        when(request.getParameter("subjectID")).thenReturn("2");
        
        // Mocking session
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null); // No user in session

        // Mocking PackagePriceDAO
        PackagePriceDAO packagePriceDAO = mock(PackagePriceDAO.class);
        PackagePrice mockPackage = new PackagePrice();
        mockPackage.setSalePrice(100.0);
        mockPackage.setPrice(120.0);
        when(packagePriceDAO.searchByPackagePriceId(1)).thenReturn(mockPackage);
        
        // Set the mocked DAO to the servlet (you may need to set it through reflection if private)
        setField(servlet, "packagePriceDAO", packagePriceDAO);
        setField(servlet, "registrationsDAO", new RegistrationsDAO()); // Mock as needed
        setField(servlet, "userDAO", new UserDAO()); // Mock as needed

        // Call the servlet's doPost method
        servlet.doPost(request, response);

        // Check the output
        String output = writer.toString();
        assertTrue(output.contains("Status:Submission Successful"));
        assertTrue(output.contains("Full Name: John Doe"));
    }

    @Test
    public void testDoPostSQLException() throws Exception {
        // Similar setup as before
        when(request.getParameter("package")).thenReturn("1");
        when(request.getParameter("full-name")).thenReturn("John Doe");
        when(request.getParameter("email")).thenReturn("john.doe@example.com");
        when(request.getParameter("mobile")).thenReturn("1234567890");
        when(request.getParameter("gender")).thenReturn("Male");
        when(request.getParameter("subjectID")).thenReturn("2");

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null); // No user in session

        PackagePriceDAO packagePriceDAO = mock(PackagePriceDAO.class);
        PackagePrice mockPackage = new PackagePrice();
        mockPackage.setSalePrice(100.0);
        mockPackage.setPrice(120.0);
        when(packagePriceDAO.searchByPackagePriceId(1)).thenReturn(mockPackage);

        // Mocking RegistrationsDAO to throw SQLException
        RegistrationsDAO registrationsDAO = mock(RegistrationsDAO.class);
        doThrow(new SQLException()).when(registrationsDAO).addRegistration(anyInt(), anyInt(), anyInt(), anyDouble());

        setField(servlet, "packagePriceDAO", packagePriceDAO);
        setField(servlet, "registrationsDAO", registrationsDAO);
        setField(servlet, "userDAO", new UserDAO()); // Mock as needed

        // Call the servlet's doPost method
        servlet.doPost(request, response);

        // Check the output for the error message
        String output = writer.toString();
        assertTrue(output.contains("Status:Submission Failed"));
    }

    // Helper method to set private fields using reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}

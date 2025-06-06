package controller;

import dal.PackagePriceDAO;
import dal.PagesDAO;
import dal.RegistrationsDAO;
import dal.RolePermissionDAO;
import dal.SubjectDAO;
import dal.UserDAO;
import model.Registrations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import model.Users;

public class RegistrationDetail extends HttpServlet {

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    if (!hasPermission(request, response)) {
        return;
    }
    try {
        // Retrieve the registration ID from the request parameters
        String registrationIdParam = request.getParameter("id");
        // Check if the registration ID is missing or empty
        if (registrationIdParam == null || registrationIdParam.isEmpty()) {
            // Respond with a bad request error if the ID is not provided
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing registration ID");
            return;
        }

        // Parse the registration ID to an integer
        int registrationId = Integer.parseInt(registrationIdParam);

        // Create DAO instances for database access
        RegistrationsDAO registrationsDAO = new RegistrationsDAO();
        UserDAO userDAO = new UserDAO();
        SubjectDAO subjectDAO = new SubjectDAO();
        PackagePriceDAO packageDAO = new PackagePriceDAO();

        // Retrieve the registration details by ID
        Registrations registration = registrationsDAO.getRegistrationById(registrationId);

        // Check if the registration exists
        if (registration != null) {
            // Fetch additional details related to the registration
            String customerEmail = userDAO.getEmailByUserId(registration.getUserId());
            Users user = userDAO.getUserById(registration.getUserId()); // Lấy thông tin người dùng
            String subjectTitle = subjectDAO.getSubjectNameById(registration.getSubjectId());
            String packageName = packageDAO.getNameByPackageId(registration.getPackageId());
            String saleUsername = userDAO.getStaffUserNameByUserId(registration.getStaffId());

            // Set attributes to be used in the JSP for displaying registration details
            request.setAttribute("registration", registration);
            request.setAttribute("customerEmail", customerEmail);
            request.setAttribute("subjectTitle", subjectTitle);
            request.setAttribute("packageName", packageName);
            request.setAttribute("saleUsername", saleUsername);
            request.setAttribute("userDetails", user); // Đưa thông tin người dùng vào request

            // Forward the request to the RegistrationDetail.jsp page to display the details
            request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
        } else {
            // If no registration is found, set an error message and forward to the JSP
            request.setAttribute("errorMessage", "Registration not found");
            request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
        }
    } catch (NumberFormatException e) {
        // Handle case where registration ID is not a valid number
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid registration ID format");
    } catch (Exception e) {
        // Handle general exceptions, such as database access errors
        throw new ServletException("Database access error", e);
    }
}

      private boolean hasPermission(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession session = request.getSession();
    Users currentUser = (Users) session.getAttribute("user");

    // Kiểm tra nếu người dùng chưa đăng nhập thì chuyển hướng đến trang đăng nhập
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return false;
    }

    // Lấy quyền của người dùng và kiểm tra quyền truy cập với trang hiện tại
    String userRole = currentUser.getRole();
    RolePermissionDAO rolePermissionDAO = new RolePermissionDAO();
    Integer pageID = new PagesDAO().getPageIDFromUrl(request.getRequestURL().toString());

    // Nếu người dùng đã đăng nhập nhưng không có quyền, chuyển hướng về /homePage
    if (pageID != null && !rolePermissionDAO.hasPermission(userRole, pageID)) {
        response.sendRedirect(request.getContextPath() + "/Homepage");

        return false;
    } else if (pageID == null) {
        // Nếu không tìm thấy trang trong hệ thống phân quyền, chuyển đến trang lỗi
        response.sendRedirect("error.jsp");
        return false;
    }

    return true; // Người dùng có quyền truy cập trang này
}
}

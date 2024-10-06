package controller;

import dal.PackagePriceDAO;
import dal.RegistrationsDAO;
import dal.SubjectDAO;
import dal.UserDAO;
import model.Registrations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RegistrationDetail extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy ID từ tham số URL
            String registrationIdParam = request.getParameter("id");
            if (registrationIdParam == null || registrationIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing registration ID");
                return;
            }

            int registrationId = Integer.parseInt(registrationIdParam);

            // Khởi tạo DAO
            RegistrationsDAO registrationsDAO = new RegistrationsDAO();
            UserDAO userDAO = new UserDAO();
            SubjectDAO subjectDAO = new SubjectDAO();
            PackagePriceDAO packageDAO = new PackagePriceDAO();

            // Lấy thông tin registration
            Registrations registration = registrationsDAO.getRegistrationById(registrationId);

            if (registration != null) {
                // Lấy các thông tin bổ sung
                String customerEmail = userDAO.getEmailByUserId(registration.getUserId());
                String subjectTitle = subjectDAO.getSubjectNameById(registration.getSubjectId());
                String packageName = packageDAO.getNameByPackageId(registration.getPackageId());
                String saleUsername = userDAO.getStaffUserNameByUserId(registration.getStaffId());

                // Đặt thông tin registration và các thuộc tính bổ sung vào thuộc tính của yêu cầu
                request.setAttribute("registration", registration);
                request.setAttribute("customerEmail", customerEmail);
                request.setAttribute("subjectTitle", subjectTitle);
                request.setAttribute("packageName", packageName);
                request.setAttribute("saleUsername", saleUsername);
                
                // Chuyển tiếp yêu cầu đến trang JSP để hiển thị thông tin chi tiết
                request.getRequestDispatcher("RegistrationDetail.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Registration not found");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid registration ID format");
        } catch (Exception e) {
            throw new ServletException("Database access error", e);
        }
    }
}

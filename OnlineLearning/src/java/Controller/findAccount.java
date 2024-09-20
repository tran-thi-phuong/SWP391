package Controller;

import Model.User;
import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class findAccount extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail(email);

        if (user != null) {
            // Lưu email vào session
            HttpSession session = request.getSession();
            session.setAttribute("resetEmail", email);

            // Lưu thông tin người dùng vào request attributes để gửi đến JSP
            request.setAttribute("email", user.getEmail());
            request.setAttribute("username", user.getUsername());
            request.setAttribute("avatarUrl", (user.getAvatar() != null && !user.getAvatar().isEmpty())
                ? "Image/" + user.getAvatar()
                : "Image/default-avatar.jpg");
            
            // Chuyển hướng đến trang khôi phục tài khoản
            request.getRequestDispatcher("/recoverAccount.jsp").forward(request, response);
        } else {
            // Lưu thông báo lỗi vào request attributes
            request.setAttribute("errorMessage", "No account found with this email.Please try again with other email.");
            // Chuyển hướng trở lại trang forgotPassword.jsp với thông báo lỗi
            request.getRequestDispatcher("/findAccount.jsp").forward(request, response);
        }
    }
}

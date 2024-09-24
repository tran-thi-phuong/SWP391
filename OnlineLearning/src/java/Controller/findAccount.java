package controller;

import model.Users;
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
        Users user = userDAO.getUserByEmail(email);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("resetEmail", email);

            request.setAttribute("email", user.getEmail());
            request.setAttribute("username", user.getUsername());
            request.setAttribute("avatarUrl", (user.getAvatar() != null && !user.getAvatar().isEmpty())
                ? "" + user.getAvatar()
                : "images/default-avatar.jpg");

            request.getRequestDispatcher("/recoverAccount.jsp").forward(request, response);
        } else {
           
            request.setAttribute("errorMessage", "No account found with this email.Please try again with other email.");
            request.getRequestDispatcher("/findAccount.jsp").forward(request, response);
        }
    }
}

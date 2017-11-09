package servlet;

import bd.Transaction;
import data.User;
import servlet.error.InvalidPasswordException;
import servlet.error.InvalidUsernameException;
import servlet.service.UserCheckerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession().getAttribute("user") != null) {
            resp.sendRedirect("/");
        } else {
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("username");
        String password = req.getParameter("password");

        if (userName == null && password == null) {
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
            return;
        }

        User user = null;
        Transaction transaction = (Transaction) req.getAttribute("transaction");

        UserCheckerService checkerService = UserCheckerService.getInstance();

        try {
            user = checkerService.check(userName, password, transaction);
        } catch (SQLException e) {
            req.setAttribute("fail", "Connection error! Please try later");
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
        } catch (InvalidUsernameException e) {
            req.setAttribute("fail", "Invalid username!");
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
        } catch (InvalidPasswordException e) {
            req.setAttribute("fail", "Invalid password!");
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
        }

        req.getSession().setAttribute("user", user);
        resp.sendRedirect("/");
    }

}
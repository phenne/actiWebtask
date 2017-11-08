package servlet;

import bd.Transaction;
import bd.TransactionManager;
import dao.UserDao;
import data.User;

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

        User foundUser = getUser(req, userName);
        String result = checkUserData(foundUser, password);

        if (result == null) {
            req.getSession(true).setAttribute("user", foundUser);
            resp.sendRedirect("/");
        } else {
            req.setAttribute("fail", result);
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
        }
    }

    User getUser(HttpServletRequest req, String userName) {
        User user = null;
        try {
            Transaction transaction = (Transaction) req.getAttribute("transaction");
            UserDao userDao = TransactionManager.getInstance().getAssociatedUserDao(transaction);
            user = userDao.getUserByUsername(userName);
            transaction.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    String checkUserData(User user, String password) {
        if (user == null) {
            return "Invalid username!";
        }
        if (!user.getPassword().equals(password)) {
            return "Invalid password!";
        }
        return null;
    }
}
package servlet;

import bd.Transaction;
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
            getServletContext().getRequestDispatcher("/pages/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("username");
        String password = req.getParameter("password");

        if (userName == null && password == null) {
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
        }

        User foundUser = null;

        try {
            Transaction transaction = (Transaction) req.getAttribute("transaction");
            foundUser = getUser(transaction, req.getParameter("username"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String result = checkUserData(foundUser, password);
        if (result == null) {
            req.getSession(true).setAttribute("user", foundUser);
            resp.sendRedirect("/");
        } else {
            req.setAttribute("fail", result);
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
        }
    }

    User getUser(Transaction transaction, String userName) throws SQLException {
        UserDao userDao = new UserDao();
        userDao.associateTransaction(transaction);
        User foundUser = userDao.getUserByUsername(userName);
        transaction.commit();

        return foundUser;
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
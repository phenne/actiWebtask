package servlet;

import bd.Transaction;
import bd.UserDaoFactory;
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

        User foundUser = null;
        try {
            foundUser = getUser(req, userName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String result = checkUserData(foundUser, password);
        checkResult(result, req, resp, foundUser);
    }

    void checkResult(String result, HttpServletRequest request, HttpServletResponse response, User foundUser) throws IOException, ServletException {
        if (result == null) {
            request.getSession().setAttribute("user", foundUser);
            response.sendRedirect("/");
        } else {
            request.setAttribute("fail", result);
            request.getRequestDispatcher("/pages/login.jsp").forward(request, response);
        }
    }

    User getUser(HttpServletRequest req, String userName) throws SQLException {
        Transaction transaction = (Transaction) req.getAttribute("transaction");
        UserDao userDao = UserDaoFactory.getInstance().getUserDao(transaction);
        User user = userDao.getUserByUsername(userName);
        transaction.commit();
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
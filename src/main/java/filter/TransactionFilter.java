package filter;

import bd.TransactionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class TransactionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        TransactionManager transactionManager = new TransactionManager();
        req.setAttribute("transaction", transactionManager);
        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {
    }
}
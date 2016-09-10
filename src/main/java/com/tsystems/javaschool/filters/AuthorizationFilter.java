package com.tsystems.javaschool.filters;

import com.tsystems.javaschool.db.entities.Customer;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by alex on 10.09.16.
 */
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        HttpSession session = httpReq.getSession();
        Customer currentUser = (Customer) session.getAttribute("user");

        if ("/login".equals(((HttpServletRequest) request).getServletPath())) {
            chain.doFilter(request, response);
            return;
        }
        if (((HttpServletRequest) request).getServletPath().startsWith("/resources")) {
            chain.doFilter(request, response);
            return;
        }
        if (currentUser == null) {
            httpRes.sendRedirect("/login");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}

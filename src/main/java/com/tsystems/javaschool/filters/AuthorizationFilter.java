package com.tsystems.javaschool.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by alex on 10.09.16.
 *
 * Filter for authorization
 */
//@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Empty
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        HttpSession session = httpReq.getSession();
        String currentUser = (String) session.getAttribute("user");

        String path = ((HttpServletRequest) request).getServletPath();

        if (path.startsWith("/resources")) { // Resources should be available every time
            chain.doFilter(request, response);
            return;
        }
        if ("/error".equals(path)) {  // Also errors =)
            chain.doFilter(request, response);
            return;
        }
        if ("/".equals(path)) {  // Define home path for every user
            if ("admin".equals(currentUser)) {
                httpRes.sendRedirect("/admin/customers");
                return;
            } else if ("customer".equals(currentUser)) {
                httpRes.sendRedirect("/customer");
                return;
            } else {
                httpRes.sendRedirect("/login");
                return;
            }

        }
        if (currentUser == null) {
            if ("/login".equals(path)) {
                chain.doFilter(request, response);
                return;
            }
            httpRes.sendRedirect("/login");
        } else {
            if ("admin".equals(currentUser) && path.startsWith("/customer/") ||  // If admin tries to access customer page,
                    "customer".equals(currentUser) && path.startsWith("/admin/")) {  // or vice versa there is error
                ((HttpServletResponse) response).sendRedirect("/error");
                return;
            }
            if ("/login".equals(path)) {  // You cannot access login, if you are already logged
                httpRes.sendRedirect("/");
                return;
            }
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Empty
    }
}

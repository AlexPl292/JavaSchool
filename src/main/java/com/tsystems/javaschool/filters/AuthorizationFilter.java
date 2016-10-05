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
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Empty
    }
}

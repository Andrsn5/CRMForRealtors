package com.company.crm.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class EncodingFilter implements Filter {

    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) {
        String enc = filterConfig.getInitParameter("encoding");
        if (enc != null) encoding = enc;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        response.setContentType("text/html; charset=" + encoding);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}

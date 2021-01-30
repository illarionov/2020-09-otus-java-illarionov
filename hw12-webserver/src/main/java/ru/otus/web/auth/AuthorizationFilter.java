package ru.otus.web.auth;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.core.model.Role;
import ru.otus.utils.SessionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class AuthorizationFilter implements Filter {

    private ServletContext context;

    private final Set<Role> rolesPermitted;

    public AuthorizationFilter(Role... rolesPermitted) {
        this.rolesPermitted = EnumSet.noneOf(Role.class);
        this.rolesPermitted.addAll(Arrays.asList(rolesPermitted));
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.context = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        this.context.log("Requested Resource:" + uri);

        HttpSession session = request.getSession(false);

        Long userId = null;
        Role userRole = null;
        if (session != null) {
            userId = (Long) session.getAttribute(SessionUtils.SESSION_ATTRIBUTE_USER_ID);
            userRole = (Role) session.getAttribute(SessionUtils.SESSION_ATTRIBUTE_USER_ROLE);
        }

        if (session == null
                || userId == null
                || userRole == null) {
            response.sendRedirect("/login");
            return;
        }

        if (!rolesPermitted.contains(userRole)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

package ru.otus.web.servlets.admin;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.web.template.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminUsersServlet extends HttpServlet {
    private static final String USERS_PAGE_TEMPLATE = "admin/users.ftlh";

    private final DBServiceUser userService;
    private final TemplateProcessor templateProcessor;

    public AdminUsersServlet(TemplateProcessor templateProcessor, DBServiceUser userService) {
        this.templateProcessor = templateProcessor;
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<User> users = userService.findAllUsers();

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("users", users);
        
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }
}

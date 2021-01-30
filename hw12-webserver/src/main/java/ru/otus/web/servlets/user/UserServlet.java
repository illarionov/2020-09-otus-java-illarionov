package ru.otus.web.servlets.user;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.core.service.DBServiceUser;
import ru.otus.utils.SessionUtils;
import ru.otus.web.template.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UserServlet extends HttpServlet {
    private static final String USERS_PAGE_TEMPLATE = "user/index.ftlh";

    private final DBServiceUser userService;
    private final TemplateProcessor templateProcessor;

    public UserServlet(TemplateProcessor templateProcessor, DBServiceUser userService) {
        this.templateProcessor = templateProcessor;
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        var user = userService.getUser((Long)req.getSession().getAttribute(SessionUtils.SESSION_ATTRIBUTE_USER_ID));

        paramsMap.put("user", user.orElseThrow());
        
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }
}

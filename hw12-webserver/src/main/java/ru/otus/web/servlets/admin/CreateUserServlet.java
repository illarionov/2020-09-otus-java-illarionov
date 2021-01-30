package ru.otus.web.servlets.admin;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.ConstraintViolationDaoException;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceException;
import ru.otus.web.template.TemplateProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class CreateUserServlet extends HttpServlet {
    private static final String CREATE_USER_USER_PAGE_TEMPLATE = "admin/create_user.ftlh";

    private static final Logger logger = LoggerFactory.getLogger(CreateUserServlet.class);
    
    private final DBServiceUser userService;
    private final TemplateProcessor templateProcessor;
    
    public CreateUserServlet(TemplateProcessor templateProcessor, DBServiceUser userService) {
        this.templateProcessor = templateProcessor;
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        UserForm userForm = new UserForm();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("messages", emptyList());
        paramsMap.put("form", userForm);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CREATE_USER_USER_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserForm userForm = new UserForm();
        List<String> messages = new ArrayList<>();

        userForm.read(request);
        messages.addAll(userForm.validate());

        if (messages.isEmpty()) {
            try {
                User newUser = userForm.buildUser();
                long userId = userService.saveUser(newUser);
                response.sendRedirect("/admin");
                return;
            } catch (DbServiceException e) {
                if (e.getCause() instanceof ConstraintViolationDaoException
                        && "login".equals(((ConstraintViolationDaoException) e.getCause()).getContraintColumnName())
                ) {
                    messages.add("Логин уже существует");
                } else {
                    messages.add("Ошибка создания пользователя");
                    logger.error("Failed to create user", e);
                }
            }
        }

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("form", userForm);
        paramsMap.put("messages", messages);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CREATE_USER_USER_PAGE_TEMPLATE, paramsMap));
    }
}

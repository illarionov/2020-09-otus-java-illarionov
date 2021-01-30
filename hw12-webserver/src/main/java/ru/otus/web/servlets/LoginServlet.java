package ru.otus.web.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.core.model.Role;
import ru.otus.utils.SessionUtils;
import ru.otus.web.auth.AuthenticationResult;
import ru.otus.web.auth.UserAuthService;
import ru.otus.web.template.TemplateProcessor;

import java.io.IOException;
import java.util.*;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.util.Collections.emptyList;
import static ru.otus.utils.ServletUtils.getParameterOrEmptyString;

public class LoginServlet extends HttpServlet {
    private static final String DEFAULT_LOGIN = "user7";
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final String LOGIN_PAGE_TEMPLATE = "login.ftlh";

    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginForm loginForm = new LoginForm();
        renderLoginPageTemplate(loginForm, emptyList(), response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LoginForm loginForm = new LoginForm();
        List<String> messages = new ArrayList<>();

        loginForm.read(request);
        messages.addAll(loginForm.validate());

        if (!messages.isEmpty()) {
            response.setStatus(SC_UNAUTHORIZED);
            renderLoginPageTemplate(loginForm, messages, response);
            return;
        }

        AuthenticationResult result = userAuthService.authenticate(loginForm.getLogin(), loginForm.getPassword());

        if (result.getStatus() == AuthenticationResult.Status.SUCCESS) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(SessionUtils.MAX_INACTIVE_INTERVAL);
            session.setAttribute(SessionUtils.SESSION_ATTRIBUTE_USER_ID, result.getUserId());
            session.setAttribute(SessionUtils.SESSION_ATTRIBUTE_USER_ROLE, result.getRole());
            response.sendRedirect(result.getRole() == Role.ADMIN ? "/admin" : "/user");
        } else {
            messages.add("Неверный логин или пароль");
            response.setStatus(SC_UNAUTHORIZED);
            renderLoginPageTemplate(loginForm, messages, response);
        }
    }

    private void renderLoginPageTemplate(LoginForm loginForm, List<String> messages, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("messages", messages);
        paramsMap.put("form", loginForm);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, paramsMap));
    }

    public final static class LoginForm {
        private String login = DEFAULT_LOGIN;
        private String password = "";

        public LoginForm() {
        }

        public void read(HttpServletRequest request) {
            this.login = getParameterOrEmptyString(request, PARAM_LOGIN);
            this.password = getParameterOrEmptyString(request, PARAM_PASSWORD);
        }

        public List<String> validate() {
            List<String> errors = new ArrayList<>();

            if (login.isEmpty()) {
                errors.add("Логин не указан");
            }

            if (password.isEmpty()) {
                errors.add("Пароль не указан");
            }

            return errors;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }
}

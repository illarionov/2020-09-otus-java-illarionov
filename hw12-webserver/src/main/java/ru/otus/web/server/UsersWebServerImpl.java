package ru.otus.web.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.core.model.Role;
import ru.otus.core.service.DBServiceUser;
import ru.otus.utils.FileSystemHelper;
import ru.otus.web.auth.AuthorizationFilter;
import ru.otus.web.auth.UserAuthService;
import ru.otus.web.servlets.LoginServlet;
import ru.otus.web.servlets.LogoutServlet;
import ru.otus.web.servlets.admin.AdminUsersServlet;
import ru.otus.web.servlets.admin.CreateUserServlet;
import ru.otus.web.servlets.user.UserServlet;
import ru.otus.web.template.TemplateProcessor;

import java.util.List;

public class UsersWebServerImpl implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    protected final TemplateProcessor templateProcessor;
    private final UserAuthService authService;
    private final DBServiceUser userService;
    private final Server server;

    public UsersWebServerImpl(int port,
                              UserAuthService authService,
                              DBServiceUser userService,
                              TemplateProcessor templateProcessor) {
        this.userService = userService;
        this.templateProcessor = templateProcessor;
        this.server = new Server(port);
        this.authService = authService;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(servletContextHandler);

        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        servletContextHandler.addServlet(new ServletHolder(new LogoutServlet()), "/logout");

        servletContextHandler.addServlet(new ServletHolder(new UserServlet(templateProcessor, userService)), "/user");

        servletContextHandler.addServlet(new ServletHolder(new AdminUsersServlet(templateProcessor, userService)), "/admin");
        servletContextHandler.addServlet(new ServletHolder(new CreateUserServlet(templateProcessor, userService)), "/admin/user/create");

        applySecurity(servletContextHandler);

        return servletContextHandler;
    }

    private void applySecurity(ServletContextHandler servletContextHandler) {
        AuthorizationFilter adminOnlyFilter = new AuthorizationFilter(Role.ADMIN);
        for (String urlPattern: List.of("/admin", "/admin/*")) {
            servletContextHandler.addFilter(new FilterHolder(adminOnlyFilter), urlPattern, null);
        }

        AuthorizationFilter anyUserFilter = new AuthorizationFilter(Role.ADMIN, Role.USER);
        for (String urlPattern: List.of("/user")) {
            servletContextHandler.addFilter(new FilterHolder(anyUserFilter), urlPattern, null);
        }
    }
}

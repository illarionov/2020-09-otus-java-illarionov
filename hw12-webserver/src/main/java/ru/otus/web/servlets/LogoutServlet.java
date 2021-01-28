package ru.otus.web.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    public LogoutServlet() {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("/");
    }
}

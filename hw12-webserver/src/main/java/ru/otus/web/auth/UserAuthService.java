package ru.otus.web.auth;

public interface UserAuthService {
    AuthenticationResult authenticate(String login, String password);
}

package ru.otus.web.auth;

import ru.otus.core.service.DBServiceUser;

public class UserAuthServiceImpl implements UserAuthService {

    private final DBServiceUser userService;

    public UserAuthServiceImpl(DBServiceUser userService) {
        this.userService = userService;
    }

    @Override
    public AuthenticationResult authenticate(String login, String password) {
        return userService.getUserByLogin(login)
                .map(user -> {
                    if (user.getPassword().equals(password)) {
                        return AuthenticationResult.success(user.getId(), user.getRole());
                    } else {
                        return AuthenticationResult.failed();
                    }
                })
                .orElse(AuthenticationResult.failed());
    }

}

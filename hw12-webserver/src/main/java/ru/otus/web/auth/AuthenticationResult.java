package ru.otus.web.auth;

import ru.otus.core.model.Role;

import java.util.Objects;

public class AuthenticationResult {

    public enum Status {
        SUCCESS, FAILED
    }

    private static final AuthenticationResult AUTH_RESULT_FAILED = new AuthenticationResult(Status.FAILED, null, null);

    private final Status status;

    private final Long userId;

    private final Role role;

    public static AuthenticationResult success(long userId, Role role) {
        return new AuthenticationResult(Status.SUCCESS, userId, role);
    }

    public static AuthenticationResult failed() {
        return AUTH_RESULT_FAILED;
    }

    private AuthenticationResult(Status status, Long userId, Role role) {
        this.status = status;
        this.userId = userId;
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public long getUserId() {
        if (status != Status.SUCCESS) {
            throw new IllegalStateException("Authentication failed");
        }
        return userId;
    }

    public Role getRole() {
        if (status != Status.SUCCESS) {
            throw new IllegalStateException("Authentication failed");
        }
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticationResult that = (AuthenticationResult) o;
        return status == that.status && Objects.equals(userId, that.userId) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, userId, role);
    }
}

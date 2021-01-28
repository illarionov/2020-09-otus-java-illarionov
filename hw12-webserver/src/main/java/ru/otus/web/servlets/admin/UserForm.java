package ru.otus.web.servlets.admin;

import jakarta.servlet.http.HttpServletRequest;
import ru.otus.core.model.Role;
import ru.otus.core.model.User;
import ru.otus.core.model.UserBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.otus.utils.ServletUtils.getParameterOrEmptyString;

public class UserForm {
    public static final String PARAM_LOGIN = "login";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_ROLE = "role";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_AGE = "age";
    public static final String PARAM_ADDRESS = "address";
    public static final String PARAM_PHONE = "phone";

    private static final Role DEFAULT_ROLE = Role.USER;
    private static final String FIELD_VALID = "";

    private String login = "";
    private String password = "";
    private Role role = DEFAULT_ROLE;
    private String name = "";
    private String age = "";
    private String address = "";
    private String phone = "";

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void read(HttpServletRequest request) {
        this.login = getParameterOrEmptyString(request, PARAM_LOGIN);
        this.password = getParameterOrEmptyString(request, PARAM_PASSWORD);
        try {
            this.role = Role.valueOf(request.getParameter(PARAM_ROLE));
        } catch (Exception e) {
            this.role = DEFAULT_ROLE;
        }

        this.name = getParameterOrEmptyString(request, PARAM_NAME);
        this.age = getParameterOrEmptyString(request, PARAM_AGE);
        this.address = getParameterOrEmptyString(request, PARAM_ADDRESS);
        this.phone = getParameterOrEmptyString(request, PARAM_PHONE);
    }

    public User buildUser() {
        List<String> errors = validate();
        if (!errors.isEmpty()) {
            throw new IllegalStateException("Form not validated. " + errors);
        }
        
        UserBuilder newUserBuilder = User.builder()
                .setLogin(login)
                .setPassword(password)
                .setRole(role)
                .setName(name);

        if (!age.isEmpty()) {
            newUserBuilder.setAge(Integer.valueOf(age));
        }

        if (!address.isEmpty()) {
            newUserBuilder.setAddress(address);
        }

        if (!phone.isEmpty()) {
            newUserBuilder.addPhone(phone);
        }

        return newUserBuilder.build();
    }

    public List<String> validate() {
        List<String> errors = Stream.of(
                validateLogin(),
                validatePassword(),
                validateRole(),
                validateName(),
                validateAge(),
                validateAddress(),
                validatePhone()
        ).filter(error -> !FIELD_VALID.equals(error))
                .collect(Collectors.toUnmodifiableList());

        return errors;
    }

    private String validateLogin() {
        if (login.isEmpty()) {
            return "Логин не указан";
        }
        if (login.length() < 3) {
            return "Логин слишком короткий";
        }
        return FIELD_VALID;
    }

    private String validatePassword() {
        if (password.isEmpty()) {
            return "Пароль не указан";
        }
        if (password.length() < 5) {
            return "Пароль слишком короткий";
        }
        return FIELD_VALID;
    }

    private String validateRole() {
        return FIELD_VALID;
    }

    private String validateName() {
        if (name.isEmpty()) {
            return "Имя не указано";
        }
        return FIELD_VALID;
    }

    private String validateAge() {
        if (age.isEmpty()) {
            return FIELD_VALID;
        }
        int ageInt;

        try {
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException nfe) {
            return "Возраст должен быть положительным числом";
        }

        if (ageInt <= 0) {
            return "Возраст должен быть положительным числом";
        }

        return FIELD_VALID;
    }

    private String validateAddress() {
        return FIELD_VALID;
    }

    private String validatePhone() {
        return FIELD_VALID;
    }
}

package ru.otus.controllers.admin;

import ru.otus.core.model.Role;
import ru.otus.core.model.User;
import ru.otus.core.model.UserBuilder;

import javax.validation.constraints.*;

public class UserForm {
    private static final Role DEFAULT_ROLE = Role.ADMIN;

    @NotBlank(message = "Логин не указан")
    @Size(min=3, message = "Логин слишком короткий")
    private String login = "";

    @NotBlank(message = "Пароль не указан")
    @Size(min=5, message = "Пароль слишком короткий")
    private String password = "";

    @NotNull(message = "Роль не указана")
    private Role role = DEFAULT_ROLE;

    @NotBlank(message = "Имя не указано")
    private String name = "";

    @Positive(message = "Возраст должен быть положительным числом")
    private Integer age;

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

    public Integer getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserForm() {
    }
    
    public User buildUser() {
        UserBuilder newUserBuilder = User.builder()
                .setLogin(login)
                .setPassword(password)
                .setRole(role)
                .setAge(age)
                .setAddress(address)
                .setName(name);

        if (!phone.isEmpty()) {
            newUserBuilder.addPhone(phone);
        }

        return newUserBuilder.build();
    }
}

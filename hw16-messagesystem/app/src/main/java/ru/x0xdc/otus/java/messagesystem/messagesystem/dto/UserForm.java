package ru.x0xdc.otus.java.messagesystem.messagesystem.dto;

import ru.x0xdc.otus.java.messagesystem.core.model.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

public class UserForm implements Serializable {
    public static final List<String> FIELD_ORDER = List.of("login", "password", "role", "name", "age", "address", "phone");

    private static final Role DEFAULT_ROLE = Role.ADMIN;

    private static final long serialVersionUID = 1L;

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

    public UserForm() {
    }

    public UserForm(UserForm src) {
        this.login = src.getLogin();
        this.password = src.getPassword();
        this.role = src.getRole();
        this.name = src.getName();
        this.age = src.getAge();
        this.address = src.getAddress();
        this.phone = src.getPhone();
    }


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
}

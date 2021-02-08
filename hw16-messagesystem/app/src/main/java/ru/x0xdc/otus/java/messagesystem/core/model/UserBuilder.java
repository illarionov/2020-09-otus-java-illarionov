package ru.x0xdc.otus.java.messagesystem.core.model;

import org.springframework.lang.Nullable;
import ru.x0xdc.otus.java.messagesystem.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class UserBuilder {
    private long id;
    private String login;
    private String password;
    private Role role = Role.USER;
    private String name;
    private Integer age;
    private Address address;
    private List<Phone> phones = new ArrayList<>();

    public UserBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public UserBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setRole(Role role) {
        this.role = role;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setAge(@Nullable Integer age) {
        this.age = age;
        return this;
    }

    public UserBuilder setAddress(@Nullable Address address) {
        this.address = address;
        return this;
    }

    public UserBuilder setAddress(long id, String street) {
        return setAddress(new Address(id, street));
    }

    public UserBuilder setAddress(@Nullable String street) {
        return street != null ? setAddress(0, street) : setAddress((Address) null);
    }

    public UserBuilder setPhones(List<Phone> phones) {
        Preconditions.checkNotNull(phones, "phones");
        this.phones.clear();
        this.phones.addAll(phones);
        return this;
    }

    public UserBuilder addPhone(String phone) {
        this.phones.add(new Phone(0, phone));
        return this;
    }

    public User build() {
        Preconditions.checkNotBlank(login, "login");
        Preconditions.checkNotBlank(password, "password");
        Preconditions.checkNotBlank(name, "name");
        Preconditions.checkNotNull(role, "role");
        Preconditions.checkNotNull(phones, "role");

        return new User(id, login, password, role, name, age, address, phones);
    }
}
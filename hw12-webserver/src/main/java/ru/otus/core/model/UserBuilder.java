package ru.otus.core.model;

import ru.otus.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class UserBuilder {
    private long id;
    private String login;
    private String password;
    private Role role = Role.USER;
    private String name;
    private Integer age;
    private Address address;
    private List<Phone> phones = new ArrayList<>();

    UserBuilder() {
    }
    
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

    public UserBuilder setAge(Integer age) {
        this.age = age;
        return this;
    }

    public UserBuilder setAddress(Address address) {
        this.address = address;
        return this;
    }

    public UserBuilder setAddress(long id, String street) {
        return setAddress(new Address(id, street));
    }

    public UserBuilder setAddress(String street) {
        return street != null ? setAddress(0, street) : setAddress((Address) null);
    }

    public UserBuilder setPhones(List<Phone> phones) {
        Objects.requireNonNull(phones);
        this.phones = phones;
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

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);

        user.setId(id);
        user.setName(name);
        user.setAge(age);
        user.setAddress(address);
        if (address != null) {
            address.setUser(user);
        }
        user.setPhones(phones);
        for (Phone phone: phones) {
            phone.setUser(user);
        }
        return user;
    }
}

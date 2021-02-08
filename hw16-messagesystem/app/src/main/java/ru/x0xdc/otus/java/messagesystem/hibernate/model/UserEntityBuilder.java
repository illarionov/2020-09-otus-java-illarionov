package ru.x0xdc.otus.java.messagesystem.hibernate.model;

import ru.x0xdc.otus.java.messagesystem.core.model.Role;
import ru.x0xdc.otus.java.messagesystem.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class UserEntityBuilder {
    private long id;
    private String login;
    private String password;
    private Role role = Role.USER;
    private String name;
    private Integer age;
    private AddressEntity address;
    private List<PhoneEntity> phones = new ArrayList<>();

    UserEntityBuilder() {
    }
    
    public UserEntityBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public UserEntityBuilder setLogin(String login) {
        this.login = login;
        return this;
    }

    public UserEntityBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserEntityBuilder setRole(Role role) {
        this.role = role;
        return this;
    }

    public UserEntityBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserEntityBuilder setAge(Integer age) {
        this.age = age;
        return this;
    }

    public UserEntityBuilder setAddress(AddressEntity address) {
        this.address = address;
        return this;
    }

    public UserEntityBuilder setAddress(long id, String street) {
        return setAddress(new AddressEntity(id, street));
    }

    public UserEntityBuilder setAddress(String street) {
        return street != null ? setAddress(0, street) : setAddress((AddressEntity) null);
    }

    public UserEntityBuilder setPhones(List<PhoneEntity> phones) {
        Objects.requireNonNull(phones);
        this.phones = phones;
        return this;
    }

    public UserEntityBuilder addPhone(String phone) {
        this.phones.add(new PhoneEntity(0, phone));
        return this;
    }

    public UserEntity build() {
        Preconditions.checkNotBlank(login, "login");
        Preconditions.checkNotBlank(password, "password");
        Preconditions.checkNotBlank(name, "name");
        Preconditions.checkNotNull(role, "role");

        UserEntity user = new UserEntity();
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
        for (PhoneEntity phone: phones) {
            phone.setUser(user);
        }
        return user;
    }
}

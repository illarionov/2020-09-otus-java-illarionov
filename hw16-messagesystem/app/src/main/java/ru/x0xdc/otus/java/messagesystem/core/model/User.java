package ru.x0xdc.otus.java.messagesystem.core.model;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public final class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long id;
    private final String login;
    private final String password;
    private final Role role;
    private final String name;
    @Nullable
    private final Integer age;
    @Nullable
    private final Address address;
    private final List<Phone> phones;

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    User(long id, String login, String password, Role role, String name, @Nullable Integer age, @Nullable Address address, List<Phone> phones) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = List.copyOf(phones);
    }

    public long getId() {
        return id;
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

    @Nullable
    public Integer getAge() {
        return age;
    }

    @Nullable
    public Address getAddress() {
        return address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public UserBuilder toBuilder() {
        return new UserBuilder()
                .setId(id)
                .setLogin(login)
                .setPassword(password)
                .setRole(role)
                .setAge(age)
                .setAddress(address)
                .setPhones(phones);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}

package ru.x0xdc.otus.java.messagesystem.hibernate.model;

import org.hibernate.annotations.ColumnDefault;
import ru.x0xdc.otus.java.messagesystem.core.model.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name="login", nullable = false, unique = true)
    private String login;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="role", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("USER")
    private Role role;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = true)
    private Integer age;

    @OneToOne(targetEntity = AddressEntity.class, optional = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
    private AddressEntity address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
    private List<PhoneEntity> phones;

    public static UserEntityBuilder builder() {
        return new UserEntityBuilder();
    }

    protected UserEntity() {
    }

    UserEntity(long id, String name, String login, String password, Role role, Integer age, AddressEntity address, List<PhoneEntity> phones) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phones = phones;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public List<PhoneEntity> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneEntity> phones) {
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + "***" + '\'' +
                ", role=" + role +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }

}

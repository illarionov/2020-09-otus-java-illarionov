package ru.x0xdc.otus.java.messagesystem.hibernate.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name="street", nullable = false)
    private String street;

    @OneToOne(mappedBy = "address")
    private UserEntity user;

    public AddressEntity() {
    }

    public AddressEntity(long id, String street) {
        this.id = id;
        this.street = street;
    }

    public AddressEntity(long id, String street, UserEntity user) {
        this.id = id;
        this.street = street;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressEntity address = (AddressEntity) o;
        return id == address.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
